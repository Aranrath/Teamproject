package tp.model;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.mail.Address;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

public class Model {

	private ArrayList<String> sessionTabsIds;
	private Options options;
	//TODO Filepath bei Installation anpassen
	private final String standardDirectory = "..\\..\\Desktop\\";
	
	public Model() {
		loadSessionTabsIds();
	}
	
	
	//------------------establish Database connection-----------------------------------------------
	private Connection connect() 
	{
		Connection conn = null; 
		try 
		{
			conn = DriverManager.getConnection("jdbc:sqlite:teamprojectDatabase.db");
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return conn;
	}
	
	
	//-------------------Calculations--------------------------------------------------------------

	public ArrayList<Appointment> loadNext24hourAppointments() {
		//TODO Test
		String sql = "SELECT id FROM appointment WHERE (date == date() AND endTime >= DATE('now')) OR (date == DATE('now', '+1 day') AND startTime<= date())";		
		ArrayList<Appointment> result = new ArrayList<Appointment>();
		try 	(Connection conn = this.connect();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql))
		{
			while(rs.next())
			{
				int id = rs.getInt("id");
				result.add(getAppointment(id));
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}
	
	public Date[] getWorkWeekOfDate(Date date) {
		Date[] d = new Date[5];
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.setTime(date);	
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		d[0] = new Date(cal.getTime().getTime());
		cal.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
		d[1] = new Date(cal.getTime().getTime());
		cal.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
		d[2] = new Date(cal.getTime().getTime());
		cal.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
		d[3] = new Date(cal.getTime().getTime());
		cal.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
		d[4] = new Date(cal.getTime().getTime());
		return d;
	}
	
	public ObservableList<Reminder> getDueReminders() {
		ObservableList<Reminder> result = FXCollections.observableArrayList();
		String sql ="SELECT id FROM reminder WHERE date <= date()";
		try(Connection conn = this.connect();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql))
		{
			while(rs.next()) 
			{
				int id = rs.getInt("id");
				result.add(getReminder(id));
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public int getKwOfDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.WEEK_OF_YEAR);
	}
	
	//TODO Test am Jahresübergang
	public ObservableList<Appointment> getWeeksAppointments(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.setTime(date);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		Date weekStart = new Date(cal.getTime().getTime());
		cal.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
		Date weekEnd = new Date(cal.getTime().getTime());
		
		ObservableList<Appointment> result = FXCollections.observableArrayList();
		String sql = "SELECT * FROM appointment WHERE date >= " + weekStart + " AND date <= " + weekEnd;
		try (Connection conn = this.connect();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql))
		{
			while(rs.next())
			{
				int id =rs.getInt("id");
				result.add(getAppointment(id));
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}
	
	public Date getStartOfNextWeek(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.WEEK_OF_YEAR, +1);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		cal.setTime(date);
		return new Date(cal.getTime().getTime());
		
	}

	public java.sql.Date getEndOfPreviousWeek(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.WEEK_OF_YEAR, -1);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
		return new Date(cal.getTime().getTime());		
	}
	
	//------------------File: Loader&Saver + Getter/Setter---------------------------------------------------------------
	
	
	public ArrayList<String> getSessionTabsIds() {
		if (sessionTabsIds == null)
		{
			loadSessionTabsIds();
		}
		return sessionTabsIds;
	}
	
	public void saveSessionTabsIds() 
	{
		try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(standardDirectory +"SessionTabsIds.ser")))
		{
			oos.writeObject(sessionTabsIds);
		}
		catch (Exception e)
		{
			System.out.println("saving sessionTabsIds failed");
		}

	}

	public void saveOptions() 
	{

		try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(standardDirectory + "Options.ser")))
		{
			oos.writeObject(options);
		} 
		catch (IOException e) 
		{
			System.out.println("saving options failed");
		}
	}
	
	public void saveEditedOptions(Options changedOptions) {
		options = changedOptions;
		saveOptions();
	}

	@SuppressWarnings("unchecked")
	public void loadSessionTabsIds() 
	{

		try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(standardDirectory + "SessionTabsIds.ser")))
		{
			sessionTabsIds = (ArrayList<String>) ois.readObject();
		}
		catch (Exception e)
		{
		}
		if(sessionTabsIds == null) {
			sessionTabsIds = new ArrayList<String>();
			saveSessionTabsIds();
		}
	}

	public void loadOptions() {
		
		try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(standardDirectory + "Options.ser")))
		{
			options = (Options) ois.readObject();
		}
		catch (Exception e)
		{
		}	
		if (options == null) {
			options = new Options(null, null, null);
			saveOptions();
		}

	}
	
	public Options getOptions()
	{
		if(options==null)
		{
			loadOptions();
		}
		return options;
	}

	// ------------Datenbank Abfragen--------------------------------------------------------------------
	
	// ------------Datenbank Getter----------------------------------------------------------------------

	
	public Appointment getAppointment(int id) {
		Appointment result = new Appointment(0, null, 0, 0, null);
		String sql = "SELECT * FROM appointment WHERE id = " + id;
		try 	(Connection conn = this.connect();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql))
		{
			if (rs.next()) {
			
				int concernId = rs.getInt("concern");
				Date date = rs.getDate("date");
				long startTime = rs.getLong("startTime");
				long endTime = rs.getLong("endTime");
				String roomNmb = rs.getString("roomNmb");
				result = new Appointment(concernId, date, startTime, endTime, roomNmb);
				result.setId(id);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
			return result;
	}


	private ObservableList<Appointment> getAppointments(long concernId) {
		ObservableList<Appointment>  result = FXCollections.observableArrayList();
		String sql = "SELECT id FROM appointment WHERE concern = " + concernId;
		try (Connection conn = this.connect();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql))
		{
			while(rs.next()) {
				int id = rs.getInt("id");
				result.add(getAppointment(id));
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}


	public Concern getConcern(long concernId) 
	{
		Concern result = new Concern(null, null);
		String sql = "SELECT * FROM concern WHERE id = " + concernId;
		try (Connection conn = this.connect();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql))
		{
			if (rs.next()) {
			
				String title = rs.getString("title"); 
				ObservableList<Form> forms = getConcernForms(concernId);
				Topic topic =getTopic(rs.getInt("topic"));
				ObservableList<Appointment> appointments = getAppointments(concernId);
				ObservableList<Reminder> reminders = getReminders(concernId);
				ObservableList<Student> students = getStudents(concernId);
				String notes = rs.getString("notes");	
			
				result = new Concern (concernId, title, forms, topic, appointments, reminders, students, notes);
			}
	
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}


	public ObservableList<Concern> getConcerns() {
		ObservableList<Concern> result = FXCollections.observableArrayList();
		String sql = "SELECT * FROM concern";
		try (Connection conn = this.connect();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql))
		{
			while(rs.next())
			{
				int id = rs.getInt("id");
				result.add(getConcern(id));
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}

	private ObservableList<Form> getConcernForms(long concernId) {
		ObservableList<Form>  result = FXCollections.observableArrayList();
		String sql = "SELECT form FROM concern_forms WHERE concern = " + concernId;
		try (Connection conn = this.connect();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql))
		{
			while(rs.next()) {
				int id = rs.getInt("form");
				result.add(getForm(id));
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}


	public ArrayList<EMail> getEMails(Student student) {
		ArrayList<EMail> mail = new ArrayList<EMail>();
		ArrayList<String> mailAddress = student.geteMailAddresses();
		String sql = null;
		if(mailAddress.size()==3) {
			sql = "SELECT * FROM eMail WHERE eMailAddress = '"+ mailAddress.get(0) + "' OR eMailAddress = '" 
					+ mailAddress.get(1) + "' OR eMailAddress = '"+ mailAddress.get(2) + "' ORDER BY date ASC";
		}else if(mailAddress.size()==2) {
			sql = "SELECT * FROM eMail WHERE eMailAddress = '"+ mailAddress.get(0) + "' OR eMailAddress = '" 
					+mailAddress.get(1) + "' ORDER BY date ASC";
		}else if (mailAddress.size()==1) {
			sql = "SELECT * FROM eMail WHERE eMailAddress = '"+ mailAddress.get(0) +"' ORDER BY date ASC";
		}else {
			return mail;
		}
		try (Connection conn = this.connect();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql))
		{
			while(rs.next()) {
				String subject = rs.getString("subject");
				String content = rs.getString("content");
				String eMailAddress = rs.getString("eMailAddress");
				Boolean recieved = rs.getBoolean("received");
				Date date = rs.getDate("date");
				mail.add(new EMail(content, subject, eMailAddress, date, recieved));
			}
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return mail;
	}
	
	public EMail getLastEmail(Student student) {
		ArrayList<EMail> mails = getEMails(student);
		if(!mails.isEmpty()) {
			return mails.get(mails.size() - 1);
		}
		return null;
	}


	private ArrayList<String> getEMailAddressess(int mtrNr) {
		ArrayList<String> result = new ArrayList<String>();
		String sql ="SELECT emailAddress FROM student_emailAddress WHERE student = " + mtrNr;
		
		try (Connection conn = this.connect();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql))
			{
			while (rs.next()) {
				result.add(rs.getString("emailAddress"));
			}
			}catch (Exception e) {
				e.printStackTrace();
			}
		return result;
	}


	public Form getForm(int id) {
		//TODO Teest
		Form result = new Form(null, null);
		String sql = "SELECT * FROM form WHERE id = " + id;
		try 	(Connection conn = this.connect();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql))
		{
			if (rs.next()) {
		
				String title = rs.getString("title");
			
				File file = File.createTempFile(title, ".tmp");
				try(FileOutputStream out = new FileOutputStream(file);
						InputStream in = rs.getBinaryStream("file")){
					byte[] buffer = new byte[1024];
					while(in.read(buffer)>0) {
						out.write(buffer);
					}
				}catch(Exception e) {
					e.printStackTrace();
				}
				result = new Form(title, file);
				result.setId(id);
				file.deleteOnExit();
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private ObservableList<Subject> getPassedSubjects(int mtrNr) {
		ObservableList<Subject>  result = FXCollections.observableArrayList();
		String sql = "SELECT subject FROM passed_subjects WHERE student = " + mtrNr;
		try (Connection conn = this.connect();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql))
		{
			while(rs.next()) {
				int id = rs.getInt("subject");
				result.add(getSubject(id));
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public ObservableList<PO> getPOs() 
	{
		ObservableList<PO> result = FXCollections.observableArrayList();
		String sql = "SELECT * FROM po";
		try (Connection conn = this.connect();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql))
		{
			while(rs.next())
			{
				int id = rs.getInt("id");
				String name = rs.getString("name");
				ObservableList<Subject> optional = getSubjects(id, true);
				ObservableList<Subject> mandatory = getSubjects(id, false);
				PO po = new PO(name);
				po.setId(id);
				po.setOptionalSubjects(optional);
				po.setMandatorySubjects(mandatory);
				result.add(po);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return result;
		
	}


	public Reminder getReminder(int id) {
		Reminder result = new Reminder(null, null);
		String sql = "SELECT * FROM reminder WHERE id = " + id;
		try 	(Connection conn = this.connect();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql))
		{
			if (rs.next()) {
		
				String message = rs.getString("message");
				Date date = rs.getDate("date");
				result = new Reminder(message, date);
				result.setId(id);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}


	private ObservableList<Reminder> getReminders(long concernId) {
		ObservableList<Reminder>  result = FXCollections.observableArrayList();
		String sql = "SELECT id FROM reminder WHERE concern = " + concernId;
		try (Connection conn = this.connect();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql))
		{
			while(rs.next()) {
				int id = rs.getInt("id");
				result.add(getReminder(id));
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public ObservableList<Reminder> getNewReminders(Date lastReminderCheck) {
		
		ObservableList<Reminder> result = FXCollections.observableArrayList();
		String sql ="SELECT id FROM reminder WHERE date >= date() AND date <= " + lastReminderCheck;
		try(Connection conn = this.connect();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql))
		{
			while(rs.next()) 
			{
				int id = rs.getInt("id");
				result.add(getReminder(id));
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		if (result.isEmpty()) {
			return null;
		}
		return result;
	}


	public Statistic getStatistic(int statisticId) 
	{
		// TODO Auto-generated method stub
		return null;
	}


	public Student getStudent(int mtrNr) 
	{
	Student result = new Student(0, null, null, null, 0, null, null, null, null, null, null);
	String sql1 = "SELECT * FROM student WHERE matrNr = " + mtrNr;
	String sql2 = "SELECT concern FROM concern_student WHERE student = " + mtrNr;
	
	try (Connection conn = this.connect();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql1))				
		{
			if (rs.next()) {
			
				String name = rs.getString("name");
				String firstname = rs.getString("firstname");
				ArrayList<String> eMailAddressess = getEMailAddressess(mtrNr);
				int semester = rs.getInt("semester");
				String notes = rs.getString("notes");
				ObservableList<Subject> passedSubjects= getPassedSubjects(mtrNr);
				Image img = null;
				try(InputStream is= rs.getBinaryStream("image")){
					if (is!=null) {
						img = new Image(is);		
					}
				}catch(Exception e) {
					e.printStackTrace();
				}
				String gender = rs.getString("gender");
				
				result = new Student(mtrNr, name, firstname, eMailAddressess, semester, notes, passedSubjects, img, null, gender, null);
				if(getLastEmail(result)!=null) {
					Date lastContact = getLastEmail(result).getDate();
					result.setLastContact(lastContact);
				}
			}
		}	
		catch(Exception e)
		{
			e.printStackTrace();
		}	
		try (Connection conn = this.connect();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql2))				
		{
			ObservableList<Long> concerns = FXCollections.observableArrayList();
			while(rs.next())
			{
				concerns.add(rs.getLong("concern"));
			}
			result.setConcernIds(concerns);
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		return result;
	}


	public ObservableList<Student> getStudents() {
		ObservableList<Student> result = FXCollections.observableArrayList();
		String sql = "SELECT * FROM student";
		try (Connection conn = this.connect();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql))
		{
			while(rs.next())
			{
				int mtrNr = rs.getInt("matrNr");
				result.add(getStudent(mtrNr));
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}


	private ObservableList<Student> getStudents(long concernId) {
		ObservableList<Student>  result = FXCollections.observableArrayList();
		String sql = "SELECT student FROM concern_student WHERE concern = " + concernId;
		try (Connection conn = this.connect();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql))
		{
			while(rs.next()) {
				result.add(getStudent(rs.getInt("student")));
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}


	private Subject getSubject(int id) {
		Subject result = new Subject(null, 0);
		String sql = "SELECT * FROM subject WHERE id = " + id;
		try (Connection conn = this.connect();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql))
			{
					if (rs.next()) {
					String title = rs.getString("title");
					int ects = rs.getInt("ects");
				
					result = new Subject(title, ects);
					result.setId(id);
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
		return result;
	}


	public ObservableList<Subject> getSubjects() 
	{
		ObservableList<Subject> result = FXCollections.observableArrayList();
		String sql = "SELECT * FROM subject";
		try (Connection conn = this.connect();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql))
		{
			while(rs.next())
			{
				int id = rs.getInt("id");
				result.add(getSubject(id));
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}
	
	public ObservableList<Subject> getSubjects(PO po) 
	{
		ObservableList<Subject> result = getSubjects();
		result = addMandatoryOptional(result, po);
		return result;
	}
	

	private ObservableList<Subject> addMandatoryOptional(ObservableList<Subject> result, PO po) {
		String sql = "SELECT * FROM po_subject WHERE po = " + po.getId();
		try (Connection conn = this.connect();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql))
			{
				while(rs.next())
				{
					int id = rs.getInt("subject");
					System.out.println(id);
					boolean optional = rs.getBoolean("optional");
					for (Subject s: result) {
						if(s.getId() == id) {
							s.setOptional(optional);
							s.setMandatory(!optional);
							break;
						}
					}
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		return result;
	}
	

	private ObservableList<Subject> getSubjects(int poId, boolean optional) {
		ObservableList<Subject> result = FXCollections.observableArrayList();
		String sql = "SELECT subject FROM po_subject WHERE po = " + poId + " AND optional = " + optional;
		try (Connection conn = this.connect();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql))
		{
			while(rs.next()) {
				result.add(getSubject(rs.getInt("subject")));
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}


	public Topic getTopic(int topicId) {
		Topic result = new Topic(null);
		String sql = "SELECT * FROM topic WHERE id = " + topicId;
		try(Connection conn = this.connect();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)){
			if (rs.next()) {
				String title = rs.getString("title");
			
				ObservableList<Form> forms = getTopicForms(topicId);
				result = new Topic(title, forms);	
				result.setId(topicId);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}


	public ObservableList<Topic> getTopics() 
	{
		ObservableList<Topic> result = FXCollections.observableArrayList();
		String sql = "SELECT * FROM topic";
		try (Connection conn = this.connect();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql))
		{
			while(rs.next())
			{
				int id = rs.getInt("id");
				String title = rs.getString("title");
				ObservableList<Form> forms = getTopicForms(id);
				Topic topic = new Topic(title, forms);
				topic.setId(id);
				result.add(topic);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}


	public ObservableList<Form> getTopicForms() 
	{
		ObservableList<Form> result = FXCollections.observableArrayList();
		String sql = "SELECT * FROM form";
		try (Connection conn = this.connect();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql))
		{
			while(rs.next())
			{
				int id = rs.getInt("id");
				Form form = getForm(id);
				result.add(form);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}


	private ObservableList<Form> getTopicForms(int topicId) {
		ObservableList<Form>  result = FXCollections.observableArrayList();
		String sql = "SELECT form FROM topic_forms WHERE topic = " + topicId;
		try (Connection conn = this.connect();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql))
		{
			while(rs.next()) {
				int id = rs.getInt("form");
				result.add(getForm(id));
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	// ------------Datenbank Datenännderungen------------------------------------------------------------
	
	public void saveNewAppointment(Appointment appointment) {
		String sql = "INSERT INTO appointment(concern, date, startTime, endTime, roomNmb) values (?, ?, ?, ?, ?)";
		try (Connection conn = this.connect();
			PreparedStatement pstmt = conn.prepareStatement(sql))
		{
			pstmt.setLong(1, appointment.getConcernId());
			pstmt.setDate(2, appointment.getDate());
			pstmt.setLong(3, appointment.getStartTime());
			pstmt.setLong(4, appointment.getEndTime());
			pstmt.setString(5, appointment.getRoomNmb());
			pstmt.executeUpdate();
		} 
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}


	private void addAppointments(ObservableList<Appointment> appointments) {
		for (Appointment a: appointments) {
			saveNewAppointment(a);
		}
	}


	public void deleteAppointment(Appointment appointmentToDelete) {
		String sql = "DELETE FROM appointment WHERE id = "+ appointmentToDelete.getId();
		try (Connection conn = this.connect();
			Statement stmt = conn.createStatement())
		{
			stmt.executeQuery(sql);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}


	public int saveNewConcern(Concern concern) {
		
		int id = 0; 
		String sql1 = "INSERT INTO concern (title, topic, notes) values (?, ?, ?)";
		String sql2 = "SELECT last_insert_rowid()";
		try (Connection conn = this.connect();
			PreparedStatement pstmt = conn.prepareStatement(sql1);
			Statement stmt = conn.createStatement();
			)
		{
			pstmt.setString(1, concern.getTitle());
			pstmt.setLong(2, concern.getTopic().getId());
			pstmt.setString(3, concern.getNotes());
			pstmt.executeUpdate();
			try (ResultSet rs = stmt.executeQuery(sql2)){
				if (rs.next()) {
					id = rs.getInt(1);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			addConcernForms(concern.getId(), concern.getFiles());
			addConcernStudents(concern.getId(), concern.getStudents());
			addAppointments(concern.getAppointments());
			addReminders(concern.getId(), concern.getReminders());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return id;
	}


	public void saveEditedConcern(Concern concern) {
		String sql1 = "UPDATE concern SET title = " + concern.getTitle() + ", topic = " + concern.getTopic().getId() +
				", notes = " + concern.getNotes();
		String sql2 = "DELETE FROM concern_forms WHERE concern = " + concern.getId();
		String sql3 = "DELETE FROM concern_student WHERE concern = " + concern.getId();
		String sql4 = "DELETE FROM appointment WHERE concern = " + concern.getId();
		String sql5 = "DELETE FROM reminder WHERE concern = " + concern.getId();
		try (Connection conn = this.connect();
			Statement stmt = conn.createStatement())
		{
			stmt.executeQuery(sql1);
			stmt.executeQuery(sql2);
			stmt.executeQuery(sql3);
			stmt.executeQuery(sql4);
			stmt.executeQuery(sql5);
			addConcernForms(concern.getId(), concern.getFiles());
			addConcernStudents(concern.getId(), concern.getStudents());
			addAppointments(concern.getAppointments());
			addReminders(concern.getId(), concern.getReminders());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}


	public void deleteConcern(Concern c) 
	{
		String sql = "DELETE FROM concern WHERE id = "+ c.getId();
		try (Connection conn = this.connect();
			Statement stmt = conn.createStatement())
		{
			stmt.executeUpdate(sql);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}


	private void addConcernForms(long id, ObservableList<Form> data) {
		String sql;
		try (Connection conn = this.connect();
				Statement stmt = conn.createStatement())
			{
			for (Form form: data){
				sql="INSERT INTO concern_forms (concern, form) VALUES (" + id + ", " + form.getId() + ")";
				stmt.executeQuery(sql);
			}
			}catch (Exception e) {
				e.printStackTrace();
			}
	}


	private void addConcernsStudent(ObservableList<Long> concernIds, int mtrNr) {
		String sql;
		try (Connection conn = this.connect();
				Statement stmt = conn.createStatement())
			{
			for (Long concern: concernIds){
				sql="INSERT INTO concern_student (concern, student) VALUES (" + concern + ", " + mtrNr + ")";
				stmt.executeQuery(sql);
			}
			}catch (Exception e) {
				e.printStackTrace();
			}
		
	}


	private void addConcernStudents(long id, ObservableList<Student> students) {
		String sql;
		try (Connection conn = this.connect();
				Statement stmt = conn.createStatement())
			{
			for (Student student: students){
				sql="INSERT INTO concern_student (concern, student) VALUES (" + id + ", " + student.getMtrNr()+ ")";
				stmt.executeUpdate(sql);
			}
			}catch (Exception e) {
				e.printStackTrace();
			}
	}


	public void saveNewForm(Form form) 
	{
		String sql = "INSERT INTO form (title, file) VALUES (?, ?)";
		try (Connection conn = this.connect();
			PreparedStatement pstmt = conn.prepareStatement(sql))
		{	
			File file = form.getFile();
			pstmt.setString(1, form.getName());
			pstmt.setBytes(2, readFile(file.getAbsolutePath()));
			pstmt.executeUpdate();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private byte[] readFile(String file) {
        ByteArrayOutputStream bos = null;
        File f = new File(file);
        try (FileInputStream fis = new FileInputStream(f)){            
            byte[] buffer = new byte[1024];
            bos = new ByteArrayOutputStream();
            for (int len; (len = fis.read(buffer)) != -1;) {
                bos.write(buffer, 0, len);
            }
        } catch (Exception e) {
        	e.printStackTrace();
        }

        return bos != null ? bos.toByteArray() : null;
    }


	public void deleteForm(Form f) 
	{
		String sql = "DELETE FROM form WHERE name = "+ f.getId();
		try (Connection conn = this.connect();
			Statement stmt = conn.createStatement())
		{
			stmt.executeQuery(sql);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}


	public void saveMail(EMail email) 
	{
		String sql = "INSERT INTO eMail(subject, content, eMailAddress, recived) values (?, ?, ?, ?)";
		try (Connection conn = this.connect();
			PreparedStatement pstmt = conn.prepareStatement(sql))
		{
			pstmt.setString(1, email.getSubject());
			pstmt.setString(2, email.getContent());
			pstmt.setString(3, email.getMailAddress());
			pstmt.setBoolean(4, email.isReceived());
			pstmt.executeUpdate();
		} 
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}


	public PO saveNewPO(PO po) {
		String sql = "INSERT INTO po(name) VALUES ('"+ po.getName() +"')";
		String sql2 = "SELECT last_insert_rowid()";
		try (Connection conn = this.connect();
				Statement stmt = conn.createStatement())
			{
			stmt.executeUpdate(sql);
			int poId = 0;
			try (ResultSet rs = stmt.executeQuery(sql2)){
				if (rs.next()) {
					poId = rs.getInt(1);
					po.setId(poId);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			addPoSubject(poId, po.getOptionalSubjects(), true);
			addPoSubject(poId, po.getMandatorySubjects(), false);
			
			} catch (Exception e) {
				e.printStackTrace();
			}
		return po;
		
	}
	
	public void saveEditedPO(String newPOName, ObservableList<Subject> selectedMandatorySubjects,
			ObservableList<Subject> selectedOptionalSubjects, PO po) 
	{
		String sql1 = "UPDATE po SET name = "+ newPOName +" WHERE id = " + po.getId();
		String sql2 = "DELETE FROM po_subject WHERE po = " + po.getId();
		try (Connection conn = this.connect();
			Statement stmt = conn.createStatement())
		{
			stmt.executeQuery(sql1);
			stmt.executeQuery(sql2);
			addPoSubject(po.getId(), selectedOptionalSubjects, true);
			addPoSubject(po.getId(), selectedMandatorySubjects, false);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}


	public void deletePO(PO poToDelete) {
		String sql = "DELETE FROM po WHERE id = " + poToDelete.getId();
		try (Connection conn = this.connect();
				Statement stmt = conn.createStatement()) {
			stmt.executeUpdate(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	private void addPoSubject(long poId, ObservableList<Subject> subjects, boolean optional) {
		String sql;
		try (Connection conn = this.connect();
			Statement stmt = conn.createStatement())
		{
			for (Subject subject: subjects) {
				sql = "INSERT INTO po_subject (subject, po, optional) VALUES ("+ subject.getId() + ", " + poId + ", " + optional + ")";
				stmt.executeQuery(sql);
			}
		}catch (Exception e)
		{
			e.printStackTrace();
		}
	}


	private void addReminder(long conId, Reminder reminder) {
		String sql = "INSERT INTO reminder(date, message, concern) values (?, ?, ?)";
		try (Connection conn = this.connect();
			PreparedStatement pstmt = conn.prepareStatement(sql))
		{
			pstmt.setDate(1, reminder.getDate());
			pstmt.setString(2, reminder.getMessage());
			pstmt.setLong(3, conId);
			pstmt.executeUpdate();
		} 
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}


	private void addReminders(long conId, ObservableList<Reminder> reminders) {
		for (Reminder r: reminders) {
			addReminder(conId, r);
		}
	}


	public void deleteReminder(Reminder reminderToDelete) {
		String sql = "DELETE FROM reminder WHERE id = " + reminderToDelete.getId();
		try (Connection conn = this.connect();
				Statement stmt = conn.createStatement()) {
			stmt.executeUpdate(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public Subject saveNewSubject(Subject subject)
	{
		String sql1 = "INSERT INTO subject(title, ects) VALUES('" + subject.getTitle() +"'," + subject.getEcts() +")";
		String sql2 = "SELECT last_insert_rowid()";
		try (Connection conn = this.connect();
			Statement stmt = conn.createStatement())
		{
			stmt.executeUpdate(sql1);
			int id = 0;
			try (ResultSet rs = stmt.executeQuery(sql2)){
				if (rs.next()) {
					id = rs.getInt(1);
					subject.setId(id);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return subject;
	}

	public void saveEditedSubject(Subject subject)
	{
		String sql = "UPDATE subject SET title = "+subject.getTitle()+", ects = "+subject.getEcts() + "WHERE id = " + subject.getId();
		try (Connection conn = this.connect();
			Statement stmt = conn.createStatement())
		{
			stmt.executeQuery(sql);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}


	public void deleteSubject(Subject subjectToDelete) {
		String sql = "DELETE FROM subject WHERE id = " + subjectToDelete.getId();
		try (Connection conn = this.connect();
				Statement stmt = conn.createStatement()) {
			stmt.executeUpdate(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public boolean saveNewStatistic(Statistic statistic) {
		boolean successful;
		try
		{
			//TODO Save Statistic
			successful = true;
		}
		catch(Exception e)
		{
			successful = false;
		}
		return successful;
	}


	public void deleteStatistic(Statistic statisticToDelete) {
		// TODO Auto-generated method stub
		
	}

//	File file = form.getFile();
//	pstmt.setString(1, form.getName());
//	pstmt.setBytes(2, readFile(file.getAbsolutePath()));

//	private byte[] readFile(String file) {
//        ByteArrayOutputStream bos = null;
//        File f = new File(file);
//        try (FileInputStream fis = new FileInputStream(f)){            
//            byte[] buffer = new byte[1024];
//            bos = new ByteArrayOutputStream();
//            for (int len; (len = fis.read(buffer)) != -1;) {
//                bos.write(buffer, 0, len);
//            }
//        } catch (Exception e) {
//        	e.printStackTrace();
//        }
//
//        return bos != null ? bos.toByteArray() : null;
//    }
	
	public void saveNewStudent(Student student) {
		Image img = student.getImage();
		try(ByteArrayOutputStream os = new ByteArrayOutputStream();
		   InputStream is = new ByteArrayInputStream(os.toByteArray())){
			ImageIO.write(SwingFXUtils.fromFXImage(img, null),"png", os); 
		
			String sql = "INSERT INTO student(matrNr, name, firstName, semester, po, image, notes, gender) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
			try(Connection conn = this.connect();
					PreparedStatement pstmt = conn.prepareStatement(sql))
			{
				pstmt.setInt(1, student.getMtrNr());
				pstmt.setString(2, student.getName());
				pstmt.setString(3, student.getFirstName());
				pstmt.setInt(4, student.getSemester());
				if(student.getPo()!=null) {
					pstmt.setLong(5, student.getPo().getId());
				}
				else {
					pstmt.setInt(5, 0);
				}
				pstmt.setBytes(6, os.toByteArray());
				pstmt.setString(7, student.getNotes());
				pstmt.setString(8, student.getGender());
				pstmt.executeUpdate();
				addStudentEmail(student.getMtrNr(), student.geteMailAddresses());
				addPassedSubjects(student.getMtrNr(), student.getPassedSubjects());
				if (student.getConcernIds()!= null) {
					addConcernsStudent(student.getConcernIds(), student.getMtrNr());	
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}


	public void saveEditedStudent(Student student) {	
		
		Image img = student.getImage();
		try(ByteArrayOutputStream os = new ByteArrayOutputStream();
		   InputStream is = new ByteArrayInputStream(os.toByteArray())){
			ImageIO.write(SwingFXUtils.fromFXImage(img, null),"png", os);
			String sql1 = "UPDATE student SET name = '"+student.getName()+"', firstname = '"+student.getFirstName()+"', semester = "+student.getSemester()+
				", notes = '"+student.getNotes()+", img = " +is + 
				"WHERE matrNr = " + student.getMtrNr();
			String sql2 = "DELETE FROM concern_student WHERE student = " + student.getMtrNr();
			String sql3 = "DELETE FROM passed_subjects WHERE student = " + student.getMtrNr();
			
			Student oldStudent = getStudent(student.getMtrNr());
			//get MailAddresses to be added
			ArrayList<String> newMailAddresses = student.geteMailAddresses();
			newMailAddresses.removeAll(oldStudent.geteMailAddresses());
			//get MailAddresses to be deleted
			ArrayList<String> oldMailAddresses = oldStudent.geteMailAddresses();
			oldMailAddresses.removeAll(student.geteMailAddresses());
			try(Connection conn = this.connect();
					Statement stmt = conn.createStatement())
			{
				stmt.executeUpdate(sql1);
				stmt.executeUpdate(sql2);
				stmt.executeUpdate(sql3);
				for (String address: oldMailAddresses) {
					String sql = "DELETE FROM student_emailAddress WHERE emailAddress = " + address;
					stmt.executeUpdate(sql);
				}
				addStudentEmail(student.getMtrNr(), newMailAddresses);
				addPassedSubjects(student.getMtrNr(), student.getPassedSubjects());
				addConcernsStudent(student.getConcernIds(), student.getMtrNr());
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}


	public void changeStudentMtrNr(int oldMtrNr, int newMtrNr) {
		String sql ="UPDATE student SET matrNr = " + newMtrNr + " WHERE matrNr = " + oldMtrNr;
		try(Connection conn = this.connect();
				Statement stmt = conn.createStatement())
		{
			stmt.executeUpdate(sql);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}


	public void saveEditedStudentNotes(Student student, String notes) {
		String sql ="UPDATE student SET notes = '" + notes + "' WHERE matrNr = " + student.getMtrNr();
		try(Connection conn = this.connect();
				Statement stmt = conn.createStatement())
		{
			stmt.executeUpdate(sql);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}


	public boolean mtrAlreadyExists(int mtrNr) {
		String sql = "SELECT * FROM student WHERE matrNr = " + mtrNr;
		
		try (Connection conn = this.connect();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql))				
			{
				if (rs.next()) {
					return true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		return false;
	}


	public void deleteStudent(Student s) 
	{
		String sql = "DELETE FROM student WHERE matrNr = "+ s.getMtrNr();
		try (Connection conn = this.connect();
			Statement stmt = conn.createStatement())
		{
			stmt.executeUpdate(sql);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}


	private void addStudentEmail(int mtrNr, ArrayList<String> geteMailAddresses) {
		String sql;
		try (Connection conn = this.connect();
				Statement stmt = conn.createStatement())
			{
			for (String mail: geteMailAddresses){
				sql="INSERT INTO student_emailAddress (student, emailAddress) VALUES (" + mtrNr + ", " + mail + ")";
				stmt.executeQuery(sql);
			}
			}catch (Exception e) {
				e.printStackTrace();
			}
	}

	private void addPassedSubjects(int mtrNr, ObservableList<Subject> passedSubjects) {
		String sql;
		try (Connection conn = this.connect();
				Statement stmt = conn.createStatement())
			{
			for (Subject subject: passedSubjects){
				sql="INSERT INTO passed_subjects (student, subject) VALUES (" + mtrNr + ", " + subject.getId() + ")";
				stmt.executeQuery(sql);
			}
			}catch (Exception e) {
				e.printStackTrace();
			}
	}


	public Topic saveNewTopic(Topic newTopic) 
	{
		String sql1 = "INSERT INTO topic (title) VALUES ('" + newTopic.getTitle() + "')";
		String sql2 = "SELECT last_insert_rowid()";
		try (Connection conn = this.connect();
			Statement stmt = conn.createStatement();)
		{
			stmt.executeUpdate(sql1);
			int id = 0;
			try (ResultSet rs = stmt.executeQuery(sql2)){
				if (rs.next()) {
					id = rs.getInt(1);
					newTopic.setId(id);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
				if(id!=0) {
					addTopicForms(id, newTopic.getForms());
				}
		
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}	
		return newTopic;
	}

	public void saveEditedTopic(Topic topic) 
	{
		String sql1 = "UPDATE topic SET title = "  + topic.getTitle() + "WHERE id = " + topic.getId();
		String sql2 = "DELETE FROM topic_forms WHERE topic = " + topic.getId();
		try (Connection conn = this.connect();
				Statement stmt = conn.createStatement())
			{
				stmt.executeQuery(sql1);
				stmt.executeQuery(sql2);
				addTopicForms(topic.getId(), topic.getForms());
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
	}


	public void deleteTopic(Topic topicToDelete) {
		String sql = "DELETE FROM topic WHERE id = " + topicToDelete.getId();
		try (Connection conn = this.connect();
				Statement stmt = conn.createStatement()) {
			stmt.executeUpdate(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	private void addTopicForms(long id, ObservableList<Form> selectedForms) {
		String sql;
		try (Connection conn = this.connect();
				Statement stmt = conn.createStatement())
			{
			for (Form form: selectedForms){
				sql="INSERT INTO topic_forms (topic, form) VALUES (" + id + ", " + form.getId() + ")";
				stmt.executeUpdate(sql);
			}
			}catch (Exception e) {
				e.printStackTrace();
			}
	}


	
	
	// ------------EMail--------------------------------------------------------------------
	//TODO kommt das überhaupt ins Model? SendMail is ja auch im Presenter....
	
	public void checkMail(Student student) {
		// For filtering the eMails
		Date lastEmail = getLastEmail(student).getDate();
		if (lastEmail == null) {
			lastEmail = new Date(Long.MIN_VALUE);
		}
		try {
			// Prepare connection to the Mail Server
			Properties mailProps = new Properties();
			Session mailSession = Session.getDefaultInstance(mailProps);
			Store store = mailSession.getStore("imaps");
			store.connect("mail.fh-trier.de", options.getUserID(), options.getPassword());
			
			// Get received eMails
			Folder inbox = store.getFolder("INBOX");
			inbox.open(Folder.READ_ONLY);
			// Get all inbox Messages
			for (int i = inbox.getMessageCount(); i>0; i--) {
				Message msg = inbox.getMessage(i);
				// Filter eMails for unprocessed ones with Date lastEmail
				Date sendDate = new Date(msg.getSentDate().getTime());
				if (sendDate.compareTo(lastEmail) > 0 ) {
					// Filter eMails for the corresponding student
					ArrayList<String> fromAddresses = new ArrayList<String>();
					Address senders[] = msg.getFrom();
					for (Address address : senders) {
					    fromAddresses.add(((InternetAddress)address).getAddress());
					}
					// If a MailAddress of sender and student are the same 
					String mailAddress = null;
					for (String address: student.geteMailAddresses()) {
						if (fromAddresses.contains(address)){
							mailAddress = address;
						}
					}
					if (mailAddress != null) {
						String subject = msg.getSubject();
						String content = getEmailContent(msg);
						// save eMail into Database
						EMail email = new EMail(content, subject, mailAddress, sendDate, true);
						saveMail(email);
					}					
				}else {
					//eMails prior to this one have already been processed
					break;
				}
			}
			
			// Do with the send Mails as with the received Mails
			Folder outbox = store.getFolder("sent-mail");
			outbox.open(Folder.READ_ONLY);
			for (int i = outbox.getMessageCount(); i>0; i--) {
				Message msg = outbox.getMessage(i);
				Date sendDate = new Date(msg.getSentDate().getTime());
				if (sendDate.compareTo(lastEmail) > 0 ) {
					ArrayList<String> toAddresses = new ArrayList<String>();
					Address[] recipients = msg.getAllRecipients();
					for (Address address : recipients) {
					    toAddresses.add(((InternetAddress)address).getAddress());
					}
					String mailAddress = null;
					for (String address: student.geteMailAddresses()) {
						if (toAddresses.contains(address)){
							mailAddress = address;
						}
					}
					if (mailAddress != null){
						String subject = msg.getSubject();
						String content = getEmailContent(msg);
						EMail email = new EMail(content, subject, mailAddress, sendDate, false);
						saveMail(email);
					}
				}else {
					break;
				}
				
			}
		}catch(Exception e) {
			
		}
	}
	
	//does the same as checkMail(Sudent) just for retrieving eMails from a specific eMailadress before the last known eMail
	public void checkMail(Student student, String mailAddress) {
		Date lastEmail = getLastEmail(student).getDate();
		if (lastEmail == null) {
			lastEmail = new Date(System.currentTimeMillis());
		}
		try {
			Properties mailProps = new Properties();
			Session mailSession = Session.getDefaultInstance(mailProps);
			Store store = mailSession.getStore("imaps");
			store.connect("mail.fh-trier.de", options.getUserID(), options.getPassword());
			Folder inbox = store.getFolder("INBOX");
			inbox.open(Folder.READ_ONLY);
			for (int i = inbox.getMessageCount(); i>0; i--) {
				Message msg = inbox.getMessage(i);
				Date sendDate = new Date(msg.getSentDate().getTime());
				if (sendDate.compareTo(lastEmail) < 0 ) {
					ArrayList<String> fromAddresses = new ArrayList<String>();
					Address senders[] = msg.getFrom();
					for (Address address : senders) {
					    fromAddresses.add(((InternetAddress)address).getAddress());
					}
					if (fromAddresses.contains(mailAddress)) {
						String subject = msg.getSubject();
						String content = getEmailContent(msg);
						EMail email = new EMail(content, subject, mailAddress, sendDate, true);
						saveMail(email);
					}					
				}else {
					break;
				}
			}
			
			Folder outbox = store.getFolder("sent-mail");
			outbox.open(Folder.READ_ONLY);
			for (int i = outbox.getMessageCount(); i>0; i--) {
				Message msg = outbox.getMessage(i);
				Date sendDate = new Date(msg.getSentDate().getTime());
				if (sendDate.compareTo(lastEmail) < 0 ) {
					ArrayList<String> toAddresses = new ArrayList<String>();
					Address[] recipients = msg.getAllRecipients();
					for (Address address : recipients) {
					    toAddresses.add(((InternetAddress)address).getAddress());
					}
					if(toAddresses.contains(mailAddress)){
						String subject = msg.getSubject();
						String content = getEmailContent(msg);
						EMail email = new EMail(content, subject, mailAddress, sendDate, false);
						saveMail(email);
					}
				}else {
					break;
				}
				
			}
		}catch(Exception e) {
			
		}
	}
	
public static String getEmailContent(Part message) throws Exception {
		
		String result = null;
	      //check if the content is plain text
	      if (message.isMimeType("text/plain")) {
	         result = (String) message.getContent();
	      } 
	      //check if the content has attachment
	      else if (message.isMimeType("multipart/*")) {
	         Multipart mp = (Multipart) message.getContent();
	         int count = mp.getCount();
	         for (int i = 0; i < count; i++)
	            result += getEmailContent(mp.getBodyPart(i));
	      } 
	      //check if the content is a nested message
	      else if (message.isMimeType("message/rfc822")) {
	         result = getEmailContent((Part) message.getContent());
	      }
	      else {
	         Object o = message.getContent();
	         if (o instanceof String) {
	            result = (String) o;
	         } 
	         else {
	            result = "unknown: "+ o.toString();
	         }
	      }
	      return result;

	   }


public Image getDefaultStudentImage() {
	// TODO Auto-generated method stub
	return new Image("https://i.pinimg.com/originals/e2/69/8e/e2698e465dbf3f13844e896e00f0ea30.jpg");
//	return null;
	
}


	public int calculateEcts(ObservableList<Subject> passedSubjects, PO po)
	{
		int ects = 0;
		
		for(Subject sub : passedSubjects)
		{
			if(po.getMandatorySubjects().contains(sub) || po.getOptionalSubjects().contains(sub))
			{
				ects += sub.getEcts();
			}
		}  
		
		return ects;
	}
}
