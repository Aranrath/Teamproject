package tp.concern;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tp.Presenter;
import tp.model.Appointment;
import tp.model.Concern;
import tp.model.Form;
import tp.model.Reminder;
import tp.model.Student;
import tp.model.Topic;
import tp.options.EditTopicView;


public class ConcernView extends GridPane {

	private Presenter presenter;
	private Concern concern;

	// ==================================

	private Label titleLabel;
	private Label concernTitleLabel;
	private TextField titleTextField;
	private Button editTitleButton;

	private Label topicLabel;
	private ComboBox<Topic> topicComboBox;
	private Button newTopicButton;

	private Label studentLabel;
	private TextField searchTextField;
	private Button searchButton;
	private Button addStudentButton;
	private Button removeStudentButton;
	private TableView<Student> studentTableView;

	private Label notesLabel;
	private TextArea notesTextArea;

	private Label reminderLabel;
	private Button newReminderButton;
	private Button deleteReminderButton;
	private TableView<Reminder> reminderTableView;

	private Label fileLabel;
	private Button addFileButton;
	private Button removeFileButton;
	private TableView<Form> fileTableView;

	private Label appointmentLabel;
	private Button newAppointmentButton;
	private Button deleteAppointmentButton;
	private TableView<Appointment> appointmentTableView;

	// neu
	public ConcernView(Presenter presenter) {
		this.presenter = presenter;
		concern = new Concern();
		presenter.saveNewConcern(concern);
		buildView();
		titleTextField.setText(concern.getTitle());
	}

	public ConcernView(Presenter presenter, Concern concern) {
		this.presenter = presenter;
		this.concern = concern;
		buildView();
		fillView();
	}

