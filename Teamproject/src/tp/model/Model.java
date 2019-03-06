package tp.model;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.URL;
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
			fout = new FileOutputStream("");
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
			//TODO path anpassen, für spätere Installation
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
			//TODO hier is ne Error message file not found ---Wusste nicht ob die file mit .ser am ende richtig ist da kennt ihr euch besser aus bitte passt das mal an, ansonsten fehlt da natr�lich auch der Filepath da wir den ja noch nicht fix angegeben haben.
			fis = new FileInputStream(new File("sessionTabsIds.ser"));
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
			//TODO hier is ne Error message, file not found  ---Wusste nicht ob die file mit .ser am ende richtig ist da kennt ihr euch besser aus bitte passt das mal an, ansonsten fehlt da natr�lich auch der Filepath da wir den ja noch nicht fix angegeben haben.
			fis = new FileInputStream(new File("Options.ser"));
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
		String PATH = "C:\\Users\\Mephisto\\eclipse-workspace\\Teamproject\\src\\ExportedData";
		String sql = "SELECT * FROM student WHERE eMailAddresses = " + emailAdressse;
		Image img = null;
		int mtrNr = 0; 
		String name = null; 
		String firstname = null; 
		ObservableList<String> eMailAddresses = FXCollections.observableArrayList();
		int semester = 0; 
		String notes = null; 
		int ects = 0;
		Concern[] concerns = null;
		
		
		try (Connection conn = this.connect();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql))
		{
			while(rs.next())
			{
				
				mtrNr = rs.getInt("Matrikelnummer");
				name = rs.getString("name");
				firstname = rs.getString("firstname");
				eMailAddresses.add(rs.getString("eMailAddressess"));
				semester = rs.getInt("semester");
				notes = rs.getString("notes");
				ects = rs.getInt("ects");
				concerns = rs.getInt("concerns");
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
				Image image = new Image(file.toURI().toString());	
			}
			s = new Student(mtrNr, name, firstname, eMailAddresses, semester, notes, ects, img, concerns);
		}
		
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}		
		return s;
	}
	
	public Student getStudent(int mtrNr) 
	{
		String PATH = "C:\\Users\\Mephisto\\eclipse-workspace\\Teamproject\\src\\ExportedData"; 
		String sql = "SELECT * FROM student WHERE Matrikelnummer = " + mtrNr;
		Student s = new Student(0, sql, sql, null, 0, sql, 0, null, null);
		Image img = null;
		String name = null; 
		String firstname = null; 
		ArrayList<String> eMail = new ArrayList<String>();
		String[] eMailArr = null;
		int semester = 0; 
		String notes = null; 
		int ects = 0;
		Concern[] concerns = null;
		
		
		try (Connection conn = this.connect();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql))
		{
			while(rs.next())
			{
				mtrNr = rs.getInt("Matrikelnummer");
				name = rs.getString("name");
				firstname = rs.getString("firstname");
				eMail.add(rs.getString("eMailAddressess"));
				semester = rs.getInt("semester");
				notes = rs.getString("notes");
				ects = rs.getInt("ects");
				concerns = rs.getInt("concerns");
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
				Image image = new Image(file.toURI().toString());
				eMailArr = new String[eMail.size()];
				eMailArr = eMail.toArray(eMailArr);		
			}
			s = new Student(mtrNr, name, firstname, eMailArr, semester, notes, ects, img, concerns);
		}
		
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}		
		return s;
	}
	
	public Concern getConcern(int concernId) 
	{
		Concern c = new Concern();
		String sql = "SELECT * FROM concern";
		int id = 0;
		String title = null;
		int topic = 0;
		String notes = null;
		try (Connection conn = this.connect();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql))
		{
			while(rs.next())
			{
				id = rs.getInt("id");
				title = rs.getString("titel");
				topic = rs.getInt("topic");
				notes = rs.getString("notes");
			}
		c.setId(id);
		c.setTitle(title);
		c.setTopic(topic);
		c.setNotes(notes);
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		
		return c;
	}
	
	public Statistic getStatistic(int statisticId) 
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	public ObservableList<PO> getPOs() 
	{
		PO p = new PO(null);
		String sql = "SELECT * FROM po";
		String name = null;
		try (Connection conn = this.connect();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql))
		{
			while(rs.next())
			{
				name = rs.getString("name");
			}
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		p = new PO(name);
		return (ObservableList<PO>) p;
		
	}
	
	public ObservableList<Topic> getTopics() 
	{
		Topic t = new Topic(null);
		String sql = "SELECT * FROM topic";
		String title = null;
		try (Connection conn = this.connect();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql))
		{
			while(rs.next())
			{
				title = rs.getString("titel");
			}
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		t = new Topic(title);
		return (ObservableList<Topic>) t;
	}

	public ObservableList<Subject> getSubjects() 
	{
		Subject s = new Subject(0, null, 0);
		String sql = "SELECT * FROM subject";
		String title = null;
		int ects = 0;
		int id = 0;
		try (Connection conn = this.connect();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql))
		{
			while(rs.next())
			{
				id = rs.getInt("id");
				title = rs.getString("titel");
				ects = rs.getInt("ects");
			}
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		s = new Subject(id, title, ects);
		return (ObservableList<Subject>) s;
	}
	
	public ObservableList<Form> getForms() 
	{
		Form f = new Form(null, null);
		String PATH = "C:\\Users\\Mephisto\\eclipse-workspace\\Teamproject\\src\\ExportedData";
		String sql = "SELECT * FROM form";
		String name = null;
		File file = null;
		try (Connection conn = this.connect();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql))
		{
			while(rs.next())
			{
				name = rs.getString("name");
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
				file = new File(PATH+name+".pdf");
			}
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		f = new Form(name, file);
		return (ObservableList<Form>) f;
	}
	
	public ObservableList<Concern> getConcerns(Student student) 
	{
		Concern c = new Concern();
		Student s = student;
		int MatNr = s.getMtrNr();
		String name = s.getName();
		String sql = "SELECT c.titel FROM concern c, concer_student cs WHERE c.id = cs.concern AND cs.student = " + MatNr;
		String concern = null;
		
		try (Connection conn = this.connect();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql))
		{
			while(rs.next())
			{
				concern = rs.getString("c.titel");
			}
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		c = new Concern(concern);
		return c;
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

	public void saveNewSubject(String title, int ects) 
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

	public void saveEditedSubject(String title, int ects, int id) 
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
		//geänderten Namen für po übernehmen
		po.setName(newPOName);
		//geänderte mandatory Subjects (als Array) für po übernehmen
		po.setMandatorySubjects(selectedMandatorySubjects.toArray(new Subject[selectedMandatorySubjects.size()]));
		//geänderte optional Subjects (als Array) für po übernehmen
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
		// TODO Auto-generated method stub
		
	}


	public EMail[] getEMails(Student student) {
		// TODO Auto-generated method stub
		//TODO Email in chronologischer reihenfolge. �lteste Email zuerst
		return null;
	}


	public void saveEditedConcern(Concern concern) {
		// TODO Auto-generated method stub
		
	}


	public void saveNewConcern(Concern concern) {
		// TODO Concern ID zuweisen mit concern.setId(neueId);
		// TODO Auto-generated method stub
		
	}
}
