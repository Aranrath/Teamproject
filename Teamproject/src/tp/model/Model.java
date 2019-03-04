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
import java.sql.Blob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.ObservableList;

public class Model {

	private String[] sessionTabsIds;
	private Options options;
	
	public Model() {
		loadSessionTabsIds();
	}
	
	
	//------------------establish Database connection-----------------------------------------------
	private Connection connect() 
	{
		String url = "jdbc:sqlite:Teamprojekt.db";
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

	public Appointment[] loadNext24hourAppointments() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Date[] getWorkWeekOfDate(Date date) {
		// TODO Auto-generated method stub
		return null;
	}
	public int getKwOfDate(Date date) {
		// TODO Auto-generated method stub
		return 0;
	}
	public Appointment[] getWeeksAppointments(int shownKw) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Date getStartOfNextWeek(Date date) {
		// TODO Auto-generated method stub
		return null;
		
	}

	public java.sql.Date getEndOfPreviousWeek(Date date) {
		// TODO Auto-generated method stub
		return null;
	}
	
	//------------------File: Loader&Saver + Getter/Setter---------------------------------------------------------------
	
	
	public String[] getSessionTabs() {
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

	public String[] loadSessionTabsIds() 
	{
		FileInputStream fis;
		ObjectInputStream ois;
		try
		{
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
	
	public void setSessionTabs(String[] sessionTabs) {
		this.sessionTabsIds = sessionTabs;
	}
	

	// ------------Datenbank Abfragen--------------------------------------------------------------------

	
	public Student getStudent(String emailAdressse) 
	{
		String PATH = ""; //filepath for img
		String sql = "SELECT * FROM student WHERE eMailAddresses = " + emailAdressse;
		try (Connection conn = this.connect();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql))
		{
			while(rs.next())
			{
				int matNr = rs.getInt("Matrikelnummer");
				String name = rs.getString("name");
				String firstname = rs.getString("firstname");
				String eMailAddresses = rs.getString("eMailAddresses");
				int semester = rs.getInt("semester");
				String notes = rs.getString("notes");
				int ects = rs.getInt("ects");
				Blob ph = rs.getBlob("img");
				System.out.println(ph);
				InputStream in = ph.getBinaryStream();
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				OutputStream outputStream = new FileOutputStream(PATH);
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
				System.out.println("Matrikelnummer: " + matNr + " Name: " + name + " Firstname: " + firstname + " eMail Addresses: " + eMailAddresses + " Semester: " + semester + " Notes: " + notes + " ECTS: " + ects + " Image saved at: " + PATH );
			}
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		return null;
	}
	
	public Student getStudent(int mtrNr) 
	{
		String PATH = ""; //Image Filepath
		String sql = "SELECT * FROM student WHERE Matrikelnummer = " + mtrNr;
		try (Connection conn = this.connect();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql))
		{
			while(rs.next())
			{	
				int matNr = rs.getInt("Matrikelnummer");
				String name = rs.getString("name");
				String firstname = rs.getString("firstname");
				String eMailAddresses = rs.getString("eMailAddresses");
				int semester = rs.getInt("semester");
				String notes = rs.getString("notes");
				int ects = rs.getInt("ects");
				Blob ph = rs.getBlob("img");
				System.out.println(ph);
				InputStream in = ph.getBinaryStream();
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				OutputStream outputStream = new FileOutputStream(PATH);
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
				System.out.println("Matrikelnummer: " + matNr + " Name: " + name + " Firstname: " + firstname + " eMail Addresses: " + eMailAddresses + " Semester: " + semester + " Notes: " + notes + " ECTS: " + ects + " Image saved at: " + PATH );
			}
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		return null;
	}
	
	public Concern getConcern(int concernId) 
	{
		String sql = "SELECT * FROM concern";
		try (Connection conn = this.connect();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql))
		{
			while(rs.next())
			{
				int id = rs.getInt("id");
				String titel = rs.getString("titel");
				int topic = rs.getInt("topic");
				System.out.println("Concern ID: " + id + " Titel: " + titel + " related Topic ID: " + topic);
			}
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		return null;
	}
	
	public Statistic getStatistic(int statisticId) 
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	public ObservableList<PO> getPOs() 
	{
		String sql = "SELECT * FROM po";
		try (Connection conn = this.connect();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql))
		{
			while(rs.next())
			{
				String name = rs.getString("name");
				System.out.println("Po´s: " + name);
			}
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		return null;
		
	}
	
	public ObservableList<Topic> getTopics() 
	{
		String sql = "SELECT * FROM topic";
		try (Connection conn = this.connect();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql))
		{
			while(rs.next())
			{
				int id = rs.getInt("id");
				String titel = rs.getString("titel");
				String linkedForms = rs.getString("linkedForms");
				System.out.println("Topic ID: " + id + " Titel: " + titel + " Forms linked to topic: " + linkedForms);
			}
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		return null;
	}

	public ObservableList<Subject> getSubjects() 
	{
		String sql = "SELECT * FROM subject";
		try (Connection conn = this.connect();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql))
		{
			while(rs.next())
			{
				int id = rs.getInt("id");
				String titel = rs.getString("titel");
				int ects = rs.getInt("ects");
				System.out.println("Subject ID: " + id + " Titel: " + titel + " ECTS: " + ects);
			}
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		return null;
	}
	
	public ObservableList<Form> getForms() 
	{
		String PATH = ""; //Filepath
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
				OutputStream outputStream = new FileOutputStream(PATH);
				int length = (int) ph.length();
				int bufferSize = 1024;
				byte[] buffer = new byte[bufferSize];
				while ((length = in.read(buffer)) != -1)
				{
					System.out.println("writing " + length + " bytes");
					out.write(buffer, 0, length);
				}
				out.writeTo(outputStream);
				System.out.println("Form: " + name + " was saved at: " + PATH);
			}
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		return null;
	}
	
	public ObservableList<Concern> getConcerns(Student student) 
	{
		Student s = student;
		int MatNr = s.getMtrNr();
		String name = s.getName();
		String sql = "SELECT c.titel FROM concern c, concer_student cs WHERE c.id = cs.concern AND cs.student = " + MatNr;
		try (Connection conn = this.connect();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql))
		{
			while(rs.next())
			{
				String concern = rs.getString("c.titel");
				System.out.println("Student: " + name + " has following concerns: " + concern + ".");
			}
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		return null;
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
}
