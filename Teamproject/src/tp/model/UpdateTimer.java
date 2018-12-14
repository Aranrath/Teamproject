package tp.model;

import java.util.Date;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;
import javax.mail.internet.MimeMessage;

public class UpdateTimer {

	// used to filter E-Mails
	private static Date lastUpdate;
	// TODO to be initialized
	private Model model;

	public UpdateTimer() {
		// TODO
		// Aktuelle Termine der Rechten ToolBar;
		// TabSession aktualisieren und speichern;
		// nach neuen E-Mails checken und in Datenbank eintragen

		// Time aktualisierungszeitpunkt; //damit nach Termin seitenleiste aktualisiert
		// for every Termin des RESTTages (jetzt >= aktualisierungszeitpunkt)
	}

	public void checkMail(String userID, String passwd) {
		try {
			// get current Date/Time to update the Attribute lastUpdate when finished
			Date updateDate = new Date();

			// Create an empty properties object
			Properties mailProps = new Properties();
			// Create a session with the Java Mail API
			Session mailSession = Session.getDefaultInstance(mailProps);
			// Create a Store that references your POP3 mailbox
			Store store = mailSession.getStore(new URLName("pop3://" + userID + ":pass@pop.fh-trier.de"));
			// Connect to the POP3 server
			store.connect("mail.fh-trier.de", userID, passwd);
			// Locate the INBOX (POP3 has only one folder
			// and its name is INBOX)
			Folder inbox = store.getFolder("INBOX");
			// Open the folder
			inbox.open(Folder.READ_ONLY);

			// Get all messages
			for (int i = inbox.getMessageCount(); i >= 0; i--) {
				// TODO filter
				Message msg = inbox.getMessage(i + 1);
				// Filter for new E-Mails (after the date of the last Update)
				Date mailSend = msg.getSentDate();
				// if mail is not processed yet, add E-Mail to database
				if (mailSend.compareTo(lastUpdate) >= 0)
					;
				{

					Address senders[] = msg.getFrom();
					Student student = model.getStudentWithEmail(senders[0].toString());
					String subject = msg.getSubject();
					// If the message is a MimeMessage (most are)
					// The content is returned as an Object,
					// but if it's just plain text, it will
					// give back a string.
					// TODO was wenn nich mimeMessage
					String content = null;
					if (msg instanceof MimeMessage) {
						System.out.println("Message text:");
						MimeMessage mimeMsg = (MimeMessage) msg;
						content = (String) mimeMsg.getContent();
					}
					EMail mail = new EMail(content, subject, student, true);
					// TODO if(Mail noch nicht in Datenbank) {model.saveMail(mail);}
					if (!model.mailInDb(mail)) {
						model.saveMail(mail);
					}
				}
			}
			lastUpdate = updateDate;
		} catch (Exception e) {
			// TODO Fehlerbehandlung ohne crash des Programms
			e.printStackTrace();
		}
	}
}
