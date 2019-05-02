package tp.appointment;

import java.sql.Date;
import java.time.Instant;
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
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import tp.Presenter;
import tp.model.Appointment;
import tp.model.Concern;

public class WeekScheduleView extends GridPane
{
	private Presenter presenter;

	private int shownKw;
	private Date[] shownWorkWeek;
	
	private LocalTime earliestTime;
	private LocalTime latestTime;
	
	
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
	private VBox [] weeksAppointmentsVBoxes;
	private Label [] weeksDateLabels;
	
	private VBox mondayAppointmentsVBox;
	private VBox thuesdayAppointmentsVBox;
	private VBox wednesdayAppointmentsVBox;
	private VBox thursdayAppointmentsVBox;
	private VBox fridayAppointmentsVBox;

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
		
		earliestTime = LocalTime.of(5, 59);
		latestTime = LocalTime.of(20, 1);
		
		//-------------------------------------------------
		//ScheduleTable erstellen
		
		timeTableGridPane = new GridPane();
		timeTableGridPane.setBackground(
		new Background(new BackgroundFill(Color.ALICEBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
		timeTableGridPane.setHgap(10);
//		timeTableGridPane.setVgap(5);
		timeTableGridPane.setPadding(new Insets(10,10,10,10));
		
		//Wochentagsspalten erstellen
		mondayAppointmentsVBox = new VBox();
		thuesdayAppointmentsVBox = new VBox();
		wednesdayAppointmentsVBox = new VBox();
		thursdayAppointmentsVBox = new VBox();
		fridayAppointmentsVBox = new VBox();
		
		weeksAppointmentsVBoxes = new VBox[] {mondayAppointmentsVBox, thuesdayAppointmentsVBox, wednesdayAppointmentsVBox, thursdayAppointmentsVBox, fridayAppointmentsVBox};
		weeksDateLabels = new Label[] {new Label("<Datum Montag>"),new Label("<Datum Dienstag>"),new Label("<Datum Mittwoch>"),new Label("<Datum Donnerstag>"),new Label("<Datum Freitag>")};
		
		//Zusammenfügen
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
			timeTableGridPane.add(weeksAppointmentsVBoxes[i], i, 3);
			
			weeksAppointmentsVBoxes[i].setBackground(
					new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
			
			weeksAppointmentsVBoxes[i].setMaxHeight(Double.MAX_VALUE);
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
				showCurrentWeekButton.setOnAction((event)->{});
				datePicker.setOnAction((event)->{
					fillView(new Date(datePicker.getValue().toEpochDay()));
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
		

		//Wochentagsspalten leeren
		for(int i = 0; i <weeksAppointmentsVBoxes.length;i++)
		{
			weeksAppointmentsVBoxes[i].getChildren().clear();
		}
		
		
		for(int i = 0; i<5; i++)
		{
			
			weeksDateLabels[i].setText(shownWorkWeek[i] + "");
			
			ArrayList<Appointment> thisDayAppointments = presenter.getAppointments(shownWorkWeek[i]);
			
			
			//TODO Maße anpassen
			for(Appointment a : thisDayAppointments)
			{
				
				LocalTime appointmentStartTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(a.getStartTime()), ZoneId.systemDefault()).toLocalTime();
				LocalTime appointmentEndTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(a.getEndTime()), ZoneId.systemDefault()).toLocalTime();
		

				//Wenn Termin zwischen 6 und 20 Uhr ist:
				if(appointmentStartTime.isAfter(earliestTime) && appointmentEndTime.isBefore(latestTime))
				{
					Concern concernOfAppointment = presenter.getConcern(a.getConcernId());
					
					Button appointmentButton = new Button(a.getStartTimeString() + " - " + a.getEndTimeString() + " " +concernOfAppointment.getTitle());
					if(!a.getRoomNmb().isEmpty())
					{
						appointmentButton.setText(appointmentButton.getText() + " (" + a.getRoomNmb() + ")");
					}
					
					appointmentButton.setMaxWidth(Double.MAX_VALUE);
					
					appointmentButton.setOnAction(event -> {
						presenter.openConcernTab(concernOfAppointment);
					});
					appointmentButton.setStyle("-fx-base: #C5EFF7");
					
					weeksAppointmentsVBoxes[i].getChildren().add(appointmentButton);
				}
				
				
			}
			
		}
		
		//TODO Zeiten = Buttonhöhe + Position
		//TEST:
		
		weeksAppointmentsVBoxes[0].setVisible(false);
		Pane pane = new Pane();
		
		pane.setBackground(
				new Background(new BackgroundFill(Color.BEIGE, CornerRadii.EMPTY, Insets.EMPTY)));
		timeTableGridPane.add(pane, 0, 3);
		
		Button label = new Button("2");
		
		pane.getChildren().addAll(label);

		pane.setMaxWidth(Double.MAX_VALUE);
		GridPane.setHgrow(pane, Priority.ALWAYS);
		
		
		
		label.setMaxWidth(Double.MAX_VALUE);
		GridPane.setHgrow(label, Priority.ALWAYS);
		
		label.setMaxHeight(Double.MAX_VALUE);
		
		System.out.println(label.localToScene(pane.getBoundsInLocal()));
		
		
		
			
		
		
		
		
	}


}
