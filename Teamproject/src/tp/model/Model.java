package tp.model;

import java.util.ArrayList;
import java.util.Date;

import javafx.collections.ObservableList;

public class Model {

	private String[] sessionTabsIds;
	private Options options;

	public Model() {
		loadSessionTabsIds();
	}
	
	//-------------------Calculations--------------------------------------------------------------

	public Appointment[] loadNext24hourAppointments() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Date[] getWorkWeekOfDate(Date date) {
		// TODO Auto-generated method stub
		return null;
	}
	public int getKwOfDate(Date date) {
		// TODO Auto-generated method stub
		return 0;
	}
	public Appointment[] getWeeksAppointments(int shownKw) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Date getStartOfNextWeek(Date date) {
		// TODO Auto-generated method stub
		return null;
		
	}

	public Date getEndOfPreviousWeek(Date date) {
		// TODO Auto-generated method stub
		return null;
	}
	
	//------------------File: Loader&Saver + Getter/Setter---------------------------------------------------------------
	
	
	public String[] getSessionTabs() {
		if (sessionTabsIds == null)
		{
			return loadSessionTabsIds();
		}
		return sessionTabsIds;
	}
	
	public void saveSessionTabs() {
		// TODO Java object out Stream

	}

	public void saveOptions() {
		// TODO Java object out Stream

	}

	public String[] loadSessionTabsIds() {
		// TODO Java object in stream, wenn da, sonst neue Datei
		// this.tabSession = ...
		return sessionTabsIds;

	}

	public Options loadOptions() {
		// TODO Java object in stream, wenn da, sonst neue Datei
		// this.options = ...
		return options;

	}
	
	public Options getOptions()
	{
		if(options!=null)
		{
			return options;
		}
		else
		{
			options = loadOptions();
			return options;
		}
	}
	
	public void setSessionTabs(String[] sessionTabs) {
		this.sessionTabsIds = sessionTabs;
	}
	

	// ------------Datenbank Abfragen--------------------------------------------------------------------

	
	public Student getStudent(String emailAdressse) {
		//TODO return Student where email = email
		return null;
	}
	
	public Student getStudent(int mtrNr) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Concern getConcern(int concernId) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Statistic getStatistic(int statisticId) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public ObservableList<PO> getPOs() {
		// TODO Auto-generated method stub
		return null;
		
	}
	public ObservableList<Topic> getTopics() {
		// TODO Auto-generated method stub
		return null;
	}

	public ObservableList<Subject> getSubjects() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public ObservableList<Form> getForms() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public ObservableList<Concern> getConcerns(Student student) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public boolean mailInDb(EMail email) {
		//TODO sqlabfrage
		return true;
	}
	
	public void deleteStudent(Student s) {
		// TODO Auto-generated method stub
		
	}

	public boolean saveNewSubject(String title, int ects) {
		//TODO
		//create new Subject with calculated unique id
		//add the new Subject to the Database;
		//return true if successfull, otherwise system.out.println("ERROR: Subject '" + title + "' could not be added to database"); return false;
		return false;
		
	}

	public boolean saveEditedSubject(String title, int ects, int id) {
		// TODO s.o. �hnlich
		return true;
	}


	public void saveNewTopic(String title, ArrayList<Object> selectedForms) {
		// TODO Auto-generated method stub
		
	}

	public void saveEditedTopic(String text, ArrayList<Object> selectedForms, Topic topic) {
//		int id = topic.getId();
		// TODO Auto-generated method stub
		
	}

	public void saveEditedPO(String newPOName, ArrayList<Object> selectedMandatorySubjects,
			ArrayList<Object> selectedOptionalSubjects, PO po) {
		//ge�nderten Namen f�r po �bernehmen
		po.setName(newPOName);
		//ge�nderte mandatory Subjects (als Array) f�r po �bernehmen
		po.setMandatorySubjects(selectedMandatorySubjects.toArray(new Subject[selectedMandatorySubjects.size()]));
		//ge�nderte optional Subjects (als Array) f�r po �bernehmen
		po.setOptionalSubjects(selectedOptionalSubjects.toArray(new Subject[selectedOptionalSubjects.size()]));

		//TODO save changed PO
	}

	public void deleteConcern(Concern c) {
		// TODO Auto-generated method stub
		
	}

	public void saveNewForm(Form form) {
		// TODO Auto-generated method stub
		
	}

	public void deleteForm(Form f) {
		// TODO Auto-generated method stub
		
	}

	public void saveMail(EMail email) {
		// TODO add EMail to Database
		
	}












}
