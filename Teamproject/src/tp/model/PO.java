package tp.model;

import javafx.collections.ObservableList;

public class PO {
	
	private String name;
	private ObservableList<Subject> optionalSubjects;
	private ObservableList<Subject> mandatorySubjects;
	
	public PO(String name)
	{
		this.name = name;
	}

	
	//---------------------------------------Getter Setter

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public ObservableList<Subject> getOptionalSubjects() {
		return optionalSubjects;
	}


	public ObservableList<Subject> getMandatorySubjects() {
		return mandatorySubjects;
	}


	public void setOptionalSubjects(ObservableList<Subject> optionalSubjects) {
		this.optionalSubjects = optionalSubjects;
	}


	public void setMandatorySubjects(ObservableList<Subject> mandatorySubjects) {
		this.mandatorySubjects = mandatorySubjects;
	}
	
	

	
	
	

}
