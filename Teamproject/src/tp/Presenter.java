package tp;

import tp.model.Appointment;
import tp.model.Model;
import tp.model.Session;

public class Presenter
{
	Model model;
	MainView mainView;

	public Presenter(Model model, MainView mainView) {
		this.mainView = mainView;
		this.model = model;
		
		//TODO Hole vom Model die Tabs der letzten Sitzung und öffne diese.
		
	}

	public Session getSession() {
		Session session = model.loadSession();
		return session;
	}

	public Appointment[] getNext24hourAppointments() {
		return model.loadNext24hourAppointments();
	}

}
