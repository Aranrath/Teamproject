package tp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
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
import tp.model.Student;
import tp.model.Subject;
import tp.model.Topic;
import tp.model.statistics.Statistic;
import tp.model.statistics.StatisticComponent;

public class Presenter {
	private Model model;
	private MainView mainView;

	public Presenter(Model model) {
		this.model = model;

	}
	
	// ======================Klassen (Static) Methoden======================
	
	public static boolean containsAll(String searchText, String ...searchTerm) {
	    for (String s : searchTerm)
	    {
	    	if (!searchText.contains(s)) 
	        {
	        	return false;
	        }
	    }
	        
	    return true;
	}

	// ======================Updater======================

	public void updateRightToolbar() {
		mainView.updateRightToolBar();
	}

	// ===============Getter&Setter========================

	public void setMainView(MainView mainView) {
		this.mainView = mainView;
	}

	public ArrayList<String> getSessionTabsIds() {
		return model.getSessionTabsIds();
	}

	public ArrayList<Appointment> getNext24hourAppointments() {
		return model.getNext24hourAppointments();
	}

	public Student getStudent(long l) {
		return model.getStudent(l);

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
		return model.getEMails(student.geteMailAddresses());
	}

	public Date[] getWorkWeekOfDate(Date date) {
		return model.getWorkWeekOfDate(date);
	}

	public int getKwOfDate(Date date) {
		return model.getKwOfDate(date);
	}

