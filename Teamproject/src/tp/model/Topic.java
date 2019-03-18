package tp.model;

import java.util.ArrayList;

public class Topic {
	
	private String title;
	private ArrayList<Form> linkedForms;
	int id;
	
	public Topic(String title)
	{
		this.title = title;
	}
	public Topic(String title, ArrayList<Form> forms)
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
	public ArrayList<Form> getLinkedForms() {
		return linkedForms;
	}
	public int getId() {
		return id;
	}
	public void setLinkedForms(ArrayList<Form> linkedForms) {
		this.linkedForms = linkedForms;
	}
	public void setId(int id) {
		this.id = id;
	}

	
	
	

	

}
