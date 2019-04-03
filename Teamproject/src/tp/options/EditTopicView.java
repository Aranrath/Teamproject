package tp.options;

import org.controlsfx.control.CheckListView;

import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import tp.Presenter;
import tp.model.Form;
import tp.model.Topic;

public class EditTopicView extends GridPane {

	//from constructor paramters
	private Stage stage;
	private Presenter presenter;
	
	//from presenter
	private ObservableList<Topic> allTopics;
	private ObservableList<Form> allForms;

	private Label titleLabel;
	private TextField titleTextField;
	private Label formLabel;
	private CheckListView<Form> allFormsListView;
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
		
		this.allForms = presenter.getTopicForms();
		this.allTopics = presenter.getTopics();
		
		errorLabel = new Label("");
		titleLabel = new Label("Titel:");
		titleTextField = new TextField();
		formLabel = new Label("Zugeh�rige Formulare:");
		saveButton = new Button("Speichern");
		
		if(allForms != null)
		{
			allFormsListView = new CheckListView<Form>(allForms);
		}
		else
		{
			allFormsListView = new CheckListView<Form>();
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
				errorLabel.setText("Titel muss ausgef�llt sein");
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
				
				ObservableList<Form> selectedForms = allFormsListView.getCheckModel().getCheckedItems();
				//UNTERSCHIED: speicher als neues Thema
				presenter.saveNewTopic(titleTextField.getText(), selectedForms);
				stage.close();
			}
		});

	}

	// Editing an existing Topic
	private void fillView(Topic topic) {
		//check Forms that already belong to Topic
		for(Form f: topic.getForms()) {
			allFormsListView.getSelectionModel().select(f);
		}
		saveButton.setOnAction((event)->{
			if(titleTextField.getText().equals(""))
			{
				errorLabel.setText("Titel muss ausgef�llt sein");
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
				ObservableList<Form> selectedForms = allFormsListView.getCheckModel().getCheckedItems();
				//UNTERSCHIED: speicher das ge�nderte Thema
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
