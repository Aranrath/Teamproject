package tp.model;

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
	
	//------------------Loader&Saver---------------------------------------------------------------
	
	public void saveSessionTabs() {
		// TODO Java object out Stream

	}

	public void saveOptions() {
		// TODO Java object out Stream

	}
	
	public void saveMail(EMail email) {
		// TODO add EMail to Database
		
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

	// ------------Getter&Setter--------------------------------------------------------------------

	
	public Student getStudent(String email) {
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
	
	public String[] getSessionTabs() {
		if (sessionTabsIds == null)
		{
			return loadSessionTabsIds();
		}
		return sessionTabsIds;
	}

	public void setSessionTabs(String[] sessionTabs) {
		this.sessionTabsIds = sessionTabs;
	}
	
	
	

	//------------Boolean Abfragen------------------------------------------------------------------
	
	public boolean mailInDb(EMail email) {
		//TODO sqlabfrage
		return true;
	}
	//----------------------------------------------------------------------------------------------







}
