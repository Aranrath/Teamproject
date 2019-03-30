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
import javafx.stage.Stage;
import tp.Presenter;
import tp.concern.ConcernView;
import tp.model.Appointment;

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
		add(timeLabel,0,1);
		add(startTimeHBox,1,1);
		add(toLabel, 2,1);
		add(endTimeHBox,3,1);
		add(roomLabel,0,2);
		add(roomTextField,1,2);
		add(saveButton,3,2);
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
			Date date = Date.valueOf(datePicker.getValue());
			long startTime = Time.valueOf( startHourTextField.getText() + ":"+ startMinuteTextField.getText() +":00").getTime();
			long endTime = Time.valueOf( endHourTextField.getText() + ":"+ endMinuteTextField.getText() +":00").getTime();
			String roomNmbr = roomTextField.getText();		
			
			Appointment newAppointment = new Appointment(date, startTime, endTime, roomNmbr);
			
			concernView.addAppointment(newAppointment);
			presenter.saveNewAppointment(newAppointment);
			stage.close();
		});
		
		
		
	}

}
