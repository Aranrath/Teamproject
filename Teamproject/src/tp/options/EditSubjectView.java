package tp.options;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import tp.Presenter;
import tp.model.Subject;

public class EditSubjectView extends GridPane {

	private Presenter presenter;
	private Stage stage;
	private OptionsView optionsView;
	
	private int ects;

	private Button minusEctsButton;
	private Button plusEctsButton;
	private Button saveButton;
	private TextField titleTextField;
	private Label ectsLabel;
	private Label errorLabel;

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
		setPadding(new Insets(20, 20, 20, 20));
		setHgap(20);
		setVgap(20);

		titleTextField = new TextField();
		minusEctsButton = new Button("-");
		plusEctsButton = new Button("+");
		saveButton = new Button("Speichern");
		ectsLabel = new Label();
		errorLabel = new Label();
		errorLabel.setTextFill(Color.RED);
		
		//=====================================================

		add(new Label("Titel"), 0, 0);
		add(titleTextField, 1, 0, 3, 1);
		add(new Label("ECTS"), 0, 1);
		add(minusEctsButton, 1, 1);
		add(ectsLabel, 2, 1);
		GridPane.setHalignment(ectsLabel, HPos.CENTER);
		add(plusEctsButton, 3, 1);
		GridPane.setHalignment(plusEctsButton, HPos.RIGHT);
		add(errorLabel, 0,2,4,1);
		GridPane.setHalignment(errorLabel, HPos.RIGHT);
		add(saveButton, 0, 3, 4, 1);
		GridPane.setHalignment(saveButton, HPos.RIGHT);
		
		//=====================================================

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
		
		//=====================================================
		//Constraints
		
		ColumnConstraints col = new ColumnConstraints();
		col.setPercentWidth(100 / 4);
		
		getColumnConstraints().addAll(col, col,col, col);
		
		//-----------------------------------------------------
		
		RowConstraints row = new RowConstraints();
		row.setPercentHeight(100 / 4);
				     
		getRowConstraints().addAll(row, row, row, row);
		

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
		ectsLabel.setText(String.valueOf(ects));
		titleTextField.setText(subjectToEdit.getTitle());
		
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
