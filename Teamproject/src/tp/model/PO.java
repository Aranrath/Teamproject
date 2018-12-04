package tp.model;

public class PO {
	
	String name;
	Subject[] subjects;
	
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

	public Subject[] getSubjects() {
		return subjects;
	}

	public void setSubjects(Subject[] subjects) {
		this.subjects = subjects;
	}
	
	

}
