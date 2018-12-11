package tp.model;

import java.util.Date;

public class ReminderMail {
	
	private int id;
	private String message;
	private Date date;
	
	public ReminderMail(int id, String message, Date date)
	{
		this.id = id;
		this.message = message;
		this.date = date;
	}

}
