package tp.concern;

import java.sql.Date;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tp.Presenter;
import tp.appointment.NewAppointmentView;
import tp.forms.FormsView;
import tp.model.Appointment;
import tp.model.Concern;
import tp.model.Form;
import tp.model.MyTab;
import tp.model.Reminder;
import tp.model.Student;
import tp.model.Topic;
import tp.options.EditTopicView;
import tp.reminders.NewReminderView;

public class ConcernView extends GridPane {

	private Presenter presenter;
	private Concern concern;
	private MyTab tab;

	// ==================================

	private Label titleLabel;
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
	private Button deleteFileButton;
	private TableView<Form> fileTableView;

	private Label appointmentLabel;
	private HBox appointmentHBox;
	private Button newAppointmentButton;
	private Button deleteAppointmentButton;
	private TableView<Appointment> appointmentTableView;

	// neue ConcernView
	public ConcernView(Presenter presenter, MyTab tab) {
		this.presenter = presenter;
		this.tab = tab;
		buildView();
		titleTextField.setText(new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime()));
	}

	// neue Concern View mit bereits gewählten Studenten
	public ConcernView(Presenter presenter, MyTab tab, ObservableList<Student> students) {
		this.presenter = presenter;
		this.tab = tab;
		buildView();
		titleTextField.setText(new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime()));

		// Studenten einsetzen
		studentTableView.setItems(students);
	}

	public ConcernView(Presenter presenter, MyTab tab, Concern concern) {
		this.presenter = presenter;
		this.tab = tab;
		this.concern = concern;
		buildView();
		fillView();
	}

	@SuppressWarnings("unchecked")
	private void buildView() {
		setPadding(new Insets(20));
		setHgap(20);
		setVgap(20);

		titleLabel = new Label("Titel:");
		titleTextField = new TextField("");
		errorLabel = new Label("eror :D");
		if (concern == null) {
			saveButton = new Button("Anliegen erstellen");
		} else {
			saveButton = new Button("Änderungen speichern");
		}
		topicLabel = new Label("Thema:");
		topicComboBox = new ComboBox<Topic>(presenter.getTopics());
		newTopicButton = new Button("+");
		topicHBox = new HBox(topicComboBox, newTopicButton);
		newTopicButton.setMinSize(Button.USE_PREF_SIZE, Button.USE_PREF_SIZE);
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
		deleteFileButton = new Button("Löschen");
		fileHBox = new HBox(addFileButton, deleteFileButton);
		fileTableView = new TableView<Form>();

		appointmentLabel = new Label("Termine");
		newAppointmentButton = new Button("Neu");
		deleteAppointmentButton = new Button("Löschen");
		appointmentHBox = new HBox(newAppointmentButton, deleteAppointmentButton);
		appointmentTableView = new TableView<Appointment>();
		
		// ======================================================================
		
		TableColumn<Student, Integer> mtrNrCol = new TableColumn<Student, Integer>("Matrikelnr.");
		mtrNrCol.setCellValueFactory(new PropertyValueFactory<>("mtrNr"));
		
		TableColumn<Student, String> nameCol = new TableColumn<Student, String>("Nachname");
		nameCol.setCellValueFactory(new PropertyValueFactory<Student, String>("name"));
		
		TableColumn<Student, String> firstNameCol = new TableColumn<Student, String>("Vorname");
		firstNameCol.setCellValueFactory(new PropertyValueFactory<Student, String>("firstName"));

		studentTableView.getColumns().addAll(mtrNrCol, nameCol, firstNameCol);


		TableColumn<Reminder, Date> dateCol = new TableColumn<Reminder, Date>("Datum");
		dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));

		TableColumn<Reminder, String> messageCol = new TableColumn<Reminder, String>("Nachricht");
		messageCol.setCellValueFactory(new PropertyValueFactory<>("message"));

		dateCol.setResizable(false);
		messageCol.setResizable(false);

		double width = dateCol.widthProperty().get();
		messageCol.prefWidthProperty().bind(reminderTableView.widthProperty().subtract(width));

		reminderTableView.getColumns().addAll(dateCol, messageCol);
		
		
		TableColumn<Form, String> fileNameCol = new TableColumn<Form, String>("Dateiname");
		fileNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
		fileTableView.getColumns().add(fileNameCol);
		
		
		TableColumn<Appointment, Date> appDateCol = new TableColumn<Appointment, Date>("Datum");
		appDateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
		
		TableColumn<Appointment, Long> startTimeCol = new TableColumn<Appointment, Long>("Von");
		startTimeCol.setCellValueFactory(new PropertyValueFactory<>("startTimeString"));
		
		TableColumn<Appointment, Long> endTimeCol = new TableColumn<Appointment, Long>("Bis");
		endTimeCol.setCellValueFactory(new PropertyValueFactory<>("endTimeString"));
		
		TableColumn<Appointment, String> roomNmbCol = new TableColumn<Appointment, String>("Raum");
		roomNmbCol.setCellValueFactory(new PropertyValueFactory<>("roomNmb"));
		
		appointmentTableView.getColumns().addAll(appDateCol, startTimeCol, endTimeCol, roomNmbCol);

		// ======================================================================

		add(titleLabel, 0, 0);
		add(titleTextField, 1, 0, 3, 1);
		add(errorLabel, 4, 1, 2, 1);
		GridPane.setHalignment(errorLabel, HPos.LEFT);
		add(saveButton, 4, 0, 2, 1);
		GridPane.setHalignment(saveButton, HPos.LEFT);
		add(topicLabel, 0, 1);
		add(topicHBox, 1, 1, 3, 1);

		add(studentLabel, 0, 2);
		add(searchHBox, 1, 2, 3, 1);
		add(studentHBox, 4, 2, 2, 1);
		studentHBox.setSpacing(5);
		studentHBox.setAlignment(Pos.CENTER_RIGHT);
		add(studentTableView, 0, 3, 6, 1);

		add(notesLabel, 6, 0);
		add(notesTextArea, 6, 1, 3, 3);

		add(reminderLabel, 0, 4);
		add(reminderHBox, 1, 4, 2, 1);
		add(reminderTableView, 0, 5, 3, 1);
		reminderHBox.setSpacing(5);
		reminderHBox.setAlignment(Pos.CENTER_RIGHT);

		add(fileLabel, 3, 4);
		add(fileHBox, 4, 4, 2, 1);
		fileHBox.setSpacing(5);
		fileHBox.setAlignment(Pos.CENTER_RIGHT);
		add(fileTableView, 3, 5, 3, 1);

		add(appointmentLabel, 6, 4);
		add(appointmentHBox, 7, 4, 2, 1);
		appointmentHBox.setSpacing(5);
		appointmentHBox.setAlignment(Pos.CENTER_RIGHT);
		add(appointmentTableView, 6, 5, 3, 1);

		// ==================================================

		ColumnConstraints column = new ColumnConstraints();
		column.setPercentWidth(100 / 9);
		getColumnConstraints().add(column);
		getColumnConstraints().add(column);
		getColumnConstraints().add(column);
		getColumnConstraints().add(column);
		getColumnConstraints().add(column);
		getColumnConstraints().add(column);
		getColumnConstraints().add(column);
		getColumnConstraints().add(column);
		getColumnConstraints().add(column);

		// ==================================================
		saveButton.setOnAction((event) -> {

			String newTitle = titleTextField.getText();
			Topic newTopic = topicComboBox.getSelectionModel().getSelectedItem();

			if (newTitle.equals("") || newTopic == null)
			{
				errorLabel.setText("Titel und Thema müssen gesetzt sein");
				errorLabel.setTextFill(Color.RED);
				return;
			}
			else if (concernTitleAlreadyExists(newTitle))
			{
				errorLabel.setText("Titel bereits vergeben");
				errorLabel.setTextFill(Color.RED);
				return;
			}
			else if (concern == null)
			{
				// Concern mit angegebenen Titel erstellen
				concern = new Concern(newTitle, newTopic);
				
				//übrige Attribute auslesen uns setzen
				concern.setStudents(studentTableView.getItems());
				concern.setNotes(notesTextArea.getText());
				concern.setReminders(reminderTableView.getItems());
				concern.setAppointments(appointmentTableView.getItems());
				
				//Speicher nur Files die nicht zum Topic gehören
				ObservableList<Form> newFiles = fileTableView.getItems();
				newFiles.removeAll(newTopic.getForms());
				concern.setFiles(newFiles);
				

				int newConcernId = presenter.saveNewConcern(concern);
				saveButton.setText("Änderungen speichern");

				// Tabbeschriftung anpassen
				tab.setText(newTitle);
				tab.setTabId("c" + newConcernId);
			} 
			else
			{
				// Attribute speichern
				concern.setTitle(newTitle);
				concern.setTopic(newTopic);
				
				concern.setStudents(studentTableView.getItems());
				concern.setNotes(notesTextArea.getText());
				concern.setReminders(reminderTableView.getItems());
				concern.setAppointments(appointmentTableView.getItems());
				
				//Speicher nur Files die nicht zum Topic gehören
				ObservableList<Form> newFiles = fileTableView.getItems();
				newFiles.removeAll(newTopic.getForms());
				concern.setFiles(newFiles);

				presenter.saveEditedConcern(concern);

				// Tabbeschriftung anpassen
				tab.setText(newTitle);
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
			if (searchTextField.getText().equals("")) {

			} else {
				ObservableList<Student> students = studentTableView.getItems();
				String searchTerms[] = searchTextField.getText().toLowerCase().split("");
				ObservableList<Student> searchResult = FXCollections.observableArrayList();
				for (Student student : students) {

					for (String mail : student.geteMailAddresses()) {
						for (String term : searchTerms) {
							if (mail.toLowerCase().contains(term)) {
								searchResult.add(student);
							}
						}

					}

					for (String term : searchTerms) {
						if (student.getFirstName().toLowerCase().contains(term)) {
							searchResult.add(student);
						}
					}

					for (String term : searchTerms) {
						if (student.getName().toLowerCase().contains(term)) {
							searchResult.add(student);
						}
					}

					for (String term : searchTerms) {
						if (("" + student.getMtrNr()).contains(term)) {
							searchResult.add(student);
						}
					}

				}

				// suchErgebnis anzeigen in TableView
				studentTableView.setItems(searchResult);
			}

		});

		addStudentButton.setOnAction((event) -> {
			Stage stage = new Stage();
			stage.setAlwaysOnTop(true);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setTitle("Studenten hinzufügen");
			stage.setScene(new Scene(new AddStudentToConcernView(presenter, stage, this, studentTableView.getItems()),
					450, 450));
			stage.show();
		});

		removeStudentButton.setOnAction((event) -> {
			studentTableView.getItems().remove(studentTableView.getSelectionModel().getSelectedItem());
		});

		newReminderButton.setOnAction((event) -> {
			Stage stage = new Stage();
			stage.setAlwaysOnTop(true);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setTitle("Neue Erinnerung");
			stage.setScene(new Scene(new NewReminderView(stage, reminderTableView.getItems()), 450, 450));
			stage.show();
		});

		deleteReminderButton.setOnAction((event) -> {
			Reminder reminderToDelete = reminderTableView.getSelectionModel().getSelectedItem();
			reminderTableView.getItems().remove(reminderToDelete);
			presenter.deleteReminder(reminderToDelete);
		});

		addFileButton.setOnAction((event) -> {
			Stage stage = new Stage();
			stage.setAlwaysOnTop(true);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setTitle("Datei zum Concern hinzufügen");
			stage.setScene(new Scene(new FormsView(presenter, stage, this, fileTableView.getItems()), 450, 450));
			stage.show();
		});

		
		deleteFileButton.setOnAction((event) -> {
			Form fileToDelete = fileTableView.getSelectionModel().getSelectedItem();
			
			//Wenn die Datei nicht zum ausgewählten Topic gehört
			if(!topicComboBox.getSelectionModel().getSelectedItem().getForms().contains(fileToDelete))
			{
				presenter.deleteForm(fileToDelete);
			}	
			
			fileTableView.getItems().remove(fileToDelete);

		});

		newAppointmentButton.setOnAction((event) -> {
			Stage stage = new Stage();
			stage.setAlwaysOnTop(true);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setTitle("Datei zum Concern hinzufügen");
			stage.setScene(new Scene(new NewAppointmentView(stage, presenter, this), 450, 450));
			stage.show();
		});

		deleteAppointmentButton.setOnAction((event) -> {
			Appointment appointmentToDelete = appointmentTableView.getSelectionModel().getSelectedItem();
			appointmentTableView.getItems().remove(appointmentToDelete);
			presenter.deleteAppointment(appointmentToDelete);
		});

		topicComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal)->{
			if(oldVal != null) {
				fileTableView.getItems().removeAll(oldVal.getForms());
			}
		    fileTableView.getItems().addAll(newVal.getForms());
		});

	}

	private boolean concernTitleAlreadyExists(String newTitle) {
		ObservableList<Concern> allConcerns = presenter.getConcerns();

		for (Concern c : allConcerns) {
			if (c.getTitle().equals(newTitle)) {
				return true;
			}

		}

		return false;
	}

	private void fillView() {
		titleTextField.setText(concern.getTitle());
		topicComboBox.getSelectionModel().select(concern.getTopic());
		studentTableView.setItems(concern.getStudents());
		reminderTableView.setItems(concern.getReminders());
		fileTableView.setItems(concern.getFiles());
		fileTableView.getItems().addAll(concern.getTopic().getForms());
		notesTextArea.setText(concern.getNotes());
		appointmentTableView.setItems(concern.getAppointments());

	}

	public void addStudentsToConcern(ObservableList<Student> students) {
		studentTableView.setItems(students);
	}
	
	public void addFilesToConcern(ObservableList<Form> files)
	{
		fileTableView.setItems(files);
	}
	
	public void addAppointment(Appointment appointment)
	{
		appointmentTableView.getItems().add(appointment);
	}

}
