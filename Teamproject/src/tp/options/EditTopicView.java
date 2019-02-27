package tp.options;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import tp.Presenter;
import tp.model.Topic;

public class EditTopicView extends GridPane {

	//from constructor paramters
	private Stage stage;
	private Presenter presenter;
	
	//from presenter
	private ArrayList<Topic> allTopics;
	private ArrayList<Object> allForms;

	private Label titleLabel;
	private TextField titleTextField;
	private Label formLabel;
	private ListView<Object> allFormsListView;
	private Label errorLabel;
	private Button saveButton;

	// =======================Constructors======================================

	//new Topic
	public EditTopicView(Stage stage, Presenter presenter) {
		this.stage = stage;
		this.presenter = presenter;

		buildView();
		fillView();
	}

	//With Topic to edit
	public EditTopicView( Stage stage, Presenter presenter, Topic toEditTopic) {
		this.stage = stage;
		this.presenter = presenter;

		buildView();
		fillView(toEditTopic);
	}
	// =================================================================

	private void buildView() {
		setPadding(new Insets(10,10,10,10));
		setHgap(10);
		setVgap(10);
		
		this.allForms = presenter.getForms();
		this.allTopics = presenter.getTopics();
		
		errorLabel = new Label("");
		titleLabel = new Label("Titel:");
		titleTextField = new TextField();
		formLabel = new Label("Zugehörige Formulare:");
		saveButton = new Button("Speichern");
		
		if(allForms != null)
		{
			ObservableList<Object> observableAllFormsList = FXCollections.observableArrayList(allForms);
			allFormsListView = new ListView<Object>(observableAllFormsList);
		}
		else
		{
			allFormsListView = new ListView<Object>();
		}
		
		add(titleLabel,0,0);
		add(titleTextField,1,0);
		add(errorLabel,1,1);
		add(formLabel,0,2,2,1);
		add(allFormsListView,0,3,2,1);
		add(saveButton,1,4);
		GridPane.setHalignment(saveButton, HPos.RIGHT);

	}

	// new Topic
	private void fillView() {
		saveButton.setOnAction((event)->{
			if(titleTextField.getText().equals(""))
			{
				errorLabel.setText("Titel muss ausgefüllt sein");
				errorLabel.setTextFill(Color.RED);
				return;
				
			}
			if(titleAlreadyExists(titleTextField.getText()))
			{
				errorLabel.setText("Titel bereits vorhanden");
				errorLabel.setTextFill(Color.RED);
				return;
			}
			else
			{
				ArrayList<Object> selectedForms = new ArrayList<Object>();
				for(Object o : allFormsListView.getItems())
				{
					selectedForms.add(o);
					//TODO nur alle ausgewählten
				}
				//UNTERSCHIED: speicher als neues Thema
				presenter.saveNewTopic(titleTextField.getText(), selectedForms);
				stage.close();
			}
		});

	}

	// Editing an existing Topic
	private void fillView(Topic topic) {
		saveButton.setOnAction((event)->{
			if(titleTextField.getText().equals(""))
			{
				errorLabel.setText("Titel muss ausgefüllt sein");
				errorLabel.setTextFill(Color.RED);
				return;
				
			}
			if(titleAlreadyExists(titleTextField.getText()))
			{
				errorLabel.setText("Titel bereits vorhanden");
				errorLabel.setTextFill(Color.RED);
				return;
			}
			else
			{
				ArrayList<Object> selectedForms = new ArrayList<Object>();
				for(Object o : allFormsListView.getItems())
				{
					selectedForms.add(o);
					//TODO nur alle ausgewählten
				}
				//UNTERSCHIED: speicher das geänderte Thema
				presenter.saveEditedTopic(titleTextField.getText(), selectedForms, topic);
				stage.close();
			}
		});
		
		
	}

	// =================================================================

	private boolean titleAlreadyExists(String newTitle) {
		for(Topic t: allTopics)
		{
			if(t.getTitle().equals(newTitle))
			{
				return true;
			}
		}
		return false;
	}
}
