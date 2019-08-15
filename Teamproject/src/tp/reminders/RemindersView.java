package tp.reminders;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import tp.Presenter;
import tp.model.Reminder;

public class RemindersView extends GridPane{
	
	private Presenter presenter;
	
	//=================================
	
	private ListView<Reminder> remindersList;
	private Label remindersLabel;
	private Button toConcernButton;
	private Button deleteReminderButton;
	
	public RemindersView(Presenter presenter)
	{
		this.presenter = presenter;
		buildView();
	}

	private void buildView() {
		
		setPadding(new Insets(10, 10, 10, 10));
		setHgap(10);
		setVgap(10);
		
		remindersList = new ListView<Reminder>();
		remindersList.setItems(presenter.getDueReminders());
		remindersLabel = new Label("Zu bearbeitende Erinnerungen");
		toConcernButton = new Button("Zum zugehörigen Anliegen");
		deleteReminderButton = new Button("Erinnerung löschen");
		
		//==============================================
		
		
		
		//==============================================
		
		add(remindersLabel,0,0);
		GridPane.setHalignment(remindersLabel, HPos.LEFT);
		add(remindersList,0,1,2,1);
		add(toConcernButton,0,2);
		GridPane.setHalignment(toConcernButton, HPos.LEFT);
		add(deleteReminderButton,1,2);
		GridPane.setHalignment(toConcernButton, HPos.RIGHT);
		
	}

	public void updateView() {
		remindersList.setItems(presenter.getDueReminders());
	}
	
	

}