	private void buildView() {
		setPadding(new Insets(20));
		setHgap(20);
		setVgap(20);

		titleLabel = new Label("Titel:");
		titleTextField = new TextField("");
		concernTitleLabel = new Label("");
		concernTitleLabel.setVisible(false);
		editTitleButton = new Button("Speichern");

		topicLabel = new Label("Thema:");
		topicComboBox = new ComboBox<Topic>(presenter.getTopics());
		newTopicButton = new Button("Neues Thema");

		studentLabel = new Label("Studenten:");
		searchTextField = new TextField("");
		searchTextField.setPromptText("Durchsuche Studenten");
		searchTextField.getParent().requestFocus();
		searchButton = new Button("Suchen");
		addStudentButton = new Button("Hinzufügen");
		removeStudentButton = new Button("Entfernen");
		studentTableView = new TableView<Student>();

		notesLabel = new Label("Notizen");
		notesTextArea = new TextArea();

		reminderLabel = new Label("Erinnerungen");
		newReminderButton = new Button("Neu");
		deleteReminderButton = new Button("Löschen");
		reminderTableView = new TableView<Reminder>();

		fileLabel = new Label("Dateien");
		addFileButton = new Button("Hinzufügen");
		removeFileButton = new Button("Entfernen");
		fileTableView = new TableView<Form>();

		appointmentLabel = new Label("Termine");
		newAppointmentButton = new Button("Neu");
		deleteAppointmentButton = new Button("Löschen");
		appointmentTableView = new TableView<Appointment>();

		// ==================================================
		editTitleButton.setOnAction((event) -> {
			
			//Bearbeitungsmodus zu Anzeigemodus (titleTextField visible und Button "save", Nach Start
			if(titleTextField.isVisible())
			{
				String newTitle = titleTextField.getText();
				if(newTitle.equals(""))
				{
					newTitle = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
				}
				//Geänderten Concern speichern
				concern.setTitle(newTitle);
				presenter.saveEditedConcern(concern);
				//Buttonbeschriftung zu "Bearbeiten"
				editTitleButton.setText("Ändern");
				//Label Beschriftung aktualisieren
				concernTitleLabel.setText(newTitle);
				//Visibilities ändern
				titleTextField.setVisible(false);
				concernTitleLabel.setVisible(true);
			}
			//Anzeigemodus zu Bearbeitungsmodus
			else if(concernTitleLabel.isVisible())
			{
				String oldTitle = concernTitleLabel.getText();
				//Buttonbeschriftung zu "Bearbeiten"
				editTitleButton.setText("Speichern");
				//TextField Beschriftung aktualisieren
				titleTextField.setText(oldTitle);
				//Visibilities ändern
				titleTextField.setVisible(true);
				concernTitleLabel.setVisible(false);
			}
		});

		newTopicButton.setOnAction((event) -> {
			Stage stage = new Stage();
			stage.setAlwaysOnTop(true);
			stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Neues Thema");
            stage.setScene(new Scene(new EditTopicView(stage, presenter), 450, 450));
            stage.show();
		});

		searchButton.setOnAction((event) -> {
			if(searchTextField.getText().equals(""))
			{
				
			}
			else
			{
				ObservableList<Student> students = studentTableView.getItems();
				String searchTerms[] = searchTextField.getText().toLowerCase().split("");
				ObservableList<Student> searchResult  = FXCollections.observableArrayList();
				for(Student student : students)
				{
					
					for(String mail : student.geteMailAddresses())
					{
						for(String term : searchTerms)
						{
							if(mail.toLowerCase().contains(term))
							{
								searchResult.add(student);
							}
						}
						
						
					}
					
					for(String term : searchTerms)
					{
						if(student.getFirstName().toLowerCase().contains(term))
						{
							searchResult.add(student);
						}
					}
					
					for(String term : searchTerms)
					{
						if(student.getName().toLowerCase().contains(term))
						{
							searchResult.add(student);
						}
					}
					
					for(String term : searchTerms)
					{
						if(("" + student.getMtrNr()).contains(term))
						{
							searchResult.add(student);
						}
					}
					
				}
				
				//suchErgebnis anzeigen in TableView
				studentTableView.setItems(searchResult);
			}
			
		});

		addStudentButton.setOnAction((event) -> {
			// TODO
		});

		removeStudentButton.setOnAction((event) -> {
			// TODO
		});


		newReminderButton.setOnAction((event) -> {
			// TODO
		});

		deleteReminderButton.setOnAction((event) -> {
			// TODO
		});

		addFileButton.setOnAction((event) -> {
			// TODO
		});

		removeFileButton.setOnAction((event) -> {
			// TODO
		});

		newAppointmentButton.setOnAction((event) -> {
			// TODO
		});

		deleteAppointmentButton.setOnAction((event) -> {
			// TODO
		});

		topicComboBox.setOnAction((event) -> {
			// TODO
		});
		
		//TODO Notes müssen beim schließen gespeichert werden!!

		// ==================================================

//		 TODO use, then remove;
//		GridPane.setHalignment(titleTextField, HPos.RIGHT);

		add(titleLabel, 0, 0);
		add(titleTextField, 1, 0, 3, 1);
		add(concernTitleLabel, 1, 0, 3, 1);
		add(editTitleButton, 4, 0);

		add(topicLabel, 0, 1);
		add(topicComboBox, 1, 1, 3, 1);
		add(newTopicButton, 4, 1);

		add(studentLabel, 0, 2);
		add(searchTextField, 1, 2, 2, 1);
		add(searchButton, 3, 2);
		add(addStudentButton, 4, 2);
		add(removeStudentButton, 5, 2);
		add(studentTableView, 0, 3, 6, 1);

		add(notesLabel, 6, 0);
		add(notesTextArea, 6, 1, 3, 3);

		add(reminderLabel, 0, 4);
		add(newReminderButton, 1, 4);
		add(deleteReminderButton, 2, 4);
		add(reminderTableView, 0, 5, 3, 1);

		add(fileLabel, 3, 4);
		add(addFileButton, 4, 4);
		add(removeFileButton, 5, 4);
		add(fileTableView, 3, 5, 3, 1);

		add(appointmentLabel, 6, 4);
		add(newAppointmentButton, 7, 4);
		add(deleteAppointmentButton, 8, 4);
		add(appointmentTableView, 6, 5, 3, 1);

	}

	private void fillView() {
		titleTextField.setText(concern.getTitle());
		topicComboBox.getSelectionModel().select(concern.getTopic());
		studentTableView.setItems(concern.getStudents());
		reminderTableView.setItems(concern.getReminders());
		fileTableView.setItems(concern.getData());
		notesTextArea.setText(concern.getNotes());
		appointmentTableView.setItems(concern.getAppointments());

	}

}
