package tp.concern;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
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
	//lokal hinterlegte Studenten, nicht unbedingt gespeichert. Nutzen: Basis für Suchanfragen
	private ObservableList<Student> localStudents;
	private ObservableList<Student> filteredStudents;

	// ==================================

	private Label titleLabel;
	private TextField titleTextField;
	private Label errorLabel;
	private Button saveButton;
	private Button closeButton;
	private Label closeStatusLabel;
	private HBox topicHBox;
	private Label topicLabel;
	private ComboBox<Topic> topicComboBox;
	private Button newTopicButton;

	private Label studentLabel;
	private TextField searchTextField;
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
		localStudents = FXCollections.observableArrayList();
		
		buildView();
		titleTextField.setText(new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime()));
	}

	// neue Concern View mit bereits gewählten Studenten
	public ConcernView(Presenter presenter, MyTab tab, ObservableList<Student> students) {
		this.presenter = presenter;
		this.tab = tab;
		localStudents = FXCollections.observableArrayList(students);
		
		buildView();
		titleTextField.setText(new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime()));

		
	}
	
	//bei bestehendem Concern
	public ConcernView(Presenter presenter, MyTab tab, Concern concern) {
		this.presenter = presenter;
		this.tab = tab;
		this.concern = concern;
		if(concern.getStudents() != null) {
			localStudents = concern.getStudents();
		}
		else
		{
			localStudents = FXCollections.observableArrayList();
		}
		
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
		errorLabel = new Label("");
		if (concern == null)
		{
			saveButton = new Button("Anliegen erstellen");
		}
		else
		{
			saveButton = new Button("Änderungen speichern");
		}

		
		closeButton = new Button("Aniegen schließen");
		closeStatusLabel = new Label();
		closeStatusLabel.setVisible(false);
		
		if(concern == null)
		{
			closeButton.setVisible(false);
		}
		
		//Anliegen abgeschlossen
		else if(concern != null && concern.getClosingDate() != null)
		{
			closeButton.setVisible(false);
			if(concern.isCompleted() == true)
			{
				closeStatusLabel.setText("Status: Erledigt (" + concern.getClosingDate() + ")");
			}
			else
			{
				closeStatusLabel.setText("Status: Abgebrochen (" + concern.getClosingDate() + ")");
			}
			
			closeStatusLabel.setVisible(true);
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

		addStudentButton = new Button("Hinzufügen");
		removeStudentButton = new Button("Entfernen");
		studentHBox = new HBox(addStudentButton, removeStudentButton);
		filteredStudents =  FXCollections.observableArrayList(localStudents);
		studentTableView = new TableView<Student>(filteredStudents);
	
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
		add(saveButton, 4, 0);
		GridPane.setHalignment(saveButton, HPos.LEFT);
		add(closeButton, 5, 0);
		GridPane.setHalignment(closeButton, HPos.LEFT);
		
		GridPane.setHalignment(saveButton, HPos.LEFT);
		add(topicLabel, 0, 1);
		add(topicHBox, 1, 1, 3, 1);

		add(studentLabel, 0, 2);
		add(searchTextField, 1, 2, 3, 1);
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
			else if (concern == null)
			{
				if (concernTitleAlreadyExists(newTitle))
				{
					errorLabel.setText("Titel bereits vergeben");
					errorLabel.setTextFill(Color.RED);
					return;
				}
				// Concern mit angegebenen Titel erstellen
				concern = new Concern(newTitle, newTopic);
				
				//übrige Attribute auslesen uns setzen
				concern.setStudents(localStudents);
				concern.setNotes(notesTextArea.getText());
				concern.setReminders(reminderTableView.getItems());
				concern.setAppointments(appointmentTableView.getItems());
				
				//Speicher nur Files die nicht zum Topic gehören
				ObservableList<Form> newFiles = fileTableView.getItems();
				newFiles.removeAll(newTopic.getForms());
				concern.setFiles(newFiles);
				

				int newConcernId = presenter.saveNewConcern(concern);
				saveButton.setText("Änderungen speichern");
				closeButton.setVisible(true);

				// Tabbeschriftung anpassen
				tab.setText(newTitle);
				tab.setTabId("c" + newConcernId);
			} 
			else
			{
				// Attribute speichern
				concern.setTitle(newTitle);
				concern.setTopic(newTopic);
				
				concern.setStudents(localStudents);
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
			stage.setScene(new Scene(new EditTopicView(stage, presenter, this), 450, 450));
			stage.show();
		});
		
		
		searchTextField.textProperty().addListener((obs, oldText, newText) -> {
			filterStudents(newText);
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
			Student studentToRemove = studentTableView.getSelectionModel().getSelectedItem();
			localStudents.remove(studentToRemove);
			filteredStudents.remove(studentToRemove);
		});

		newReminderButton.setOnAction((event) -> {
			Stage stage = new Stage();
			stage.setAlwaysOnTop(true);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setTitle("Neue Erinnerung hinzufügen");
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
			stage.setTitle("Datei zum Anliegen hinzufügen");
			stage.setScene(new Scene(new FormsView(presenter, stage, this, fileTableView.getItems()), 450, 450));
			stage.show();
		});

		
		deleteFileButton.setOnAction((event) -> {
			Form fileToDelete = fileTableView.getSelectionModel().getSelectedItem();
			
			//Wenn die Datei nicht zum ausgewählten Topic gehört
			if(topicComboBox.getSelectionModel().getSelectedItem()!= null && !topicComboBox.getSelectionModel().getSelectedItem().getForms().contains(fileToDelete))
			{
				presenter.deleteForm(fileToDelete);
			}	
			
			fileTableView.getItems().remove(fileToDelete);

		});

		newAppointmentButton.setOnAction((event) -> {
			Stage stage = new Stage();
			stage.setAlwaysOnTop(true);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setTitle("Neuen Termin hinzufügen");
			stage.setScene(new Scene(new NewAppointmentView(stage, presenter, this), 450, 450));
			stage.show();
		});

		deleteAppointmentButton.setOnAction((event) -> {
			Appointment appointmentToDelete = appointmentTableView.getSelectionModel().getSelectedItem();
			appointmentTableView.getItems().remove(appointmentToDelete);
			presenter.deleteAppointment(appointmentToDelete);
		});
		
		closeButton.setOnAction(event ->{
			Alert alert = new Alert(AlertType.CONFIRMATION);
	        alert.setTitle("Anliegen " +  concern.getTitle() + " abschließen");
	        alert.setHeaderText("Bitte wählen sie den korrekten Abschluss-Status des Anliegen"
	        						+"\n" + "INFO: Abgeschlossene Anliegen sind (mit Ausnahme des Fehleintrages) weiterhin einsehbar."
	        						+"\n" + "ACHTUNG: Das Schließen eines Anliegens ist nicht umkehrbar");
	 
	        ButtonType completed = new ButtonType("Erledigt");
	        ButtonType uncompleted = new ButtonType("Abgebrochen");
	        ButtonType deletable = new ButtonType("Fehleintrag (Löschen)");
	 
	        // Standard ButtonTypes entfernen
	        alert.getButtonTypes().clear();
	 
	        //Eigene ButtonTypes hinzufügen
	        alert.getButtonTypes().addAll(completed, uncompleted, deletable);
	 
	        //Alert anzeigen
	        Optional<ButtonType> option = alert.showAndWait();
	 
	        //Resultat verarbeiten
	        if (option.get() == completed)
	        {
	            concern.isCompleted(true);
	            concern.setClosingDate(new java.sql.Date(Calendar.getInstance().getTime().getTime()));
	            presenter.saveEditedConcern(concern);
	            closeButton.setVisible(false);
	            
	        }
	        else if (option.get() == uncompleted)
	        {
	        	concern.setClosingDate(new java.sql.Date(Calendar.getInstance().getTime().getTime()));
	        	presenter.saveEditedConcern(concern);
	        	closeButton.setVisible(false);
	        } 
	        else if (option.get() == deletable)
	        {
	        	presenter.deleteConcern(concern);
	        	presenter.closeThisTab(tab);
	        } 
		});
		

		topicComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal)->{
			if(oldVal != null && oldVal.getForms() != null) {
				fileTableView.getItems().removeAll(oldVal.getForms());
			}
			if(newVal != null && newVal.getForms() != null) {
				fileTableView.getItems().addAll(newVal.getForms());
			}
		});
		
		
		studentTableView.setOnMousePressed(new EventHandler<MouseEvent>() {
		    @Override 
		    public void handle(MouseEvent event) {
		        if (event.isPrimaryButtonDown() && event.getClickCount() > 1) {
		        	Student selectedStudent = studentTableView.getSelectionModel().getSelectedItem();
		        	if(selectedStudent != null)
		        	{
		        		presenter.openStudenTab(selectedStudent);;
		        	}
		                               
		        }
		    }
		});

	}

	private boolean concernTitleAlreadyExists(String newTitle) {
		ArrayList<Concern> allConcerns = new ArrayList<>(presenter.getConcerns());
		
		if(concern != null)
		{
			allConcerns.remove(concern);
		}
		
		for (Concern c : allConcerns) {
			if (c.getTitle().equals(newTitle)) {
				return true;
			}

		}

		return false;
	}
	
	private void filterStudents(String searchTerm) {

		if(searchTerm.isEmpty())
		{
			filteredStudents.clear();
			filteredStudents.addAll(localStudents);
		}
		else
		{
			
			filteredStudents.clear();
			String [] searchTerms = searchTerm.toLowerCase().split(" ");
			
			
			for (Student student : localStudents)
			{
				if(containsAll(student.toString().toLowerCase(), searchTerms))
				{
					filteredStudents.add(student);
				}
				
			}
			
		}
	}
	
	public static boolean containsAll(String searchText, String ...searchTerm) {
	    for (String s : searchTerm)
	    {
	    	if (!searchText.contains(s)) 
	        {
	        	return false;
	        }
	    }
	        
	    return true;
	}


	private void fillView() {
		titleTextField.setText(concern.getTitle());
		topicComboBox.getSelectionModel().select(concern.getTopic());
		if (concern.getReminders() != null) {
			reminderTableView.setItems(concern.getReminders());
		}
		if (concern.getFiles() != null) {
			fileTableView.setItems(concern.getFiles());
		}
		if (concern.getTopic() != null && concern.getTopic().getForms() != null) {
			fileTableView.getItems().addAll(concern.getTopic().getForms());
		}
		notesTextArea.setText(concern.getNotes());
		if (concern.getAppointments() != null) {
			appointmentTableView.setItems(concern.getAppointments());
		}
		
	}

	public void addStudentsToConcern(ObservableList<Student> students) {
		localStudents.clear();
		localStudents.addAll(students);
		
		filterStudents(searchTextField.getText());
		
	}
	
	public void addFilesToConcern(ObservableList<Form> files)
	{
		fileTableView.setItems(files);
	}
	
	public void addAppointment(Appointment appointment)
	{
		appointmentTableView.getItems().add(appointment);
	}
	
	public void addNewTopic(Topic topic)
	{
		topicComboBox.getItems().add(topic);
		topicComboBox.getSelectionModel().select(topic);
	}

}
