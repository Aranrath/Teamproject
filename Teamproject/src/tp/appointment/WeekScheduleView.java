package tp.appointment;

import java.sql.Date;
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
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import tp.Presenter;
import tp.model.Appointment;

public class WeekScheduleView extends GridPane
{
	Presenter presenter;

	private int shownKw;
	private Date[] shownWorkWeek;
	
	//==============================================
	
	private Label kwLabel;
	private Button leftButton;
	private Button rightButton;
	private Button showCurrentWeekButton;
	private DatePicker datePicker;
	private Label dateLabel;
	private HBox timeTableHBox;
	
	//==============================================
	
	private VBox [] weeksAppointmentsVBoxes;
	
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
		timeTableHBox = new HBox();
		dateLabel = new Label("Zeige Woche des Datums: ");
		
		//TODO Abstand zwischen Spalten ??
//		timeTableHBox.setSpacing(40);
//		timeTableHBox.setPadding(new Insets(20,20,20,20));
		
		timeTableHBox.setBackground(
				new Background(new BackgroundFill(Color.AQUAMARINE, CornerRadii.EMPTY, Insets.EMPTY)));
		datePicker = new DatePicker();
		
		mondayAppointmentsVBox = new VBox(new Label("Montag"));
		thuesdayAppointmentsVBox = new VBox(new Label("Dienstag"));
		wednesdayAppointmentsVBox = new VBox(new Label("Mittwoch"));
		thursdayAppointmentsVBox = new VBox(new Label("Donnerstag"));
		fridayAppointmentsVBox = new VBox(new Label("Freitag"));
		
		weeksAppointmentsVBoxes = new VBox[] {mondayAppointmentsVBox, thuesdayAppointmentsVBox, wednesdayAppointmentsVBox, thursdayAppointmentsVBox, fridayAppointmentsVBox};
		
		timeTableHBox = new HBox(mondayAppointmentsVBox, thuesdayAppointmentsVBox, wednesdayAppointmentsVBox, thursdayAppointmentsVBox, fridayAppointmentsVBox);
		timeTableHBox.setBackground(
				new Background(new BackgroundFill(Color.CORNFLOWERBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
		
		
		HBox datePickerAndLabelBox = new HBox(dateLabel, datePicker);
		
		HBox navigationBox = new HBox(leftButton, kwLabel, rightButton);
		
		
		//==============================================

		add(datePickerAndLabelBox,0,0);
		datePickerAndLabelBox.setAlignment(Pos.CENTER_RIGHT);
		
		add(showCurrentWeekButton, 1,0);
		GridPane.setHalignment(showCurrentWeekButton, HPos.LEFT);
		GridPane.setValignment(showCurrentWeekButton, VPos.CENTER);
		

		add(navigationBox,0,1,2,1);
		navigationBox.setAlignment(Pos.CENTER);
		
		add(timeTableHBox,0,2,2,1);
		
		//========================================================================
				//Column Constraints
						
				ColumnConstraints col = new ColumnConstraints();
				col.setPercentWidth(100/2);
				getColumnConstraints().addAll(col, col);
				
				//--------------------------------------------------
				//Row Constraints
						
				RowConstraints buttonRow = new RowConstraints();
				buttonRow.setPercentHeight(15/2);
						
				RowConstraints scheduleHBoxRow = new RowConstraints();
				scheduleHBoxRow.setPercentHeight(85);
						
				getRowConstraints().add(buttonRow);
				getRowConstraints().add(buttonRow);
				getRowConstraints().add(scheduleHBoxRow);
		
				//==============================================
				
				leftButton.setOnAction((event)->{
					Date date = presenter.getEndOfPreviousWeek(shownWorkWeek[0]);
					fillView(date);
				});
				rightButton.setOnAction((event)->{
					Date date = presenter.getStartOfNextWeek(shownWorkWeek[shownWorkWeek.length-1]);
					fillView(date);
				});
				showCurrentWeekButton.setOnAction((event)->{});
				datePicker.setOnAction((event)->{
					fillView(new Date(datePicker.getValue().toEpochDay()));
				});
	}
	
	private void fillView(Date date)
	{
		shownWorkWeek = presenter.getWorkWeekOfDate(date);
		
		if(shownWorkWeek.length != 5)
		{
			System.out.println("ERROR: Wochendaten falsch erhalten: " +  shownWorkWeek);
		}
		
		shownKw = presenter.getKwOfDate(date);
		kwLabel.setText(shownKw + ". KW von " + shownWorkWeek[0] + " bis " + shownWorkWeek[4]);
		
		for(int i = 0; i<5; i++)
		{
			weeksAppointmentsVBoxes[i].getChildren().add(new Label("(" + shownWorkWeek[i] + ")"));
			
			ArrayList<Appointment> thisDayAppointments = presenter.getAppointments(shownWorkWeek[i]);
			
			
			//TODO Maße anpassen
			for(Appointment a : thisDayAppointments)
			{
				Button appointmentButton = new Button(a.getStartTimeString() + " - " + a.getEndTimeString() + " (" + a.getRoomNmb() + ")");
				appointmentButton.setOnAction(event -> {
					presenter.openConcernTab(presenter.getConcern(a.getConcernId()));
				});
				appointmentButton.setStyle("-fx-base: #C5EFF7");
				
				weeksAppointmentsVBoxes[i].getChildren().add(appointmentButton);
			}
			
		}
		
		
			
		
		
		
		
	}


}
