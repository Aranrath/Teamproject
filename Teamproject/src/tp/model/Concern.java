package tp.model;

import java.sql.Date;

import javafx.collections.ObservableList;

public class Concern {

	private int id;
	private String title;
	private ObservableList<Form> files;
	private Topic topic;
	private ObservableList<Appointment> appointments;
	private ObservableList<Reminder> reminders;
	private ObservableList<Student> students;
	private String notes;
	
	//for Tables
	private Date nextAppointment;
	private String studentsString;
	
	
	public Concern(String title, Topic topic)
	{
		this.title = title;
		this.topic = topic;
	}

	public Concern(int id, String title, ObservableList<Form> files, Topic topic, ObservableList<Appointment> appointments,
			ObservableList<Reminder> reminders, ObservableList<Student> students, String notes) 
	{
		this.id = id;
		this.title= title;
		this.files= files;
		this.topic= topic;
		this.appointments = appointments;
		this.reminders= reminders;
		this.students= students;
		this.notes= notes;
		
		nextAppointment = null;
		for (Appointment a: appointments) {
			if (nextAppointment == null) {
				nextAppointment = a.getDate();
			}else if (a.getDate().after(new Date(System.currentTimeMillis())) && nextAppointment.before(a.getDate())){
				nextAppointment = a.getDate();
			}
		}
		
		for (Student s: students) {
			studentsString += s.getFirstName().charAt(0) + ". " + s.getName() + ", ";
		}
	}

	public Date getNextAppointment() {
		return nextAppointment;
	}

	public String getStudentsString() {
		return studentsString;
	}

	public int getId() {

		return id;

	}

	public String getTitle() {
		return title;
	}

	public ObservableList<Form> getFiles() {
		return files;
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

	public void setFiles(ObservableList<Form> files) {
		this.files = files;
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
