package tp.options;

import java.util.ArrayList;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import tp.Presenter;
import tp.model.Subject;

public class EditSubjectView extends GridPane {

	ArrayList<Subject> subjects;
	Presenter presenter;
	int ects;

	Button minusEctsButton;
	Button plusEctsButton;
	Button saveButton;

	TextField titleTextField;
	Label ectsLabel;

	public EditSubjectView(ArrayList<Subject> subjects, Presenter presenter) {
		this.subjects = subjects;
		this.presenter = presenter;
		ects = 5;
		
		buildView();
	}

	public EditSubjectView(ArrayList<Subject> subjects, Presenter presenter, Subject subjectToEdit) {
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
		ectsLabel = new Label(String.valueOf(ects));
		plusEctsButton = new Button("+");
		saveButton = new Button("Speichern");

		add(new Label("Titel"), 0, 0);
		add(titleTextField, 1, 0, 3, 1);
		add(new Label("ECTS"), 0, 1);
		add(minusEctsButton, 1, 1);
		add(ectsLabel, 2, 1);
		add(plusEctsButton, 3, 1);
		add(saveButton,0,2,4,1);
		GridPane.setHalignment(saveButton, HPos.RIGHT);
		

	}

	private void fillView(Subject subjectToEdit) {
		// TODO Auto-generated method stub

	}

}
