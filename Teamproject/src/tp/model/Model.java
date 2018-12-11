package tp.model;

public class Model {
	
	private Session session;

	public Model()
	{
		loadLastSession();
	}
	
	private void loadLastSession() {
		// TODO Java object in Stream
		generateDummySession();
		
	}


	public void saveLastSession()
	{
		//TODO Java object out Stream
		
	}

	public Session getSession() {
		return session;
		
	}

	//----------------------------------
	
	private void generateDummySession() {
		// TODO Auto-generated method stub
		
	}
	
}
