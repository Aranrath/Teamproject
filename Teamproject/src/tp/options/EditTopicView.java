package tp.options;

import java.util.ArrayList;

import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import tp.Presenter;
import tp.model.Topic;

public class EditTopicView extends GridPane{
	
	private Stage stage;
	private ArrayList<Topic> topics;
	private Presenter presenter;
	private ArrayList<Object> forms;
	
	private TextField title;
	
	
	public EditTopicView(ArrayList<Topic> topics, Stage stage, Presenter presenter) {
		this.stage = stage;
		this.topics = topics;
		this.presenter = presenter;
		
		buildView();
		fillView();
	}
	
	public EditTopicView(ArrayList<Topic> topics, Stage stage, Presenter presenter, Topic topic) {
		this.stage = stage;
		this.topics = topics;
		this.presenter = presenter;
		
		buildView();
		fillView(topic);
	}


	private void buildView() {
		forms = presenter.getForms();
		
	}

	//new Topic
	private void fillView() {
		forms = presenter.getForms();
		
	}
	
	//Editing an existing Topic
	private void fillView(Topic topic) {
		
		
	}
	

	

}
