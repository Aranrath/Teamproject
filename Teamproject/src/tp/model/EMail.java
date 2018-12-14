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
}
