package tp.model;

import javafx.collections.ObservableList;

public class PO {
	
	
	private long id;
	private String name;
	private ObservableList<Subject> optionalSubjects;
	private ObservableList<Subject> mandatorySubjects;
	
	public PO(String name)
	{
		this.name = name;
	}
	
	public PO(String name, ObservableList<Subject> optionalSubjects, ObservableList<Subject> mandatorySubjects) {
		this.name = name;
		this.optionalSubjects = optionalSubjects;
		this.mandatorySubjects = mandatorySubjects;
	}

	
	//---------------------------------------Getter Setter

	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
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
	
	@Override
	public String toString() {
		return name;
	}

	
	
	

}
