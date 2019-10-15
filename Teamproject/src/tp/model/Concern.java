package tp.model;

import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;

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

	public Concern(String title)
	{
		this.title = title;
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
	
	public boolean isCompleted() {
		return isCompleted;
	}
	
	public void setCompleted(boolean isCompleted) {
		this.isCompleted = isCompleted;
	}

	public Date getClosingDate() {
		return closingDate;
	}

	public void setClosingDate(Date closingDate) {
		this.closingDate = closingDate;
	}
	
	
	//For TableView
	public Date getNextAppointment() {
		Date nextAppointment = null;
		//Time um zu überprüfen ob startTime des Termins am heutigen Tag nach jetzigem Zeitpunkt liegt
		java.util.Date utilDate = new java.util.Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        Long time = Time.valueOf(sdf.format(utilDate)).getTime();
		for (Appointment a: appointments) {
			Date currentDate = setTimeToZero(a.getDate());
			if (nextAppointment == null) {
				nextAppointment = currentDate;
			}else if(currentDate.equals(setTimeToZero(new Date(System.currentTimeMillis()))) &&
					a.getStartTime()>=time) {
				nextAppointment = currentDate;
			}else if (currentDate.after(new Date(System.currentTimeMillis())) && nextAppointment.before(currentDate)){
				nextAppointment = currentDate;
			}
		}
		return nextAppointment;
	}
	
	private Date setTimeToZero(Date date) {
		//To be able to compare the date to the one in the dataBase, set Time to 0.
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);	
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return new Date(cal.getTime().getTime());
		}

	public String getStudentsString() {
		String studentsString = "";
		for (Student s: students) {
			studentsString += s.getFirstName().charAt(0) + ". " + s.getName() + ", ";
		}
		return studentsString;
	}
	
	public String getFilters() {
		String concernString = title;
		
		if(topic != null) {
			concernString += " " + topic.getTitle();
		}
		for(Student student: students) {
			concernString += " " + student.getFirstName() + " " + student.getName();
		}
		return concernString;
	}

	@Override
	public String toString() {
		return title;
	}

	
	

}
