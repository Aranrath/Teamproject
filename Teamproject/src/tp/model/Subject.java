package tp.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class Subject {
	private String title;
	private int ects;
	private int id;

	// For TableViews
	private BooleanProperty optional = new SimpleBooleanProperty();
	private BooleanProperty mandatory = new SimpleBooleanProperty();

	public Subject(String title, int ects) {
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

	//For TableView
	public BooleanProperty optionalProperty() {
		return optional;
	}
	
	public void setOptional(boolean optional) {
		this.optional.set(optional);
	}

	public BooleanProperty mandatoryProperty() {
		return mandatory;
	}

	public void setMandatory(boolean mandatory) {
		this.mandatory.set(mandatory);
	}

	@Override
	public String toString() {
		return title;
	}

}
