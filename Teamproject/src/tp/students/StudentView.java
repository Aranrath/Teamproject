package tp.students;

import java.util.ArrayList;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
import javafx.scene.layout.ColumnConstraints;
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
import tp.model.MyTab;
import tp.model.Student;

public class StudentView extends GridPane {

	private Presenter presenter;
	private Student student;
	private MyTab tab;

	// ====================================GUI

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
	private Label studentPO;
	private Label semesterLabel;
	private Label studentSemester;
	private Label ectsLabel;
	private Label studentECTS;
	private Label concernsLabel;
	private Button newConcernButton;
	private Button deleteConcernButton;
	private ListView<Concern> connectedConcernsListView;
	private Label errorLabel;
	private Button editStudentButton;
	private Label notesLabel;
	private TextArea studentNotes;
	
	//-----------------------------GUI: mailExchange Box

	private Label mailExchangeLabel;
	private GridPane mailGridPane;
	private ScrollPane mailExchangeScrollPane;
	private VBox mailExchangeVBox;
	private TextField mailCCTextField;
	private TextArea mailContentTextArea;
	private Button sendMailButton;

	public StudentView(Student student, Presenter presenter, MyTab tab) {
		this.student = student;
		this.presenter = presenter;
		this.tab = tab;
		buildView();
		fillView();
		fillMailView();
	}
	
	public StudentView(Student student, Presenter presenter, MyTab tab, ArrayList<String> changedMailAddresses) {
		this.student = student;
		this.presenter = presenter;
		this.tab = tab;
		buildView();
		fillView();
		fillMailView(changedMailAddresses);
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
		studentPO = new Label();
		ectsLabel = new Label("ECTS");
		studentECTS = new Label();
		semesterLabel = new Label("Semester");
		studentSemester = new Label();
		concernsLabel = new Label("Anliegen");
		newConcernButton = new Button("Neues Anliegen");
		deleteConcernButton = new Button("Student aus Anliegen entfernen");
		connectedConcernsListView = new ListView<Concern>();
		errorLabel = new Label("Ich bin ein Error-Label, bitch");
		errorLabel.setVisible(false);
		editStudentButton = new Button("Profil bearbeiten");
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
		mailContentTextArea = new TextArea("");
		mailContentTextArea.setPromptText("Inhalt");
		sendMailButton = new Button("Senden");

		// =====================================
		
		add(studentImage, 0, 0, 1, 8);
		add(takePictureButton, 0, 8);
		GridPane.setHalignment(takePictureButton, HPos.CENTER);
		add(nameLabel, 1, 0);
		add(studentFirstName, 2, 0);
		add(studentLastName, 3, 0);
		add(mailLabel, 1, 3);
		add(studentMail_1, 2, 3, 2, 1);
		add(studentMail_2, 2, 4, 2, 1);
		add(studentMail_3, 2, 5, 2, 1);
		add(mtrNrLabel, 1, 1);
		add(studentMtrNr, 2, 1);
		add(poLabel, 1, 2);
		add(studentPO, 2, 2,2,1);
		add(ectsLabel, 1, 6);
		add(studentECTS, 2, 6);
		add(semesterLabel, 1, 7);
		add(studentSemester, 2, 7);
		add(concernsLabel, 4, 2,2,1);
		GridPane.setHalignment(concernsLabel, HPos.LEFT);
		add(newConcernButton, 5, 8);
		add(deleteConcernButton,4,8);
		add(connectedConcernsListView, 4, 3, 2, 5);
		add(errorLabel, 3, 1, 3, 1);
		GridPane.setHalignment(errorLabel, HPos.RIGHT);
		add(editStudentButton, 5, 0);
		GridPane.setHalignment(editStudentButton, HPos.RIGHT);
		add(notesLabel, 0, 9);
		GridPane.setHalignment(notesLabel, HPos.LEFT);
		add(studentNotes, 0, 10);
		add(mailExchangeLabel, 1, 9);
		GridPane.setHalignment(mailExchangeLabel, HPos.LEFT);
		add(mailGridPane, 1, 10, 5, 1);

		// ================build mailGridPane====================

		mailGridPane.add(mailExchangeScrollPane, 0, 0, 2, 1);
		mailGridPane.add(mailCCTextField, 0, 1);
		GridPane.setHalignment(mailCCTextField, HPos.LEFT);
		mailGridPane.add(mailContentTextArea, 0, 2);
		mailGridPane.add(sendMailButton, 1, 1, 1, 2);
		GridPane.setHalignment(sendMailButton, HPos.CENTER);
		GridPane.setValignment(sendMailButton, VPos.CENTER);


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
			stage.setScene(new Scene(new TakeImageView(stage, presenter, this), 450, 450));
			stage.show();
		});


		newConcernButton.setOnAction((event) -> {
			ObservableList<Student> list = FXCollections.observableArrayList();
			list.add(student);
			presenter.openNewConcernTab(list);

		});
		
		deleteConcernButton.setOnAction((event) -> {
			Concern selectedConcern = connectedConcernsListView.getSelectionModel().getSelectedItem();
			selectedConcern.getStudents().remove(student);
			connectedConcernsListView.getItems().remove(selectedConcern);
			
		});

		editStudentButton.setOnAction((event) -> {
			presenter.openEditStudentView(student);
			presenter.closeThisTab(tab);
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

		studentNotes.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				student.setNotes(newValue);
				presenter.saveEditedStudent(student);
				
			}
		});
	}

	private void fillView() {
		if (student.getImage() != null) {
			studentImage.setImage(student.getImage());
		}
		studentFirstName.setText(student.getFirstName());
		studentLastName.setText(student.getName());
		
		ArrayList<String> mail = student.geteMailAddresses();
		
		if(mail.size()==1)
		{
			studentMail_1.setText(student.geteMailAddresses().get(0));
		}
		else if(mail.size()==2)
		{
			studentMail_1.setText(student.geteMailAddresses().get(0));
			studentMail_2.setText(student.geteMailAddresses().get(1));
		}
		else if(mail.size()==3)
		{
			studentMail_1.setText(student.geteMailAddresses().get(0));
			studentMail_2.setText(student.geteMailAddresses().get(1));
			studentMail_3.setText(student.geteMailAddresses().get(2));
		}	

		studentMtrNr.setText("" + student.getMtrNr());
		
		if(student.getPo() != null);
		{
			studentPO.setText(student.getPo().getName());
		}
	
		studentECTS.setText("" + student.getEcts());
		studentSemester.setText("" + student.getSemester());
		connectedConcernsListView = new ListView<Concern>(student.getConcerns());
		studentNotes.setText(student.getNotes());
		//TODO für Mail nen loading label oder sowas

		
	}
	
//	================================================================================================
//							Mail Stuff
//	================================================================================================

	public void fillMailView(ArrayList<String> changedMailAddresses) {
		for (String address: changedMailAddresses) {
			try {
				Thread t = new Thread(()->presenter.checkMail(student, address));
				t.start();
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		fillMailView();
	}
	
	public void fillMailView() {
		if(!student.geteMailAddresses().isEmpty()) {
			try {
				Thread t = new Thread(()->presenter.checkMail(student));
				t.start();
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// Fill MailExchange
			ArrayList<EMail> mails = presenter.getEMails(student);
			for (EMail mail : mails) {
				addMailToView(mail);
			}
			mailExchangeVBox.layout();
			mailExchangeScrollPane.setVvalue(1.0d);
		}else {
			//TODO in loading label: keine E-Mailaddresse vorhanden.
		}
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
