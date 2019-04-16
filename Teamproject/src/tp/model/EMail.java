package tp.model;

import java.sql.Date;

public class EMail {

	private long id;
	private Date date;
	private String subject;
	private String content;
	private String mailAddress;
	private boolean received; // true if e-mail was received, false if e-mail was send

	public EMail(String content, String subject, String mailAddress, Date date, boolean received) {
		this.content = content;
		this.subject = subject;
		this.mailAddress = mailAddress;
		this.date = date;
		this.received = received;
	}

	public long getId() {
		return id;
	}

	public String getSubject() {
		return subject;
	}

	public String getContent() {
		return content;
	}

	public String getMailAddress() {
		return mailAddress;
	}

	public Date getDate() {
		return date;
	}

	public boolean isReceived() {
		return received;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setmailAddress(String mailAddress) {
		this.mailAddress = mailAddress;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setReceived(boolean received) {
		this.received = received;
	}

}
