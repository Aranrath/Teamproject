package tp.students;

import org.controlsfx.control.CheckListView;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import tp.Presenter;
import tp.model.PO;
import tp.model.Subject;

public class SelectPassedSubjectsView extends GridPane
{
	
	private Presenter presenter;
	private Stage stage;
	private EditStudentView editStudentView;
	private PO po;
	
	//===========================
	
	private Label mandatorySubjectsLabel;
	private CheckListView<Subject> mandatorySubjectsListView;
	private Label optionalSubjectsLabel;
	private CheckListView<Subject> optionalSubjectsListView;
	private Label otherSubjectsLabel;
	private CheckListView<Subject> otherSubjectsListView;
	private Button saveButton;
	

	public SelectPassedSubjectsView(Stage stage, Presenter presenter, EditStudentView editStudentView, PO po, ObservableList<Subject> passedSubjects) {
		this.stage = stage;
		this.presenter = presenter;
		this.editStudentView = editStudentView; 
		this.po = po;
		
		buildView();
		fillView(passedSubjects);
	}

	public SelectPassedSubjectsView(Stage stage, Presenter presenter, EditStudentView editStudentView, PO po) {
		this.stage = stage;
		this.presenter = presenter;
		this.editStudentView = editStudentView;
		this.po = po;
		
		buildView();
	}

	private void buildView()
	{
		setPadding(new Insets(10, 10, 10, 10));
		setHgap(10);
		setVgap(10);
		
		mandatorySubjectsLabel = new Label("Pflichtmodule");
		mandatorySubjectsListView = new CheckListView<Subject>(po.getMandatorySubjects());
		optionalSubjectsLabel  = new Label("Wahlpflichtmodule");
		optionalSubjectsListView= new CheckListView<Subject>(po.getOptionalSubjects());
		otherSubjectsLabel = new Label("Andere Module (ungewertet)");
		
		ObservableList<Subject> otherSubjects = presenter.getSubjects();
		otherSubjects.removeAll(mandatorySubjectsListView.getItems());
		otherSubjects.removeAll(optionalSubjectsListView.getItems());
		otherSubjectsListView = new CheckListView<Subject>(otherSubjects);

		saveButton = new Button("Speichern");
		
		//====================================
		
		//TODO Maße
		
		//====================================
		
		add(mandatorySubjectsLabel,0,0);
		add(mandatorySubjectsListView,0,1);
		add(optionalSubjectsLabel, 1,0);
		add(optionalSubjectsListView, 1,1);
		add(otherSubjectsLabel,2,0);
		add(otherSubjectsListView,2,1);
		add(saveButton,2,2);
		GridPane.setHalignment(saveButton, HPos.RIGHT);
		
		//================================
		
		saveButton.setOnAction((event) -> {
			
			ObservableList<Subject> updatedPassedSubjects = FXCollections.observableArrayList(mandatorySubjectsListView.getCheckModel().getCheckedItems());
			
			updatedPassedSubjects.addAll(optionalSubjectsListView.getCheckModel().getCheckedItems());
			
			updatedPassedSubjects.addAll(otherSubjectsListView.getCheckModel().getCheckedItems());
			
			editStudentView.updatePassedSubjects(updatedPassedSubjects);
			
			stage.close();
		});

	}
	
	private void fillView(ObservableList<Subject> passedSubjects) {
		
		//TODO Noch gehen bestandene Fächer nach Wechsel der PO verloren.... :(
		
		
		for(Subject passedSubject : passedSubjects)
		{
			checkSubject(passedSubject);

		}
		
	}

	private void checkSubject(Subject passedSubject) {
		for(Subject mandatorySubject: mandatorySubjectsListView.getItems())
		{
			if(mandatorySubject.getId() == passedSubject.getId())
			{
				mandatorySubjectsListView.getCheckModel().check(passedSubject);
				return;
			}
		}
		for(Subject optionalSubject: optionalSubjectsListView.getItems())
		{
			if(optionalSubject.getId() == passedSubject.getId())
			{
				optionalSubjectsListView.getCheckModel().check(passedSubject);
				return;
			}
		}
		for(Subject otherSubject: otherSubjectsListView.getItems())
		{
			if(otherSubject.getId() == passedSubject.getId())
			{
				otherSubjectsListView.getCheckModel().check(passedSubject);
				return;
			}
		}
		
	}
	
	

}
