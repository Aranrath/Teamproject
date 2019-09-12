package tp.appointment;

import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import tp.Presenter;
import tp.model.Appointment;
import tp.model.Concern;

public class WeekScheduleView extends GridPane
{
	public static final int EARLIEST_TIME = 6;
	public static final int LATEST_TIME = 20;
	
	public static final int MILLI_PER_HOUR = 3600000;
	
	
	//==============================================
	
	private Presenter presenter;

	private int shownKw;
	private Date[] shownWorkWeek;
	
	//==============================================
	
	private Label kwLabel;
	private Button leftButton;
	private Button rightButton;
	private Button showCurrentWeekButton;
	private DatePicker datePicker;
	private Label dateLabel;
	private GridPane timeTableGridPane;
	
	//==============================================
	
	//zum drüber iterieren
	private Pane[] appointmentButtonsPanes;
	private Label [] weeksDateLabels;

	public WeekScheduleView(Presenter presenter)
	{
		this.presenter = presenter;
		buildView();
		//fillView mit heutigem Datum
		fillView(new Date(new java.util.Date().getTime()));
	}

	private void buildView()
	{
		setPadding(new Insets(10,10,10,10));
		setHgap(10);
		setVgap(10);
		
		kwLabel = new Label("<Some strange>. KW von <whenever> bis <forever>");
		leftButton = new Button("<");
		rightButton = new Button(">");
		showCurrentWeekButton = new Button("Zurück zur aktuellen Woche");
		dateLabel = new Label("Zeige Woche des Datums: ");
		datePicker = new DatePicker();
		
		HBox datePickerAndLabelBox = new HBox(dateLabel, datePicker);
		HBox navigationBox = new HBox(leftButton, kwLabel, rightButton);
		
		//-------------------------------------------------
		//ScheduleTable erstellen
		
		timeTableGridPane = new GridPane();
		timeTableGridPane.setBackground(
		new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
		timeTableGridPane.setHgap(10);
		timeTableGridPane.setPadding(new Insets(10,10,10,10));
		
		appointmentButtonsPanes = new Pane[] {new Pane(), new Pane(), new Pane(), new Pane(), new Pane()};
		weeksDateLabels = new Label[] {new Label("<Datum Montag>"),new Label("<Datum Dienstag>"),new Label("<Datum Mittwoch>"),new Label("<Datum Donnerstag>"),new Label("<Datum Freitag>")};
		
		
		timeTableGridPane.add(new Label("Montag"), 0, 0);
		timeTableGridPane.add(new Label("Dienstag"), 1, 0);
		timeTableGridPane.add(new Label("Mittwoch"), 2, 0);
		timeTableGridPane.add(new Label("Donnerstag"), 3, 0);
		timeTableGridPane.add(new Label("Freitag"), 4, 0);
		
		
		for(int i = 0; i<5; i++)
		{
			timeTableGridPane.add(weeksDateLabels[i], i, 1);
		}
		
		timeTableGridPane.add(new Label(""), 0, 2);
		
		for(int i = 0; i<5; i++)
		{
			timeTableGridPane.add(appointmentButtonsPanes[i], i, 3);
			
			appointmentButtonsPanes[i].setBackground(
					new Background(new BackgroundFill(Color.ALICEBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
		}
		
		//------------------------------------------
		//ScheduleTable Column Constraints
		ColumnConstraints tableCol = new ColumnConstraints();
		tableCol.setPercentWidth(100/5);
		timeTableGridPane.getColumnConstraints().addAll(tableCol, tableCol,tableCol,tableCol,tableCol);
		
		//------------------------------------------
		//ScheduleTable Row Constraints
		
		RowConstraints topLineRow = new RowConstraints();
		topLineRow.setPercentHeight(10/3);
				
		RowConstraints appointmentsRow = new RowConstraints();
		appointmentsRow.setPercentHeight(90);
				
		timeTableGridPane.getRowConstraints().addAll(topLineRow, topLineRow,topLineRow, appointmentsRow);
		
		
		//==============================================

		add(datePickerAndLabelBox,0,0);
		datePickerAndLabelBox.setAlignment(Pos.CENTER_RIGHT);
		
		add(showCurrentWeekButton, 1,0);
		GridPane.setHalignment(showCurrentWeekButton, HPos.LEFT);
		GridPane.setValignment(showCurrentWeekButton, VPos.CENTER);
		

		add(navigationBox,0,1,2,1);
		navigationBox.setAlignment(Pos.CENTER);
		
		add(timeTableGridPane,0,2,2,1);
		
		//========================================================================
				//Column Constraints
						
				ColumnConstraints col = new ColumnConstraints();
				col.setPercentWidth(100/2);
				getColumnConstraints().addAll(col, col);
				
				//--------------------------------------------------
				//Row Constraints
						
				RowConstraints buttonRow = new RowConstraints();
				buttonRow.setPercentHeight(10/2);
						
				RowConstraints scheduleHBoxRow = new RowConstraints();
				scheduleHBoxRow.setPercentHeight(90);
						
				getRowConstraints().add(buttonRow);
				getRowConstraints().add(buttonRow);
				getRowConstraints().add(scheduleHBoxRow);
		
				//==============================================
				
				leftButton.setOnAction((event)->{
					Date date = presenter.getEndOfPreviousWeek(shownWorkWeek[0]);
					fillView(date);
				});
				rightButton.setOnAction((event)->{
					Date date = presenter.getStartOfNextWeek(shownWorkWeek[4]);
					fillView(date);
				});
				showCurrentWeekButton.setOnAction((event)->{
					Date date = new Date(System.currentTimeMillis());
					fillView(date);
				});
				datePicker.setOnAction((event)->{
					fillView(Date.valueOf(datePicker.getValue()));
				});
	}
	
	private void fillView(Date date)
	{
		//Je Wochentag ein Datum erhalten
		shownWorkWeek = presenter.getWorkWeekOfDate(date);
		
		//Nummer der Kalendarwoche holen
		shownKw = presenter.getKwOfDate(date);
		
		//Anzeige an kw und Wochendaten anpassen
		kwLabel.setText(" " + shownKw + ". KW von " + shownWorkWeek[0] + " bis " + shownWorkWeek[4] + " ");
		
		
		for(int i = 0; i<5; i++)
		{
			//Daten der Wochentage anpassen
			weeksDateLabels[i].setText(shownWorkWeek[i] + "");
			
			ArrayList<Appointment> thisDayAppointments = presenter.getAppointments(shownWorkWeek[i]);
			
			appointmentButtonsPanes[i].getChildren().clear();
			
			for(Appointment a : thisDayAppointments)
			{
				
				LocalTime appointmentStartTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(a.getStartTime()), ZoneId.systemDefault()).toLocalTime();
				LocalTime appointmentEndTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(a.getEndTime()), ZoneId.systemDefault()).toLocalTime();
				
				//Wenn Termin zwischen 6 und 20 Uhr ist:
				if(appointmentStartTime.isAfter(LocalTime.of(EARLIEST_TIME,0).minusMinutes(1)) && appointmentEndTime.isBefore(LocalTime.of(LATEST_TIME,0).plusMinutes(1)))
				{
					Concern concernOfAppointment = presenter.getConcern(a.getConcernId());
					
					//---------------------------------------------------------------------
					//Beschriftung, Aussehen und Verhalten
					
					Button appointmentButton = new Button(a.getStartTimeString() + " - " + a.getEndTimeString());
					if(!a.getRoomNmb().isEmpty())
					{
						appointmentButton.setText(appointmentButton.getText() + " (" + a.getRoomNmb() + ")");
					}
					appointmentButton.setText(appointmentButton.getText() +  "\n" +concernOfAppointment.getTitle());
					
					appointmentButton.setTooltip(new Tooltip(appointmentButton.getText()));
					
					
					appointmentButton.setStyle("-fx-base: #C5EFF7");
					
					appointmentButton.setOnAction(event -> {
						presenter.openConcernTab(concernOfAppointment);
					});
					
					//---------------------------------------------------------------------
					//Größe und Position

					appointmentButton.prefWidthProperty().bind(appointmentButtonsPanes[i].widthProperty());
					
					int paneHeightInMillis = (LATEST_TIME - EARLIEST_TIME) *  MILLI_PER_HOUR;
					
					appointmentButton.prefHeightProperty().bind(appointmentButtonsPanes[i].heightProperty().divide(paneHeightInMillis).multiply(a.getDuration().intValue()));
					
					int yTranslationInMilli =  ((appointmentStartTime.getHour() +  appointmentStartTime.getMinute()/60) - EARLIEST_TIME) * MILLI_PER_HOUR;
					
					appointmentButton.translateYProperty().bind(appointmentButtonsPanes[i].heightProperty().divide(paneHeightInMillis).multiply(yTranslationInMilli));
					
					//---------------------------------------------------------------------
					
					appointmentButtonsPanes[i].getChildren().add(appointmentButton);

					
				}
				
				
			}
			
		}
	}

	public void updateView() {
		LocalDate pickerDate = datePicker.getValue();
		if(pickerDate != null)
		{
			fillView(Date.valueOf(pickerDate));
		}
		else
		{
			fillView(shownWorkWeek[0]);
		}
		
		
	}


}
