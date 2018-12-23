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
		// TODO Auto-generated method stub
		
	}

	private void fillView() {
		// TODO Auto-generated method stub
		
	}
	
	private void fillView(Topic topic) {
		
		
	}
	

	

}
