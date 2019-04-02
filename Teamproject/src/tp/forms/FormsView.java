package tp.forms;

import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tp.Presenter;
import tp.concern.ConcernView;
import tp.model.Form;

public class FormsView extends GridPane {

	private Presenter presenter;
	private ObservableList<Form> forms;
	
	//-------------------------------------------
	
	private ConcernView concernView;
	private Stage stage;

	// ===================== GUI Elemente ========================

	private Button addFormButton;
	private Button deleteFormButton;
	private Button renameFormButton;
	private Label searchBar;
	private ListView<Form> formListView;
	
	//-----------------------------------------------------------
	
	private Label selectedFormsLabel;
	private ListView<Form> selectedFormsListView;
	private Button toRightButton;
	private Button toLeftButton;
	private Button selectedFormsToConcernButton;


	// =================== Konstruktor ===========================

	public FormsView(Presenter presenter) {
		this.presenter = presenter;
		this.forms = presenter.getTopicForms();
		buildView();
	}

	public FormsView(Presenter presenter, Stage stage, ConcernView concernView, ObservableList<Form> filesAlreadyInConcern) {
		this(presenter);
		this.stage = stage;
		this.concernView = concernView;
		
		selectedFormsListView.setVisible(true);
		toRightButton.setVisible(true);
		toLeftButton.setVisible(true);
		selectedFormsToConcernButton.setVisible(true);
		selectedFormsLabel.setVisible(true);
		
		formListView.getItems().removeAll(filesAlreadyInConcern);
		selectedFormsListView.getItems().addAll(filesAlreadyInConcern);

	}

	private void buildView() {
		setPadding(new Insets(20));
		setHgap(20);
		setVgap(20);

		addFormButton = new Button("Neues Formular");
		deleteFormButton = new Button("Ausgewählte Formulare entfernen");
		renameFormButton = new Button("Umbenennen");
		formListView = new ListView<Form>(forms);
		searchBar = new Label("SearchBar");
		
		//--------------------------------------------------------------------
		
		selectedFormsLabel = new Label("Ausgewählte Dateien");
		selectedFormsListView = new ListView<Form>();
		toRightButton = new Button(">");
		toLeftButton = new Button("<");
		selectedFormsToConcernButton = new Button("Ausgewählte Dateien zum Concern hinzufügen");
		
		selectedFormsLabel.setVisible(false);
		selectedFormsListView.setVisible(false);
		toRightButton.setVisible(false);
		toLeftButton.setVisible(false);
		selectedFormsToConcernButton.setVisible(false);

		
		// =====================================================================

		add(searchBar, 0, 0);
		GridPane.setHalignment(searchBar, HPos.LEFT);

		add(renameFormButton, 1, 0);
		GridPane.setHalignment(renameFormButton, HPos.RIGHT);
		
		add(formListView, 0, 1, 2, 2);

		add(addFormButton, 1, 3);
		GridPane.setHalignment(addFormButton, HPos.RIGHT);

		add(deleteFormButton, 0, 3);
		GridPane.setHalignment(deleteFormButton, HPos.LEFT);
		
		//------------------------------------------------------------------------
		
		add(selectedFormsLabel,3,0);
		GridPane.setHalignment(selectedFormsLabel, HPos.LEFT);
		
		add(selectedFormsListView,3,1);

		add(selectedFormsToConcernButton,3,2);
		GridPane.setHalignment(selectedFormsToConcernButton, HPos.RIGHT);

		VBox leftRightButtonBox = new VBox();
		leftRightButtonBox.getChildren().addAll(toLeftButton, toRightButton);
		leftRightButtonBox.setSpacing(10);
		toRightButton.setMaxWidth(Double.MAX_VALUE);
		toLeftButton.setMaxWidth(Double.MAX_VALUE);
		add(leftRightButtonBox, 2, 1);
		leftRightButtonBox.setAlignment(Pos.CENTER);
		
		
		//========================================================================
		
		addFormButton.setOnAction((event) -> {
			Stage stage = new Stage();
			stage.setAlwaysOnTop(true);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setTitle("Neues Formular hinzufügen");
			stage.setScene(new Scene(new NewFormView(stage, presenter, this, forms), 450, 450));
			stage.show();
		});
		deleteFormButton.setOnAction((event) -> {
			for(Form f: formListView.getSelectionModel().getSelectedItems())
			{
				presenter.deleteForm(f);
			}
		});
		
		renameFormButton.setOnAction((event) -> {
			formListView.getSelectionModel().getSelectedItem();
		});
		
		//--------------------------------------------------------------

		toRightButton.setOnAction(event -> {
			ObservableList<Form> itemsToMove = formListView.getSelectionModel().getSelectedItems();

			selectedFormsListView.getItems().addAll(itemsToMove);
			formListView.getItems().removeAll(itemsToMove);
		});
		
		toLeftButton.setOnAction(event -> {
			ObservableList<Form> itemsToMove = selectedFormsListView.getSelectionModel().getSelectedItems();

			formListView.getItems().addAll(itemsToMove);
			selectedFormsListView.getItems().removeAll(itemsToMove);
		});

		
		selectedFormsToConcernButton.setOnAction(event -> {
			concernView.addFilesToConcern(selectedFormsListView.getItems());
			stage.close();
		});

	}

	public void addNewForm(Form newForm) {
		formListView.getItems().add(newForm);
		
	}


}
