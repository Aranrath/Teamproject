package tp.students;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tp.Presenter;
import tp.model.Concern;
import tp.model.PO;
import tp.model.Student;
import tp.options.EditPOView;

public class EditStudentView extends GridPane{
	
	private Presenter presenter;
	
	//====================================
	
	private ImageView studentImage;
	private Button takePictureButton;
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
	private TextField studentECTS;
	private Label concernsLabel;
//	TODO Nur in Übersicht
//	private Button newConcernButton;
	private ListView<Concern> connectedConcernsListView;
	private Label errorLabel;
	private Button saveEditedProfileButton;
	private Label notesLabel;
	private TextArea studentNotes;
	private Label mailExchangeLabel;
	private VBox mailExchangeBox;

	public EditStudentView(Presenter presenter) {
		this.presenter = presenter;
		buildView();
	}

	public EditStudentView(Presenter presenter, Student student) {
		this.presenter = presenter;
		buildView();
		fillView(student);
	}
	
	private void buildView() {
		
		setPadding(new Insets(10, 10, 10, 10));
		setHgap(10);
		setVgap(10);
		
		studentImage = new ImageView();
		studentImage.setFitWidth(300);
		studentImage.setPreserveRatio(true);
		studentImage.setSmooth(true);
		studentImage.setCache(true);
		
		takePictureButton = new Button("Bild aufnehmen");
		nameLabel = new Label ("Name");
		studentFirstName = new TextField();
		studentLastName = new TextField();
		mailLabel = new Label("E-Mail Adresse");
		studentMail_1 = new TextField();
		studentMail_2 = new TextField();
		studentMail_3 = new TextField();
		mtrNrLabel = new Label("Mtr.Nr");
		studentMtrNr = new TextField();
		poLabel = new Label ("Studiengang");
		studentPO = new ComboBox<PO>(presenter.getPOs());
		newPOButton = new Button("neue PO");
		ectsLabel = new Label("ECTS");
		studentECTS = new TextField();
		semesterLabel = new Label("Semester");
		studentSemester = new TextField();
		concernsLabel = new Label("Anliegen");
//		TODO Nur in Übersicht
//		newConcernButton = new Button("Neues Anliegen");
		connectedConcernsListView = new ListView<Concern>();
		errorLabel = new Label("Ich bin ein Error-Label, bitch");
		errorLabel.setVisible(false);
		saveEditedProfileButton = new Button("Profil speichern");
		notesLabel = new Label("Notizen");
		studentNotes = new TextArea();
		studentNotes.setPrefWidth(300);
		mailExchangeLabel = new Label("E-Mail Austausch");
		mailExchangeBox = new VBox();
		mailExchangeBox.setBackground(new Background(new BackgroundFill(Color.CORNFLOWERBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
		
		
		//=====================================
		add(studentImage,0,0,1,6);
		add(takePictureButton,0,6);
		GridPane.setHalignment(takePictureButton, HPos.CENTER);
		add(nameLabel,1,0);
		GridPane.setHalignment(nameLabel, HPos.LEFT);
		add(studentFirstName,2,0);
		GridPane.setHalignment(studentFirstName, HPos.LEFT);
		add(studentLastName,3,0);
		GridPane.setHalignment(studentLastName, HPos.LEFT);
		add(mailLabel,1,3);
		GridPane.setHalignment(mailLabel, HPos.LEFT);
		add(studentMail_1,2,3,2,1);
		GridPane.setHalignment(studentMail_1, HPos.LEFT);
		add(studentMail_2,2,4,2,1);
		GridPane.setHalignment(studentMail_2, HPos.LEFT);
		add(studentMail_3,2,5,2,1);
		GridPane.setHalignment(studentMail_3, HPos.LEFT);
		add(mtrNrLabel,1,2);
		GridPane.setHalignment(mtrNrLabel, HPos.LEFT);
		add(studentMtrNr, 2,2,2,1);
		GridPane.setHalignment(studentMtrNr, HPos.LEFT);
		add (poLabel,1,1);
		GridPane.setHalignment(poLabel, HPos.LEFT);
		add(studentPO,2,1);
		GridPane.setHalignment(studentPO, HPos.LEFT);
		add(newPOButton,3,1);
		GridPane.setHalignment(newPOButton, HPos.LEFT);
		add(ectsLabel,4,1);
		GridPane.setHalignment(ectsLabel, HPos.LEFT);
		add(studentECTS,5,1);
		GridPane.setHalignment(studentECTS, HPos.LEFT);
		add(semesterLabel,4,0);
		GridPane.setHalignment(semesterLabel, HPos.LEFT);
		add(studentSemester,5,0);
		GridPane.setHalignment(studentSemester, HPos.LEFT);
		add(concernsLabel,4,2);
		GridPane.setHalignment(concernsLabel, HPos.LEFT);
//		TODO nur in Übersicht
//		add(newConcernButton,5,2);
//		GridPane.setHalignment(newConcernButton, HPos.LEFT);
		add(connectedConcernsListView,4,3,2,3);
		add(errorLabel,1,6,4,2);
		GridPane.setHalignment(errorLabel, HPos.RIGHT);
		add(saveEditedProfileButton,5,6,1,2);
		GridPane.setValignment(saveEditedProfileButton, VPos.CENTER);
		GridPane.setHalignment(saveEditedProfileButton, HPos.RIGHT);
		add(notesLabel,0,7);
		GridPane.setHalignment(notesLabel, HPos.LEFT);
		add(studentNotes,0,8);
		add(mailExchangeLabel,1,7);
		GridPane.setHalignment(mailExchangeLabel, HPos.LEFT);
		add(mailExchangeBox,1,8,5,1);
		
		
		//=====================================
		
		takePictureButton.setOnAction((event)->{
			Stage stage = new Stage();
			stage.setAlwaysOnTop(true);
			stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Foto aufnehmen");
            stage.setScene(new Scene(new TakeImageView(stage, presenter, this), 450, 450));
            stage.show();
		});
		
		saveEditedProfileButton.setOnAction((event)->{
			//TODO
		});
		
		newPOButton.setOnAction((event)->{
			Stage stage = new Stage();
			stage.setAlwaysOnTop(true);
			stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Neue PO");
            stage.setScene(new Scene(new EditPOView(presenter.getPOs(), presenter.getSubjects(),stage, presenter), 450, 450));
            stage.show();
		});
		
//		TODO Nur bei Übersicht
//		newConcernButton.setOnAction((event)->{
//			//TODO
//		});
		
		
		
	}
	private void fillView(Student student) {
		if(student.getImage()!=null)
		{
			studentImage.setImage(student.getImage());
		}
		studentFirstName.setText(student.getFirstName());
		studentLastName.setText(student.getName());
		studentMail_1.setText(student.geteMailAddresses().get(0));
		studentMail_2.setText(student.geteMailAddresses().get(1));
		studentMail_3.setText(student.geteMailAddresses().get(2));
		studentMtrNr.setText("" + student.getMtrNr());
		studentPO.getSelectionModel().select(student.getPo());
		studentECTS.setText("" + student.getEcts());
		studentSemester.setText("" + student.getSemester());
		connectedConcernsListView = new ListView<Concern>(presenter.getConcerns(student));
		studentNotes.setText(student.getNotes());
	}

	public void updateImage(Image image)
	{
		studentImage.setImage(image);
	}
}
