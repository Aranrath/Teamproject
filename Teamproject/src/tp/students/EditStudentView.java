package tp.students;

import java.io.File;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tp.Presenter;
import tp.model.Concern;
import tp.model.MyTab;
import tp.model.PO;
import tp.model.Student;
import tp.model.Subject;
import tp.options.EditPOView;

public class EditStudentView extends GridPane {

	private Presenter presenter;
	private Student student;
	private MyTab tab;
	private ObservableList<Concern> concerns;
	
	private ObservableList<Subject> localPassedSubjects;

	// ====================================

	private ImageView studentImage;
	private Button takePictureButton;
	private Button searchPictureButton;
	private Label nameLabel;
	private TextField studentFirstName;
	private TextField studentLastName;
	private Label mailLabel;
	private TextField studentMail_1;
	private TextField studentMail_2;
	private TextField studentMail_3;
	private Label mtrNrLabel;
	private TextField studentMtrNr;
	private Label poLabel;
	private ComboBox<PO> studentPO;
	private Button newPOButton;
	private Label semesterLabel;
	private TextField studentSemester;
	private Label ectsLabel;
	private Label studentECTS;
	private Button passedSubjectsButton;
	
	private Label concernsLabel;
	private Button newConcernButton;
	private Button deleteConcernButton;
	private ListView<Concern> concernsListView;
	private Label errorLabel;
	private Button saveButton;
	private Label notesLabel;
	private TextArea studentNotes;

	private Label genderLabel;
	private ComboBox<String> genderComboBox;

	public EditStudentView(Presenter presenter, MyTab tab) {
		this.presenter = presenter;
		this.tab = tab;
		localPassedSubjects = FXCollections.observableArrayList();
		
		buildView();
	}

	public EditStudentView(Presenter presenter, Student student, MyTab tab) {
		this.presenter = presenter;
		this.student = student;
		this.tab = tab;
		localPassedSubjects = FXCollections.observableArrayList(student.getPassedSubjects());
		
		buildView();
		fillView();
	}

