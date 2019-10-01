package tp.model;

import java.io.Serializable;
import java.sql.Date;


public class Options implements Serializable{
	
	//--------------------------------------------------
	//öffentliche statische Attribute
	//--------------------------------------------------
	
	public static final int EARLIEST_CALENDAR_TIME = 6;
	public static final int LATEST_CALENDAR_TIME = 20;
	
	public static final int usualNumberOfMtrNrDigits = 6;
	public static final boolean saveWarningAtClose = true;
	public static final boolean spaceToolbarButtons = false;
	
	//Farben
	public static final String BUTTON_COLOR = "#98AFBC";
	public static final String TEMPORARY_OBJECT_COLOR = "#CDE6F5";
	public static final String SIGNAL_COLOR = "#C17564";
	public static final String TOOLBAR_COLOR = "#63514A";
	
	//Button-Farbe setzen:
	//myButton.setStyle("-fx-base: "+ BUTTON_COLOR);
	//Background-Farbe setzen:
	//myObject.setBackground(new Background(new BackgroundFill(Color.valueOf(Options.TOOLBAR_COLOR), CornerRadii.EMPTY, Insets.EMPTY)));
	//Schriftfarbe setzen:
	//myLabel.setTextFill(Color.web("#0076a3"));
	
	
	//--------------------------------------------------
	//private Attribute

	private static final long serialVersionUID = 7924155370404919520L;
	
	private String userName; //Name of the User, send together with E-Mail.
	private String userID; //Hochschulkennung
	private String password;
	private String lastUsedIP;
	private Date lastReminderCheck;
	private String lastUsedSavePath;
	
	public Options(String userName, String userID, String password)
	{
		this.userName = userName;
		this.userID = userID;
		this.password = password;
		lastReminderCheck = new Date(System.currentTimeMillis());
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

	public String getLastUsedSavePath() {
		return lastUsedSavePath;
	}

	public void setLastUsedSavePath(String lastUsedSavePath) {
		this.lastUsedSavePath = lastUsedSavePath;
	}
	

	
}
