package tp.appointment;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import tp.Presenter;
import tp.concern.ConcernView;
import tp.model.Appointment;
import tp.model.Concern;

public class NewAppointmentView extends GridPane{
	
	private Stage stage;
	private Presenter presenter;
	private ConcernView concernView;
	
	//=======================================
	
	private Label dateLabel;
	private DatePicker datePicker;
	private Label timeLabel;
	private HBox startTimeHBox;
	private Label toLabel;
	private HBox endTimeHBox;
	private Label roomLabel;
	private TextField roomTextField;
	private Button saveButton;
	private TextField startHourTextField;
	private TextField startMinuteTextField;
	private TextField endHourTextField;
	private TextField endMinuteTextField;
	private Label errorLabel;
	

	public NewAppointmentView(Stage stage, Presenter presenter, ConcernView concernView) {
		this.stage = stage;
		this.presenter = presenter;
		this.concernView = concernView;
		buildView();
	}


	private void buildView() {
		setPadding(new Insets(10, 10, 10, 10));
		setHgap(10);
		setVgap(10);

		dateLabel = new Label("Datum:");
		datePicker = new DatePicker(LocalDate.now().plusDays(1));
		timeLabel = new Label("Zeit:");
		toLabel = new Label(" bis ");
		roomLabel = new Label("Raum:");
		roomTextField = new TextField();
		saveButton = new Button("Speichern");
		errorLabel = new Label("");
		errorLabel.setTextFill(Color.RED);
		
		startTimeHBox = new HBox();
		startHourTextField = new TextField("00");
		startMinuteTextField = new TextField("00");
		startTimeHBox.getChildren().addAll(startHourTextField,new Label(":"),startMinuteTextField);
		
		endTimeHBox = new HBox();
		endHourTextField = new TextField("00");
		endMinuteTextField = new TextField("00");
		endTimeHBox.getChildren().addAll(endHourTextField,new Label(":"),endMinuteTextField);
		
		//===========================================
			
		add(dateLabel,0,0);
		add(datePicker,1,0);
		
		add(roomLabel,0,1);
		add(roomTextField,1,1);
		
		add(timeLabel,0,2);
		add(startTimeHBox,1,2);
		
		add(toLabel, 0,3);
		add(endTimeHBox,1,3);
		
		add(errorLabel,0,4,2,1);
		GridPane.setHalignment(errorLabel, HPos.RIGHT);
		
		add(saveButton,1,5);
		GridPane.setHalignment(saveButton, HPos.RIGHT);
		
		//=======================================
		
		//|| startHourTextField.getText().length() < 2 || Integer.parseInt(startHourTextField.getText().substring(0, 1))>2 || Integer.parseInt(startHourTextField.getText().substring(1, 2))>4
		
		startHourTextField.setOnKeyTyped(event ->{
	        if(startHourTextField.getText().length() >= 2) 
	        {
	        	event.consume();
	        }
	    });
		
		// || startMinuteTextField.getText().length() < 2 || Integer.parseInt(startMinuteTextField.getText().substring(0, 1))>2 || Integer.parseInt(startMinuteTextField.getText().substring(1, 2))>4
		startMinuteTextField.setOnKeyTyped(event ->{
	        if(startMinuteTextField.getText().length() >= 2) 
	        {
	        	event.consume();
	        }
	    });
		
		// || endHourTextField.getText().length() < 2 || Integer.parseInt(endHourTextField.getText().substring(0, 1))>2 || Integer.parseInt(endHourTextField.getText().substring(1, 2))>4
		endHourTextField.setOnKeyTyped(event ->{
	        if(endHourTextField.getText().length() >= 2) 
	        {
	        	event.consume();
	        }
	    });
		
		// || endMinuteTextField.getText().length() < 2 || Integer.parseInt(endMinuteTextField.getText().substring(0, 1))>2 || Integer.parseInt(endMinuteTextField.getText().substring(1, 2))>4
		endMinuteTextField.setOnKeyTyped(event ->{
	        if(endMinuteTextField.getText().length() >= 2) 
	        {
	        	event.consume();
	        }
	    });
		
		saveButton.setOnAction(event -> {
			
			
			int startHour;
			int startMinute;
			int endHour;
			int endMinute;
			
			
			//---------------------------------------------------------
			
			
			try
			{
				startHour = Integer.parseInt(startHourTextField.getText());
				startMinute = Integer.parseInt(startMinuteTextField.getText());
				endHour = Integer.parseInt(endHourTextField.getText());
				endMinute = Integer.parseInt(endMinuteTextField.getText());
			}
			//error check beim Auslesen
			catch(Exception e)
			{
				errorLabel.setText("Fehler bei der Zeit-Eingabe");
				return;
			}
			
			//Ausgelesene Zeiten nicht legitim
			if(startHour > 23 || startHour <0 || startMinute > 59 || startMinute < 0)
			{
				errorLabel.setText("Die ausgewählte Start-Zeit ist nicht legitim");
				return;
			}
			if(endHour > 23 || endHour <0 || endMinute > 59 || endMinute < 0)
			{
				errorLabel.setText("Die ausgewählte End-Zeit ist nicht legitim");
				return;
			}
			
			long startTime = Time.valueOf( startHour + ":"+ startMinute +":00").getTime();
			long endTime = Time.valueOf( endHour + ":"+ endMinute +":00").getTime();
			Date date = Date.valueOf(datePicker.getValue());
			
			//Test: Start- vor Endzeit
			if(startTime > endTime)
			{
				errorLabel.setText("Die Start-Zeit muss vor der End-Zeit liegen");
				return;
			}
			
			//Test: überschneidende Termine
			Appointment clashingAppointment = presenter.checkAppointmentAvailability(date, startTime, endTime);
			
			//Es gibt eine Überschneidung mit einem Termin im Anliegen clashingConcern
			if(clashingAppointment != null)
			{
				Concern clashingConcern = presenter.getConcern(clashingAppointment);
				errorLabel.setText("Überschneidung mit einem Termin im Anliegen " + clashingConcern.getTitle() + "\n"
									+ "in der Zeit von " + clashingAppointment.getStartTimeString() + " bis " + clashingAppointment.getEndTimeString());
				return;
			}
			
			//End:errorCheck
			//---------------------------------------------------------
			
			
			String roomNmbr = roomTextField.getText();
			Appointment newAppointment = new Appointment(date, startTime, endTime, roomNmbr);
			concernView.addAppointment(newAppointment);
			stage.close();
		});
		
		
		
	}

}