	private void buildView() {

		setPadding(new Insets(10, 10, 10, 10));
		setHgap(10);
		setVgap(10);

		studentImage = new ImageView();
		studentImage.setFitWidth(300);
		studentImage.setPreserveRatio(true);
		studentImage.fitHeightProperty().bind(heightProperty().divide(3));
		
		GridPane.setHalignment(studentImage, HPos.CENTER);

		takePictureButton = new Button("Bild aufnehmen");
		searchPictureButton = new Button("Bild laden");
		nameLabel = new Label("Name");
		studentFirstName = new TextField();
		studentFirstName.setPromptText("Vorname");
		studentLastName = new TextField();
		studentLastName.setPromptText("Nachname");
		mailLabel = new Label("E-Mail Adressen");
		studentMail_1 = new TextField();
		studentMail_1.setPromptText("mustermann@fh-trier.de");
		studentMail_2 = new TextField();
		studentMail_2.setPromptText("mustermann@gmail.com");
		studentMail_3 = new TextField();
		studentMail_3.setPromptText("musterteam@gmail.com");
		mtrNrLabel = new Label("Mtr.Nr");
		studentMtrNr = new TextField();
		studentMtrNr.setPromptText("000000");

		poLabel = new Label("Studiengang");
		studentPO = new ComboBox<PO>(presenter.getPOs());
		newPOButton = new Button("+");
		HBox poHBox = new HBox(studentPO, newPOButton);
		newPOButton.setMinSize(Button.USE_PREF_SIZE, Button.USE_PREF_SIZE);
		studentPO.setMaxWidth(Double.MAX_VALUE);
		HBox.setHgrow(studentPO, Priority.ALWAYS);
		
		ectsLabel = new Label("ECTS");
		studentECTS = new Label("" + 0);
		passedSubjectsButton = new Button("Bestandene Fächer ändern");
		
		semesterLabel = new Label("Semester");
		studentSemester = new TextField();
		studentSemester.setPromptText("0");
		concernsLabel = new Label("Anliegen");
		concernsLabel.setVisible(false);
		concerns = FXCollections.observableArrayList();
		newConcernButton = new Button("Neues Anliegen");
		newConcernButton.setVisible(false);
		deleteConcernButton = new Button("Anliegen entfernen");
		deleteConcernButton.setVisible(false);
		
		concernsListView = new ListView<Concern>(concerns);
		concernsListView.setVisible(false);
		errorLabel = new Label("MatrikelNr und Name müssen gesetzt sein");
		errorLabel.setTextFill(Color.RED);
		errorLabel.setVisible(false);
		saveButton = new Button("Profil speichern");
		notesLabel = new Label("Notizen");
		studentNotes = new TextArea();
		studentNotes.setPromptText("Ein Student.");
		genderLabel = new Label("Geschlecht");
		ObservableList<String> genderOptions = FXCollections.observableArrayList("unbekannt", "männlich", "weiblich",
				"divers");
		genderComboBox = new ComboBox<String>(genderOptions);
		genderComboBox.setValue("unbekannt");
		genderComboBox.setMaxWidth(Double.MAX_VALUE);
	
		
		// =====================================
		add(studentImage, 0, 0, 1, 8);
		
		HBox pictureButtonHBox = new HBox(searchPictureButton, takePictureButton);
		add(pictureButtonHBox, 0, 8);
		pictureButtonHBox.setAlignment(Pos.CENTER);
		if (student == null) {
			studentImage.setImage(presenter.getDefaultStudentImage());
		}

		add(nameLabel, 1, 0);
		add(studentFirstName, 2, 0);
		add(studentLastName, 3, 0);

		add(mtrNrLabel, 1, 1);
		add(studentMtrNr, 2, 1);

		add(poLabel, 1, 2);
		add(poHBox, 2, 2, 2, 1);

		add(mailLabel, 1, 3);
		add(studentMail_1, 2, 3, 2, 1);
		add(studentMail_2, 2, 4, 2, 1);
		add(studentMail_3, 2, 5, 2, 1);

		add(ectsLabel, 1, 6);
		add(studentECTS, 2, 6);
		add(passedSubjectsButton, 3, 6);
		
		add(semesterLabel, 1, 7);
		add(studentSemester, 2, 7);
		add(genderLabel, 1, 8);
		add(genderComboBox, 2, 8);

		add(concernsLabel, 4, 2, 2, 1);
		GridPane.setHalignment(concernsLabel, HPos.LEFT);
		add(newConcernButton, 5, 8);
		GridPane.setHalignment(newConcernButton, HPos.RIGHT);
		add(deleteConcernButton,4,8);
		
		add(concernsListView, 4, 3, 2, 5);

		add(errorLabel, 3, 1, 3, 1);
		GridPane.setHalignment(errorLabel, HPos.RIGHT);
		add(saveButton, 5, 0);
		GridPane.setHalignment(saveButton, HPos.RIGHT);

		add(notesLabel, 0, 9);
		GridPane.setHalignment(notesLabel, HPos.LEFT);
		add(studentNotes, 0, 10);

		// ===================================================================

		ColumnConstraints col0 = new ColumnConstraints();
		col0.setPercentWidth(30);
		ColumnConstraints col1 = new ColumnConstraints();
		col1.setPercentWidth(10);
		ColumnConstraints col2 = new ColumnConstraints();
		col2.setPercentWidth(15);
		ColumnConstraints col3 = new ColumnConstraints();
		col3.setPercentWidth(15);
		ColumnConstraints col4 = new ColumnConstraints();
		col4.setPercentWidth(15);
		ColumnConstraints col5 = new ColumnConstraints();
		col5.setPercentWidth(15);

		getColumnConstraints().addAll(col0, col1, col2, col3, col4, col5);

		// ===================================================================

		takePictureButton.setOnAction((event) -> {
			Stage stage = new Stage();
			stage.setAlwaysOnTop(true);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setTitle("Foto aufnehmen");
			stage.setResizable(false);
			stage.setScene(new Scene(new TakeImageView(stage, presenter, this), getWidth()*(0.6), getHeight()*(0.7)));
			stage.show();
		});
		
		passedSubjectsButton.setOnAction((event) -> {
			
            if(studentPO.getSelectionModel().getSelectedItem() == null)
            {
            	errorLabel.setText("PO muss ausgewählt sein um bestandene Fächer auszuwählen");
        		errorLabel.setVisible(true);
        		return;
            }
            else
            {
    			Stage stage = new Stage();
    			stage.setAlwaysOnTop(true);
    			stage.initModality(Modality.APPLICATION_MODAL);
                stage.setTitle("Bestandene Fächer auswählen");
                
                if(localPassedSubjects == null)
                {
                	stage.setScene(new Scene(new SelectPassedSubjectsView(stage, presenter, this, studentPO.getSelectionModel().getSelectedItem()), 800, 500));
                }
                else
                {
                	stage.setScene(new Scene(new SelectPassedSubjectsView(stage, presenter, this, studentPO.getSelectionModel().getSelectedItem(), localPassedSubjects), 800, 500));
                }
                
                stage.setResizable(false);
                stage.show();
            }
            
		});
		
		searchPictureButton.setOnAction((event)->{
			
			FileChooser fileChooser = new FileChooser();
            
            //Set extension filter
            FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
            FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
            fileChooser.getExtensionFilters().addAll(extFilterJPG, extFilterPNG);
              
            //Show open file dialog
            File file = fileChooser.showOpenDialog(null);
                       
            try {
            	  studentImage.setImage(new Image(file.toURI().toString()));
            } catch (Exception e)
            {
            }

		
		});


		newConcernButton.setOnAction((event) -> {
			ObservableList<Student> list = FXCollections.observableArrayList();
			list.add(student);
			presenter.openNewConcernTab(list);

		});
		
		deleteConcernButton.setOnAction((event) -> {
			Concern selectedConcern = concernsListView.getSelectionModel().getSelectedItem();
			if(selectedConcern!= null)
			{
				selectedConcern.getStudents().remove(student);
				concernsListView.getItems().remove(selectedConcern);
			}
		});
		
		concernsListView.setOnMousePressed(new EventHandler<MouseEvent>() {
		    @Override 
		    public void handle(MouseEvent event) {
		        if (event.isPrimaryButtonDown() && event.getClickCount() > 1) {
		        	Concern selectedConcern = concernsListView.getSelectionModel().getSelectedItem();
		        	if(selectedConcern != null)
		        	{
		        		presenter.openConcernTab(selectedConcern);
		        	}
		                               
		        }
		    }
		});

		saveButton.setOnAction((event) -> {
			
			//-----------------------------Auszulesende Attribute

			String name;
			int mtrNr;
			ArrayList<String> eMailAddresses;
			String firstName;
			Image image;
			PO po;
			int semester;
			ObservableList<Concern> concerns;
			String notes;
			String gender;
			
			//-----------------------------Auslesen (und evtl. Werte checken)
			
			ArrayList<String> changedMailAddresses;

			//Pflichtfelder auslesen:
			if (studentMtrNr.getText().isEmpty() || studentLastName.getText().isEmpty())
			{
				errorLabel.setText("MatrikelNr und Name müssen gesetzt sein");
				errorLabel.setVisible(true);
				return;
				
			}
			else
			{
				try
				{
					mtrNr = Integer.parseInt(studentMtrNr.getText());
					name = studentLastName.getText();
				}
				catch (Exception e) {
					errorLabel.setText("MatrikelNr nicht zulässig");
					errorLabel.setVisible(true);
					return;
				}
			}
			
			if((student == null && presenter.mtrAlreadyExists(mtrNr)) || (student != null && student.getMtrNr() != mtrNr && presenter.mtrAlreadyExists(mtrNr)) )
			{
				errorLabel.setText("Matrikelnummer bereits vergeben");
				errorLabel.setVisible(true);
			}
			
			//EMail Adressen auslesen
			eMailAddresses = new ArrayList<String>();
			if (!studentMail_1.getText().isEmpty()) {
				eMailAddresses.add(studentMail_1.getText());
			}
			if (!studentMail_2.getText().isEmpty()) {
				eMailAddresses.add(studentMail_2.getText());
			}
			if (!studentMail_3.getText().isEmpty()) {
				eMailAddresses.add(studentMail_3.getText());
			}
			
			firstName = studentFirstName.getText().trim();
			if(studentImage.getImage() != presenter.getDefaultStudentImage())
			{
				image = studentImage.getImage();
			}
			else
			{
				image = null;
			}
			po = studentPO.getSelectionModel().getSelectedItem();

			if(!studentSemester.getText().isEmpty())
			{
				try
				{
					semester = Integer.parseInt(studentSemester.getText());
				}
				catch(Exception e)
				{
					errorLabel.setText("Semesterangabe ungültig");
					errorLabel.setVisible(true);
					return;
				}
			}
			else
			{
				semester = 0;
			}


			
			concerns = concernsListView.getItems();
			notes = studentNotes.getText();
			gender = genderComboBox.getValue();
			
			
			
			//----------------------------Speichern
			
			//Speichern wenn noch kein Student exisitert
			if (student == null)
			{
				changedMailAddresses = new ArrayList<String>();
				
				student = new Student(mtrNr, name);
				student.seteMailAddresses(eMailAddresses);
				student.setFirstName(firstName);
				student.setImage(image);
				student.setPo(po);
				student.setSemester(semester);
				student.setPassedSubjects(localPassedSubjects);
				student.setNotes(notes);
				student.setGender(gender);
				
				presenter.saveNewStudent(student);
				
			}
			//Bestehenden Studenten ändern und speichern
			else
			{

				changedMailAddresses = new ArrayList<String>(eMailAddresses);
				changedMailAddresses.removeAll(student.geteMailAddresses());
				
				if(student.getMtrNr() != mtrNr)
				{
					presenter.changeStudentMtrNr(student.getMtrNr(), mtrNr);
					student.setMtrNr(mtrNr);
				}
				student.setName(name);
				student.seteMailAddresses(eMailAddresses);
				student.setFirstName(firstName);
				student.setImage(image);
				student.setPo(po);
				student.setSemester(semester);
				student.setPassedSubjects(localPassedSubjects);
				ObservableList<Long> concernIds = FXCollections.observableArrayList();
				for (Concern c : concerns) {
					concernIds.add(c.getId());
				}
				student.setConcernIds(concernIds);
				student.setNotes(notes);
				student.setGender(gender);

				presenter.saveEditedStudent(student);

			}

			presenter.openStudenTab(student, changedMailAddresses);
			presenter.closeThisTab(tab);

		});

		newPOButton.setOnAction((event) -> {
			Stage stage = new Stage();
			stage.setAlwaysOnTop(true);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setTitle("Neue PO");
			stage.setResizable(false);
			stage.setScene(new Scene(new EditPOView(stage, presenter, (EditStudentView) newPOButton.getParent().getParent()), getWidth()*(0.6), getHeight()*(0.7)));
			stage.show();
		});
		
		
		studentPO.setOnAction((event)-> {
			updateEctsDisplay();
		});
		 
		
	}

