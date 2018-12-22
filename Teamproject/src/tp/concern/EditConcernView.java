package tp.concern;

import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import tp.model.Concern;
import tp.model.Student;

public class EditConcernView extends HBox{
	
	Concern concern;
	
	//new
	public EditConcernView()
	{
		buildView();
	}
	
	//new with preselected Students
	public EditConcernView(ObservableList<Student> students)
	{
		buildView();
		fillStudents(students);
	}
	


	//edit existing Concern
	public EditConcernView(Concern concern)
	{
		this.concern = concern;
		buildView();
		fillView(concern);
	}
	
	private void buildView()
	{
		getChildren().addAll(new Label("EditConernView"));
	}

	private void fillView(Concern concern)
	{
		fillStudents(concern.getStudents());
	}
	
	private void fillStudents(ObservableList<Student> students) {
		// TODO Auto-generated method stub
		
	}
}
