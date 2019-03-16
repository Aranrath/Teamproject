package tp.model;


import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
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

import javax.imageio.ImageIO;
import javax.sql.rowset.serial.SerialBlob;

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
			System.out.println(e.getMessage());
		}
		return conn;
	}
	
	
	//-------------------Calculations--------------------------------------------------------------

	public ArrayList<Appointment> loadNext24hourAppointments() {
		//TODO abfrage gibt nur 'Heutige' Appointments nich next 24h
		String sql = "SELECT * FROM appointment WHERE DATE(startTime) == DATE('now')";		
		ArrayList<Appointment> result = new ArrayList<Appointment>();
		try 	(Connection conn = this.connect();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql))
		{
			while(rs.next())
			{
				int id = rs.getInt("id");
				Concern concern = getConcern(rs.getInt("concern"));
				Date date = rs.getDate("date");
				long startTime = rs.getLong("startTime");
				long endTime = rs.getLong("endTime");
				int roomNmb = rs.getInt("roomNmb");
				Date reminderTime = rs.getDate("reminderDate");
				Boolean reminderTimeisActive = rs.getBoolean("reminderDateActive");
				result.add(new Appointment(id, concern, date, startTime, endTime, roomNmb, reminderTime, reminderTimeisActive));
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
	
	public int getKwOfDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.WEEK_OF_YEAR);
	}
	
	public ObservableList<Appointment> getWeeksAppointments(int shownKw) {
		ObservableList<Appointment> appointments = FXCollections.observableArrayList();
		//TODO sql-abfrage überprüfen, neumachen... macht net viel sinn...
		String sql = "SELECT * FROM appointment WHERE DATE(date) >= DATE('now', 'weekday 0', '-7 days')";
		try (Connection conn = this.connect();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql))
		{
			while(rs.next())
			{
				int id =rs.getInt("id");
				Concern concern = getConcern(rs.getInt("concern"));
				Date date = rs.getDate("date");
				long startTime = rs.getLong("startDate");
				long endTime = rs.getLong("endDate");
				int roomNmb = rs.getInt("roomNmb");
				Date reminderTime = rs.getDate("reminderDate");
				Boolean reminderTimeisActive = rs.getBoolean("reminderDateActive");
				Appointment app = new Appointment(id, concern, date, startTime, endTime, roomNmb, reminderTime, reminderTimeisActive);
				appointments.add(app);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return appointments;
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

	
	public Student getStudent(String emailAdressse) 
	{
		//TODO path anpassen
		String PATH = "C:\\Users\\Mephisto\\eclipse-workspace\\Teamproject\\src\\ExportedData";
		String sql = "SELECT * FROM student WHERE eMailAddresses = " + emailAdressse;

		
		try (Connection conn = this.connect();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql))
		{
			rs.next();
			
			//TODO EmailAdressess and Concerns
				int mtrNr = rs.getInt("Matrikelnummer");
				String name = rs.getString("name");
				String firstname = rs.getString("firstname");
				//String[] eMailAddressess = new String[3];
				int semester = rs.getInt("semester");
				String notes = rs.getString("notes");
				int ects = rs.getInt("ects");
				//Concern con = getConcern(rs.getInt("concern"));
				//concerns = FXCollections.observableArrayList(con);
				Blob ph = rs.getBlob("img");
				System.out.println(ph);
				InputStream in = ph.getBinaryStream();
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				OutputStream outputStream = new FileOutputStream(PATH+mtrNr+".png");
				int length = (int) ph.length();
				int bufferSize = 1024;
				byte[] buffer = new byte[bufferSize];
				while((length = in.read(buffer)) != -1)
				{
					System.out.println("writing " + length + " bytes");
					out.write(buffer, 0, length);
				}
				out.writeTo(outputStream);
				in.close();
				File file = new File(PATH+mtrNr+".png");
				Image img = new Image(file.toURI().toString());				
			
			return new Student(mtrNr, name, firstname, null, semester, notes, ects, img, null);	
		}		
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}	
		return new Student(0, null, null, null, 0, null, 0, null, null);
	}
	
	public Student getStudent(int mtrNr) 
	{
		//TODO Path
		String PATH = "C:\\Users\\Mephisto\\eclipse-workspace\\Teamproject\\src\\ExportedData"; 
		String sql = "SELECT * FROM student WHERE Matrikelnummer = " + mtrNr;
		
		try (Connection conn = this.connect();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql))
		{

			rs.next();
			
			//TODO EmailAdressess and Concerns
				String name = rs.getString("name");
				String firstname = rs.getString("firstname");
				//String[] eMailAddressess = new String[3];
				int semester = rs.getInt("semester");
				String notes = rs.getString("notes");
				int ects = rs.getInt("ects");
				//Concern con = getConcern(rs.getInt("concern"));
				//concerns = FXCollections.observableArrayList(con);
				Blob ph = rs.getBlob("img");
				System.out.println(ph);
				InputStream in = ph.getBinaryStream();
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				OutputStream outputStream = new FileOutputStream(PATH+mtrNr+".png");
				int length = (int) ph.length();
				int bufferSize = 1024;
				byte[] buffer = new byte[bufferSize];
				while((length = in.read(buffer)) != -1)
				{
					System.out.println("writing " + length + " bytes");
					out.write(buffer, 0, length);
				}
				out.writeTo(outputStream);
				in.close();
				File file = new File(PATH+mtrNr+".png");
				Image img = new Image(file.toURI().toString());				
			
			return new Student(mtrNr, name, firstname, null, semester, notes, ects, img, null);
		}		
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}	
		return new Student(0, null, null, null, 0, null, 0, null, null);
	}
	
	public Concern getConcern(int concernId) 
	{
		String sql = "SELECT * FROM concern WHERE id = " + concernId;
		try (Connection conn = this.connect();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql))
		{
			rs.next();
			
			String title = rs.getString("title"); 
			Topic topic =getTopic(rs.getInt("topic"));
			String notes = rs.getString("notes");	
			
			Concern con = new Concern (title, null, topic, null, null, null, notes);
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		
		return new Concern(null, null, null, null, null, null, null);
	}
	
	public Topic getTopic(int topicId) {
		String sql = "SELECT * FROM topic WHERE id = " + topicId;
		try(Connection conn = this.connect();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)){
			rs.next();
			
			//TODO getForms(concernId)
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
		ObservableList<PO> po = FXCollections.observableArrayList();
		String sql = "SELECT * FROM po";
		try (Connection conn = this.connect();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql))
		{
			while(rs.next())
			{
				String name = rs.getString("name");
				po.add(new PO (name));
			}
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		return po;
		
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
				String title = rs.getString("titel");
				int ects = rs.getInt("ects");
				subject.add(new Subject (id, title, ects));
			}
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		return subject;
	}
	
	public ObservableList<Form> getForms() 
	{
		ObservableList<Form> form = FXCollections.observableArrayList();
		//TODO path
		String PATH = "C:\\Users\\Mephisto\\eclipse-workspace\\Teamproject\\src\\ExportedData"; 
		String sql = "SELECT * FROM form";
		try (Connection conn = this.connect();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql))
		{
			while(rs.next())
			{
				String name = rs.getString("name");
				//TODO blob richtig?
				Blob ph = rs.getBlob("file");
				InputStream in = ph.getBinaryStream();
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				//TODO dafuq file output Stream? Überarbeiten! 
				OutputStream outputStream = new FileOutputStream(PATH+name+".pdf");
				int length = (int) ph.length();
				int bufferSize = 1024;
				byte[] buffer = new byte[bufferSize];
				while ((length = in.read(buffer)) != -1)
				{
					System.out.println("writing " + length + " bytes");
					out.write(buffer, 0, length);
				}
				out.writeTo(outputStream);
				File file = new File(PATH+name+".pdf");
				form.add(new Form(name, file));
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
		ObservableList<Concern> concerns = FXCollections.observableArrayList();

		String sql = "SELECT c.title, cd.data, c.topic, ca.appointment, cr.reminders, cs.student, c.notes "
				+ "FROM concern c, concer_student cs, concern_appointment ca, concern_dokument cd, concern_reminderMail cr "
				+ "WHERE c.id = cs.concern AND cs.student = " + student.getMtrNr()+" AND c.id = ca.concern AND c.id = cd.concern AND c.id = cr.concern";		
		try (Connection conn = this.connect();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql))
		{
			while(rs.next())
			{
				String title = rs.getString("c.title");
				//form.setName(rs.getString("dokument"));
				//TODO data füllen
				ObservableList<Form> data = FXCollections.observableArrayList();
				Topic topic = getTopic(rs.getInt("topic"));
				//TODO fill Lists
				ObservableList<Appointment> appointments = FXCollections.observableArrayList();
				ObservableList<Reminder> reminders = FXCollections.observableArrayList();
				ObservableList<Student> students = FXCollections.observableArrayList();
				String notes = rs.getString("notes");
				concerns.add(new Concern(title, data, topic, appointments, reminders, students, notes));
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
		EMail em = email;
		int ID = em.getId();
		String sql = "SELECT FROM eMail WHERE id = " + ID;
		try (Connection conn = this.connect();
			Statement stmt = conn.createStatement())
			{
				stmt.executeQuery(sql);
			}
			catch(Exception e)
			{
				System.out.println(e.getMessage());
				return false;
			}
		return true;
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
		String sql = "INSERT INTO subject(titel, ects) VALUES(" + title +"," + ects +")";
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
		String sql = "UPDATE subject SET titel = "+title+", ects = "+ects;
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

	public void saveEditedTopic(String text, ObservableList<Form> selectedForms, Topic topic) 
	{
		//TODO
	}

	public void saveEditedPO(String newPOName, ObservableList<Subject> selectedMandatorySubjects,
			ObservableList<Subject> selectedOptionalSubjects, PO po) 
	{
		//TODO
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
		String sql = "DELETE * FROM form WHERE name = "+f.getName();
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
			//TODO  verbindung Student
			pstmt.setBoolean(4, email.isReceived());
			pstmt.executeUpdate();
		} 
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
	}


	public void saveEditedStudent(Student student) {
		//TODO evt andere sachen die auch geuodatet werden können dadurch...
		String name = student.getName();
		String firstname = student.getFirstName();
		String eMailAddresses = student.geteMailAddresses().toString();
		int semester = student.getSemester();
		String notes = student.getNotes();
		int ects = student.getEcts();
		Image img = student.getImage();
		//TODO ...???Blob = null unerwünscht
		Blob blob = null;
		BufferedImage bi = SwingFXUtils.fromFXImage(img, null);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			ImageIO.write(bi, "png", out);
			byte[] res = out.toByteArray();
			blob = new SerialBlob(res);
		} catch (IOException | SQLException e) {
			e.printStackTrace();
		}
		String sql = "UPDATE Student SET name = "+name+", firstname = "+firstname+", eMailAddresses = "+eMailAddresses+", semester = "+semester+", notes = "+notes+", ects = "+ects+", img = "+blob;
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
		//TODO die observable Lists schon wieder vergessen....
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


	public void saveEditedOptions(Options changedOptions) {
		// TODO Auto-generated method stub
		
	}
}