	private void fillView() {
		concernsLabel.setVisible(true);
		concernsListView.setVisible(true);
		newConcernButton.setVisible(true);
		deleteConcernButton.setVisible(true);

		if (student.getImage() != null) {
			studentImage.setImage(student.getImage());
		} else {
			studentImage.setImage(presenter.getDefaultStudentImage());
		}
		studentFirstName.setText(student.getFirstName());
		studentLastName.setText(student.getName());
		if (student.geteMailAddresses().size() >= 1) {
			studentMail_1.setText(student.geteMailAddresses().get(0));
		}
		if (student.geteMailAddresses().size() >= 2) {
			studentMail_2.setText(student.geteMailAddresses().get(1));
		}
		if (student.geteMailAddresses().size() == 3) {
			studentMail_3.setText(student.geteMailAddresses().get(2));
		}
		
		studentMtrNr.setText(student.getMtrNrString());
		
		studentPO.getSelectionModel().select(student.getPo());
		studentECTS.setText("" + presenter.calculateEcts(student.getPassedSubjects(), student.getPo()));
		studentSemester.setText("" + student.getSemester());
		if (student.getConcernIds()!=null) {
			concerns.clear();
			for (long id : student.getConcernIds()){
				concerns.add(presenter.getConcern(id));
			}
		}
		studentNotes.setText(student.getNotes());
		genderComboBox.setValue(student.getGender());
	}
	


