package tp.model;

public class Model {

	private String[] sessionTabs;
	private Options options;

	public Model() {
		loadSessionTabs();
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
		// TODO Auto-generated method stub
		
	}

	public String[] loadSessionTabs() {
		// TODO Java object in stream, wenn da, sonst neue Datei
		// this.tabSession = ...
		return sessionTabs;

	}

	public Options loadOptions() {
		// TODO Java object in stream, wenn da, sonst neue Datei
		// this.options = ...
		return options;

	}

	// ------------Getter&Setter--------------------------------------------------------------------

	public String[] getSessionTabs() {
		return sessionTabs;
	}

	public void setSessionTabs(String[] sessionTabs) {
		this.sessionTabs = sessionTabs;
	}
	//----------------------------------------------------------------------------------------------



}
