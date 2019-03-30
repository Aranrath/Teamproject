package tp.model;

import java.sql.Date;
import java.sql.Time;

public class Appointment {

	private int id;
	private int concernId;
	private Date date;
	private long startTime;
	private long endTime;
	private String roomNmb;

	public Appointment(int concernId, Date date, long startTime, long endTime, String roomNmb) {
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

	public int getId() {
		return id;
	}

	public int getConcernId() {
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


	public void setId(int id) {
		this.id = id;
	}

	public void setConcernId(int concernId) {
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
	
	//For TableVIews
	public String getStartTimeString() {
		return new Time(startTime).toString();
	}
	
	public String getEndTimeString() {
		return new Time(startTime).toString();
	}

}
