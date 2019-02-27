package tp.model;

public class EMail {

	private int id;
	//TODO Attribute subject to SQL
	private String subject;
	private String content;
	private Student student;
	//TODO new Attribute, to be integratet in SQL
	private boolean received;	//true if e-mail was received, false if e-mail was send
	
	public EMail(String content, String subject, Student student, boolean received) {
		//TODO generate ID
		this.content = content;
		this.subject = subject;
		this.student = student;
		this.received = received;
	}

	public int getId() {
		return id;
	}

	public String getSubject() {
		return subject;
	}

	public String getContent() {
		return content;
	}

	public Student getStudent() {
		return student;
	}

	public boolean isReceived() {
		return received;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public void setReceived(boolean received) {
		this.received = received;
	}
	
	
}
