package tp.options;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import tp.Presenter;
import tp.model.Subject;

public class EditSubjectView extends GridPane {

	Presenter presenter;
	Stage stage;
	OptionsView optionsView;
	
	int ects;

	Button minusEctsButton;
	Button plusEctsButton;
	Button saveButton;
	TextField titleTextField;
	Label ectsLabel;
	Label errorLabel;

	public EditSubjectView(Stage stage, Presenter presenter, OptionsView optionsView) {
		this.stage = stage;
		this.presenter = presenter;
		this.optionsView = optionsView;

		buildView();
		fillView();
	}

	public EditSubjectView(Stage stage, Presenter presenter, Subject subjectToEdit) {
		this.stage = stage;
		this.presenter = presenter;

		buildView();
		fillView(subjectToEdit);
	}

	private void buildView() {
		setPadding(new Insets(10, 10, 10, 10));
		setHgap(10);
		setVgap(10);

		titleTextField = new TextField();
		minusEctsButton = new Button("-");
		plusEctsButton = new Button("+");
		saveButton = new Button("Speichern");
		ectsLabel = new Label();
		errorLabel = new Label();
		errorLabel.setTextFill(Color.RED);

		add(new Label("Titel"), 0, 0);
		add(titleTextField, 1, 0, 3, 1);
		add(new Label("ECTS"), 0, 1);
		add(minusEctsButton, 1, 1);
		add(ectsLabel, 2, 1);
		add(plusEctsButton, 3, 1);
		add(saveButton, 0, 2, 4, 1);
		GridPane.setHalignment(saveButton, HPos.RIGHT);
		add(errorLabel, 0,3,4,1);

		minusEctsButton.setOnAction((event) -> {
			if (ects > 1) {
				ects--;
				ectsLabel.setText(String.valueOf(ects));
			}
		});
		plusEctsButton.setOnAction((event) -> {
			ects++;
			ectsLabel.setText(String.valueOf(ects));
		});

	}

	private void fillView() {
		ects = 5;
		ectsLabel.setText(String.valueOf(ects));
		saveButton.setOnAction((event) -> {
			if(subjectTitleAlreadyInUse(titleTextField.getText()))
			{
				errorLabel.setText("Titel bereits verwendet");
			}
			else if(titleTextField.getText().equals(""))
			{
				errorLabel.setText("Titel muss ausgefüllt sein");
			}
			else
			{

				Subject newSubject = presenter.saveNewSubject(new Subject (titleTextField.getText(), Integer.parseInt(ectsLabel.getText())));
				optionsView.addNewSubject(newSubject);
				stage.close();
			}
			
		});
	}



	private void fillView(Subject subjectToEdit)
	{
		ects = subjectToEdit.getEcts();
		ectsLabel.setText(String.valueOf(ects));;
		saveButton.setOnAction((event) -> {
			if(subjectTitleAlreadyInUse(subjectToEdit, titleTextField.getText()))
			{
				errorLabel.setText("Titel bereits verwendet");
				
			}
			else if(titleTextField.getText().equals(""))
			{
				errorLabel.setText("Titel muss ausgefüllt sein");
			}
			else
			{
				subjectToEdit.setTitle(titleTextField.getText());
				subjectToEdit.setEcts(Integer.parseInt(ectsLabel.getText()));
				
				presenter.saveEditedSubject(subjectToEdit);
				stage.close();
				
			}
		});

	}
	
	
	//Kontrolliere Namen auf Doppelung unter Berücksichtigung des aktuellen Subject-Object
	private boolean subjectTitleAlreadyInUse(Subject subjectToEdit, String newTitle) {
		for(Subject s : presenter.getSubjects())
		{
			if(s.getId() != subjectToEdit.getId() && s.getTitle().equals(newTitle))
			{
				return true;
			}
		}
		return false;
	}

	//Kontrolliere Namen auf Doppelung (bei neuem Subject-Object)
	private boolean subjectTitleAlreadyInUse(String newTitle) {
		for(Subject s : presenter.getSubjects())
		{
			if(s.getTitle().equals(newTitle))
			{
				return true;
			}
		}
		return false;
	}

}
