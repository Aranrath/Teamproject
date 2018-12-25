package tp.model;

import java.util.ArrayList;

public class Topic {
	
	private String title;
	private ArrayList<Object> linkedForms;
	int id;
	
	public Topic(String title)
	{
		this.title = title;
	}
	public Topic(String title, ArrayList<Object> forms)
	{
		this.title = title;
		this.linkedForms = forms;
	}
	
	//===========Contructors=====================
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	public ArrayList<Object> getLinkedForms() {
		return linkedForms;
	}
	public int getId() {
		return id;
	}
	public void setLinkedForms(ArrayList<Object> linkedForms) {
		this.linkedForms = linkedForms;
	}
	public void setId(int id) {
		this.id = id;
	}

	
	
	

	

}
