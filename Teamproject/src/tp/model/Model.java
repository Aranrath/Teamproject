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

import javax.imageio.ImageIO;

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

	
	public Student getStudent(String emailAdresse) 
	{
		String sql = "SELECT matrNr"
				+ "FROM student, student_emailAddress "
				+ "WHERE matrNr = student and eMailAddress = " + emailAdresse;
		try (Connection conn = this.connect();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql))
		{
			rs.next();
			return getStudent(rs.getInt("matrNr"));
		}		
		catch(Exception e)
		{
			e.printStackTrace();
		}	
		//not possible to get here
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


	public Student getStudent(int mtrNr) 
	{
		Student result = new Student(0, null, null, null, 0, null, 0, null, null);
		String sql = "SELECT * FROM student WHERE Matrikelnummer = " + mtrNr;
		
		try (Connection conn = this.connect();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql))
			{
				rs.next();
				
				String name = rs.getString("name");
				String firstname = rs.getString("firstname");
				ArrayList<String> eMailAddressess = getEMailAddressess(mtrNr);
				int semester = rs.getInt("semester");
				String notes = rs.getString("notes");
				int ects = rs.getInt("ects");
				ObservableList<Concern> concerns = getConcerns(mtrNr);
				Image img = null;
				try(InputStream is= rs.getBinaryStream("image")){
					img = new Image(is);				
				}catch(Exception e) {
					e.printStackTrace();
				}
				
				result = new Student(mtrNr, name, firstname, eMailAddressess, semester, notes, ects, img, concerns);	
			}		
			catch(Exception e)
			{
				e.printStackTrace();
			}	
			return result;
	}
	
	public Concern getConcern(int concernId) 
	{
		Concern result = new Concern(null, null);
		String sql = "SELECT * FROM concern WHERE id = " + concernId;
		try (Connection conn = this.connect();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql))
		{
			rs.next();
			
			String title = rs.getString("title"); 
			ObservableList<Form> forms = getConcernForms(concernId);
			Topic topic =getTopic(rs.getInt("topic"));
			ObservableList<Appointment> appointments = getAppointments(concernId);
			ObservableList<Reminder> reminders = getReminders(concernId);
			ObservableList<Student> students = getStudents(concernId);
			String notes = rs.getString("notes");	
			
			result = new Concern (concernId, title, forms, topic, appointments, reminders, students, notes);

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}
	
	private ObservableList<Student> getStudents(int concernId) {
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


	private ObservableList<Reminder> getReminders(int concernId) {
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

	private ObservableList<Appointment> getAppointments(int concernId) {
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
	
	public Appointment getAppointment(int id) {
		Appointment result = new Appointment(0, null, 0, 0, null, null, false);
		String sql = "SELECT * FROM appointment WHERE id = " + id;
		try 	(Connection conn = this.connect();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql))
		{
			rs.next();
			
			int concernId = rs.getInt("concern");
			Date date = rs.getDate("date");
			long startTime = rs.getLong("startTime");
			long endTime = rs.getLong("endTime");
			String roomNmb = rs.getString("roomNmb");
			Date reminderTime = rs.getDate("reminderDate");
			Boolean reminderTimeisActive = false;
			if (reminderTime!=null) {
				reminderTimeisActive = true;
			}
			result = new Appointment(concernId, date, startTime, endTime, roomNmb, reminderTime, reminderTimeisActive);
			result.setId(id);
		}catch(Exception e) {
			e.printStackTrace();
		}
			return result;
	}


	private ObservableList<Form> getConcernForms(int concernId) {
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
	
	private Form getForm(int id) {
		//TODO Teest
		Form result = new Form(null, null);
		String sql = "SELECT * FROM form WHERE id = 0 " + id;
		try 	(Connection conn = this.connect();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql))
		{
			rs.next();
		
			String title = rs.getString("title");
			
			File file = File.createTempFile(title, ".tmp");
			try(FileOutputStream out = new FileOutputStream(file);
					InputStream in = rs.getBinaryStream("image")){
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
		}catch(Exception e) {
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
			rs.next();
		
			String message = rs.getString("message");
			Date date = rs.getDate("date");
			result = new Reminder(message, date);
			result.setId(id);
		}catch(Exception e) {
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
			rs.next();
			
			String title = rs.getString("title");
			ObservableList<Form> forms = getTopicForms(topicId);
			
			result = new Topic(title, forms);	
		}catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public Statistic getStatistic(int statisticId) 
	{
		// TODO Auto-generated method stub
		return null;
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
		
		private Subject getSubject(int id) {
			Subject result = new Subject(null, 0);
			String sql = "SELECT * FROM subject WHERE id = " + id;
			try (Connection conn = this.connect();
					Statement stmt = conn.createStatement();
					ResultSet rs = stmt.executeQuery(sql))
				{
					rs.next();
					String title = rs.getString("title");
					int ects = rs.getInt("ects");
					
					result = new Subject(title, ects);
					result.setId(id);
				}catch (Exception e) {
					e.printStackTrace();
				}
			return result;
		}


	public ObservableList<Topic> getTopics() 
	{
		ObservableList<Topic> topic = FXCollections.observableArrayList();
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
				topic.add(new Topic (title, forms));
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return topic;
	}

	public ObservableList<Subject> getSubjects() 
	{
		ObservableList<Subject> subject = FXCollections.observableArrayList();
		String sql = "SELECT * FROM subject";
		try (Connection conn = this.connect();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql))
		{
			while(rs.next())
			{
				int id = rs.getInt("id");
				subject.add(getSubject(id));
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return subject;
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
	
	private ObservableList<Concern> getConcerns(int mtrNr) {
		ObservableList<Concern> concerns = FXCollections.observableArrayList();

		String sql = "SELECT concern FROM concern_student WHERE student = " + mtrNr;
		try (Connection conn = this.connect();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql))
		{
			while(rs.next())
			{
				int id = rs.getInt("concern");
				concerns.add(getConcern(id));
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return concerns;
	}
	
	
	public boolean mailInDb(EMail email) 
	{
		String sql = "SELECT * FROM email WHERE subject = " + email.getSubject() + " AND content = " + email.getContent() + " AND student = " 
				+ email.getStudent().getMtrNr() + " AND date = " + email.getDate();	
		try (Connection conn = this.connect();
					Statement stmt = conn.createStatement();
					ResultSet rs = stmt.executeQuery(sql))
			{
				if (rs.next()) {
					return true;
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		return false;
	}
	
	public void deleteStudent(Student s) 
	{
		Student ds = s;
		int matNr = ds.getMtrNr();
		String sql = "DELETE * FROM student WHERE Matrikelnummer = "+ matNr;
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

	public boolean saveNewSubject(String title, int ects) 
	{
		String sql = "INSERT INTO subject(title, ects) VALUES(" + title +"," + ects +")";
		try (Connection conn = this.connect();
			Statement stmt = conn.createStatement())
		{
			stmt.executeQuery(sql);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}	
		return true;
		
	}

	public boolean saveEditedSubject(String title, int ects, int id) 
	{
		String sql = "UPDATE subject SET title = "+title+", ects = "+ects + "WHERE id = " + id;
		try (Connection conn = this.connect();
			Statement stmt = conn.createStatement())
		{
			stmt.executeQuery(sql);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}


	public void saveNewTopic(String title, ObservableList<Form> selectedForms) 
	{
		String sql1 = "INSERT INTO topic(title) VALUES (" + title + ")";
		String sql2 = "SELECT id FROM topic WHERE title = " + title;
		try (Connection conn = this.connect();
			Statement stmt = conn.createStatement())
		{
			stmt.executeQuery(sql1);
			try (ResultSet rs = stmt.executeQuery(sql2))
			{
				int id = rs.getInt("id");
				addTopicForms(id, selectedForms);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}		
	}

	private void addTopicForms(int id, ObservableList<Form> selectedForms) {
		String sql;
		try (Connection conn = this.connect();
				Statement stmt = conn.createStatement())
			{
			for (Form form: selectedForms){
				sql="INSERT INTO topic_forms (topic, form) VALUES (" + id + ", " + form.getId() + ")";
				stmt.executeQuery(sql);
			}
			}catch (Exception e) {
				e.printStackTrace();
			}
	}


	public void saveEditedTopic(String title, ObservableList<Form> selectedForms, Topic topic) 
	{
		String sql1 = "UPDATE topic SET title = "  + title + "WHERE id = " + topic.getId();
		String sql2 = "DELETE FROM topic_forms WHERE topic = " + topic.getId();
		try (Connection conn = this.connect();
				Statement stmt = conn.createStatement())
			{
				stmt.executeQuery(sql1);
				stmt.executeQuery(sql2);
				for (Form form: selectedForms) {
					int id = form.getId();
					addTopicForms(id, selectedForms);
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
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
	
		private void addPoSubject(int poId, ObservableList<Subject> subjects, boolean optional) {
			String op = null;
			if (optional) {
				op = "x";
			}
			String sql;
			try (Connection conn = this.connect();
				Statement stmt = conn.createStatement())
			{
				for (Subject subject: subjects) {
					sql = "INSERT INTO po_subject (subject, po, optional) VALUES ("+ subject.getId() + ", " + poId + ", " + op + ")";
					stmt.executeQuery(sql);
				}
			}catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	

	public void deleteConcern(Concern c) 
	{
		String sql = "DELETE * FROM concern WHERE id = "+ c.getId();
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

	public void saveNewForm(Form form) 
	{
		String sql = "INSERT INTO form (title, file) VALUES (?, ?)";
		try (Connection conn = this.connect();
			PreparedStatement pstmt = conn.prepareStatement(sql))
		{	
			File file = form.getFile();
			pstmt.setString(1, form.getName());
			try(InputStream is = new FileInputStream(file)){
				pstmt.setBinaryStream(2, is);
			}catch(Exception e) {
				e.printStackTrace();
			}
			pstmt.executeUpdate();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void deleteForm(Form f) 
	{
		String sql = "DELETE * FROM form WHERE name = "+ f.getId();
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
		String sql = "INSERT INTO eMail(subject, content, student, recived) values (?, ?, ?, ?)";
		try (Connection conn = this.connect();
			PreparedStatement pstmt = conn.prepareStatement(sql))
		{
			pstmt.setString(1, email.getSubject());
			pstmt.setString(2, email.getContent());
			pstmt.setInt(3, email.getStudent().getMtrNr());
			pstmt.setBoolean(4, email.isReceived());
			pstmt.executeUpdate();
		} 
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}


	public void saveEditedStudent(Student student) {
		//TODO Test..
		Image img = student.getImage();
		try(ByteArrayOutputStream os = new ByteArrayOutputStream();
		   InputStream is = new ByteArrayInputStream(os.toByteArray())){
			ImageIO.write(SwingFXUtils.fromFXImage(img, null),"png", os); 
		
			String sql1 = "UPDATE student SET name = "+student.getName()+", firstname = "+student.getFirstName()+", semester = "+student.getSemester()+
				", notes = "+student.getNotes()+", ects = "+student.getEcts()+", img = " +is + 
				"WHERE matrNr = " + student.getMtrNr();
			String sql2 = "DELETE * FROM student_emailAddress WHERE student = " + student.getMtrNr();
			String sql3 = "DELETE * FROM concern_student WHERE student = " + student.getMtrNr();
			try(Connection conn = this.connect();
					Statement stmt = conn.createStatement())
			{
				stmt.executeQuery(sql1);
				stmt.executeQuery(sql2);
				stmt.executeQuery(sql3);
				addStudentEmail(student.getMtrNr(), student.geteMailAddresses());
				addConcernsStudent(student.getConcerns(), student.getMtrNr());			
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}


	private void addConcernsStudent(ObservableList<Concern> concerns, int mtrNr) {
		String sql;
		try (Connection conn = this.connect();
				Statement stmt = conn.createStatement())
			{
			for (Concern concern: concerns){
				sql="INSERT INTO concern_student (concern, student) VALUES (" + concern.getId() + ", " + mtrNr + ")";
				stmt.executeQuery(sql);
			}
			}catch (Exception e) {
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


	public ArrayList<EMail> getEMails(Student student) {
		ArrayList<EMail> mail = new ArrayList<EMail>();
		String sql = "SELECT * FROM eMail WHERE student = "+ student.getMtrNr() +" ORDER BY date ASC";
		try (Connection conn = this.connect();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql))
		{
			String subject = rs.getString("subject");
			String content = rs.getString("content");
			Boolean recieved = rs.getBoolean("received");
			Date date = rs.getDate("date");
			mail.add(new EMail(content, subject, student, date, recieved));
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return mail;
	}
	
	public void saveEditedConcern(Concern concern) {
		String sql1 = "UPDATE concern SET title = " + concern.getTitle() + ", topic = " + concern.getTopic().getId() +
				", notes = " + concern.getNotes();
		String sql2 = "DELETE * FROM concern_forms WHERE concern = " + concern.getId();
		String sql3 = "DELETE * FROM concern_student WHERE concern = " + concern.getId();
		String sql4 = "DELETE * FROM appointment WHERE concern = " + concern.getId();
		String sql5 = "DELETE * FROM reminder WHERE concern = " + concern.getId();
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


	private void addConcernForms(int id, ObservableList<Form> data) {
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


	private void addConcernStudents(int id, ObservableList<Student> students) {
		String sql;
		try (Connection conn = this.connect();
				Statement stmt = conn.createStatement())
			{
			for (Student student: students){
				sql="INSERT INTO concern_student (concern, student) VALUES (" + id + ", " + student.getMtrNr()+ ")";
				stmt.executeQuery(sql);
			}
			}catch (Exception e) {
				e.printStackTrace();
			}
	}


	private void addAppointments(ObservableList<Appointment> appointments) {
		for (Appointment a: appointments) {
			addAppointment(a);
		}
	}


	private void addAppointment(Appointment appointment) {
		String sql = "INSERT INTO appointment(concern, date, startTime, endTime, roomNmb, reminderTime) values (?, ?, ?, ?, ?, ?)";
		try (Connection conn = this.connect();
			PreparedStatement pstmt = conn.prepareStatement(sql))
		{
			pstmt.setInt(1, appointment.getConcernId());
			pstmt.setDate(2, appointment.getDate());
			pstmt.setLong(3, appointment.getStartTime());
			pstmt.setLong(4, appointment.getEndTime());
			pstmt.setString(5, appointment.getRoomNmb());
			pstmt.setDate(6, appointment.getReminderTime());
			pstmt.executeUpdate();
		} 
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}


	private void addReminders(int conId, ObservableList<Reminder> reminders) {
		for (Reminder r: reminders) {
			addReminder(conId, r);
		}
	}


	private void addReminder(int conId, Reminder reminder) {
		String sql = "INSERT INTO reminder(date, message, concern) values (?, ?, ?)";
		try (Connection conn = this.connect();
			PreparedStatement pstmt = conn.prepareStatement(sql))
		{
			pstmt.setDate(1, reminder.getDate());
			pstmt.setString(2, reminder.getMessage());
			pstmt.setInt(3, conId);
			pstmt.executeUpdate();
		} 
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}


	public int saveNewConcern(Concern concern) {
		
		//TODO return die zugewisene id des neuen concerns
		
		String sql = "INSERT INTO concern (title, topic, notes) values (?, ?, ?)";
		try (Connection conn = this.connect();
			PreparedStatement pstmt = conn.prepareStatement(sql))
		{
			pstmt.setString(1, concern.getTitle());
			pstmt.setInt(2, concern.getTopic().getId());
			pstmt.setString(3, concern.getNotes());
			pstmt.executeUpdate();
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


	public void deleteStatistic(Statistic statisticToDelete) {
		// TODO Auto-generated method stub
		
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


	public ObservableList<Reminder> getNewReminders(java.util.Date lastReminderCheck) {
		ObservableList<Reminder> result = FXCollections.observableArrayList();
		String sql ="SELECT id FROM reminder WHERE date <= date() AND date >= " + new Date(lastReminderCheck.getTime());
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


	public ObservableList<Concern> getConcerns() {
		// TODO Auto-generated method stub
		return null;
		
		//return leere liste wenn keine vorhanden
	}


	public ObservableList<Student> getStudents() {
		// TODO Auto-generated method stub
		return null;
	}


	
}
