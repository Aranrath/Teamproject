package tp.model;

import java.sql.Date;

public class Reminder {
	
	private int id;
	private String message;
	private Date date;
	
	public Reminder(String message, Date date)
	{
		this.message = message;
		this.date = date;
	}

	public int getId() {
		return id;
	}

	public String getMessage() {
		return message;
	}

	public Date getDate() {
		return date;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	

}
