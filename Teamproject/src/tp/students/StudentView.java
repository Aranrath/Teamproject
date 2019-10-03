package tp.students;

import java.io.File;
import java.util.ArrayList;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
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
	ObservableList<Concern> concerns;

	// ====================================GUI

	private ImageView studentImage;
	private Button takePictureButton;
	private Button searchPictureButton;
	private Label nameLabel;
	private Label studentName;
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
	private Label genderLabel;
	private Label studentGender;
	
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

	private StackPane placeholderPane;
	private Label placeholderLabel;
	
	private Label mailExchangeLabel;
	private GridPane mailGridPane;
	private ScrollPane mailExchangeScrollPane;
	private VBox mailExchangeVBox;
	private ComboBox<String> mailRecipientAdress;
	private TextField mailCCTextField;
	private TextArea mailContentTextArea;
	private Button sendMailButton;

	public StudentView(Student student, Presenter presenter, MyTab tab) {
		this.student = student;
		this.presenter = presenter;
		this.tab = tab;
		buildView();
		fillView();
		Thread t = new Thread(()->fillMailView());
		t.start();
	}
	
	public StudentView(Student student, Presenter presenter, MyTab tab, ArrayList<String> changedMailAddresses) {
		this.student = student;
		this.presenter = presenter;
		this.tab = tab;
		buildView();
		fillView();
		Thread t = new Thread(()->fillMailView(changedMailAddresses));
		t.start();
	}

	private void buildView() {

		setPadding(new Insets(10, 10, 10, 10));
		setHgap(10);
		setVgap(10);
		
		//Student Image------------------------------------
		studentImage = new ImageView();

		studentImage.setFitWidth(300);
		
		studentImage.setPreserveRatio(true);
		studentImage.fitHeightProperty().bind(heightProperty().divide(3));
		
		GridPane.setHalignment(studentImage, HPos.CENTER);
//		studentImage.minHeight(studentImage.prefHeight(USE_COMPUTED_SIZE));
		
		//--------------------------------------------------
		
		
		takePictureButton = new Button("Bild aufnehmen");
		searchPictureButton = new Button("Bild laden");
		takePictureButton.setMinSize(Button.USE_PREF_SIZE, Button.USE_PREF_SIZE);
		searchPictureButton.setMinSize(Button.USE_PREF_SIZE, Button.USE_PREF_SIZE);
		
		
		nameLabel = new Label("Name");
		studentName = new Label();
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
		genderLabel = new Label("Geschlecht");
		studentGender = new Label();
		concernsLabel = new Label("Anliegen");
		newConcernButton = new Button("Neues Anliegen");
		deleteConcernButton = new Button("Anliegen entfernen");

		concerns = FXCollections.observableArrayList();
		connectedConcernsListView = new ListView<Concern>(concerns);
		errorLabel = new Label("Ich bin ein Error-Label");
		errorLabel.setVisible(false);
		editStudentButton = new Button("Profil bearbeiten");
		notesLabel = new Label("Notizen");
		studentNotes = new TextArea();

		//----------------------------------------------------
		
		
		placeholderLabel = new Label("E-Mails werden geladen");
		placeholderLabel.setTextFill(Color.WHITE);
		placeholderPane = new StackPane(placeholderLabel);
		placeholderPane.setBackground(
				new Background(new BackgroundFill(Color.CORNFLOWERBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
		StackPane.setAlignment(placeholderLabel, Pos.CENTER);
		
		mailExchangeLabel = new Label("E-Mail Austausch");
		mailGridPane = new GridPane();
		
		mailGridPane.setMaxHeight(Double.MAX_VALUE);

		GridPane.setVgrow(mailGridPane, Priority.SOMETIMES);
		
		//loadingMailPlaceholderPane zunächst angezeigt bis MailGridPane geladen ist
		mailGridPane.setVisible(false);
		mailGridPane.setBackground(
				new Background(new BackgroundFill(Color.CORNFLOWERBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
		mailExchangeScrollPane = new ScrollPane();
		mailExchangeScrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
		mailExchangeScrollPane.setVbarPolicy(ScrollBarPolicy.ALWAYS);
		mailExchangeVBox = new VBox();
		mailExchangeVBox.setPadding(new Insets(5, 20, 5, 5));
		mailExchangeVBox.prefWidthProperty().bind(mailExchangeScrollPane.widthProperty());
		
		mailExchangeScrollPane.setMaxWidth(Double.MAX_VALUE);
		GridPane.setHgrow(mailExchangeScrollPane, Priority.ALWAYS);
		
		mailRecipientAdress = new ComboBox<String>();
		mailRecipientAdress.setMaxWidth(Double.MAX_VALUE);
		GridPane.setHgrow(mailRecipientAdress, Priority.ALWAYS);
		
		mailCCTextField = new TextField("");
		mailCCTextField.setPromptText("Betreff");
		
		mailContentTextArea = new TextArea("");
		mailContentTextArea.setPromptText("Inhalt");
		sendMailButton = new Button("Senden");
		
		mailContentTextArea.prefHeightProperty().bind(mailGridPane.heightProperty());
		
		sendMailButton.setMinSize(Button.USE_PREF_SIZE, Button.USE_PREF_SIZE);
		sendMailButton.setMaxWidth(Double.MAX_VALUE);
		
		// =====================================
		
		add(studentImage, 0, 0, 1, 8);
		HBox pictureButtonHBox = new HBox(searchPictureButton, takePictureButton);
		add(pictureButtonHBox, 0, 8);
		pictureButtonHBox.setAlignment(Pos.CENTER);
		add(nameLabel, 1, 0);
		add(studentName, 2, 0,2,1);
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
		add(genderLabel, 1, 8);
		add(studentGender, 2, 8);
		
		add(concernsLabel, 4, 2,2,1);
		GridPane.setHalignment(concernsLabel, HPos.LEFT);
		add(newConcernButton, 5, 8);
		GridPane.setHalignment(newConcernButton, HPos.RIGHT);
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
		add(placeholderPane, 1, 10, 5, 1);

		// ================build mailGridPane====================

		mailGridPane.add(mailExchangeScrollPane, 0, 0, 1, 4);
		mailGridPane.add(mailRecipientAdress, 1, 0);
		mailGridPane.add(mailCCTextField, 1, 1);
		mailGridPane.add(mailContentTextArea, 1, 2);
		mailGridPane.add(sendMailButton, 1, 3);
		GridPane.setHalignment(sendMailButton, HPos.CENTER);
		
		ColumnConstraints mailCol0 = new ColumnConstraints();
		mailCol0.setPercentWidth(60);
		ColumnConstraints mailCol1 = new ColumnConstraints();
		mailCol1.setPercentWidth(40);
		
		mailGridPane.getColumnConstraints().addAll(mailCol0, mailCol1);

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
			stage.getIcons().add(new Image("\\Icon.png"));
			stage.setScene(new Scene(new TakeImageView(stage, presenter, this), 315, 420));
			stage.show();
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
            	
            	updateImage(new Image(file.toURI().toString()));

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
			Concern selectedConcern = connectedConcernsListView.getSelectionModel().getSelectedItem();
			if(selectedConcern!= null)
			{
				selectedConcern.getStudents().remove(student);
				connectedConcernsListView.getItems().remove(selectedConcern);
			}
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
			EMail mail = presenter.sendMail(userID, userName, student.getName(), mailRecipientAdress.getValue(), mailCC, mailContent);
			//if mailSend succsessful
			if (mail != null) {
				//add new EMail to View
				addMailToMailExchangeBox(mail);
				//clear sendMail fields 
				mailCCTextField.clear();
				mailContentTextArea.clear();
			}
			
		});
		

		studentNotes.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				student.setNotes(newValue);
				presenter.saveEditedStudentNotes(student, studentNotes.getText());
				
			}
		});
		
		connectedConcernsListView.setOnMousePressed(new EventHandler<MouseEvent>() {
		    @Override 
		    public void handle(MouseEvent event) {
		        if (event.isPrimaryButtonDown() && event.getClickCount() > 1) {
		        	Concern selectedConcern = connectedConcernsListView.getSelectionModel().getSelectedItem();
		        	if(selectedConcern != null)
		        	{
		        		presenter.openConcernTab(selectedConcern);
		        	}
		                               
		        }
		    }
		});
		
	}

	private void fillView() {
		if (student.getImage() != null) {
			studentImage.setImage(student.getImage());
		}
		studentName.setText(student.getFirstName() + " " + student.getName());
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
		
		if(student.getPo() != null)
		{
			studentPO.setText(student.getPo().getName());
		}
	
		studentSemester.setText("" + student.getSemester());
		if (student.getConcernIds()!=null) {
			concerns.clear();
			for (long id : student.getConcernIds()){
				concerns.add(presenter.getConcern(id));
			}
		}
		studentGender.setText(student.getGender());
		studentNotes.setText(student.getNotes());

		studentECTS.setText("" + presenter.calculateEcts(student.getPassedSubjects(),student.getPo()));
	}
	
//	================================================================================================
//							Mail Stuff
//	================================================================================================

	//(Wird immer im Thread ausgeführt)
	public void fillMailView(ArrayList<String> changedMailAddresses) {
		for (String address: changedMailAddresses)
		{
			presenter.pullAllEMails(student, address);
		}
		
		fillMailView();
	}
	
	//(Wird immer im Thread ausgeführt)
	public void fillMailView() {
		//wenn Student E-Mail Adressen hinterlegt hat
		if(!student.geteMailAddresses().isEmpty())
		{
			//ComboBox für EmpfängerAdresse füllen
			Platform.runLater(new Runnable() {
                @Override
                public void run() {
                	mailRecipientAdress.setItems(FXCollections.observableArrayList(student.geteMailAddresses()));
        			//Wenn vorhanden eMail der letzten erhaltenen E-Mail auswählen
        			if(presenter.getLastEmail(student) != null)
        			{
        				mailRecipientAdress.getSelectionModel().select(presenter.getLastEmail(student).getMailAddress());
        			}
        			else
        			{
        				mailRecipientAdress.getSelectionModel().select(0);
        			}
                }
            });
			
			
			
			//(seit dem letzen pull)
			presenter.pullNewEMails(student);
			
			// Fülle E-MailBox
			ArrayList<EMail> mails = presenter.getEMails(student);
			//Wenn Mails gefunden wurden...
			if(!mails.isEmpty())
			{
				Platform.runLater(()->
				{
					mailExchangeScrollPane.setContent(mailExchangeVBox);
					for (EMail mail : mails)
					{
							addMailToMailExchangeBox(mail);
							placeholderPane.setVisible(false);
							mailGridPane.setVisible(true);
					}
				});
			}
			//Wenn keine Mails gefunden wurden...
			else
			{
				Platform.runLater(()->{
					getChildren().remove(placeholderPane);
					mailGridPane.setVisible(true);
					placeholderLabel.setText("Keine E-Mails gefunden");
					mailExchangeScrollPane.setContent(placeholderPane);
				});
			}
			
		}
		//Wenn zum Studenten keine E-Mail Adressen hinterlegt sind
		else
		{
			Platform.runLater(()->{
				placeholderLabel.setText("Keine E-Mail Adressen hinterlegt");
			});
		}
		
	}	
	
	public void addMailToMailExchangeBox(EMail mail) {
		if (mail != null) {
			Label date = new Label(mail.getMailAddress() + " - " + mail.getDate().toString());
			Label subject = new Label(mail.getSubject());
			Label content = new Label(mail.getContent());
			
			//-----------------layouting
			
			//Breite an mailExchangeVBox anpassen
			date.prefWidthProperty().bind(mailExchangeVBox.widthProperty());
			subject.prefWidthProperty().bind(mailExchangeVBox.widthProperty());
			content.prefWidthProperty().bind(mailExchangeVBox.widthProperty());

			//Datum steht unten in der Mitte
			date.setAlignment(Pos.BASELINE_CENTER);

			//Gesendete Mails sind am rechten Rand ausgerichtet
			if (!mail.isReceived()) {
				subject.setAlignment(Pos.BASELINE_RIGHT);
				content.setAlignment(Pos.BASELINE_RIGHT);
				subject.setTextAlignment(TextAlignment.RIGHT);
				content.setTextAlignment(TextAlignment.RIGHT);
			}

			//---------------------------------------
			
			//VBox zusammensetzen
			mailExchangeVBox.getChildren().add(date);
			mailExchangeVBox.getChildren().add(subject);
			mailExchangeVBox.getChildren().add(content);

			//nach unten scrollen zu den aktuellen Mails
			mailExchangeVBox.layout();
			mailExchangeScrollPane.setVvalue(1.0d);
			
		}
	}
	
	
	//==============================================================================
	
	public void updateImage(Image image) {
		studentImage.setImage(image);
		student.setImage(image);
		presenter.saveEditedStudent(student);
	}

	public void updateView() {
		student = presenter.getStudent(student.getId());
		fillView();
		mailExchangeVBox.getChildren().clear();
		mailGridPane.setVisible(false);
		placeholderPane.setVisible(true);
		Thread t = new Thread(() -> fillMailView());
		t.start();
	}

}
