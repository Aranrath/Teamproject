package tp.model;

import java.io.Serializable;
import java.sql.Date;


public class Options implements Serializable{
	
	//--------------------------------------------------
	//öffentliche statische Attribute
	//--------------------------------------------------
	
	public static final int usualNumberOfMtrNrDigits = 6;
	public static final boolean saveWarningAtClose = true;
	public static final boolean spaceToolbarButtons = false;
	
	//Farben
	public static final String BUTTON_COLOR = "#98AFBC";
	public static final String TEMPORARY_OBJECT_COLOR = "#CDE6F5"; //AZUREISH WHITE; hellblau
	public static final String SIGNAL_COLOR = "#C17564";
	public static final String TOOLBAR_COLOR = "#63514A";
	public static final String TAB_BACKGROUND_COLOR = "FFFFFF";
	
	//"#A7C0CE"; //PASTEL BLUE; Mittelblau
	//"#87919E"; //ROMANSILVER; Dunkelblau
	//"#707078"; //NICKEL; dunkles Grau-Blau
	
	
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
	private Date lastReminderCheck;
	
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

}
