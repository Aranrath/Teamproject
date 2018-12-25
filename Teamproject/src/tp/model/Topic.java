package tp.model;

import java.util.ArrayList;

public class Topic {
	
	private String title;
	private ArrayList<Object> forms;
	
	public Topic(String title)
	{
		this.title = title;
	}
	public Topic(String title, ArrayList<Object> forms)
	{
		this.title = title;
		this.forms = forms;
	}
	public String getTitle() {
		return title;
	}
	public ArrayList<Object> getForms() {
		return forms;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public void setForms(ArrayList<Object> forms) {
		this.forms = forms;
	}

	

}