	public ArrayList<Appointment> getAppointments(Date date) {
		return model.getAppointments(date);
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

	public Topic getTopic(long topicId) {
		return model.getTopic(topicId);
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

	public ObservableList<Form> getUniversalForms() {
		return model.getUniversalForms();
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

	public Form saveNewForm(Form form) {
		return model.saveNewForm(form);

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

	public void saveOptions() {
		model.saveOptions();
	}

	public void handleUnsavedTabs() {
		
		if(Options.saveWarningAtClose)
		{
			Alert alert = new Alert(AlertType.WARNING, "Ungespeicherte Änderungen gehen verloren." + "\n" + "\n" + "Sind Sie sicher, dass alle Änderungen gespeichert wurden?",
					ButtonType.YES, ButtonType.CANCEL);
			alert.showAndWait();

			if (alert.getResult() == ButtonType.YES) {
				
				//speichere die Tabs der aktuellen Session über deren ID
				model.setSessionTabsIds(mainView.getCurrentTabsIds());
				
				saveOptions();
				
				Platform.exit();
			}
		}
		else
		{
			//speichere die Tabs der aktuellen Session über deren ID
			model.setSessionTabsIds(mainView.getCurrentTabsIds());
			
			Platform.exit();
		}

	}


	public ObservableList<Reminder> getDueReminders() {
		return model.getDueReminders();
	}

	public void deleteStatistic(Statistic statisticToDelete) {
		model.deleteStatistic(statisticToDelete);
		
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

	public void openStatisticTab(Statistic statistic) {
		mainView.openStatisticTab(statistic);
		
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

	public void deleteConcernStudent(long concernId, long studentId) {
		model.deleteConcernStudent(concernId, studentId);
	}
	
	public void setStudentExmatr(Student s, Date d) {
		model.setStudentExmatr(s, d);
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
	
	public void deleteNonUniversalForms() {
		model.deleteNonUniversalForms();
		
	}

	public void pullAllEMails(Student student, String address) {
		model.checkMail(student, address);
	}

	public void pullNewEMails(Student student) {
		model.checkMail(student);
	}

	public EMail sendMail(String userID, String userName, String recipientName, String mailAddress, String subject, String content) {
		return model.sendMail(userID, userName, recipientName, mailAddress, subject, content);
	}

	public Image getDefaultStudentImage() {
		return model.getDefaultStudentImage();
	}

	public boolean mtrAlreadyExists(int mtrNr) {
		return model.mtrAlreadyExists(mtrNr);
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

	public EMail getLastEmail(Student student) {
		return model.getLastStudentEmail(student);
	}

	public void saveEditedForm(Form selectedForm) {
		model.saveEditedForm(selectedForm);
		
	}

	public Statistic calculateAndSaveNewRatioStatistic(String title, 
			ArrayList<StatisticComponent> statisticComponentsList, Date date) {
		return model.calculateAndSaveNewRatioStatistic(title, statisticComponentsList, date);
	}

	public Statistic calculateAndSaveNewContinuousStatistic(String title, 
			ArrayList<StatisticComponent> statisticComponentsList, Date startDate, Date endDate) {
		return model.calculateAndSaveNewContinuousStatistic(title, statisticComponentsList, startDate, endDate);
	}

	public Statistic calculateAndSaveNewIntervalStatistic(String title, 
			ArrayList<StatisticComponent> statisticComponentsList, Date startDate, Date endDate, int step) {
		return model.calculateAndSaveNewIntervalStatistic(title, statisticComponentsList, startDate, endDate, step);
	}

	public Appointment checkAppointmentAvailability(Date date, long startTime, long endTime) {
		return model.checkAppointmentAvailability(date, startTime, endTime);
	}

	public Concern getConcern(Appointment clashingAppointment) {
		return model.getConcern(clashingAppointment);
	}

	public ObservableList<Statistic> getStatistics() {
		return model.getStatistics();
	}
	
	
	public void closeRelatedTabs(Student student)
	{
		mainView.closeRelatedTabs(student);
	}
	
	public void closeRelatedTabs(Concern concern)
	{
		mainView.closeRelatedTabs(concern);
	}
	
	public void closeRelatedTabs(Statistic statistic)
	{
		mainView.closeRelatedTabs(statistic);
	}

	public void handleExportForm(Form selectedForm) {

		 JFileChooser chooser = new JFileChooser();
		 
		 if(getOptions().getLastUsedSavePath()!= null)
		 {
			 chooser.setSelectedFile(new File(getOptions().getLastUsedSavePath() + "/" + selectedForm.getName() ));
		 }
		 else
		 {
			 chooser.setSelectedFile(new File(Model.getStandardDirectory() + "/" + selectedForm.getName() ));
		 }
		 
		 chooser.setDialogType(JFileChooser.SAVE_DIALOG);
		 chooser.setDialogTitle("Speichern unter...");
	     chooser.setVisible(true);
	     
	     String extension = selectedForm.getFileExtension().substring(1);
	     
	     chooser.removeChoosableFileFilter(chooser.getAcceptAllFileFilter());

	    FileNameExtensionFilter filter = new FileNameExtensionFilter(extension + " (Original)", extension);
	    	 
	    chooser.setFileFilter(filter);	       

	    
	     int result = chooser.showSaveDialog(null);
	     
	     if (result == JFileChooser.APPROVE_OPTION) {

	            String choosenPath = chooser.getSelectedFile().toString();
	            
	            chooser.setVisible(false);
	            
	            getOptions().setLastUsedSavePath(choosenPath.substring(0, choosenPath.lastIndexOf("\\")));
	            
	            String outputPath;
	            
	            if(choosenPath.endsWith("." + extension))
	            {
	            	outputPath = choosenPath;
	            }
	            else
	            {
	            	outputPath = choosenPath + "." + extension;
	            }
	            
	            //Schreiben
	    		try(FileOutputStream os = new FileOutputStream(outputPath))
	    		{
	    			try(FileInputStream is = new FileInputStream(selectedForm.getFile().getAbsoluteFile()))
	    			{
	    				
	    				final byte[] buffer = new byte[1024];
	    	            int n;
	    	            while ((n = is.read(buffer)) != -1)
	    	                os.write(buffer, 0, n);
	    				
	    			}

	    			
	    		} 
	    		catch (IOException e) 
	    		{
	    		}
	        
	            
	        }
		
	}
	
	

}
