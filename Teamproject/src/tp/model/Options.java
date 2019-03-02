package tp.model;

import java.io.Serializable;

public class Options implements Serializable{

	private static final long serialVersionUID = 7924155370404919520L;
	
	//TODO Sicherheit!!!!!!!!
	private String userID;
	private String password;
	private String lastUsedIP;
	
	public Options(String userID, String password)
	{
		this.userID = userID;
		this.password = password;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getLastUsedIP() {
		return lastUsedIP;
	}

	public void setLastUsedIP(String lastUsedIP) {
		this.lastUsedIP = lastUsedIP;
	}
	
	

}
