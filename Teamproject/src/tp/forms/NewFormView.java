package tp.forms;

import java.io.File;

import javafx.collections.ObservableList;
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
	private ObservableList<Form> forms;
	private File choosenFile;

	// =============== GUI =================

	private TextField nameTextField;
	private Label choosenFilePath;
	private Label nameErrorLabel;
	private Label filePathError;
	private Button changeFilePath;
	private Button saveButton;

	public NewFormView(Stage stage, Presenter presenter, FormsView formsView, ObservableList<Form> forms) {
		this.presenter = presenter;
		this.stage = stage;
		this.forms = forms;
		this.formsView = formsView;

		buildView();
	}

	private void buildView() {
		setPadding(new Insets(10, 10, 10, 10));
		setHgap(10);
		setVgap(10);

		nameTextField = new TextField("");
		choosenFilePath = new Label("");
		nameErrorLabel = new Label("Formular Name muss ausgefüllt sein");
		nameErrorLabel.setVisible(false);
		filePathError = new Label("Formular Dateipfad muss gesetzt sein");
		filePathError.setVisible(false);
		changeFilePath = new Button("ändern");
		saveButton = new Button("Speichern");

		add(new Label("Formular Name"), 0, 0);
		add(nameTextField, 1, 0, 2, 1);
		add(nameErrorLabel, 1, 1, 2, 1);
		add(new Label("Dateipfad"), 0, 2);
		add(choosenFilePath, 1, 2);
		add(changeFilePath, 2, 2);
		GridPane.setHalignment(changeFilePath, HPos.RIGHT);

		add(filePathError, 1, 3, 2, 1);
		add(saveButton, 2, 4);
		GridPane.setHalignment(saveButton, HPos.RIGHT);

		saveButton.setOnAction((event) -> {

			// Name checken
			if (nameTextField.getText().equals("")) {
				nameErrorLabel.setText("Formular Name muss ausgefüllt sein");
				nameErrorLabel.setTextFill(Color.RED);
				nameErrorLabel.setVisible(true);
			} else {
				if (nameAlreadyExists(nameTextField.getText())) {
					nameErrorLabel.setText("Name bereits vorhanden");
					nameErrorLabel.setTextFill(Color.RED);
					nameErrorLabel.setVisible(true);
				} else {
					nameErrorLabel.setVisible(false);
				}

			}

			// Path check
			if (choosenFilePath.getText().equals("")) {
				filePathError.setText("Formular Dateipfad muss gesetzt sein");
				filePathError.setTextFill(Color.RED);
				filePathError.setVisible(true);
			} 
			else {
				// ob Pfad bereits vergeben
				Form samePathForm = fileAlreadyUsed(choosenFile);
				if (samePathForm != null) {
					filePathError.setText("Datei ist bereits im Formular \"" + samePathForm.getName() + "\" gespeichert");
					filePathError.setTextFill(Color.RED);
					filePathError.setVisible(true);
				}

				else {
					filePathError.setVisible(false);
				}
			}

			if (!nameErrorLabel.isVisible() && !filePathError.isVisible()) {
				
				Form newForm = new Form(nameTextField.getText(), choosenFile);
				
				presenter.saveNewForm(newForm);
				formsView.addNewForm(newForm);
				stage.close();
			}

		});

		changeFilePath.setOnAction((event) -> {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Open Resource File");
			choosenFile = fileChooser.showOpenDialog(stage);
			try
			{
			choosenFilePath.setText(choosenFile.getAbsolutePath());
			}
			catch(Exception e)
			{
				//TODO sollte nur zum testen benutzt werden -> später raus wenn Ordner Pfad funkt
				System.out.println("Dateipfad konnte nicht notiert werden");
			}

		});

	}

	private Form fileAlreadyUsed(File choosenFile) {
		if (forms != null) {
			for (Form s : forms) {
				if (s.getFile() == choosenFile) {
					return s;
				}
			}
		}
		return null;
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
