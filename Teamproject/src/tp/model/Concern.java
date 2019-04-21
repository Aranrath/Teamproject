package tp.model;

import java.sql.Date;

import javafx.collections.ObservableList;

public class Concern {

	private long id;
	private String title;
	private ObservableList<Form> files;
	private Topic topic;
	private ObservableList<Appointment> appointments;
	private ObservableList<Reminder> reminders;
	private ObservableList<Student> students;
	private String notes;
	/*
	 * isCompleted = true + closingDate <Date> => Erfolgreich abgeschlossen am <Date>
	 * isCompleted = false + closingDate <Date> => Abgebrochen am <Date>
	 * isCompleted = false + closingDate null => Noch am Laufen
	 * "4. Zustand": Anliegen gelöscht weil Fehleintrag o.Ä., wird dann jedoch aus der DB gelöscht
	 */
	private boolean isCompleted;
	private Date closingDate;

	public Concern(String title, Topic topic)
	{
		this.title = title;
		this.topic = topic;
	}

	public Concern(long id, String title, ObservableList<Form> files, Topic topic, ObservableList<Appointment> appointments,
			ObservableList<Reminder> reminders, ObservableList<Student> students, String notes, Date completionDate, boolean isCompleted) 
	{
		this.id = id;
		this.title= title;
		this.files= files;
		this.topic= topic;
		this.appointments = appointments;
		this.reminders= reminders;
		this.students= students;
		this.notes= notes;
		this.closingDate=completionDate;
		this.isCompleted = isCompleted;
	}

	
	public long getId() {

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

	public void setId(long id) {
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
	
	public boolean isClosed() {
		return isCompleted;
	}

	public Date getCompletionDate() {
		return closingDate;
	}

	public void setClosed(boolean isClosed) {
		this.isCompleted = isClosed;
	}

	public void setCompletionDate(Date completionDate) {
		this.closingDate = completionDate;
	}
	
	
	//For TableView
	public Date getNextAppointment() {
		Date nextAppointment = null;
		for (Appointment a: appointments) {
			if (nextAppointment == null) {
				nextAppointment = a.getDate();
			}else if (a.getDate().after(new Date(System.currentTimeMillis())) && nextAppointment.before(a.getDate())){
				nextAppointment = a.getDate();
			}
		}
		return nextAppointment;
	}

	public String getStudentsString() {
		String studentsString = "";
		for (Student s: students) {
			studentsString += s.getFirstName().charAt(0) + ". " + s.getName() + ", ";
		}
		return studentsString;
	}

	@Override
	public String toString() {
		return title;
	}

	

}
