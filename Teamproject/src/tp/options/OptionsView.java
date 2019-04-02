package tp.options;

import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tp.Presenter;
import tp.model.Options;
import tp.model.PO;
import tp.model.Subject;
import tp.model.Topic;

public class OptionsView extends GridPane {

	private Presenter presenter;
	private Options options;
	
	//============================================

	private ListView<PO> posList;
	private ListView<Topic> topicsList;
	private ListView<Subject> subjectsList;

	private ObservableList<PO> pos;
	private ObservableList<Topic> topics;
	private ObservableList<Subject> subjects;

	private Button addTopicButton;
	private Button addPOButton;
	private Button addSubjectButton;
	
	//------------------------------
	private GridPane userDataGridPane;
	
	private Label userDataLabel;
	private Label userNameLabel;
	private Label currentUserNameLabel;
	private Label userIDLabel;
	private Label currentUserIDLabel;
	private Button editUserDataButton;

	public OptionsView(Presenter presenter) {
		this.presenter = presenter;
		options = presenter.getOptions();
		buildView();
		fillView();
	}

	private void buildView() {
		setPadding(new Insets(10, 10, 10, 10));
		setHgap(10);
		setVgap(10);

		posList = new ListView<PO>();
		topicsList = new ListView<Topic>();
		subjectsList = new ListView<Subject>();

		addTopicButton = new Button("+");
		addPOButton = new Button("+");
		addSubjectButton = new Button("+");
		
		//--------------------------------------------
		
		userDataGridPane = new GridPane();
		
		userDataLabel = new Label("Nutzerdaten:");
		userNameLabel = new Label("Nutzername");
		currentUserNameLabel = new Label(options.getUserName());
		userIDLabel = new Label("rf/t Kennung");
		currentUserIDLabel = new Label(options.getUserID());
		editUserDataButton = new Button("Nutzerdaten ändern");
		
		//============================================

		add(topicsList, 0, 1, 2, 1);
		add(addTopicButton, 1, 0);
		add(new Label("Themen (von Anliegen)"), 0, 0);
		GridPane.setHalignment(addTopicButton, HPos.RIGHT);

		add(posList, 2, 1, 2, 1);
		add(new Label("PO's (Studiengänge)"), 2, 0);
		add(addPOButton, 3, 0);
		GridPane.setHalignment(addPOButton, HPos.RIGHT);

		add(subjectsList, 4, 1 , 2, 1);
		add(new Label("Module"), 4, 0);
		add(addSubjectButton, 5, 0);
		GridPane.setHalignment(addSubjectButton, HPos.RIGHT);
		
		//------------------------------------
		
		userDataGridPane.setPadding(new Insets(10, 10, 10, 10));
		userDataGridPane.setHgap(10);
		userDataGridPane.setVgap(10);
		
		userDataGridPane.add(userDataLabel,0,0,2,1);
		userDataGridPane.add(userNameLabel,0,1);
		GridPane.setHalignment(currentUserNameLabel, HPos.RIGHT);
		userDataGridPane.add(currentUserNameLabel,1,1);
		userDataGridPane.add(userIDLabel,0,2);
		GridPane.setHalignment(currentUserIDLabel, HPos.RIGHT);
		userDataGridPane.add(currentUserIDLabel,1,2);
		GridPane.setHalignment(editUserDataButton, HPos.RIGHT);
		userDataGridPane.add(editUserDataButton,0,3,2,1);
		
		
		ColumnConstraints column = new ColumnConstraints();
		column.setPercentWidth(50);
		userDataGridPane.getColumnConstraints().add(column);
		userDataGridPane.getColumnConstraints().add(column);
		
		userDataGridPane.setBackground(new Background(new BackgroundFill(Color.BISQUE, null, null)));
		add(userDataGridPane,0,2,2,1);
		
		//============================================

		addTopicButton.setOnAction((event)-> {
			Stage stage = new Stage();
			stage.setAlwaysOnTop(true);
			stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Neues Thema");
            stage.setScene(new Scene(new EditTopicView(stage, presenter), 450, 450));
            stage.show();
		});
		addPOButton.setOnAction((event)-> {
			Stage stage = new Stage();
			stage.setAlwaysOnTop(true);
			stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Neue PO");
            stage.setScene(new Scene(new EditPOView(stage, presenter), 450, 450));
            stage.show();
		});
		addSubjectButton.setOnAction((event)-> {
			Stage stage = new Stage();
			stage.setAlwaysOnTop(true);
			stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Neues Modul");
            stage.setScene(new Scene(new EditSubjectView(subjects,stage, presenter), 450, 450));
            stage.show();
		});
		
		editUserDataButton.setOnAction((event) -> {
			Stage stage = new Stage();
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setAlwaysOnTop(true);
            stage.setTitle("Nutzerdaten ändern");
            stage.setScene(new Scene(new EditUserDataView(presenter, stage, options), 450, 450));
            stage.show();
		});
	}

	private void fillView() {
		pos = presenter.getPOs();
		topics = presenter.getTopics();
		subjects = presenter.getSubjects();
		
		posList.setItems(pos);
		topicsList.setItems(topics);
		subjectsList.setItems(subjects);

		//TODO Jedes Listenelement soll ein 'x' und ein 'edit' hinter haben zum bearbeiten/löschen
	}

}
