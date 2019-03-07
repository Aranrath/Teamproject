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
	
	public Model() {
		loadSessionTabsIds();
	}
	
	
	//------------------establish Database connection-----------------------------------------------
	private Connection connect() 
	{
		String url = "jdbc:sqlite:teamprojectDatabase.db";
		Connection conn = null; 
		try 
		{
			conn = DriverManager.getConnection(url);
		}
		catch (SQLException e) 
		{
			System.out.println(e.getMessage());
		}
		return conn;
	}
	
	
	//-------------------Calculations--------------------------------------------------------------

	public ArrayList<Appointment> loadNext24hourAppointments() {
		String sql = "SELECT * FROM appointment WHERE DATE(startDate) == DATE('now')";		
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
				long startTime = rs.getLong("startDate");
				long endTime = rs.getLong("endDate");
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
		cal.setTime(date);
		d[0] = (Date) cal.getTime();
		cal.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
		cal.setTime(date);
		d[1] = (Date) cal.getTime();
		cal.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
		cal.setTime(date);
		d[2] = (Date) cal.getTime();
		cal.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
		cal.setTime(date);
		d[3] = (Date) cal.getTime();
		cal.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
		cal.setTime(date);
		d[4] = (Date) cal.getTime();
		return d;
	}
	
	public int getKwOfDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.WEEK_OF_YEAR);
	}
	
	public ObservableList<Appointment> getWeeksAppointments(int shownKw) {
		String sql = "SELECT * FROM appointment WHERE DATE(startDate) >= DATE('now', 'weekday 0', '-7 days') OR DATE(endDate) >= DATE('now', 'weeday 0', '-7 days'))";
		try (Connection conn = this.connect();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql))
		{
			while(rs.next())
			{
				int id = rs.getInt("id");
				System.out.println("Appointments of this week: " + id);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public Date getStartOfNextWeek(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.WEEK_OF_YEAR, +1);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		cal.setTime(date);
		return (Date) cal.getTime();
		
	}

	public java.sql.Date getEndOfPreviousWeek(Date date) {
		int day = 6;
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.WEEK_OF_YEAR, -1);
		cal.set(Calendar.DAY_OF_WEEK, day);
		return (Date) cal.getTime();		
	}
	
	//------------------File: Loader&Saver + Getter/Setter---------------------------------------------------------------
	
	
	public ArrayList<String> getSessionTabs() {
		if (sessionTabsIds == null)
		{
			return loadSessionTabsIds();
		}
		return sessionTabsIds;
	}
	
	public void saveSessionTabs() 
	{
		FileOutputStream fout;
		ObjectOutputStream oos;
		try
		{
			fout = new FileOutputStream("C:\\\\Users\\\\Mephisto\\\\eclipse-workspace\\\\Teamproject\\\\src\\\\sessionTabsIds.ser");
			oos = new ObjectOutputStream(fout);
			oos.writeObject(sessionTabsIds);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		// TODO Java object out Stream

	}

	public void saveOptions() 
	{
		FileOutputStream fout;
		ObjectOutputStream oos;
		try 
		{
			//TODO path anpassen, fÃ¼r spÃ¤tere Installation
			fout = new FileOutputStream("C:\\Users\\Mephisto\\eclipse-workspace\\Teamproject\\src\\Options.ser");
			oos = new ObjectOutputStream(fout);
			oos.writeObject(options);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		// TODO Java object out Stream
	}

	public ArrayList<String> loadSessionTabsIds() 
	{
		FileInputStream fis;
		ObjectInputStream ois;
		try
		{
			//Filepath anpassen
			fis = new FileInputStream(new File("C:\\\\Users\\\\Mephisto\\\\eclipse-workspace\\\\Teamproject\\\\src\\\\sessionTabsIds.ser"));
			ois = new ObjectInputStream(fis);
			ois.readObject();
		}
		catch (IOException | ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		// TODO Java object in stream, wenn da, sonst neue Datei
		// this.tabSession = ...
		return sessionTabsIds;
	}

	public Options loadOptions() {
		
		FileInputStream fis;
		ObjectInputStream ois;
		try
		{
			//Filepath anpassen
			fis = new FileInputStream(new File("C:\\Users\\Mephisto\\eclipse-workspace\\Teamproject\\src\\Options.ser"));
			ois = new ObjectInputStream(fis);
			ois.readObject();
		}
		catch (IOException | ClassNotFoundException e)
		{
			e.printStackTrace();
		}	
		// TODO Java object in stream, wenn da, sonst neue Datei
		// this.options = ...
		return options;

	}
	
	public Options getOptions()
	{
		if(options!=null)
		{
			return options;
		}
		else
		{
			options = loadOptions();
			return options;
		}
	}
	
	public void setSessionTabs(ArrayList<String> sessionTabs) {
		this.sessionTabsIds = sessionTabs;
	}
	

	// ------------Datenbank Abfragen--------------------------------------------------------------------

	
	public Student getStudent(String emailAdressse) 
	{
		Student result = null;
		String PATH = "C:\\Users\\Mephisto\\eclipse-workspace\\Teamproject\\src\\ExportedData";
		String sql = "SELECT * FROM student WHERE eMailAddresses = " + emailAdressse;
		ArrayList<Concern> con = new ArrayList<Concern>();
		Image img = null;
		int mtrNr = 0; 
		String name = null; 
		String firstname = null; 
		ObservableList<String> eMailAddressess = null;
		Concern[] concerns = null;
		int semester = 0; 
		String notes = null; 
		int ects = 0;
		Concern concern;
		
		try (Connection conn = this.connect();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql))
		{
			while(rs.next())
			{
				mtrNr = rs.getInt("Matrikelnummer");
				name = rs.getString("name");
				firstname = rs.getString("firstname");
				eMailAddressess = FXCollections.observableArrayList(rs.getString("eMailAddressess"));
				semester = rs.getInt("semester");
				notes = rs.getString("notes");
				ects = rs.getInt("ects");
				concern = getConcern(rs.getInt("concern"));
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
				img = new Image(file.toURI().toString());
				con.add(concern);
				concerns = new Concern[con.size()];
				concerns = con.toArray(concerns);	
				
			}
			result = new Student(mtrNr, name, firstname, eMailAddressess, semester, notes, ects, img, concerns);	
		}		
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}	
		return result;
	}
	
	public Student getStudent(int mtrNr) 
	{
		String PATH = "C:\\Users\\Mephisto\\eclipse-workspace\\Teamproject\\src\\ExportedData"; 
		String sql = "SELECT * FROM student WHERE Matrikelnummer = " + mtrNr;
		Student result = null;	
		ArrayList<Concern> con = new ArrayList<Concern>();
		Image img = null; 
		String name = null; 
		String firstname = null; 
		ObservableList<String> eMailAddressess = null;
		Concern[] concerns = null;
		int semester = 0; 
		String notes = null; 
		int ects = 0;
		Concern concern;
		
		try (Connection conn = this.connect();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql))
		{
			while(rs.next())
			{
				mtrNr = rs.getInt("Matrikelnummer");
				name = rs.getString("name");
				firstname = rs.getString("firstname");
				eMailAddressess = FXCollections.observableArrayList(rs.getString("eMailAddressess"));
				semester = rs.getInt("semester");
				notes = rs.getString("notes");
				ects = rs.getInt("ects");
				concern = getConcern(rs.getInt("concern"));
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
				img = new Image(file.toURI().toString());
				con.add(concern);
				concerns = new Concern[con.size()];
				concerns = con.toArray(concerns);	
				
			}
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
		Concern con = null;
		Topic topic = null;
		String title = null;
		String notes = null;
		String sql = "SELECT * FROM concern WHERE id = "+concernId;
		try (Connection conn = this.connect();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql))
		{
			while(rs.next())
			{
				title = rs.getString("title"); 
				topic.setId(rs.getInt("topic"));
				notes = rs.getString("notes");	
			}
			con = new Concern (title, null, topic, null, null, null, notes);
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		
		return con;
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
		ArrayList<Object> linkedForms = new ArrayList<Object>();
		try (Connection conn = this.connect();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql))
		{
			while(rs.next())
			{
				String title = rs.getString("titel");
				linkedForms.add(rs.getString("linkedForms"));
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
		String PATH = "C:\\Users\\Mephisto\\eclipse-workspace\\Teamproject\\src\\ExportedData";
		String sql = "SELECT * FROM form";
		try (Connection conn = this.connect();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql))
		{
			while(rs.next())
			{
				String name = rs.getString("name");
				Blob ph = rs.getBlob("file");
				System.out.println(ph);
				InputStream in = ph.getBinaryStream();
				ByteArrayOutputStream out = new ByteArrayOutputStream();
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
		int MtrNr = student.getMtrNr();
		Form form = null;
		Topic topic = null;
		Appointment appointment = null;
		Reminder reminder = null;
		String sql = "SELECT c.title, cd.data, c.topic, ca.appointment, cs.student, c.notes "
				+ "FROM concern c, concer_student cs, concern_appointment ca, concern_dokument cd, concern_reminderMail cr WHERE c.id = cs.concern AND cs.student = " + MtrNr;		
		try (Connection conn = this.connect();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql))
		{
			while(rs.next())
			{
				String title = rs.getString("c.title");
				String test2 = rs.getString("f.name");
				form.setName(rs.getString("dokument"));
				ObservableList<Form> data = FXCollections.observableArrayList(form);
				topic.setId(rs.getInt("topic"));
				appointment.setId(rs.getInt("ca.appointment"));
				ObservableList<Appointment> appointments = FXCollections.observableArrayList(appointment);
				student.setMtrNr(rs.getInt("cs.student"));
				ObservableList<Reminder> reminders = FXCollections.observableArrayList(reminder);
				ObservableList<Student> students = FXCollections.observableArrayList(student);
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
		System.out.println("Student " + matNr + " deleted.");
	}

	public boolean saveNewSubject(String title, int ects) 
	{
		String t = title;
		int e  = ects;
		String sql = "INSERT INTO subject(titel, ects) VALUES(?,?)";
		try (Connection conn = this.connect();
			PreparedStatement pstmt = conn.prepareStatement(sql))
		{
			pstmt.setString(1, t);
			pstmt.setInt(2, e);
			pstmt.executeUpdate();
		}
		catch (Exception ex)
		{
			System.out.println(ex.getMessage());
			System.out.println("ERROR: Subject " + t + " could not be added to database");
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
			System.out.println("ERROR: Subject "+ title + " could not be changed.");
			return false;
		}
		return true;
	}


	public void saveNewTopic(String title, ObservableList<Form> selectedForms) 
	{
		String t = title;
		String linkedForms = selectedForms.toString();
		String sql = "INSERT INTO topic(titel, linkedForms) VALUES (?,?)";
		try (Connection conn = this.connect();
			PreparedStatement pstmt = conn.prepareStatement(sql))
		{
			pstmt.setString(1, t);
			pstmt.setString(2, linkedForms);
			pstmt.executeUpdate();
			
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
		System.out.println("Topic " + t + " saved.");
		
	}

	public void saveEditedTopic(String text, ObservableList<Form> selectedForms, Topic topic) 
	{
		Topic t = topic;
		String linkedForms = t.getLinkedForms().toString();
		String titel = t.getTitle();
		int id = t.getId();
		String sql = "UPDATE topic SET titel = "+titel+", linkedForms ="+linkedForms+" WHERE id = "+id; 
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

	public void saveEditedPO(String newPOName, ObservableList<Subject> selectedMandatorySubjects,
			ObservableList<Subject> selectedOptionalSubjects, PO po) 
	{
		
		po.setName(newPOName);		
		po.setMandatorySubjects(selectedMandatorySubjects.toArray(new Subject[selectedMandatorySubjects.size()]));		
		po.setOptionalSubjects(selectedOptionalSubjects.toArray(new Subject[selectedOptionalSubjects.size()]));


		String sql = "UPDATE po SET name = "+newPOName;
		try (Connection conn = this.connect();
			Statement stmt = conn.createStatement())
		{
			stmt.executeQuery(sql);
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
	}
	

	public void deleteConcern(Concern c) 
	{
		Concern dc = c;
		int ID = dc.getId();
		String sql = "DELETE * FROM concern WHERE id = "+ ID;
		try (Connection conn = this.connect();
			Statement stmt = conn.createStatement())
		{
			stmt.executeQuery(sql);
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		System.out.println("Concern " + ID + " deleted.");
	}

	public void saveNewForm(Form form) 
	{
		Form sf = form;
		String name = sf.getName();
		FileInputStream fis = null;
		String sql = "INSERT INTO form (name, file) VALUES (?, ?)";
		try (Connection conn = this.connect();
			PreparedStatement pstmt = conn.prepareStatement(sql))
		{	
			File file = sf.getFile();
			fis = new FileInputStream(file);
			pstmt.setString(1, name);
			pstmt.setBinaryStream(2,  fis, (int) file.length());
			pstmt.executeUpdate();
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
		System.out.println("Form " + name + " saved.");
	}

	public void deleteForm(Form f) 
	{
		Form df = f;
		String formname = df.getName();
		String sql = "DELETE * FROM form WHERE name = "+formname;
		try (Connection conn = this.connect();
			Statement stmt = conn.createStatement())
		{
			stmt.executeQuery(sql);
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		System.out.println("Form" + f + " deleted.");
	}

	public void saveMail(EMail email) 
	{
		
		EMail em = email;
		int ID = em.getId();
		String subject = em.getSubject();
		String content = em.getContent();
		boolean received = em.isReceived();
		String sql = "INSERT INTO eMail(subject, content, student, recived) values (?, ?, ?, ?)";
		try (Connection conn = this.connect();
			PreparedStatement pstmt = conn.prepareStatement(sql))
		{
			pstmt.setString(2, subject);
			pstmt.setString(3, content);
			pstmt.setBoolean(4, received);
			pstmt.executeUpdate();
		} 
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		System.out.println("eMail " + ID + " saved.");
	}


	public void saveEditedStudent(Student student) {
		Student s = student;
		String name = s.getName();
		String firstname = s.getFirstName();
		String eMailAddresses = s.geteMailAddresses().toString();
		int semester = s.getSemester();
		String notes = s.getNotes();
		int ects = s.getEcts();
		Image img = s.getImage();
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
		Student s = student;
		int MtrNr = s.getMtrNr();
		EMail[] mailArr = null;
		String sql = "SELECT * FROM eMail WHERE student = "+MtrNr+" ORDER BY date ASC";
		try (Connection conn = this.connect();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql))
		{
			String subject = rs.getString("subject");
			String content = rs.getString("content");
			Student student1 = getStudent(rs.getInt("student"));
			Boolean recieved = rs.getBoolean("received");
			Date date = rs.getDate("date");
			mail.add(new EMail(content, subject, student, date, recieved));
			mailArr = new EMail[mail.size()];
			mailArr = mail.toArray(mailArr);
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
		// TODO Auto-generated method stub
		//TODO Email in chronologischer reihenfolge. Älteste Email zuerst
		return mailArr;
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
		String sql = "UPDATE concern SET title = "+title+", data = "+data+", topic = "+topic+", appointments ="+appointments+", reminders = "+reminders+", students = "+students+", notes = "+notes;
		try (Connection conn = this.connect();
			Statement stmt = conn.createStatement())
		{
			stmt.executeUpdate(sql);
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
	}


	public void saveNewConcern(Concern concern) {
		Concern con = concern;
		String sql = "INSERT INTO Concern (title, topic, notes) VALUES (?,?,?)";
		String title = con.getTitle();
		Topic topic = con.getTopic();
		String notes = con.getNotes();
		try(Connection conn = this.connect();
			PreparedStatement pstmt = conn.prepareStatement(sql))
		{
			pstmt.setString(2, title);
			pstmt.setInt(3, topic.getId());
			pstmt.setString(4, notes);
			pstmt.executeUpdate();
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		// TODO Concern ID zuweisen mit concern.setId(neueId);
		// TODO Auto-generated method stub
		
	}
}
