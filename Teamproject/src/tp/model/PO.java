package tp.model;

public class PO {
	
	private String name;
	private Subject[] optionalSubjects;
	private Subject[] mandatorySubjects;
	
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


	public Subject[] getOptionalSubjects() {
		return optionalSubjects;
	}


	public Subject[] getMandatorySubjects() {
		return mandatorySubjects;
	}


	public void setOptionalSubjects(Subject[] optionalSubjects) {
		this.optionalSubjects = optionalSubjects;
	}


	public void setMandatorySubjects(Subject[] mandatorySubjects) {
		this.mandatorySubjects = mandatorySubjects;
	}
	
	

	
	
	

}
