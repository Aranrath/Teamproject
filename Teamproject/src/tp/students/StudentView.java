package tp.students;

import javafx.scene.layout.HBox;
import tp.Presenter;
import tp.model.Student;

public class StudentView extends HBox{
	
	Student student;
	
	public StudentView(int mtrNr, Presenter presenter)
	{
		this.student = presenter.getStudent(mtrNr);
		
	}

}
