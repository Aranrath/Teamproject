package tp.options;

import java.util.ArrayList;

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

	ArrayList<Subject> subjects;
	Presenter presenter;
	Stage stage;
	int ects;

	Button minusEctsButton;
	Button plusEctsButton;
	Button saveButton;

	TextField titleTextField;
	Label ectsLabel;
	Label errorLabel;

	public EditSubjectView(ArrayList<Subject> subjects, Stage stage, Presenter presenter) {
		this.stage = stage;
		this.subjects = subjects;
		this.presenter = presenter;

		buildView();
		fillView();
	}

	public EditSubjectView(ArrayList<Subject> subjects, Stage stage, Presenter presenter, Subject subjectToEdit) {
		this.stage = stage;
		this.subjects = subjects;
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
			if(!titleTextField.getText().equals(""))
			{
				stage.close();
				presenter.saveNewSubject(titleTextField.getText(), Integer.parseInt(ectsLabel.getText()));
			}
			else
			{
				errorLabel.setText("Titel muss ausgefüllt sein");
				errorLabel.setTextFill(Color.RED);
			}
			
		});
	}

	private void fillView(Subject subjectToEdit) {
		ects = subjectToEdit.getEcts();
		ectsLabel.setText(String.valueOf(ects));;
		saveButton.setOnAction((event) -> {
			if(!titleTextField.getText().equals(""))
			{
				stage.close();
				presenter.saveEditedSubject(titleTextField.getText(), Integer.parseInt(ectsLabel.getText()), subjectToEdit.getId() );
			}
			else
			{
				errorLabel.setText("Titel muss ausgefüllt sein");
				errorLabel.setTextFill(Color.RED);
			}
		});

	}

}
