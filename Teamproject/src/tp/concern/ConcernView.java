package tp.concern;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import tp.Presenter;
import tp.model.Appointment;
import tp.model.Concern;
import tp.model.Form;
import tp.model.Reminder;
import tp.model.Student;
import tp.model.Topic;


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
	private Button saveNotesButton;

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
		saveNotesButton = new Button("Speichern");

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
			// TODO
		});

		newTopicButton.setOnAction((event) -> {
			// TODO
		});

		searchButton.setOnAction((event) -> {
			// TODO
		});

		addStudentButton.setOnAction((event) -> {
			// TODO
		});

		removeStudentButton.setOnAction((event) -> {
			// TODO
		});

		saveNotesButton.setOnAction((event) -> {
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
		add(saveNotesButton, 8, 0);
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
