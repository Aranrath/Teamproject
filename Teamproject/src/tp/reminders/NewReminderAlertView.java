package tp.reminders;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import tp.Presenter;
import tp.model.Reminder;

public class NewReminderAlertView extends GridPane
{
	private Presenter presenter;
	private ObservableList<Reminder> newReminders;
	private Stage stage;
	
	//===========================
	
	private Label newRemindersLabel;
	private ListView<Reminder> remindersListView;
	private Label addedToRemindersViewLabel;
	private Button showRemindersViewButton;
	
	
	public NewReminderAlertView(Presenter presenter, Stage stage, ObservableList<Reminder> newReminders)
	{
		this.presenter = presenter;
		this.newReminders = newReminders;
		this.stage = stage;
		buildView();
		
	}

	private void buildView() {
		
		setPadding(new Insets(10, 10, 10, 10));
		setHgap(10);
		setVgap(10);
		
		newRemindersLabel = new Label("Neue Erinnerungen seit Ihrer letzen Sitzung:");
		remindersListView = new ListView<Reminder>(newReminders);
		addedToRemindersViewLabel = new Label("Die Erinnerungen wurden der List Ihrer zu bearbeitenden Erinnerungen hinzugefügt");
		showRemindersViewButton = new Button("Zu bearbeitende Erinnerungen anzeigen");
		
		//=============================
		
		add(newRemindersLabel,0,0);
		add(remindersListView,0,1);
		add(addedToRemindersViewLabel,0,2);
		add(showRemindersViewButton,0,3);
		
		//==============================
		
		showRemindersViewButton.setOnAction(event -> {
			stage.close();
			presenter.openRemindersTab();
		});
		
	}


}
