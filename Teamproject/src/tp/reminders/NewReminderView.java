package tp.reminders;

import java.sql.Date;
import java.time.LocalDate;

import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import tp.model.Reminder;

public class NewReminderView extends GridPane{
	
	private Stage stage;
	private ObservableList<Reminder> reminders;
	
	//=======================================
	
	private Label dateLabel;
	private DatePicker datePicker;
	private TextArea messageTextArea;
	private Label errorLabel;
	private Button saveButton;
	

	public NewReminderView(Stage stage, ObservableList<Reminder> reminders) {

		this.stage = stage;
		this.reminders = reminders;
		buildView();
	}


	private void buildView() {
		setPadding(new Insets(10, 10, 10, 10));
		setHgap(10);
		setVgap(10);

		dateLabel = new Label("Datum:");
		datePicker= new DatePicker(LocalDate.now().plusDays(7));
		messageTextArea = new TextArea();
		errorLabel = new Label("Nachricht muss gesetzt sein");
		errorLabel.setTextFill(Color.RED);
		errorLabel.setVisible(false);
		saveButton = new Button("Speichern");
		
		//===========================================
			
		add(dateLabel,0,0);
		add(datePicker,1,0);
		add(messageTextArea,0,1,2,1);
		add(errorLabel,0,2,2,1);
		GridPane.setHalignment(errorLabel, HPos.RIGHT);
		add(saveButton,0,3,2,1);
		GridPane.setHalignment(saveButton, HPos.RIGHT);
		
		//=======================================
		
		saveButton.setOnAction(event -> {
			Date date = Date.valueOf(datePicker.getValue());
			String message = messageTextArea.getText();
			
			
			if(message.equals("") || message == null)
			{
				errorLabel.setVisible(true);
			}
			else
			{
				reminders.add(new Reminder(message,date));
				stage.close();
				
			}
			
		});
		
	}

}
