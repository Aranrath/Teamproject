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
		// TODO Auto-generated method stub
		
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
	//----------------------------------------------------------------------------------------------



}
