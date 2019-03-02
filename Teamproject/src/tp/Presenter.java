package tp;

import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import tp.model.Appointment;
import tp.model.Concern;
import tp.model.EMail;
import tp.model.Form;
import tp.model.Model;
import tp.model.PO;
import tp.model.Statistic;
import tp.model.Student;
import tp.model.Subject;
import tp.model.Topic;

public class Presenter {
	private Model model;
	private MainView mainView;

	public Presenter(Model model) {
		this.model = model;

	}

	// ======================Updater======================
	public void updateWeekView() {
		// TODO
	}

	public void updateRightToolbar() {
		mainView.updateRightToolBar();
	}

	public void updateTabViews() {
		// TODO
	}

	// =====================Mail==========================

	public void sendMail(String userID, String userName, Student recipient, String subject, String content) {
		try {
			// Create a properties file containing
			// the host address of your SMTP server
			Properties mailProps = new Properties();
			mailProps.put("mail.smtp.host", "mail.fh-trier.de");

			// Create a session with the Java Mail API
			Session mailSession = Session.getDefaultInstance(mailProps);
			// Create a new mail message
			MimeMessage message = new MimeMessage(mailSession);
			// Set the From and the Recipient
			message.setFrom(new InternetAddress(userID + "@fh-trier.de", userName));
			// TODO get richtige?? E-Mail adresse (wie zugeordnet?)
			message.setRecipient(Message.RecipientType.TO,
					new InternetAddress(recipient.geteMailAddresses()[0], recipient.getName()));
			// Set the subject
			message.setSubject(subject);
			// Set the message text
			message.setText(content);
			// Save all the changes you have made
			// to the message
			message.saveChanges();
			// Create a transport object for sending mail
			Transport transport = mailSession.getTransport("smtp");
			// Send the message
			transport.connect();
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();

			// save E-mail to Database
			EMail email = new EMail(content, subject, recipient, false);
			model.saveMail(email);
		} catch (Exception e) {
			// TODO Fehlerbehandlung ohne crash des Programms
			e.printStackTrace();
		}
	}

	// ===============Getter&Setter========================

	public void setMainView(MainView mainView) {
		this.mainView = mainView;
	}

	public String[] getSessionTabsIds() {
		return model.loadSessionTabsIds();
	}

	public Appointment[] getNext24hourAppointments() {
		return model.loadNext24hourAppointments();
	}

	public Student getStudent(int mtrNr) {
		return model.getStudent(mtrNr);

	}

	public Concern getConcern(int concernId) {
		return model.getConcern(concernId);
	}

	public Statistic getStatistic(int statisticId) {
		return model.getStatistic(statisticId);
	}

	public void deleteStudent(Student s) {
		model.deleteStudent(s);

	}

	public void openNewStudentTab() {
		mainView.openNewStudentTab();

	}

	public void openNewConcernTab(ObservableList<Student> students) {
		mainView.openNewConcernTab(students);

	}
	public void openNewConcernTab() {
		mainView.openNewConcernTab();

	}

	public Date[] getWorkWeekOfDate(Date date) {
		return model.getWorkWeekOfDate(date);
	}

	public int getKwOfDate(Date date) {
		return model.getKwOfDate(date);
	}

	public Appointment[] getWeeksAppointments(int shownKw) {
		return model.getWeeksAppointments(shownKw);
	}

	public Date getStartOfNextWeek(Date date) {
		return model.getStartOfNextWeek(date);
	}

	public Date getEndOfPreviousWeek(Date date) {
		return model.getEndOfPreviousWeek(date);
	}

	public ObservableList<PO> getPOs() {
		return model.getPOs();
	}

	public ObservableList<Topic> getTopics() {
		return model.getTopics();
	}

	public ObservableList<Subject> getSubjects() {
		return model.getSubjects();
	}

	public void saveNewSubject(String title, int ects) {
		if (model.saveNewSubject(title, ects)) {
			mainView.updateSubjectRelatedTabs();
		}
		else
		{
			Alert alert = new Alert(AlertType.ERROR, "Neues Modul konnte nicht in der Datenbank gespeichert werden",
					ButtonType.OK);
			alert.showAndWait();
		}
	}

	public void saveEditedSubject(String title, int ects, int id) {

		if (model.saveEditedSubject(title, ects, id)) {
			mainView.updateSubjectRelatedTabs();
		}
		else
		{
			Alert alert = new Alert(AlertType.ERROR, "Modul-Änderungen konnte nicht in der Datenbank gespeichert werden",
					ButtonType.OK);
			alert.showAndWait();
		}
		
	}

	public ObservableList<Form> getForms() {
		return model.getForms();
	}

	public void saveNewTopic(String title, ArrayList<Object> selectedForms) {
		model.saveNewTopic(title, selectedForms);
		
	}
	
	public void saveEditedTopic(String text, ArrayList<Object> selectedForms, Topic topic) {
		model.saveEditedTopic(text, selectedForms, topic);
		
	}

	public void saveEditedPO(String newPOName, ArrayList<Object> selectedOptionalSubjects, ArrayList<Object> selectedMandatorySubjects, PO po) {
		model.saveEditedPO(newPOName, selectedOptionalSubjects,selectedMandatorySubjects, po);
		
	}

	public void deleteConcern(Concern c) {
		model.deleteConcern(c);
		
	}

	public void saveNewForm(Form form) {
		model.saveNewForm(form);
		
	}

	public void deleteForm(Form f) {
		model.deleteForm(f);
		
	}

	public ObservableList<Concern> getConcerns(Student student) {
		return model.getConcerns(student);
	}

}
