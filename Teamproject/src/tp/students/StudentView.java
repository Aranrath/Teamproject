package tp.students;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
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
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tp.Presenter;
import tp.model.Concern;
import tp.model.EMail;
import tp.model.PO;
import tp.model.Student;
import tp.options.EditPOView;

public class StudentView extends GridPane {

	private Presenter presenter;
	private Student student;

	// ====================================

	private ImageView studentImage;
	private Button takePictureButton;
	private Label nameLabel;
	private Label studentFirstName;
	private Label studentLastName;
	private Label mailLabel;
	private Label studentMail_1;
	private Label studentMail_2;
	private Label studentMail_3;
	private Label mtrNrLabel;
	private Label studentMtrNr;
	private Label poLabel;
	private ComboBox<PO> studentPO;
	private Button newPOButton;
	private Label semesterLabel;
	private Label studentSemester;
	private Label ectsLabel;
	private Label studentECTS;
	private Label concernsLabel;
	private Button newConcernButton;
	private ListView<Concern> connectedConcernsListView;
	private Label errorLabel;
	private Button editStudentButton;
	private Label notesLabel;
	private TextArea studentNotes;

	private Label mailExchangeLabel;
	private GridPane mailGridPane;
	private ScrollPane mailExchangeScrollPane;
	private VBox mailExchangeVBox;
	private TextField mailCCTextField;
	private TextArea mailContentTextArea;
	private Button sendMailButton;

