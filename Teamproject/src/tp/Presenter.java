package tp;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import tp.model.Appointment;
import tp.model.Concern;
import tp.model.EMail;
import tp.model.Form;
import tp.model.Model;
import tp.model.MyTab;
import tp.model.Options;
import tp.model.PO;
import tp.model.Reminder;
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

	public EMail sendMail(String userID, String userName, Student recipient, String subject, String content) {
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
					new InternetAddress(recipient.geteMailAddresses().get(0), recipient.getName()));
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
			EMail email = new EMail(content, subject, recipient, new Date(System.currentTimeMillis()), false);
			model.saveMail(email);

			return email;
		} catch (Exception e) {
			// TODO Fehlerbehandlung ohne crash des Programms
			e.printStackTrace();
			return null;
		}
	}

	// ===============Getter&Setter========================

	public void setMainView(MainView mainView) {
		this.mainView = mainView;
	}

	public ArrayList<String> getSessionTabsIds() {
		return model.getSessionTabsIds();
	}

	public ArrayList<Appointment> getNext24hourAppointments() {
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
	
	public void openRemindersTab() {
		mainView.openRemindersTab();
	}

	public void openNewConcernTab(ObservableList<Student> students) {
		mainView.openNewConcernTab(students);

	}

	public void openNewConcernTab() {
		mainView.openNewConcernTab();

	}
	
	public void openNewStatisticTab() {
		mainView.openNewStatisticTab();
		
	}

	public ArrayList<EMail> getEMails(Student student) {
		return model.getEMails(student);
	}

	public Date[] getWorkWeekOfDate(Date date) {
		return model.getWorkWeekOfDate(date);
	}

	public int getKwOfDate(Date date) {
		return model.getKwOfDate(date);
	}

	public ObservableList<Appointment> getWeeksAppointments(Date date) {
		return model.getWeeksAppointments(date);
	}

	public Date getStartOfNextWeek(Date date) {
		return model.getStartOfNextWeek(date);
	}

	public java.sql.Date getEndOfPreviousWeek(Date date) {
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
		model.saveNewSubject(title, ects);
		mainView.updateSubjectRelatedTabs();
	}

	public void saveEditedSubject(String title, int ects, int id) {

		model.saveEditedSubject(title, ects, id);
		mainView.updateSubjectRelatedTabs();

	}

	public ObservableList<Form> getForms() {
		return model.getForms();
	}

	public void saveNewTopic(String title, ObservableList<Form> selectedForms) {
		model.saveNewTopic(title, selectedForms);

	}

	public void saveEditedTopic(String text, ObservableList<Form> selectedForms, Topic topic) {
		model.saveEditedTopic(text, selectedForms, topic);

	}

	public void saveEditedPO(String newPOName, ObservableList<Subject> selectedOptionalSubjects,
			ObservableList<Subject> selectedMandatorySubjects, PO po) {
		model.saveEditedPO(newPOName, selectedOptionalSubjects, selectedMandatorySubjects, po);

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

	public Options getOptions() {
		return model.getOptions();
	}

	public void saveEditedStudent(Student student) {
		model.saveEditedStudent(student);

	}

	public void saveEditedConcern(Concern concern) {
		model.saveEditedConcern(concern);
	}

	public void saveNewConcern(Concern concern) {
		model.saveNewConcern(concern);

	}

	public void saveEditedOptions(Options changedOptions) {
		model.saveEditedOptions(changedOptions);
	}

	public void handleUnsavedTabs() {
		//TODO Tabs durchgehen nach ungespeicherten Tabs und dann nacheinander mit Alerts abfragen -> boolean unsafed bei Views einfügen?
		Platform.exit();
	}


	public ObservableList<Reminder> getDueReminders() {
		// TODO Auto-generated method stub
		return null;
	}

	public void deleteStatistic(Statistic statisticToDelete) {
		model.deleteStatistic(statisticToDelete);
		
	}

	public boolean saveNewStatistic(Statistic statistic) {
		return model.saveNewStatistic(statistic);
	}

	public void showEditUserDataView(Options options) {
		mainView.showEditUserDataView(options);
		
	}

	public void showNewReminderView(Options options) {
		mainView.showNewReminderView(options);
		
	}

	public ObservableList<Reminder> getNewReminders(java.util.Date lastReminderCheck) {
		return model.getNewReminders(lastReminderCheck);
	}

	public void openStatisticTab(Statistic newStatistic) {
		mainView.openStatisticTab(newStatistic);
		
	}

	public void closeThisTab(MyTab tab) {
		mainView.closeThisTab(tab);
	}




}
