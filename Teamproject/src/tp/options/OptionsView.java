package tp.options;

import java.util.ArrayList;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tp.Presenter;
import tp.model.PO;
import tp.model.Subject;
import tp.model.Topic;

public class OptionsView extends GridPane {

	private Presenter presenter;

	ListView<PO> posList;
	ListView<Topic> topicsList;
	ListView<Subject> subjectsList;

	ArrayList<PO> pos;
	ArrayList<Topic> topics;
	ArrayList<Subject> subjects;

	private Button addTopicButton;
	private Button addPOButton;
	private Button addSubjectButton;

	public OptionsView(Presenter presenter) {
		this.presenter = presenter;
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

		add(topicsList, 0, 1, 2, 2);
		add(addTopicButton, 1, 0);
		add(new Label("Themen (von Anliegen)"), 0, 0);
		GridPane.setHalignment(addTopicButton, HPos.RIGHT);

		add(posList, 2, 1, 2, 2);
		add(new Label("PO's (Studiengänge)"), 2, 0);
		add(addPOButton, 3, 0);
		GridPane.setHalignment(addPOButton, HPos.RIGHT);

		add(subjectsList, 4, 1 , 2, 2);
		add(new Label("Module"), 4, 0);
		add(addSubjectButton, 5, 0);
		GridPane.setHalignment(addSubjectButton, HPos.RIGHT);

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
            stage.setScene(new Scene(new EditPOView(pos, subjects,stage, presenter), 450, 450));
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
	}

	private void fillView() {
		pos = presenter.getPOs();
		topics = presenter.getTopics();
		subjects = presenter.getSubjects();

		//TODO Jedes Listenelement soll ein 'x' und ein 'edit' hinter haben zum bearbeiten/löschen
	}

}
