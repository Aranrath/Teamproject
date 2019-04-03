package tp.options;


import org.controlsfx.control.CheckListView;

import javafx.collections.FXCollections;
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
import tp.model.PO;
import tp.model.Subject;

public class EditPOView extends GridPane{

	private ObservableList<Subject> allSubjects;
	private Stage stage;
	private Presenter presenter;

	private Button saveButton;
	private Label subjectErrorLabel;
	private Label poNameErrorLabel;
	private TextField poNameTextField;
	private CheckListView<Subject> selectSubjectsListView;
	
	public EditPOView(Stage stage, Presenter presenter)
	{
		allSubjects = presenter.getSubjects();
		this.stage = stage;
		this.presenter = presenter;
		
		buildView();
		fillView();
	}
	
	public EditPOView(Stage stage, Presenter presenter, PO po)
	{
		allSubjects = presenter.getSubjects();
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
		selectSubjectsListView = new CheckListView<Subject>(allSubjects);

		add(new Label("PO Name"), 0, 0);
		add(poNameTextField,1,0);
		add(poNameErrorLabel,1,1);
		
		add(selectSubjectsListView,0,2,2,1);
		
		add(subjectErrorLabel,0,3,2,1);
		add(saveButton,0,4,2,1);
		GridPane.setHalignment(saveButton, HPos.RIGHT);
		

		
		
	}
	private void fillView() {

		saveButton.setOnAction((event)->{
			if(poNameTextField.getText().equals(""))
			{
				poNameErrorLabel.setText("Titel muss ausgefüllt sein");
				poNameErrorLabel.setTextFill(Color.RED);
				return;
				
			}
			if(nameAlreadyExists(poNameTextField.getText()))
			{
				poNameErrorLabel.setText("Name bereits vorhanden");
				poNameErrorLabel.setTextFill(Color.RED);
				return;
			}
			else
			{
				ObservableList<Subject>	selectedSubjects = selectSubjectsListView.getCheckModel().getCheckedItems();
				ObservableList<Subject> selectedOptionalSubjects = FXCollections.observableArrayList();
				ObservableList<Subject> selectedMandatorySubjects = FXCollections.observableArrayList();
//				for(Subject s : selectSubjectsListView.getItems())
//				{
//					
//					//TODO wenn ListView korrekt dargestellt mit checkboxen
//					
//					//if (selected as optional)
//					selectedOptionalSubjects.add(s);
//					//else
//						//if(selected as mandatory)
//						selectedMandatorySubjects.add(s);
//				}
				//UNTERSCHIED: speicher das geänderte Thema
				PO po = new PO(poNameTextField.getText(), selectedOptionalSubjects, selectedMandatorySubjects);
				presenter.saveNewPo(po);
				stage.close();
			}
		});
		
	}
	
	private void fillView(PO po) {
		for(Subject s: po.getMandatorySubjects()) {
			selectSubjectsListView.getCheckModel().check(s);
		}
		for(Subject s: po.getOptionalSubjects()) {
			selectSubjectsListView.getCheckModel().check(s);
		}
		
		saveButton.setOnAction((event)->{
			if(poNameTextField.getText().equals(""))
			{
				poNameErrorLabel.setText("Titel muss ausgefüllt sein");
				poNameErrorLabel.setTextFill(Color.RED);
				return;
				
			}
			if(nameAlreadyExists(poNameTextField.getText()))
			{
				poNameErrorLabel.setText("Name bereits vorhanden");
				poNameErrorLabel.setTextFill(Color.RED);
				return;
			}
			else
			{
				ObservableList<Subject>	selectedSubjects = selectSubjectsListView.getCheckModel().getCheckedItems();
				ObservableList<Subject> selectedOptionalSubjects = FXCollections.observableArrayList();
				ObservableList<Subject> selectedMandatorySubjects = FXCollections.observableArrayList();
//				for(Subject s : selectSubjectsListView.getItems())
//				{
//					
//					//TODO wenn ListView korrekt dargestellt mit checkboxen
//					
//					//if (selected as optional)
//					selectedOptionalSubjects.add(s);
//					//else
//						//if(selected as mandatory)
//						selectedMandatorySubjects.add(s);
//				}
				//UNTERSCHIED: speicher das geänderte Thema
				presenter.saveEditedPO(poNameTextField.getText(),selectedMandatorySubjects, selectedOptionalSubjects, po);
				stage.close();
			}
		});
	}
	
	// =================================================================

	private boolean nameAlreadyExists(String newPOName) {
		for(PO p : presenter.getPOs())
		{
			if(p.getName().equals(newPOName))
			{
				return true;
			}
		}
		return false;
	}

}
