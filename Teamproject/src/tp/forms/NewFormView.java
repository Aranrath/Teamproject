package tp.forms;

import java.io.File;
import java.util.ArrayList;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import tp.Presenter;
import tp.model.Form;

public class NewFormView extends GridPane {

	private Presenter presenter;
	private Stage stage;
	private FormsView formsView;
	private ArrayList<Form> forms;
	private File choosenFile;
	private Boolean universal;

	// =============== GUI =================

	private TextField nameTextField;
	private Label choosenFilePathLabel;
	private Label nameErrorLabel;
	private Label filePathError;
	private Button changeFilePathButton;
	private Button saveButton;

	public NewFormView(Stage stage, Presenter presenter, FormsView formsView, ArrayList<Form> forms, Boolean universal) {
		this.presenter = presenter;
		this.stage = stage;
		this.forms = forms;
		this.formsView = formsView;
		this.universal = universal;

		buildView();
	}

	private void buildView() {
		setPadding(new Insets(10, 10, 10, 10));
		setHgap(10);
		setVgap(10);

		nameTextField = new TextField("");
		choosenFilePathLabel = new Label("");
		nameErrorLabel = new Label("Formular Name muss ausgefüllt sein");
		nameErrorLabel.setVisible(false);
		filePathError = new Label("Formular Dateipfad muss gesetzt sein");
		filePathError.setVisible(false);
		changeFilePathButton = new Button("Ändern");
		changeFilePathButton.setMinSize(Button.USE_PREF_SIZE, Button.USE_PREF_SIZE);
		saveButton = new Button("Speichern");
		saveButton.setMinSize(Button.USE_PREF_SIZE, Button.USE_PREF_SIZE);

		Label nameLabel = new Label("Formular Name");
		nameLabel.setMinSize(Label.USE_PREF_SIZE, Label.USE_PREF_SIZE);
		add(nameLabel, 0, 0);
		add(nameTextField, 1, 0, 2, 1);
		add(nameErrorLabel, 1, 1, 2, 1);
		Label pathLabel = new Label("Dateipfad");
		pathLabel.setMinSize(Label.USE_PREF_SIZE, Label.USE_PREF_SIZE);
		add(pathLabel, 0, 2);
		add(choosenFilePathLabel, 1, 2);
		add(changeFilePathButton, 2, 2);
		GridPane.setHalignment(changeFilePathButton, HPos.RIGHT);

		add(filePathError, 1, 3, 2, 1);
		add(saveButton, 2, 4);
		GridPane.setHalignment(saveButton, HPos.RIGHT);

		saveButton.setOnAction((event) -> {

			// Name checken
			if (nameTextField.getText().equals("")) {
				nameErrorLabel.setText("Formular Name muss ausgefüllt sein");
				nameErrorLabel.setTextFill(Color.RED);
				nameErrorLabel.setVisible(true);
				return;
			}
			if (nameAlreadyExists(nameTextField.getText()))
			{
				nameErrorLabel.setText("Name bereits vorhanden");
				nameErrorLabel.setTextFill(Color.RED);
				nameErrorLabel.setVisible(true);
				return;
			}
			// Path check
			if (choosenFilePathLabel.getText().equals("")) {
				filePathError.setText("Formular Dateipfad muss gesetzt sein");
				filePathError.setTextFill(Color.RED);
				filePathError.setVisible(true);
				return;
			} 

			Form newForm = new Form(nameTextField.getText(), choosenFile, universal);
				
			Form form = presenter.saveNewForm(newForm);
			formsView.addNewForm(form);
			stage.close();

		});

		changeFilePathButton.setOnAction((event) -> {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Datei auswählen");
			choosenFile = fileChooser.showOpenDialog(stage);
			try
			{
			choosenFilePathLabel.setText(choosenFile.getAbsolutePath());
			}
			catch(Exception e)
			{
			}

		});

	}


	private boolean nameAlreadyExists(String newFormName) {
		if (forms != null) {
			for (Form s : forms) {
				if (s.getName().equals(newFormName)) {
					return true;
				}
			}

		}
		return false;
	}

}
