package tp.reminders;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
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
		
		add(remindersLabel,0,0);
		GridPane.setHalignment(remindersLabel, HPos.LEFT);
		add(remindersList,0,1,2,1);
		add(toConcernButton,0,2);
		GridPane.setHalignment(toConcernButton, HPos.LEFT);
		add(deleteReminderButton,1,2);
		GridPane.setHalignment(deleteReminderButton, HPos.RIGHT);
		
		//===================================================================
		//Constraints
								
		ColumnConstraints col = new ColumnConstraints();
		col.setPercentWidth(100 / 2);
						
		getColumnConstraints().addAll(col, col);
				
		//-------------------------------------------------
				
		RowConstraints buttonRow = new RowConstraints();
		buttonRow.setPercentHeight(20/2);
				
		RowConstraints listViewRow = new RowConstraints();
		listViewRow.setPercentHeight(80);
				
		getRowConstraints().addAll(buttonRow,listViewRow ,buttonRow);
		
		//===================================================================
		
		toConcernButton.setOnAction((event) -> {
			
			Reminder selectedReminder = remindersList.getSelectionModel().getSelectedItem();
			if(selectedReminder != null)
        	{
				Long concernId = selectedReminder.getConcernId();
        		presenter.openConcernTab(presenter.getConcern(concernId));
        	}

		});
		
		deleteReminderButton.setOnAction((event) -> {
			Reminder selectedReminder = remindersList.getSelectionModel().getSelectedItem();
			if(selectedReminder != null) {
				presenter.deleteReminder(selectedReminder);
				remindersList.getItems().remove(selectedReminder);
			}
		});
		
	}

	public void updateView() {
		remindersList.setItems(presenter.getDueReminders());
	}
	
	

}
