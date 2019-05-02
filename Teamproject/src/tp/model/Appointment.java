package tp.model;

import java.sql.Date;
import java.sql.Time;

public class Appointment {

	private long id;
	private long concernId;
	private Date date;
	private long startTime;
	private long endTime;
	private String roomNmb;

	public Appointment(long concernId, Date date, long startTime, long endTime, String roomNmb) {
		this.concernId = concernId;
		this.date = date;
		this.startTime = startTime;
		this.endTime = endTime;
		this.roomNmb = roomNmb;
	}
	
	public Appointment(Date date, long startTime, long endTime, String roomNmb) {
		this.date = date;
		this.startTime = startTime;
		this.endTime = endTime;
		this.roomNmb = roomNmb;

	}

	public long getId() {
		return id;
	}

	public long getConcernId() {
		return concernId;
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


	public void setId(long id) {
		this.id = id;
	}

	public void setConcernId(long concernId) {
		this.concernId = concernId;
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
	
	//For TableViews
	public String getStartTimeString() {
		return new Time(startTime).toString().substring(0, 5);
	}
	
	public String getEndTimeString() {
		
		return new Time(endTime).toString().substring(0, 5);
	}

	//For Statistic
	//getDuration in milliseconds
	public Long getDuration(){
		return Math.abs(endTime - startTime);
	}
}