	public StudentView(Student student, Presenter presenter) {
		this.student = student;
		this.presenter = presenter;
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
		studentImage.setSmooth(true);
		studentImage.setCache(true);

		takePictureButton = new Button("Bild aufnehmen");
		nameLabel = new Label("Name");
		studentFirstName = new Label();
		studentLastName = new Label();
		mailLabel = new Label("E-Mail Adressen");
		studentMail_1 = new Label();
		studentMail_2 = new Label();
		studentMail_3 = new Label();
		mtrNrLabel = new Label("Mtr.Nr");
		studentMtrNr = new Label();
		poLabel = new Label("Studiengang");
		studentPO = new ComboBox<PO>(presenter.getPOs());
		newPOButton = new Button("neue PO");
		ectsLabel = new Label("ECTS");
		studentECTS = new Label();
		semesterLabel = new Label("Semester");
		studentSemester = new Label();
		concernsLabel = new Label("Anliegen");
		newConcernButton = new Button("Neues Anliegen");
		connectedConcernsListView = new ListView<Concern>();
		errorLabel = new Label("Ich bin ein Error-Label, bitch");
		errorLabel.setVisible(false);
		editStudentButton = new Button("Profil speichern");
		notesLabel = new Label("Notizen");
		studentNotes = new TextArea();
		studentNotes.setPrefWidth(300);

		mailExchangeLabel = new Label("E-Mail Austausch");
		mailGridPane = new GridPane();
		mailGridPane.setBackground(
				new Background(new BackgroundFill(Color.CORNFLOWERBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
		mailExchangeScrollPane = new ScrollPane();
		mailExchangeScrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
		mailExchangeScrollPane.setVbarPolicy(ScrollBarPolicy.ALWAYS);
		mailExchangeVBox = new VBox();
		mailExchangeVBox.setPadding(new Insets(5, 20, 5, 5));
		mailExchangeVBox.prefWidthProperty().bind(mailExchangeScrollPane.widthProperty());
		mailExchangeScrollPane.setContent(mailExchangeVBox);
		mailCCTextField = new TextField("");
		mailCCTextField.setPromptText("Betreff");
		mailCCTextField.getParent().requestFocus();
		mailContentTextArea = new TextArea("");
		mailContentTextArea.setPromptText("Inhalt");
		mailContentTextArea.getParent().requestFocus();
		sendMailButton = new Button("Senden");

		// =====================================

		add(studentImage, 0, 0, 1, 6);
		add(takePictureButton, 0, 6);
		GridPane.setHalignment(takePictureButton, HPos.CENTER);
		add(nameLabel, 1, 0);
		GridPane.setHalignment(nameLabel, HPos.LEFT);
		add(studentFirstName, 2, 0);
		GridPane.setHalignment(studentFirstName, HPos.LEFT);
		add(studentLastName, 3, 0);
		GridPane.setHalignment(studentLastName, HPos.LEFT);
		add(mailLabel, 1, 3);
		GridPane.setHalignment(mailLabel, HPos.LEFT);
		add(studentMail_1, 2, 3, 2, 1);
		GridPane.setHalignment(studentMail_1, HPos.LEFT);
		add(studentMail_2, 2, 4, 2, 1);
		GridPane.setHalignment(studentMail_2, HPos.LEFT);
		add(studentMail_3, 2, 5, 2, 1);
		GridPane.setHalignment(studentMail_3, HPos.LEFT);
		add(mtrNrLabel, 1, 2);
		GridPane.setHalignment(mtrNrLabel, HPos.LEFT);
		add(studentMtrNr, 2, 2, 2, 1);
		GridPane.setHalignment(studentMtrNr, HPos.LEFT);
		add(poLabel, 1, 1);
		GridPane.setHalignment(poLabel, HPos.LEFT);
		add(studentPO, 2, 1);
		GridPane.setHalignment(studentPO, HPos.LEFT);
		add(newPOButton, 3, 1);
		GridPane.setHalignment(newPOButton, HPos.LEFT);
		add(ectsLabel, 4, 1);
		GridPane.setHalignment(ectsLabel, HPos.LEFT);
		add(studentECTS, 5, 1);
		GridPane.setHalignment(studentECTS, HPos.LEFT);
		add(semesterLabel, 4, 0);
		GridPane.setHalignment(semesterLabel, HPos.LEFT);
		add(studentSemester, 5, 0);
		GridPane.setHalignment(studentSemester, HPos.LEFT);
		add(concernsLabel, 4, 2);
		GridPane.setHalignment(concernsLabel, HPos.LEFT);
		add(newConcernButton, 5, 2);
		GridPane.setHalignment(newConcernButton, HPos.LEFT);
		add(connectedConcernsListView, 4, 3, 2, 3);
		add(errorLabel, 1, 6, 4, 2);
		GridPane.setHalignment(errorLabel, HPos.RIGHT);
		add(editStudentButton, 5, 6, 1, 2);
		GridPane.setValignment(editStudentButton, VPos.CENTER);
		GridPane.setHalignment(editStudentButton, HPos.RIGHT);
		add(notesLabel, 0, 7);
		GridPane.setHalignment(notesLabel, HPos.LEFT);
		add(studentNotes, 0, 8);
		add(mailExchangeLabel, 1, 7);
		GridPane.setHalignment(mailExchangeLabel, HPos.LEFT);
		add(mailGridPane, 1, 8, 5, 1);

		// ================build mailGridPane====================

		mailGridPane.add(mailExchangeScrollPane, 0, 0, 2, 1);
		mailGridPane.add(mailCCTextField, 0, 1);
		GridPane.setHalignment(mailCCTextField, HPos.LEFT);
		mailGridPane.add(mailContentTextArea, 0, 2);
		mailGridPane.add(sendMailButton, 1, 1, 1, 2);
		GridPane.setHalignment(sendMailButton, HPos.CENTER);
		GridPane.setValignment(sendMailButton, VPos.CENTER);

		// =========================================================

		takePictureButton.setOnAction((event) -> {
			Stage stage = new Stage();
			stage.setAlwaysOnTop(true);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setTitle("Foto aufnehmen");
			stage.setScene(new Scene(new TakeImageView(stage, presenter, this), 450, 450));
			stage.show();
		});

		newPOButton.setOnAction((event) -> {
			Stage stage = new Stage();
			stage.setAlwaysOnTop(true);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setTitle("Neue PO");
			stage.setScene(
					new Scene(new EditPOView(presenter.getPOs(), presenter.getSubjects(), stage, presenter), 450, 450));
			stage.show();
		});

		newConcernButton.setOnAction((event) -> {
			ObservableList<Student> list = FXCollections.observableArrayList();
			list.add(student);
			presenter.openNewConcernTab(list);

		});

		editStudentButton.setOnAction((event) -> {
			// TODO öffne neuen EditStudentView Tab mit student. Oder setze View des Tabs
			// neu??? -> ID ändern!
			// TODO aktuellen Tab schließen
		});

		sendMailButton.setOnAction((event) -> {
			String mailCC = mailCCTextField.getText();
			String mailContent = mailContentTextArea.getText();
			String userID = presenter.getOptions().getUserID();
			String userName = presenter.getOptions().getUserName();
			EMail mail = presenter.sendMail(userID, userName, student, mailCC, mailContent);
			//add new EMail to View
			addMailToView(mail);
			mailExchangeVBox.layout();
			mailExchangeScrollPane.setVvalue(1.0d);

			
		});

		// TODO Notes müssen beim schließen gespeichert werden!!
	}

	private void fillView() {
		if (student.getImage() != null) {
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
		connectedConcernsListView = new ListView<Concern>(student.getConcerns());
		studentNotes.setText(student.getNotes());
		//TODO für Mail nen loading label oder sowas

		
	}

	public void fillMailView() {
		// Fill MailExchange
		ArrayList<EMail> mails = presenter.getEMails(student);
		for (EMail mail : mails) {
			addMailToView(mail);
		}
		mailExchangeVBox.layout();
		mailExchangeScrollPane.setVvalue(1.0d);
		//TODO mail loading label 'ausschalten'
	}
	
	public void addMailToView(EMail mail) {
		if (mail != null) {
			Label date = new Label();
			Label subject = new Label();
			Label content = new Label();

			date.setText(mail.getDate().toString());
			subject.setText(mail.getSubject());
			content.setText(mail.getContent());

			date.prefWidthProperty().bind(mailExchangeVBox.widthProperty());
			subject.prefWidthProperty().bind(mailExchangeVBox.widthProperty());
			content.prefWidthProperty().bind(mailExchangeVBox.widthProperty());

			date.setAlignment(Pos.BASELINE_CENTER);

			if (!mail.isReceived()) {
				subject.setAlignment(Pos.BASELINE_RIGHT);
				content.setAlignment(Pos.BASELINE_RIGHT);
				subject.setTextAlignment(TextAlignment.RIGHT);
				content.setTextAlignment(TextAlignment.RIGHT);
			}

			mailExchangeVBox.getChildren().add(date);
			mailExchangeVBox.getChildren().add(subject);
			mailExchangeVBox.getChildren().add(content);

			
		}
	}
	
	public void updateImage(Image image) {
		studentImage.setImage(image);
		student.setImage(image);
		presenter.saveEditedStudent(student);
	}

}
