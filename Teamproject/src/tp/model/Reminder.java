package tp.model;

import java.sql.Date;

public class Reminder {
	
	private long id;
	private String message;
	private Date date;
	//concernId statt Concern, da sonst im Model eine Endlosschleife aus getConcern/getReminder entsteht
	private long concernId;
	
	public Reminder(String message, Date date, Long concernId)
	{
		this.message = message;
		this.date = date;
		if (concernId != null) {
			this.concernId = concernId;
		}
	}

	public long getId() {
		return id;
	}

	public String getMessage() {
		return message;
	}

	public Date getDate() {
		return date;
	}

	public Long getConcernId() {
		return concernId;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setConcernId(Long concernId) {
		this.concernId = concernId;
	}

	@Override
	public String toString() {
		return date + ": " + message;
	}
	



}
