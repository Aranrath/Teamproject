package tp.model;

public class Subject
{
	private String title;
	private int ects;
	private int id;
	
	public Subject(String title, int ects)
	{
		this.title = title;
		this.ects = ects;
	}


	public String getTitle() {
		return title;
	}

	public int getEcts() {
		return ects;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setEcts(int ects) {
		this.ects = ects;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		return title;
	}
	
}
