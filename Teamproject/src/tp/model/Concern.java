package tp.model;

import javafx.collections.ObservableList;

public class Concern {

	private int id;
	private String title;
	private Object[] data;
	private Topic topic;
	private Appointment[] appointments;
	private ReminderMail[] reminders;
	ObservableList<Student> students;
	
	public Concern(int id, String title, Object[] data, Topic topic, Appointment[] appointments, ReminderMail[] reminders,ObservableList<Student> students)
	{
		this.id= id;
		this.title= title ;
		this.data= data ;
		this.topic= topic ;
		this.appointments = appointments;
		this.reminders= reminders ;
		this.students = students;
	}

	public int getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public Object[] getData() {
		return data;
	}

	public Topic getTopic() {
		return topic;
	}

	public Appointment[] getAppointments() {
		return appointments;
	}

	public ReminderMail[] getReminders() {
		return reminders;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setData(Object[] data) {
		this.data = data;
	}

	public void setTopic(Topic topic) {
		this.topic = topic;
	}

	public void setAppointments(Appointment[] appointments) {
		this.appointments = appointments;
	}

	public void setReminders(ReminderMail[] reminders) {
		this.reminders = reminders;
	}

	public ObservableList<Student> getStudents() {
		return students;
	}

	public void setStudents(ObservableList<Student> students) {
		this.students = students;
	}
	
	
	
	
}
