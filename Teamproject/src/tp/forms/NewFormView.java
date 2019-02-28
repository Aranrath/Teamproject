package tp.forms;

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

public class NewFormView extends GridPane {

	private Presenter presenter;
	private Stage stage;
	private ObservableList<Form> forms;

	// =============== GUI =================

	private TextField nameTextField;
	private Label choosenFilePath;
	private Label nameErrorLabel;
	private Label filePathError;
	private Button changeFilePath;
	private Button saveButton;

	public NewFormView(Stage stage, Presenter presenter, ObservableList<Form> forms) {
		this.presenter = presenter;
		this.stage = stage;
		this.forms = forms;

		buildView();
	}

	private void buildView() 
	{
		setPadding(new Insets(10, 10, 10, 10));
		setHgap(10);
		setVgap(10);
		
		nameTextField = new TextField("");
		choosenFilePath = new Label("");
		nameErrorLabel = new Label("");
		filePathError = new Label("");
		changeFilePath = new Button("ändern");
		saveButton = new Button ("Speichern");
		
		add(new Label("Formular Name"),0,0);
		add(nameTextField,1,0,2,1);
		add(nameErrorLabel,1,1,2,1);
		add(new Label("Dateipfad"),0,2);
		add(choosenFilePath,1,2);
		add(changeFilePath,2,2);
		GridPane.setHalignment(changeFilePath, HPos.RIGHT);
		
		add(filePathError,1,3,2,1);
		add(saveButton,2,4);
		GridPane.setHalignment(saveButton, HPos.RIGHT);
		
		saveButton.setOnAction((event)->{
			if(nameTextField.getText().equals(""))
			{
				nameErrorLabel.setText("Formular Name muss ausgefüllt sein");
				nameErrorLabel.setTextFill(Color.RED);
				return;
			}
			
			if(nameAlreadyExists(nameTextField.getText()))
			{
				nameErrorLabel.setText("Name bereits vorhanden");
				nameErrorLabel.setTextFill(Color.RED);
				return;
			}
			
			if(choosenFilePath.getText().equals(""))
			{
				filePathError.setText("Formular Dateipfad muss gesetzt sein");
				filePathError.setTextFill(Color.RED);
				return;
			}
			
			Form samePathForm = pathAlreadyUsed(nameTextField.getText());
			if(samePathForm != null)
			{
				nameErrorLabel.setText("Der Datei mit dem Pfad \'" + choosenFilePath.getText() + "\' ist bereits als Formular unter dem Namen \'" + samePathForm.getName() + "\' gespeichert");
				nameErrorLabel.setTextFill(Color.RED);
				return;
			}
			
			else
			{
				presenter.saveNewForm(new Form(nameTextField.getText(), choosenFilePath.getText()));
				stage.close();
			}
		});
		
	}

	private Form pathAlreadyUsed(String newFormFilePath) {
		for (Form s : forms) {
			if (s.getFile().getPath().equals(newFormFilePath)) {
				return s;
			}
		}
		return null;
	}

	private boolean nameAlreadyExists(String newFormName) {
		for (Form s : forms) {
			if (s.getName().equals(newFormName)) {
				return true;
			}
		}
		return false;
	}

}
