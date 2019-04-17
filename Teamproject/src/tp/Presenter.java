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
import javafx.scene.image.Image;
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
			// Send to eMailAddress where last received
			String mailAddress = model.getLastEmail(recipient).getMailAddress();
			message.setRecipient(Message.RecipientType.TO,
					new InternetAddress(mailAddress, recipient.getName()));
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
			EMail email = new EMail(content, subject, mailAddress, new Date(System.currentTimeMillis()), false);
			model.saveMail(email);

			return email;
		} catch (Exception e) {
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

	public Concern getConcern(long concernId) {
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
	
	public ObservableList<Subject> getSubjects(PO po) {
		return model.getSubjects(po);
	}


	public Subject saveNewSubject(Subject newSubject) {
		return model.saveNewSubject(newSubject);
	}

	public void saveEditedSubject(Subject subject) {

		model.saveEditedSubject(subject);

	}

	public ObservableList<Form> getTopicForms() {
		return model.getTopicForms();
	}

	public Topic saveNewTopic(Topic newTopic) {
		 return model.saveNewTopic(newTopic);

	}

	public void saveEditedTopic(Topic topic) {
		model.saveEditedTopic(topic);

	}

	public PO saveNewPo(PO po) {
		return model.saveNewPO(po);
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


	public Options getOptions() {
		return model.getOptions();
	}

	public void saveEditedStudent(Student student) {
		model.saveEditedStudent(student);

	}

	public void saveEditedConcern(Concern concern) {
		model.saveEditedConcern(concern);
	}

	public int saveNewConcern(Concern concern) {
		return model.saveNewConcern(concern);

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

	public ObservableList<Reminder> getNewReminders(Date lastReminderCheck) {
		return model.getNewReminders(lastReminderCheck);
	}

	public void openStatisticTab(Statistic newStatistic) {
		mainView.openStatisticTab(newStatistic);
		
	}

	public void closeThisTab(MyTab tab) {
		mainView.closeThisTab(tab);
	}

	public ObservableList<Concern> getConcerns() {
		return model.getConcerns();
	}

	public ObservableList<Student> getStudents() {
		return model.getStudents();
	}

	public void openEditStudentView(Student student) {
		mainView.openEditStudentTab(student);
		
	}

	public void saveNewStudent(Student student) {
		model.saveNewStudent(student);
		
	}

	public void openStudenTab(Student student) {
		mainView.openStudentTab(student);
		
	}

	public void openStudenTab(Student student, ArrayList<String> changedMailAddresses) {
		mainView.openStudentTabFromEditStudentView(student,changedMailAddresses);
		
	}

	public void deleteReminder(Reminder reminderToDelete) {
		model.deleteReminder(reminderToDelete);
	
	}

	public void deleteAppointment(Appointment appointmentToDelete) {
		model.deleteAppointment(appointmentToDelete);
		
	}

	public void saveNewAppointment(Appointment newAppointment) {
		model.saveNewAppointment(newAppointment);
		
	}

	public void pullAllEMails(Student student, String address) {
		model.checkMail(student, address);
	}

	public void pullNewEMails(Student student) {
		model.checkMail(student);
	}

	public Image getDefaultStudentImage() {
		return model.getDefaultStudentImage();
	}

	public boolean mtrAlreadyExists(int mtrNr) {
		return model.mtrAlreadyExists(mtrNr);
	}

	public void changeStudentMtrNr(int oldMtrNr, int newMtrNr) {
		model.changeStudentMtrNr( oldMtrNr,  newMtrNr);
		
	}

	public void openConcernTab(Concern concern) {
		mainView.openConcernTab(concern);
		
	}

	public void saveEditedStudentNotes(Student student, String notes) {
		model.saveEditedStudentNotes( student,  notes);
		
	}

	public int calculateEcts(ObservableList<Subject> passedSubjects, PO po) {
		return model.calculateEcts(passedSubjects, po);
	}

	public void deleteTopic(Topic topicToDelete) {
		model.deleteTopic(topicToDelete);
		
	}

	public void deletePO(PO poToDelete) {
		model.deletePO(poToDelete);
		
	}

	public void deleteSubject(Subject subjectToDelete) {
		model.deleteSubject(subjectToDelete);
		
	}




}
