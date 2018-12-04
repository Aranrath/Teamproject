package tp.model;

public class Subject
{
	int id;
	String title;
	int ects;
	
	public Subject(int id, String title, int ects)
	{
		this.id = id;
		this.title = title;
		this.ects = ects;
	}

	public int getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public int getEcts() {
		return ects;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setEcts(int ects) {
		this.ects = ects;
	}
	
	
	
}
