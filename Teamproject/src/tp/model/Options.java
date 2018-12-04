package tp.model;

import java.io.Serializable;

public class Options implements Serializable{

	private static final long serialVersionUID = 7924155370404919520L;
	
	//TODO Sicherheit!!!!!!!!
	String userID;
	String password;
	
	public Options(String userID, String password)
	{
		this.userID = userID;
		this.password = password;
	}

}
