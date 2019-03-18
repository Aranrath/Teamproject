package tp.model;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
			System.out.println(e.getMessage());
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
	
	//TODO Jahr mit übergeben?
	public ObservableList<Appointment> getWeeksAppointments(int shownKw) {
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(Calendar.DAY_OF_YEAR, shownKw);
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
			System.out.println(e.getMessage());
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
		//TODO Path
//		String PATH = "C:\\Users\\Mephisto\\eclipse-workspace\\Teamproject\\src\\ExportedData"; 
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
				
				//TODO Image
//				Blob ph = rs.getBlob("img");
//				System.out.println(ph);
//				InputStream in = ph.getBinaryStream();
//				ByteArrayOutputStream out = new ByteArrayOutputStream();
//				OutputStream outputStream = new FileOutputStream(PATH+mtrNr+".png");
//				int length = (int) ph.length();
//				int bufferSize = 1024;
//				byte[] buffer = new byte[bufferSize];
//				while((length = in.read(buffer)) != -1)
//				{
//					System.out.println("writing " + length + " bytes");
//					out.write(buffer, 0, length);
//				}
//				out.writeTo(outputStream);
//				in.close();
//				File file = new File(PATH+mtrNr+".png");
				Image img = null;				
				
				result = new Student(mtrNr, name, firstname, eMailAddressess, semester, notes, ects, img, concerns);	
			}		
			catch(Exception e)
			{
				System.out.println(e.getMessage());
			}	
			return result;
	}
	
	public Concern getConcern(int concernId) 
	{
		Concern result = new Concern(null, null, null, null, null, null, null);
		String sql = "SELECT * FROM concern WHERE id = " + concernId;
		try (Connection conn = this.connect();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql))
		{
			rs.next();
			
			String title = rs.getString("title"); 
			ObservableList<Form> forms = getForms(concernId);
			Topic topic =getTopic(rs.getInt("topic"));
			ObservableList<Appointment> appointments = getAppointments(concernId);
			ObservableList<Reminder> reminders = getReminders(concernId);
			ObservableList<Student> students = getStudents(concernId);
			String notes = rs.getString("notes");	
			
			result = new Concern (title, forms, topic, appointments, reminders, students, notes);
			result.setId(concernId);
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
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


	//TODO Klärungsbedarf Forms, Data, Dokuments.....
	private ObservableList<Form> getForms(int concernId) {
		ObservableList<Form>  result = FXCollections.observableArrayList();
		String sql = "SELECT * FROM forms WHERE concern = " + concernId;
		try (Connection conn = this.connect();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql))
		{
			while(rs.next()) {
				int id = rs.getInt("id");
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public Reminder getReminder(int id) {
		Reminder result = new Reminder(null, null);
		String sql = "SELECT * FROM reminder WHERE id = 0 " + id;
		try 	(Connection conn = this.connect();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql))
		{
			rs.next();
		
			String message = rs.getString("message");
			Date date = rs.getDate("date");
			Reminder reminder = new Reminder(message, date);
			reminder.setId(id);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public Topic getTopic(int topicId) {
		String sql = "SELECT * FROM topic WHERE id = " + topicId;
		try(Connection conn = this.connect();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)){
			rs.next();
			
			//TODO Klärungsbedarf getForms(topicId)
			String title = rs.getString("title");
			ArrayList<Object> forms;
			
			return new Topic(title, null);
			
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		return new Topic(null, null);
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
				result.add(po);
			}
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
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
				String title = rs.getString("titel");
				//TODO get linkedForms
				ArrayList<Object> linkedForms = new ArrayList<Object>();
				topic.add(new Topic (title, linkedForms));
			}
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
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
			System.out.println(e.getMessage());
		}
		return subject;
	}
	
	//TODO Klärungsbedarf... unso...
	public ObservableList<Form> getForms() 
	{
		ObservableList<Form> form = FXCollections.observableArrayList();
//		String PATH = "C:\\Users\\Mephisto\\eclipse-workspace\\Teamproject\\src\\ExportedData"; 
		String sql = "SELECT * FROM form";
		try (Connection conn = this.connect();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql))
		{
			while(rs.next())
			{
				String name = rs.getString("name");
				//TODO blobs
//				Blob ph = rs.getBlob("file");
//				InputStream in = ph.getBinaryStream();
//				ByteArrayOutputStream out = new ByteArrayOutputStream();
//				//TODO dafuq file output Stream? Überarbeiten! 
//				OutputStream outputStream = new FileOutputStream(PATH+name+".pdf");
//				int length = (int) ph.length();
//				int bufferSize = 1024;
//				byte[] buffer = new byte[bufferSize];
//				while ((length = in.read(buffer)) != -1)
//				{
//					System.out.println("writing " + length + " bytes");
//					out.write(buffer, 0, length);
//				}
//				out.writeTo(outputStream);
//				File file = new File(PATH+name+".pdf");
//				form.add(new Form(name, file));
			}
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		return form;
	}
	
	public ObservableList<Concern> getConcerns(Student student) 
	{
		return getConcerns(student.getMtrNr());
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
			System.out.println(e.getMessage());
		}
		return concerns;
	}
	
	public boolean mailInDb(EMail email) 
	{
		//TODO man hat alles außer die ID.... Neu Machen!
//		EMail em = email;
//		int ID = em.getId();
//		String sql = "SELECT FROM eMail WHERE id = " + ID;
//		try (Connection conn = this.connect();
//					Statement stmt = conn.createStatement();
//					ResultSet rs = stmt.executeQuery(sql))
//			{
//				if (rs.next()) {
//					return true;
//				}
//			}
//			catch(Exception e)
//			{
//				e.printStackTrace();
//			}
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
			System.out.println(e.getMessage());
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
		catch (Exception ex)
		{
			System.out.println(ex.getMessage());
			return false;
		}	
		return true;
		
	}

	public boolean saveEditedSubject(String title, int ects, int id) 
	{
		String sql = "UPDATE subject SET titel = "+title+", ects = "+ects + "WHERE id = " + id;
		try (Connection conn = this.connect();
			Statement stmt = conn.createStatement())
		{
			stmt.executeQuery(sql);
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
			return false;
		}
		return true;
	}


	//TODO Klärung FORMS...
	public void saveNewTopic(String title, ObservableList<Form> selectedForms) 
	{
		//TODO NOOOPE, linked Forms in anderer Tabelle gespeichert....
//		String linkedForms = selectedForms.toString();
//		String sql = "INSERT INTO topic(titel, linkedForms) VALUES (?,?)";
//		try (Connection conn = this.connect();
//			PreparedStatement pstmt = conn.prepareStatement(sql))
//		{
//			pstmt.setString(1, title);
//			pstmt.setString(2, linkedForms);
//			pstmt.executeUpdate();
//			
//		}
//		catch (Exception e)
//		{
//			System.out.println(e.getMessage());
//		}
//		System.out.println("Topic " + title + " saved.");
		
	}

	//TODO KLÄRUNG FORMS
	public void saveEditedTopic(String text, ObservableList<Form> selectedForms, Topic topic) 
	{
		//TODO
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
			for (Subject optional: selectedOptionalSubjects ) {
				int id = optional.getId();
				addSubjectToPo(id, po.getId(), true);
			}
			for (Subject mandatory: selectedMandatorySubjects ) {
				int id = mandatory.getId();
				addSubjectToPo(id, po.getId(), false);
			}
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
	}
	
		private void addSubjectToPo(int subjectId, int poId, boolean optional) {
			String sql = "INSERT INTO po_subject (subject, po, optional) VALUES (?, ?, ?)";
			try (Connection conn = this.connect();
				PreparedStatement pstmt = conn.prepareStatement(sql))
			{
				pstmt.setInt(1, subjectId);
				pstmt.setInt(2, poId);
				if(optional) {
					pstmt.setString(3, "x");
				}else {
					pstmt.setString(3, null);
				}
				pstmt.executeUpdate();
			}catch (Exception e)
			{
				System.out.println(e.getMessage());
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
			System.out.println(e.getMessage());
		}
	}

	//TODO KLÄRUNGSBEDARF
	public void saveNewForm(Form form) 
	{
		//TODO ... öhm mal sehen was fürn mist hier rauskommt...
//		Form sf = form;
//		String name = sf.getName();
//		FileInputStream fis = null;
//		String sql = "INSERT INTO form (name, file) VALUES (?, ?)";
//		try (Connection conn = this.connect();
//			PreparedStatement pstmt = conn.prepareStatement(sql))
//		{	
//			File file = sf.getFile();
//			fis = new FileInputStream(file);
//			pstmt.setString(1, name);
//			pstmt.setBinaryStream(2,  fis, (int) file.length());
//			pstmt.executeUpdate();
////		}
//		catch (Exception e)
//		{
//			System.out.println(e.getMessage());
//		}
	}

	public void deleteForm(Form f) 
	{
		//TODO evt file löschen falls iwo gespeichert worden is?? nach überarbeitung saveNewForm
		String sql = "DELETE * FROM form WHERE name = "+f.getName(); //TODO 
		try (Connection conn = this.connect();
			Statement stmt = conn.createStatement())
		{
			stmt.executeQuery(sql);
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
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
			System.out.println(e.getMessage());
		}
	}


	public void saveEditedStudent(Student student) {
		//TODO student_emailAddress und concern_student werden auch geändert
		String name = student.getName();
		String firstname = student.getFirstName();
		int semester = student.getSemester();
		String notes = student.getNotes();
		int ects = student.getEcts();
		Image img = student.getImage();
		//TODO ...???Blob = null unerwünscht
		Blob blob = null;
//		BufferedImage bi = SwingFXUtils.fromFXImage(img, null);
//		ByteArrayOutputStream out = new ByteArrayOutputStream();
//		try {
//			ImageIO.write(bi, "png", out);
//			byte[] res = out.toByteArray();
//			blob = new SerialBlob(res);
//		} catch (IOException | SQLException e) {
//			e.printStackTrace();
//		}
		String sql = "UPDATE Student SET name = "+name+", firstname = "+firstname+", semester = "+semester+
				", notes = "+notes+", ects = "+ects+", img = "+blob + 
				"WHERE matrNr = " + student.getMtrNr();
		try(Connection conn = this.connect();
			Statement stmt = conn.createStatement())
		{
			stmt.executeQuery(sql);
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
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
			System.out.println(e.getMessage());
		}
		return mail;
	}
	
	public void saveEditedConcern(Concern concern) {
		//TODO Lots to Update with one Concern...
		Concern con = concern;
		String title = con.getTitle();
		ObservableList<Form> data = FXCollections.observableArrayList(con.getData());
		Topic topic = con.getTopic();
		ObservableList<Appointment> appointments = FXCollections.observableArrayList(con.getAppointments());
		ObservableList<Reminder> reminders = FXCollections.observableArrayList(con.getReminders());
		ObservableList<Student> students = FXCollections.observableArrayList(con.getStudents());
		String notes = con.getNotes();
		//TODO observable lists in anderen Tabellen gespeichert.....
//		String sql = "UPDATE concern SET title = "+title+", data = "+data+", topic = "+topic+", appointments ="+appointments+", reminders = "+reminders+", students = "+students+", notes = "+notes;
//		try (Connection conn = this.connect();
//			Statement stmt = conn.createStatement())
//		{
//			stmt.executeUpdate(sql);
//		}
//		catch(Exception e)
//		{
//			System.out.println(e.getMessage());
//		}
	}


	public void saveNewConcern(Concern concern) {
		//TODO die observable Lists schon wieder vergessen.... Lots to insert with new Concern
//		String sql = "INSERT INTO Concern (title, topic, notes) VALUES (?,?,?)";
//		String title = con.getTitle();
//		Topic topic = con.getTopic();
//		String notes = con.getNotes();
//		try(Connection conn = this.connect();
//			PreparedStatement pstmt = conn.prepareStatement(sql))
//		{
//			pstmt.setString(2, title);
//			pstmt.setInt(3, topic.getId());
//			pstmt.setString(4, notes);
//			pstmt.executeUpdate();
//		}
//		catch(Exception e)
//		{
//			System.out.println(e.getMessage());
//		}
//		
		
	}


	public void deleteStatistic(Statistic statisticToDelete) {
		// TODO Auto-generated method stub
		
	}


	public boolean saveNewStatistic(Statistic statistic) {
		boolean successful;
		try
		{
			//TODO Gib Statistic eine id!!
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
		// TODO Auto-generated method stub
		
		//Return null wenn keine neuen Reminder!
		return null;
	}


	
}
