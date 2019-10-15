package tp.model;

import java.sql.Date;
import java.util.ArrayList;

import javafx.collections.ObservableList;

public class Student {

	private long id;
	private int matrNr;
	private String name;
	private String firstName;
	private ArrayList<String> eMailAddresses;
	private int semester;
	private String notes;
	private ObservableList<Subject> passedSubjects;
	private ObservableList<Long> concernIds;
	private PO po;
	private String gender;
	private Date exmatr;
	// For TableView
	private Date lastContact;
	
	//============================ neu gespeichert
	
	private String mtrNrString;

	public Student(String name) {
		this.name = name;
		mtrNrString = mtrToString();
	}

	public Student(long id, int mtrNr, String name, String firstName, ArrayList<String> eMailAddresses, int semester, PO po,
			String notes, ObservableList<Subject> passedSubjects, ObservableList<Long> concernIds, String gender, Date lastContact, Date exmatr) {
		this.id = id;
		this.matrNr = mtrNr;
		this.name = name;
		this.firstName = firstName;
		this.eMailAddresses = eMailAddresses;
		this.semester = semester;
		this.po = po;
		this.notes = notes;
		this.passedSubjects = passedSubjects;
		this.concernIds = concernIds;
		this.gender = gender;
		this.lastContact = lastContact;
		this.exmatr = exmatr;
		
		mtrNrString = mtrToString();
	}

	// ------------------------------------GetterSetter

	public long getId() {
		return id;
	}
	
	public int getMtrNr() {
		return matrNr;
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

	public ArrayList<String> geteMailAddresses() {
		return eMailAddresses;
	}

	public ObservableList<Long> getConcernIds() {
		return concernIds;
	}

	public PO getPo() {
		return po;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setMtrNr(int mtrNr) {
		this.matrNr = mtrNr;
		mtrNrString = mtrToString();
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

	public void setConcernIds(ObservableList<Long> concernIds) {
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
	
	public Date getExmatr() {
		return exmatr;
	}

	public void setExmatr(Date exmatr) {
		this.exmatr = exmatr;
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
	
	public String getMtrNrString() {
		return mtrNrString;
	}
	
	//=====================================================


	//für die Suche
	public String toString()
	{
		String studentString = matrNr + " " + name;
		
		if(firstName != null)
		{
			studentString += " " + firstName;
		}
		if(po != null) {
			studentString += " " + po.getName();
		}		
		return studentString;
	}
	
	//für eine einheitlichere Darstellung, da die vorderen 0en nicht mitgespeichert werden
	private String mtrToString() {
		int mtrLength = String.valueOf(getMtrNr()).length();
		int missingZeros = Options.usualNumberOfMtrNrDigits - mtrLength;
		
		String mtrString = "";
		for(int i = 0; i < missingZeros; i++)
		{
			mtrString += "0";
		}
		mtrString += getMtrNr();
		
		return mtrString;
	}


	
}
