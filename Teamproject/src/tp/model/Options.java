package tp.model;

import java.io.Serializable;
import java.util.Date;


public class Options implements Serializable{

	private static final long serialVersionUID = 7924155370404919520L;
	
	//TODO Sicherheit!!!!!!!!
	private String userName; //Name of the User, send together with E-Mail.
	private String userID; //Hochschulkennung
	private String password;
	private String lastUsedIP;
	private Date lastReminderCheck;
	
	public Options(String userName, String userID, String password)
	{
		this.userName = userName;
		this.userID = userID;
		this.password = password;
		lastReminderCheck = new Date();
	}

	public Date getLastReminderCheck() {
		return lastReminderCheck;
	}

	public void setLastReminderCheck(Date lastReminderCheck) {
		this.lastReminderCheck = lastReminderCheck;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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
