package tp.options;

import java.util.ArrayList;

import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import tp.Presenter;
import tp.model.PO;
import tp.model.Subject;

public class EditPOView extends GridPane{

	ObservableList<PO> allPOs;
	ObservableList<Subject> allSubjects;
	Stage stage;
	Presenter presenter;

	Button saveButton;
	Label subjectErrorLabel;
	Label poNameErrorLabel;
	TextField poNameTextField;
	ListView<Subject> selectSubjectsListView;
	
	public EditPOView(ObservableList<PO> pos, ObservableList<Subject> subjects, Stage stage, Presenter presenter)
	{
		this.allPOs = pos;
		this.allSubjects = subjects;
		this.stage = stage;
		this.presenter = presenter;
		
		buildView();
		fillView();
	}
	
	public EditPOView(ObservableList<PO> allPOs, ObservableList<Subject> allSubjects, Stage stage, Presenter presenter, PO po)
	{
		this.allPOs = allPOs;
		this.allSubjects = allSubjects;
		this.stage = stage;
		this.presenter = presenter;
		
		buildView();
		fillView(po);
	}

	private void buildView() {
		setPadding(new Insets(10, 10, 10, 10));
		setHgap(10);
		setVgap(10);

		poNameTextField = new TextField();
		saveButton = new Button("Speichern");
		subjectErrorLabel = new Label("");
		poNameErrorLabel = new Label("");
		selectSubjectsListView = new ListView<Subject>();

		add(new Label("PO Name"), 0, 0);
		add(poNameTextField,1,0);
		add(poNameErrorLabel,1,1);
		
		add(selectSubjectsListView,0,2,2,1);
		
		add(subjectErrorLabel,0,3,2,1);
		add(saveButton,0,4,2,1);
		GridPane.setHalignment(saveButton, HPos.RIGHT);
		

		
		
	}
	private void fillView() {

		
	}
	
	private void fillView(PO po) {
		saveButton.setOnAction((event)->{
			if(poNameTextField.getText().equals(""))
			{
				poNameErrorLabel.setText("Titel muss ausgefüllt sein");
				poNameErrorLabel.setTextFill(Color.RED);
				return;
				
			}
			if(nameAlreadyExists(poNameTextField.getText()))
			{
				poNameErrorLabel.setText("Titel bereits vorhanden");
				poNameErrorLabel.setTextFill(Color.RED);
				return;
			}
			else
			{
				ArrayList<Object> selectedOptionalSubjects = new ArrayList<Object>();
				ArrayList<Object> selectedMandatorySubjects = new ArrayList<Object>();
				for(Object o : selectSubjectsListView.getItems())
				{
					//TODO wenn ListView korrekt dargestellt mit checkboxen
					
					//if (selected as optional)
					selectedOptionalSubjects.add(o);
					//else
						//if(selected as mandatory)
						selectedMandatorySubjects.add(o);
				}
				//UNTERSCHIED: speicher das geänderte Thema
				presenter.saveEditedPO(poNameTextField.getText(),selectedMandatorySubjects, selectedOptionalSubjects, po);
				stage.close();
			}
		});
	}
	
	// =================================================================

	private boolean nameAlreadyExists(String newPOName) {
		for(Subject s : allSubjects)
		{
			if(s.getTitle().equals(newPOName))
			{
				return true;
			}
		}
		return false;
	}

}
