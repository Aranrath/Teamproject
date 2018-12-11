package tp.model;

public class Model {
	
	Session session;
	
	public Model()
	{

	}



	public void saveSession()
	{
		//TODO Java object out Stream
		
	}

	public Session loadSession() {
		//TODO Java object in stream, wenn nicht da neue Datei
		return session;
		
	}
	
	public Session getSession()
	{
		return session;
	}
	public void setSession(Session session)
	{
		this.session = session;
	}
	
	
}
