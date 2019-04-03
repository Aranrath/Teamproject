package tp.model;

import java.sql.Date;
import java.util.ArrayList;

import javafx.collections.ObservableList;
import javafx.scene.image.Image;

public class Student {

	private int mtrNr;
	private String name;
	private String firstName;
	private ArrayList<String> eMailAddresses;
	private int semester;
	private String notes;
	private ObservableList<Subject> passedSubjects;
	private Image image;
	private ObservableList<Integer> concernIds;
	private PO po;
	private String gender;
	// For TableView
	private Date lastContact;

	public Student(int mtrNr, String name) {
		this.mtrNr = mtrNr;
		this.name = name;
	}

	public Student(int mtrNr, String name, String firstName, ArrayList<String> eMailAddresses, int semester,
			String notes, ObservableList<Subject> passedSubjects, Image image, ObservableList<Integer> concernIds, String gender, Date lastContact) {
		this.mtrNr = mtrNr;
		this.name = name;
		this.firstName = firstName;
		this.eMailAddresses = eMailAddresses;
		this.semester = semester;
		this.notes = notes;
		this.passedSubjects = passedSubjects;
		this.image = image;
		this.concernIds = concernIds;
		this.gender = gender;
		this.lastContact = lastContact;

	}

	// ------------------------------------GetterSetter

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

	public Image getImage() {
		return image;
	}

	public ArrayList<String> geteMailAddresses() {
		return eMailAddresses;
	}

	public ObservableList<Integer> getConcernIds() {
		return concernIds;
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

	public void seteMailAddresses(ArrayList<String> eMailAddresses) {
		this.eMailAddresses = eMailAddresses;
	}

	public void setSemester(int semester) {
		this.semester = semester;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public void setConcernIds(ObservableList<Integer> concernIds) {
		this.concernIds = concernIds;
	}

	public void setPo(PO po) {
		this.po = po;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	// For TableView
	public void setLastContact(Date lastContact) {
		this.lastContact = lastContact;
	}

	public Date getLastContact() {
		return lastContact;
	}
	
	public ObservableList<Subject> getPassedSubjects() {
		return passedSubjects;
	}

	public void setPassedSubjects(ObservableList<Subject> passedSubjects) {
		this.passedSubjects = passedSubjects;
	}
	
	
	//=====================================================

	public String toString()
	{
		String studentString = mtrNr + " " + name;
		
		if(firstName != null)
		{
			studentString += " " + firstName;
		}
		for ( String mail : eMailAddresses )
		{
			studentString += " " + mail;
		}
		
		return studentString;
	}


	
}
