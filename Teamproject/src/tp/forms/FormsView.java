package tp.forms;

import javafx.collections.ObservableList;
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
import tp.model.Form;

public class FormsView extends GridPane {

	private Presenter presenter;
	private ObservableList<Form> forms;

	// ===================== GUI Elemente ========================

	private Button addFormButton;
	private Button deleteFormButton;
	private Button renameFormButton;
	private Label searchBar;
	private ListView<Form> formListView;

	// =================== Konstruktor ===========================

	public FormsView(Presenter presenter) {
		this.presenter = presenter;
		this.forms = presenter.getTopicForms();
		buildView();
		fillView();
	}

	private void buildView() {
		setPadding(new Insets(20));
		setHgap(20);
		setVgap(20);

		addFormButton = new Button("Formular hinzufügen");
		deleteFormButton = new Button("Ausgewählte Formulare entfernen");
		renameFormButton = new Button("Umbenennen");
		formListView = new ListView<Form>(forms);
		searchBar = new Label("SearchBar");

		// =====================================================================

		add(searchBar, 0, 0);
		GridPane.setHalignment(searchBar, HPos.LEFT);

		add(renameFormButton, 1, 0);
		GridPane.setHalignment(renameFormButton, HPos.RIGHT);
		
		add(formListView, 0, 1, 2, 1);

		add(addFormButton, 1, 2);
		GridPane.setHalignment(addFormButton, HPos.RIGHT);

		add(deleteFormButton, 0, 2);
		GridPane.setHalignment(deleteFormButton, HPos.LEFT);

	}

	private void fillView() {
		addFormButton.setOnAction((event) -> {
			Stage stage = new Stage();
			stage.setAlwaysOnTop(true);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setTitle("Neues Formular hinzufügen");
			stage.setScene(new Scene(new NewFormView(stage, presenter, forms), 450, 450));
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
			//TODO in ListView umbenennen?
		});
	}

}
