package tp.model;

public class Topic {
	
	String title;
	Object [] forms;
	
	public Topic(String title)
	{
		this.title = title;
	}
	public Topic(String title, Object[] forms)
	{
		this.title = title;
		this.forms = forms;
	}
	public String getTitle() {
		return title;
	}
	public Object[] getForms() {
		return forms;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public void setForms(Object[] forms) {
		this.forms = forms;
	}

	

}
