package tp.concern;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
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
	private Label errorLabel;
	private Button saveButton;
	private HBox topicHBox;
	private Label topicLabel;
	private ComboBox<Topic> topicComboBox;
	private Button newTopicButton;

	private Label studentLabel;
	private HBox searchHBox;
	private TextField searchTextField;
	private Button searchButton;
	private HBox studentHBox;
	private Button addStudentButton;
	private Button removeStudentButton;
	private TableView<Student> studentTableView;

	private Label notesLabel;
	private TextArea notesTextArea;

	private Label reminderLabel;
	private HBox reminderHBox;
	private Button newReminderButton;
	private Button deleteReminderButton;
	private TableView<Reminder> reminderTableView;

	private Label fileLabel;
	private HBox fileHBox;
	private Button addFileButton;
	private Button removeFileButton;
	private TableView<Form> fileTableView;

	private Label appointmentLabel;
	private HBox appointmentHBox;
	private Button newAppointmentButton;
	private Button deleteAppointmentButton;
	private TableView<Appointment> appointmentTableView;

	// neue ConcernView
	public ConcernView(Presenter presenter) {
		this.presenter = presenter;
		buildView();
		titleTextField.setText(new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime()));
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
		errorLabel = new Label ("eror :D");
		if(concern == null)
		{
			saveButton = new Button("Concern erstellen");
		}
		else
		{
			saveButton = new Button("Concern speichern");
		}
		topicLabel = new Label("Thema:");
		topicComboBox = new ComboBox<Topic>(presenter.getTopics());
		newTopicButton = new Button("+");
		newTopicButton.setMinSize(Button.USE_PREF_SIZE, Button.USE_PREF_SIZE);
		topicHBox = new HBox(topicComboBox, newTopicButton);
		topicComboBox.setMaxWidth(Double.MAX_VALUE);
		HBox.setHgrow(topicComboBox, Priority.ALWAYS);
		
		studentLabel = new Label("Studenten:");
		searchTextField = new TextField("");
		searchTextField.setPromptText("Durchsuche Studenten");
		HBox.setHgrow(searchTextField, Priority.ALWAYS);
		searchButton = new Button("Suchen");
		searchButton.setMinSize(Button.USE_PREF_SIZE, Button.USE_PREF_SIZE);
		searchHBox = new HBox(searchTextField, searchButton);
		addStudentButton = new Button("Hinzufügen");
		removeStudentButton = new Button("Entfernen");
		studentHBox = new HBox(addStudentButton, removeStudentButton);
		studentTableView = new TableView<Student>();

		notesLabel = new Label("Notizen");
		notesTextArea = new TextArea();

		reminderLabel = new Label("Erinnerungen");
		newReminderButton = new Button("Neu");
		deleteReminderButton = new Button("Löschen");
		reminderTableView = new TableView<Reminder>();
		reminderHBox = new HBox(newReminderButton, deleteReminderButton);

		fileLabel = new Label("Dateien");
		addFileButton = new Button("Hinzufügen");
		removeFileButton = new Button("Entfernen");
		fileHBox = new HBox(addFileButton, removeFileButton);
		fileTableView = new TableView<Form>();

		appointmentLabel = new Label("Termine");
		newAppointmentButton = new Button("Neu");
		deleteAppointmentButton = new Button("Löschen");
		appointmentHBox = new HBox(newAppointmentButton,deleteAppointmentButton);
		appointmentTableView = new TableView<Appointment>();

		// ==================================================
		saveButton.setOnAction((event) -> {
			
			//Bearbeitungsmodus zu Anzeigemodus (titleTextField visible und Button "save", Nach Start
			if(titleTextField.isVisible())
			{
				String newTitle = titleTextField.getText();
				
				if(newTitle.equals(""))
				{
					newTitle = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
				}
				if(concern==null)
				{
					//Concern mit angegebenen Titel erstellen
					concern = new Concern(newTitle);
					presenter.saveNewConcern(concern);
				}
				else
				{
					//Geänderten Concern speichern
					concern.setTitle(newTitle);
					presenter.saveEditedConcern(concern);
				}
				
				//Buttonbeschriftung zu "Bearbeiten"
				saveButton.setText("Titel bearbeiten");
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
				saveButton.setText("Speichern");
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

		add(titleLabel, 0, 0);
		add(titleTextField, 1, 0, 3, 1);
		add(errorLabel,4,1,2,1);
		GridPane.setHalignment(errorLabel, HPos.RIGHT);
		add(saveButton, 4, 0,2,1);
		GridPane.setHalignment(saveButton, HPos.RIGHT);
		add(topicLabel, 0, 1);
		add(topicHBox, 1, 1, 3, 1);

		add(studentLabel, 0, 2);
		add(searchHBox,1,2,3,1);
		add(studentHBox,4,2,2,1);
		studentHBox.setSpacing(5);
		studentHBox.setAlignment(Pos.CENTER_RIGHT);
		add(studentTableView, 0, 3, 6, 1);

		add(notesLabel, 6, 0);
		add(notesTextArea, 6, 1, 3, 3);

		add(reminderLabel, 0, 4);
		add(reminderHBox, 1, 4,2,1);
		add(reminderTableView, 0, 5, 3, 1);
		reminderHBox.setSpacing(5);
		reminderHBox.setAlignment(Pos.CENTER_RIGHT);

		add(fileLabel, 3, 4);
		add(fileHBox, 4, 4,2,1);
		fileHBox.setSpacing(5);
		fileHBox.setAlignment(Pos.CENTER_RIGHT);
		add(fileTableView, 3, 5, 3, 1);

		add(appointmentLabel, 6, 4);
		add(appointmentHBox, 7, 4,2,1);
		appointmentHBox.setSpacing(5);
		appointmentHBox.setAlignment(Pos.CENTER_RIGHT);
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
