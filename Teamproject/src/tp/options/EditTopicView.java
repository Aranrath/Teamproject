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
import tp.concern.ConcernView;
import tp.model.Form;
import tp.model.Topic;

public class EditTopicView extends GridPane {

	//from constructor paramters
	private Stage stage;
	private Presenter presenter;
	
	private OptionsView optionsView;
	private ConcernView concernView;
	

	private Label titleLabel;
	private TextField titleTextField;
	private Label formLabel;
	private CheckListView<Form> allFormsListView;
	private Label errorLabel;
	private Button saveButton;

	// =======================Constructors======================================

	//neues Thema (von OptionView aus gestartet)
	public EditTopicView(Stage stage, Presenter presenter, OptionsView optionsView) {
		this.stage = stage;
		this.presenter = presenter;
		this.optionsView = optionsView;

		buildView();
		fillView();
	}
	
	//neues Thema (von ConcernView aus gestartet)
	public EditTopicView(Stage stage, Presenter presenter, ConcernView concernView) {
		this.stage = stage;
		this.presenter = presenter;
		this.concernView = concernView;

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
		
		errorLabel = new Label("");
		titleLabel = new Label("Titel:");
		titleTextField = new TextField();
		formLabel = new Label("Zugehörige Formulare:");
		saveButton = new Button("Speichern");
		
		ObservableList<Form> allForms = presenter.getTopicForms();
		if(allForms != null)
		{
			allFormsListView = new CheckListView<Form>(allForms);
		}
		else
		{
			allFormsListView = new CheckListView<Form>();
		}
		
		
		// =================================================================
		
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
				
				ObservableList<Form> selectedForms = allFormsListView.getCheckModel().getCheckedItems();
				//UNTERSCHIED: speicher als neues Thema + Passe Oberfläche des aufrufenden Tab an
				Topic newTopic = presenter.saveNewTopic(new Topic (titleTextField.getText(), selectedForms));
				
				if(optionsView != null)
				{
					optionsView.addNewTopic(newTopic);
				}
				else if(concernView != null)
				{
					concernView.addNewTopic(newTopic);
				}
				
				stage.close();
			}
		});

	}

	// Bearbeite existierendes Thema -> Trage aktuelle Daten des Themas in der Oberfläche ein
	private void fillView(Topic topic) {
		
		// =================================================================
		//TODO Test ob Kästchen richtig angekreuzt werden
		System.out.println("Zugehörige Forms von: " + topic.getTitle());
		for(Form form : topic.getForms())
		{
			form.getName();
		}
		
		// =================================================================
		
		titleTextField.setText(topic.getTitle());
		
		for(Form f: topic.getForms()) {
			allFormsListView.getSelectionModel().select(f);
		}
		
		saveButton.setOnAction((event)->{
			if(titleTextField.getText().equals(""))
			{
				errorLabel.setText("Titel muss ausgefüllt sein");
				errorLabel.setTextFill(Color.RED);
				return;
				
			}
			if(titleAlreadyExists(topic, titleTextField.getText()))
			{
				errorLabel.setText("Titel bereits vorhanden");
				errorLabel.setTextFill(Color.RED);
				return;
			}
			else
			{
				ObservableList<Form> selectedForms = allFormsListView.getCheckModel().getCheckedItems();
				//UNTERSCHIED: speicher das geänderte Thema
				topic.setTitle(titleTextField.getText());
				topic.setForms(selectedForms);
				presenter.saveEditedTopic(topic);
				stage.close();
			}
		});
		
		
	}

	// =================================================================

	private boolean titleAlreadyExists(String newTitle) {
		for(Topic t: presenter.getTopics())
		{
			if(t.getTitle().equals(newTitle))
			{
				return true;
			}
		}
		return false;
	}
	
	private boolean titleAlreadyExists(Topic topic, String newTitle) {
		for(Topic t: presenter.getTopics())
		{
			if(topic.getId() != t.getId() && t.getTitle().equals(newTitle))
			{
				return true;
			}
		}
		return false;
	}
}
