package tp.model;

import java.util.Date;

public class Appointment {

	int id;
	int concernID;
	Date date;
	long startTime;
	long endTime;
	int roomNmb;
	Date reminderTime;
	boolean reminderTimeisActive;

	public Appointment(int id, int concernID, Date date, long startTime, long endTime, int roomNmb, Date reminderTime,
			boolean reminderTimeisActive) {
		this.id = id;
		this.concernID = concernID;
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

	public int getConcernID() {
		return concernID;
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

	public int getRoomNmb() {
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

	public void setConcernID(int concernID) {
		this.concernID = concernID;
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

	public void setRoomNmb(int roomNmb) {
		this.roomNmb = roomNmb;
	}

	public void setReminderTime(Date reminderTime) {
		this.reminderTime = reminderTime;
	}

	public void setReminderTimeisActive(boolean reminderTimeisActive) {
		this.reminderTimeisActive = reminderTimeisActive;
	}
	
	

}
