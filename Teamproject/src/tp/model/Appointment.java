package tp.model;

import java.sql.Date;

public class Appointment {

	private int id;
	private Concern concern;
	private Date date;
	private long startTime;
	private long endTime;
	private String roomNmb;
	private Date reminderTime;
	//TODO isActive -> Abfrage ob reminderTime == null??
	private boolean reminderTimeisActive;

	public Appointment(Concern concern, Date date, long startTime, long endTime, String roomNmb, Date reminderTime,
			boolean reminderTimeisActive) {
		this.concern = concern;
		this.date = date;
		this.startTime = startTime;
		this.endTime = endTime;
		this.roomNmb = roomNmb;
		this.reminderTime = reminderTime;
		this.reminderTimeisActive = reminderTimeisActive;

	}

	public int getId() {
		return id;
	}

	public Concern getConcern() {
		return concern;
	}

	public Date getDate() {
		return date;
	}

	public long getStartTime() {
		return startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public String getRoomNmb() {
		return roomNmb;
	}

	public Date getReminderTime() {
		return reminderTime;
	}

	public boolean isReminderTimeisActive() {
		return reminderTimeisActive;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setConcern(Concern concern) {
		this.concern = concern;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public void setRoomNmb(String roomNmb) {
		this.roomNmb = roomNmb;
	}

	public void setReminderTime(Date reminderTime) {
		this.reminderTime = reminderTime;
	}

	public void setReminderTimeisActive(boolean reminderTimeisActive) {
		this.reminderTimeisActive = reminderTimeisActive;
	}
	
	

}
