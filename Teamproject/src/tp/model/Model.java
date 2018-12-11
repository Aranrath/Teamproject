package tp.model;

public class Model {

	private TabSession tabSession;
	private Options options;

	public Model() {
		loadTabSession();
	}

	//------------------Loader&Saver---------------------------------------------------------------
	
	public void saveSession() {
		// TODO Java object out Stream

	}

	public void saveOptions() {
		// TODO Java object out Stream

	}
	
	public void saveMail(EMail email) {
		// TODO Auto-generated method stub
		
	}

	public TabSession loadTabSession() {
		// TODO Java object in stream, wenn da, sonst neue Datei
		// this.session = ...
		return tabSession;

	}

	public Options loadOptions() {
		// TODO Java object in stream, wenn da, sonst neue Datei
		// this.options = ...
		return options;

	}

	// ------------Getter&Setter--------------------------------------------------------------------

	public TabSession getSession() {
		return tabSession;
	}

	public void setSession(TabSession tabSession) {
		this.tabSession = tabSession;
	}

	public Appointment[] loadNext24hourAppointments() {
		// TODO Auto-generated method stub
		return null;
	}

}
