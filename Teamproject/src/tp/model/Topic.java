package tp.model;

import javafx.collections.ObservableList;

public class Topic {
	
	private String title;
	private ObservableList<Form> forms;
	int id;
	
	public Topic(String title)
	{
		this.title = title;
	}
	public Topic(String title, ObservableList<Form> forms)
	{
		this.title = title;
		this.forms = forms;
	}
	
	//===========Getter/Setter=====================
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	public ObservableList<Form> getForms() {
		return forms;
	}
	public int getId() {
		return id;
	}
	public void setLinkedForms(ObservableList<Form> forms) {
		this.forms = forms;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		return title;
	}

	
	
	

	

}
