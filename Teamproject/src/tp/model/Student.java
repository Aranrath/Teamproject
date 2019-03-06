package tp.model;

import javafx.collections.ObservableList;
import javafx.scene.image.Image;

public class Student{
	
	private int mtrNr;
	private String name;
	private String firstName;
	private ObservableList<String> eMailAddresses;
	private int semester;
	private String notes;
	private int ects;
	private Image image;
	private ObservableList<Concern> concerns;
	private PO po;
	
	public Student(int mtrNr, String name, String firstName,ObservableList<String> eMailAddresses, int semester, String notes, int ects, Image image, ObservableList<Concern> concerns)
	{
		this.mtrNr = mtrNr;
		this.name = name;
		this.firstName = firstName;
		this.eMailAddresses = eMailAddresses;
		this.semester = semester;
		this.notes = notes;
		this.ects = ects;
		this.image = image;
		this.concerns =  concerns;
		
	}
	
	//------------------------------------GetterSetter

	public int getMtrNr() {
		return mtrNr;
	}

	public String getName() {
		return name;
	}

	public String getFirstName() {
		return firstName;
	}

	public int getSemester() {
		return semester;
	}

	public String getNotes() {
		return notes;
	}

	public int getEcts() {
		return ects;
	}

	public Image getImage() {
		return image;
	}

	public ObservableList<String> geteMailAddresses() {
		return eMailAddresses;
	}

	public ObservableList<Concern> getConcerns() {
		return concerns;
	}

	public PO getPo() {
		return po;
	}

	public void setMtrNr(int mtrNr) {
		this.mtrNr = mtrNr;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void seteMailAddresses(ObservableList<String> eMailAddresses) {
		this.eMailAddresses = eMailAddresses;
	}

	public void setSemester(int semester) {
		this.semester = semester;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public void setEcts(int ects) {
		this.ects = ects;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public void setConcerns(ObservableList<Concern> concerns) {
		this.concerns = concerns;
	}

	public void setPo(PO po) {
		this.po = po;
	}
	
	
	
	

}
