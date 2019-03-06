package tp.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import javafx.collections.ObservableList;

public class Concern {

	private int id;
	private String title;
	private ObservableList<Form> data;
	private Topic topic;
	private ObservableList<Appointment> appointments;
	private ObservableList<Reminder> reminders;
	private ObservableList<Student> students;
	private String notes;
	

	public Concern(String title, ObservableList<Form> data, Topic topic, ObservableList<Appointment> appointments, ObservableList<Reminder> reminders,
			ObservableList<Student> students, String notes) {
		this.title = title;
		this.data = data;
		this.topic = topic;
		this.appointments = appointments;
		this.reminders = reminders;
		this.students = students;
	}
	
	public Concern()
	{
		this.title = "Unbenannt_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		
	}

	public Concern(ObservableList<Student> students) {
		this.title = "Unbenannt_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		this.students = students;
	}

	public int getId() {

		return id;

	}

	public String getTitle() {
		return title;
	}

	public ObservableList<Form> getData() {
		return data;
	}

	public Topic getTopic() {
		return topic;
	}

	public ObservableList<Appointment> getAppointments() {
		return appointments;
	}

	public ObservableList<Reminder> getReminders() {
		return reminders;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setData(ObservableList<Form> data) {
		this.data = data;
	}

	public void setTopic(Topic topic) {
		this.topic = topic;
	}

	public void setAppointments(ObservableList<Appointment> appointments) {
		this.appointments = appointments;
	}

	public void setReminders(ObservableList<Reminder> reminders) {
		this.reminders = reminders;
	}

	public ObservableList<Student> getStudents() {
		return students;
	}

	public void setStudents(ObservableList<Student> students) {
		this.students = students;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	

}
