package tp.model;

public class EMail {

	private int id;
	private String content;
	private Student student;
	//TODO new Attribute, to be integratet in SQL
	private boolean received;	//true if e-mail was received, false if e-mail was send
	
	public EMail(int id, String content, Student student, boolean received) {
		super();
		this.id = id;
		this.content = content;
		this.student = student;
		this.received = received;
	}	
}