	public void updateImage(Image image) {
		studentImage.setImage(image);
	}

	public void updateEctsDisplay() {
		if (studentPO.getSelectionModel().getSelectedItem() != null)
		{
			studentECTS.setText("" + presenter.calculateEcts(localPassedSubjects, studentPO.getSelectionModel().getSelectedItem()));
		}
		else
		{
			studentECTS.setText("");
		}
	}
	
	public void updatePassedSubjects(ObservableList<Subject> updatedPassedSubjects)
	{
		localPassedSubjects = FXCollections.observableArrayList(updatedPassedSubjects);
		updateEctsDisplay();
	}
	
	public void addNewPO(PO po)
	{
		studentPO.getItems().add(po);
		studentPO.getSelectionModel().select(po);
	}

	public void updateView() {
		
		PO selectedPO = studentPO.getSelectionModel().getSelectedItem();
		studentPO.setItems(presenter.getPOs());
		
		//PO Auswahl wiederherstellen
		if(selectedPO!= null)
		{
			for (PO po :studentPO.getItems())
			{
				if(po.getId() == selectedPO.getId())
				{
					studentPO.getSelectionModel().select(po);
					break;
				}
			}
			
		}
		updateEctsDisplay();

		//Anliegen neu laden
		if (student!= null && student.getConcernIds()!=null) {
			concerns.clear();
			for (long id : student.getConcernIds()){
				concerns.add(presenter.getConcern(id));
			}
		}
		
	}
	
	
}
