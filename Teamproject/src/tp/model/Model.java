package tp.model;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.mail.Address;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.util.Pair;
import tp.Main;
import tp.model.statistics.ContinuousStatistic;
import tp.model.statistics.IntervalStatistic;
import tp.model.statistics.RatioStatistic;
import tp.model.statistics.Statistic;
import tp.model.statistics.StatisticComponent;
import tp.model.statistics.StatisticComponent.Filter;
import tp.model.statistics.StatisticValues;

public class Model {

	private ArrayList<String> sessionTabsIds;
	private Options options;
	
	private static String standardDirectory;
	
	
	public Model() {
		loadSessionTabsIds();
	}
	
	public static String getStandardDirectory(){
		final Class<?> referenceClass = Main.class;
		final URL url =	    referenceClass.getProtectionDomain().getCodeSource().getLocation();
		try{
		    final File jarPath = new File(url.toURI()).getParentFile();
		    standardDirectory = jarPath + "\\";
		} catch(final URISyntaxException e){
		}
		return standardDirectory;
	}
	
	//------------------establish Database connection---------------------------------------------
	private Connection connect() 
	{
		Connection conn = null; 
		try 
		{
			conn = DriverManager.getConnection("jdbc:sqlite::resource:teamprojectDatabase.db");
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return conn;
	}
	
	
	//-------------------Calculations--------------------------------------------------------------

	public ArrayList<Appointment> getNext24hourAppointments() {
		//For SQL-Query
		Date now = new Date(System.currentTimeMillis());
		//To be able to compare the date to the one in the dataBase, set Time to 0.
		now = setTimeToZero(now);
		
		Calendar c = Calendar.getInstance();
        c.setTime(now);
        c.add(Calendar.DATE, 1);
        Date nextDay = new Date(c.getTimeInMillis());
        nextDay = setTimeToZero(nextDay);
		
        java.util.Date utilDate = new java.util.Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        Long time = Time.valueOf(sdf.format(utilDate)).getTime();
        
		String sql = "SELECT * FROM appointment WHERE (date = "+ now.getTime() + " AND endTime >= " + time + ") ORDER BY startTime ASC";		
		String sql2 = "SELECT * FROM appointment WHERE (date = " + nextDay.getTime() + " AND startTime <= " + time + ") ORDER BY startTime ASC";
		ArrayList<Appointment> result = new ArrayList<Appointment>();
		try 	(Connection conn = this.connect();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql);
				Statement stmt2 = conn.createStatement();
				ResultSet rs2 = stmt2.executeQuery(sql2))
		{
			while(rs.next())
			{
				int id = rs.getInt("id");
				result.add(getAppointment(id));
			}
			while(rs2.next())
			{
				int id = rs2.getInt("id");
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
		//To be able to compare the date to the one in the dataBase, set Time to 0.
		date = setTimeToZero(date);
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
	
	public Date setTimeToZero(Date date) {
		//To be able to compare the date to the one in the dataBase, set Time to 0.
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);	
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return new Date(cal.getTime().getTime());
	}
	
	public ObservableList<Reminder> getDueReminders() {
		ObservableList<Reminder> result = FXCollections.observableArrayList();
		Date date = new Date(System.currentTimeMillis());
		String sql ="SELECT * FROM reminder WHERE date < " + date.getTime();
		try(Connection conn = this.connect();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql))
		{
			while(rs.next()) 
			{
				long id = rs.getInt("id");
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
	
	public ArrayList<Appointment> getAppointments(Date date) {
		ArrayList<Appointment> result = new ArrayList<Appointment>();
		String sql = "SELECT * FROM appointment WHERE date = " + date.getTime() + " ORDER BY startTime ASC";
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
	
	
	public int calculateEcts(ObservableList<Subject> passedSubjects, PO po)
	{
		int ects = 0;
		
		for(Subject passedSub : passedSubjects)
		{
			for (Subject manSub: po.getMandatorySubjects()) {
				if(passedSub.getId() == manSub.getId()) {
					ects += passedSub.getEcts();
				}
			}
			for(Subject optSub: po.getOptionalSubjects()) {
				if(passedSub.getId() == optSub.getId()) {
					ects += passedSub.getEcts();
				}
			}
		}
		return ects;
	}


	public ArrayList<String> getSessionTabsIds() {
		if (sessionTabsIds == null)
		{
			loadSessionTabsIds();
		}
		return sessionTabsIds;
	}
	
	public void setSessionTabsIds(ArrayList<String> newSessionTabsIds) {
		this.sessionTabsIds = newSessionTabsIds;
		saveSessionTabsIds();
	}
	
	public void saveSessionTabsIds() 
	{
		try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(standardDirectory +"SessionTabsIds.ser")))
		{
			oos.writeObject(sessionTabsIds);
		}
		catch (Exception e)
		{
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
	
	// ------------Datenbank Getter----------------------------------------------------------------------

	
	public Appointment getAppointment(long id) {
		Appointment result = new Appointment(0, null, 0, 0, null);
		String sql = "SELECT * FROM appointment WHERE id = " + id;
		try 	(Connection conn = this.connect();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql))
		{
			if (rs.next()) {
			
				long concernId = rs.getInt("concern");
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
		String sql = "SELECT id FROM appointment WHERE concern = " + concernId + " ORDER BY date DESC";
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
		Concern result = new Concern(null);
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
				Date closingDate = rs.getDate("done");
				Boolean isCompleted = rs.getBoolean("complete");
			
				result = new Concern (concernId, title, forms, topic, appointments, reminders, students, notes, closingDate, isCompleted);
			}
	
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}


	public Concern getConcern(Appointment clashingAppointment) {
		Concern result = null;
		String sql = "SELECT concern FROM appointment WHERE id = " + clashingAppointment.getId();
		try (Connection conn = this.connect();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql))
			{
				while(rs.next())
				{
					long conId = rs.getLong("concern");
					result = getConcern(conId);
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
				long id = rs.getLong("id");
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


	public ArrayList<EMail> getEMails(ArrayList<String> mailAddress) {
		ArrayList<EMail> mail = new ArrayList<EMail>();
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
	
	public EMail getLastAddressEmail(String mailAddress) {
		ArrayList<String> mailAddressList = new ArrayList<String>();
		mailAddressList.add(mailAddress);
		ArrayList<EMail> mails = getEMails(mailAddressList);
		if(!mails.isEmpty()) {
			return mails.get(0);
		}
		return null;
	}
	
	public EMail getLastStudentEmail(Student student) {
		ArrayList<EMail> mails = getEMails(student.geteMailAddresses());
		if(!mails.isEmpty()) {
			return mails.get(mails.size()-1);
		}
		return null;
	}


	private ArrayList<String> getEMailAddressess(long id) {
		ArrayList<String> result = new ArrayList<String>();
		String sql ="SELECT emailAddress FROM student_emailAddress WHERE student = " + id;
		
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


	public ObservableList<Form> getUniversalForms() 
	{
		ObservableList<Form> result = FXCollections.observableArrayList();
		String sql = "SELECT * FROM form where universal = 1";
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


	public Form getForm(int id) {
		Form result = new Form(null, null, null);
		String sql = "SELECT * FROM form WHERE id = " + id;
		try 	(Connection conn = this.connect();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql))
		{
			if (rs.next()) {
		
				String title = rs.getString("title");
				String extension = rs.getString("extension");
				Boolean universal = rs.getBoolean("universal");
				File file = File.createTempFile(title, extension);
				try(FileOutputStream out = new FileOutputStream(file);
						InputStream in = rs.getBinaryStream("file")){
					byte[] buffer = new byte[1024];
					while(in.read(buffer)>0) {
						out.write(buffer);
					}
				}catch(Exception e) {
					e.printStackTrace();
				}
				result = new Form(title, file, extension, universal);
				result.setId(id);
				file.deleteOnExit();
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private ObservableList<Subject> getPassedSubjects(long id) {
		ObservableList<Subject>  result = FXCollections.observableArrayList();
		String sql = "SELECT subject FROM passed_subjects WHERE student = " + id;
		try (Connection conn = this.connect();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql))
		{
			while(rs.next()) {
				int SubjectId = rs.getInt("subject");
				result.add(getSubject(SubjectId));
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public PO getPO(long poId) {
		PO result = null;
		String sql = "SELECT * from po WHERE id = " + poId;
		try (Connection conn = this.connect();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql))
			{
				if(rs.next())
				{
					int id = rs.getInt("id");
					String title = rs.getString("title");
					ObservableList<Subject> optional = getSubjects(id, 1);
					ObservableList<Subject> mandatory = getSubjects(id, 0);
					PO po = new PO(title);
					po.setId(id);
					po.setOptionalSubjects(optional);
					po.setMandatorySubjects(mandatory);
					result = po;
				}
			}
			catch(Exception e)
			{
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
				String title = rs.getString("title");
				ObservableList<Subject> optional = getSubjects(id, 1);
				ObservableList<Subject> mandatory = getSubjects(id, 0);
				PO po = new PO(title);
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


	public Reminder getReminder(long id) {
		Reminder result = new Reminder(null, null, null);
		String sql = "SELECT * FROM reminder WHERE id = " + id;
		try 	(Connection conn = this.connect();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql))
		{
			if (rs.next()) {
		
				String message = rs.getString("message");
				Date date = rs.getDate("date");
				Long concernId = rs.getLong("concern");
				result = new Reminder(message, date, concernId);
				result.setId(id);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}


	private ObservableList<Reminder> getReminders(long concernId) {
		ObservableList<Reminder>  result = FXCollections.observableArrayList();
		String sql = "SELECT id FROM reminder WHERE concern = " + concernId + " ORDER BY date DESC";
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
		Date now = new Date(System.currentTimeMillis());
		now = setTimeToZero(now);
		
		String sql ="SELECT id FROM reminder WHERE date <= " + now.getTime() + " AND date >= " + lastReminderCheck.getTime();
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


	public Statistic getStatistic(long id) 
	{
		Statistic result = null;
		String sql = "SELECT * FROM statistic WHERE id = " + id;
		try (Connection conn = this.connect();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql))
		{
			if(rs.next()) {
				String title = rs.getString("title");
				String charttype = rs.getString("charttype");
				List<StatisticValues> values = getStatisticComponents(id);
				Date startDate = rs.getDate("startDate");
				if (charttype.equals("ratio")) {
					result = new RatioStatistic(title, values, startDate);
				}else if(charttype.equals("continuous")) {
					Date endDate = rs.getDate("endDate");
					result = new ContinuousStatistic(title, values, startDate, endDate);
				}else if(charttype.equals("interval")) {
					Date endDate = rs.getDate("endDate");
					int step = rs.getInt("step");
					result = new IntervalStatistic(title, values, startDate, endDate, step);
				}
				result.setId(rs.getLong("id"));
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}


	public ObservableList<Statistic> getStatistics() {
		ObservableList<Statistic> result = FXCollections.observableArrayList();
		String sql = "SELECT id FROM statistic";
		try (Connection conn = this.connect();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql))
		{
			while(rs.next())
			{
				long id = rs.getLong("id");
				result.add(getStatistic(id));
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}


	private List<StatisticValues> getStatisticComponents(long statisticId) {
		List<StatisticValues> result = new ArrayList<StatisticValues>();
		String sql = "SELECT * FROM statistic_component WHERE statistic = " + statisticId;
		try (Connection conn = this.connect();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql))
		{
			while(rs.next()) {
				long id = rs.getLong("id");
				String name = rs.getString("name");
				List<Pair<Integer, Integer>> values = getStatisticValues(id);
				StatisticValues statVal = new StatisticValues(name, values);
				result.add(statVal);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}


	private List<Pair<Integer, Integer>> getStatisticValues(long statCompId) {
		List<Pair<Integer, Integer>> result = new ArrayList<Pair<Integer, Integer>>();
		String sql = "SELECT * FROM statistic_values WHERE statCompId = " + statCompId + " ORDER BY valueNumber ASC";
		try (Connection conn = this.connect();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql))
		{
			while(rs.next()) {
				result.add(new Pair<Integer, Integer>(rs.getInt("valueNumber"), rs.getInt("value")));
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public Image getStudentImage(long id) {
		String sql = "SELECT image FROM student WHERE id = " + id;
		Image img = null;
		try (Connection conn = this.connect();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql))				
			{
				if (rs.next()) {
					try(InputStream is= rs.getBinaryStream("image")){
						if (is!=null) {
							img = new Image(is);		
						}
					}catch(Exception e) {
						e.printStackTrace();
					}
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		return img;
	}
	
	public Student getStudent(long id) 
	{
	Student result = new Student(0, 0, null, null, null, 0, null, null, null, null, null, null, null);
	String sql1 = "SELECT * FROM student WHERE id = " + id;
	String sql2 = "SELECT concern FROM concern_student WHERE student = " + id;
	try (Connection conn = this.connect();
			Statement stmt = conn.createStatement();
			ResultSet rs1 = stmt.executeQuery(sql1))				
		{
			if (rs1.next()) {
			
				int matrNr = rs1.getInt("matrNr");
				String name = rs1.getString("name");
				String firstname = rs1.getString("firstname");
				ArrayList<String> eMailAddressess = getEMailAddressess(id);
				int semester = rs1.getInt("semester");
				PO po = getPO(rs1.getLong("po"));
				String notes = rs1.getString("notes");
				ObservableList<Subject> passedSubjects= getPassedSubjects(id);
				String gender = rs1.getString("gender");
				Date exmatr =  rs1.getDate("exmatr");
				
				result = new Student(id, matrNr, name, firstname, eMailAddressess, semester, po, notes, passedSubjects, null, gender, null, exmatr);
				if(getLastStudentEmail(result)!=null) {
					Date lastContact = getLastStudentEmail(result).getDate();
					result.setLastContact(lastContact);
				}
			}
			try (ResultSet rs2 = stmt.executeQuery(sql2))				
			{
				ObservableList<Long> concerns = FXCollections.observableArrayList();
				while(rs2.next())
				{
					concerns.add(rs2.getLong("concern"));
				}
				result.setConcernIds(concerns);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		catch(Exception e)
		{
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
				long id = rs.getLong("id");
				result.add(getStudent(id));
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
				result.add(getStudent(rs.getLong("student")));
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}


	private Subject getSubject(long id) {
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
	

	private ObservableList<Subject> getSubjects(int poId, int optional) {
		ObservableList<Subject> result = FXCollections.observableArrayList();
		String sql = "SELECT subject FROM po_subject WHERE po = " + poId + " AND optional = " + optional;
		try (Connection conn = this.connect();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql))
		{
			while(rs.next()) {
				result.add(getSubject(rs.getLong("subject")));
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}


	public Topic getTopic(long topicId) {
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


	private ObservableList<Form> getTopicForms(long topicId) {
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
	
	public void saveNewAppointment(long conId, Appointment appointment) {
		String sql = "INSERT INTO appointment(concern, date, startTime, endTime, roomNmb) values (?, ?, ?, ?, ?)";
		try (Connection conn = this.connect();
			PreparedStatement pstmt = conn.prepareStatement(sql))
		{
			pstmt.setLong(1, conId);
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


	private void addAppointments(long conId, ObservableList<Appointment> appointments) {
		for (Appointment a: appointments) {
			saveNewAppointment(conId, a);
		}
	}


	public void deleteAppointment(Appointment appointmentToDelete) {
		String sql = "DELETE FROM appointment WHERE id = "+ appointmentToDelete.getId();
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


	public Appointment checkAppointmentAvailability(Date date, long startTime, long endTime) {
		String sql = "SELECT id FROM appointment WHERE date = " + date.getTime() + " AND ((startTime < " + endTime + " AND endTime > " + endTime + ") OR " + 
																						 "(endTime > " + startTime + " AND startTime < " + endTime + ") OR " + 
																						 "(startTime <= " + startTime + " AND endTime >= " + endTime + "))";
		try (Connection conn = this.connect();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql))
			{
				if (rs.next()) {
					int id = rs.getInt("id");
					return getAppointment(id);				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		return null;
	}


	public int saveNewConcern(Concern concern) {
		int id = 0; 
		String sql1 = "INSERT INTO concern (title, topic, notes, created) values (?, ?, ?, ?)";
		String sql2 = "SELECT last_insert_rowid()";
		try (Connection conn = this.connect();
			PreparedStatement pstmt = conn.prepareStatement(sql1);
			Statement stmt = conn.createStatement();
			)
		{
			pstmt.setString(1, concern.getTitle());
			pstmt.setLong(2, concern.getTopic().getId());
			pstmt.setString(3, concern.getNotes());
			pstmt.setDate(4, new Date(System.currentTimeMillis()));
			pstmt.executeUpdate();
			try (ResultSet rs = stmt.executeQuery(sql2)){
				if (rs.next()) {
					id = rs.getInt(1);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			addConcernForms(id, concern.getFiles());
			addConcernStudents(id, concern.getStudents());
			addAppointments(id, concern.getAppointments());
			addReminders(id , concern.getReminders());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		//lösche alle Concern_Forms, sie keinem Concern mehr zugeordnet sind
		deleteNonUniversalForms();
		return id;
	}


	public void saveEditedConcern(Concern concern) {
		String sql1 = "UPDATE concern SET title = '" + concern.getTitle() + "', topic = " + concern.getTopic().getId() +
				", notes = '" + concern.getNotes() + "', done = " + concern.getClosingDate() + ", complete = " + concern.isCompleted() + 
				" WHERE id = " + concern.getId();
		String sql2 = "DELETE FROM concern_forms WHERE concern = " + concern.getId();
		String sql3 = "DELETE FROM concern_student WHERE concern = " + concern.getId();
		String sql4 = "DELETE FROM appointment WHERE concern = " + concern.getId();
		String sql5 = "DELETE FROM reminder WHERE concern = " + concern.getId();
		try (Connection conn = this.connect();
			Statement stmt = conn.createStatement())
		{
			stmt.executeUpdate(sql1);
			stmt.executeUpdate(sql2);
			stmt.executeUpdate(sql3);
			stmt.executeUpdate(sql4);
			stmt.executeUpdate(sql5);
			addConcernForms(concern.getId(), concern.getFiles());
			addConcernStudents(concern.getId(), concern.getStudents());
			addAppointments(concern.getId(), concern.getAppointments());
			addReminders(concern.getId(), concern.getReminders());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		//lösche alle Concern_Forms, sie keinem Concern mehr zugeordnet sind
		deleteNonUniversalForms();
	}

	public void setConcernCosed(Concern c) {
		String sql = "UPDATE concern SET done = DATE('now'), complete = " + c.isCompleted() + " WHERE id = " + c.getId();
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
	
	public void deleteConcernStudent(long concernId, long studentId) {
		String sql = "DELETE FROM concern_student WHERE concern = "+ concernId + " AND student = " + studentId ;
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
				stmt.executeUpdate(sql);
			}
			}catch (Exception e) {
				e.printStackTrace();
			}
	}


	private void addConcernsStudent(ObservableList<Long> concernIds, long id) {
		String sql;
		try (Connection conn = this.connect();
				Statement stmt = conn.createStatement())
			{
			for (Long concern: concernIds){
				sql="INSERT INTO concern_student (concern, student) VALUES (" + concern + ", " + id + ")";
				stmt.executeUpdate(sql);
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
				sql="INSERT INTO concern_student (concern, student) VALUES (" + id + ", " + student.getId()+ ")";
				stmt.executeUpdate(sql);
			}
			}catch (Exception e) {
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
	
	
	public Form saveNewForm(Form form) 
	{
		String sql = "INSERT INTO form (title, file, extension, universal) VALUES (?, ?, ?, ?)";
		String sql2 = "SELECT last_insert_rowid()";
		try (Connection conn = this.connect();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			Statement stmt = conn.createStatement())
		{	
			File file = form.getFile();
			pstmt.setString(1, form.getName());
			pstmt.setBytes(2, readFile(file.getAbsolutePath()));
			
			String extension = "";
	        try {
	            if (file != null && file.exists()) {
	                String name = file.getName();
	                extension = name.substring(name.lastIndexOf("."));
	            }
	        } catch (Exception e) {
	            extension = "";
	        }
	        pstmt.setString(3,  extension);
	        pstmt.setBoolean(4, form.isUniversal());
			
			pstmt.executeUpdate();
			form.setFileExtension(extension);
			int formId = 0;
			try (ResultSet rs = stmt.executeQuery(sql2)){
				if (rs.next()) {
					formId = rs.getInt(1);
					form.setId(formId);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return form;
	}


	public void saveEditedForm(Form selectedForm) {
		String sql = "UPDATE form SET title = ?, file = ?, extension = ? WHERE id = " + selectedForm.getId();
		try (Connection conn = this.connect();
			PreparedStatement pstmt = conn.prepareStatement(sql))
		{	
			File file = selectedForm.getFile();
			pstmt.setString(1, selectedForm.getName());
			pstmt.setBytes(2, readFile(file.getAbsolutePath()));
			
			String extension = "";
	        try {
	            if (file != null && file.exists()) {
	                String name = file.getName();
	                extension = name.substring(name.lastIndexOf("."));
	            }
	        } catch (Exception e) {
	            extension = "";
	        }
	        pstmt.setString(3,  extension);
			
			pstmt.executeUpdate();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}


	public void deleteForm(Form f) 
	{
		String sql = "DELETE FROM form WHERE id = "+ f.getId();
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

	public void deleteNonUniversalForms() {
		String sql1 = "SELECT id FROM form, concern_forms WHERE NOT(form.id = concern_forms.form)";
		String sql2 = "SELECT id FROM form WHERE universal = 0";
		String sql3 = "DELETE FROM form WHERE id = ";
		try (Connection conn = this.connect();
			Statement stmt1 = conn.createStatement();
			Statement stmt2 = conn.createStatement();
			Statement stmt3 = conn.createStatement();
			ResultSet rs1 = stmt1.executeQuery(sql1);
			ResultSet rs2 = stmt2.executeQuery(sql2))
		{
			ArrayList<Long> formIds1 = new ArrayList<Long>();
			ArrayList<Long> formIds2 = new ArrayList<Long>();
			while(rs1.next())
			{
				formIds1.add(rs1.getLong("id"));
			}
			while(rs2.next())
			{
				formIds2.add(rs2.getLong("id"));
			}
			formIds1.retainAll(formIds2);
			for(Long formId: formIds1) {
				stmt3.executeUpdate(sql3  + formId);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}


	public void saveMail(EMail email) 
	{
		String sql = "INSERT INTO eMail(subject, content, eMailAddress, received, date) values (?, ?, ?, ?, ?)";
		try (Connection conn = this.connect();
			PreparedStatement pstmt = conn.prepareStatement(sql))
		{
			pstmt.setString(1, email.getSubject());
			pstmt.setString(2, email.getContent());
			pstmt.setString(3, email.getMailAddress());
			pstmt.setBoolean(4, email.isReceived());
			pstmt.setDate(5, email.getDate());
			pstmt.executeUpdate();
		} 
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}


	public PO saveNewPO(PO po) {
		String sql = "INSERT INTO po(title) VALUES ('"+ po.getName() +"')";
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
		String sql1 = "UPDATE po SET title = '"+ newPOName +"' WHERE id = " + po.getId();
		String sql2 = "DELETE FROM po_subject WHERE po = " + po.getId();
		try (Connection conn = this.connect();
			Statement stmt = conn.createStatement())
		{
			stmt.executeUpdate(sql1);
			stmt.executeUpdate(sql2);
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
				stmt.executeUpdate(sql);
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
		String sql = "UPDATE subject SET title = '" + subject.getTitle() + "' , ects = "+subject.getEcts() + " WHERE id = " + subject.getId();
		try (Connection conn = this.connect();
			Statement stmt = conn.createStatement())
		{
			stmt.executeUpdate(sql);
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

	
	public Statistic calculateAndSaveNewRatioStatistic(String title, ArrayList<StatisticComponent> statisticComponentsList, Date date) {
		//calculate
		List<StatisticValues> values = new ArrayList<StatisticValues>();
		for (StatisticComponent comp: statisticComponentsList) {
			values.add(calculateRatioStatisticValue(comp, date));
		}
		Statistic result = new RatioStatistic(title, values, date);
		
		//save
		String sql1 = "INSERT INTO statistic(title, charttype, startDate) VALUES('" + title +"', 'ratio', " + date.getTime() + ")";
		String sql2 = "SELECT last_insert_rowid()";
		try (Connection conn = this.connect();
			Statement stmt = conn.createStatement())
		{
			stmt.executeUpdate(sql1);
			int id = 0;
			try (ResultSet rs = stmt.executeQuery(sql2)){
				if (rs.next()) {
					id = rs.getInt(1);
					result.setId(id);
					saveStatisticValues(id, values);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return result;
	}


	private StatisticValues calculateRatioStatisticValue(StatisticComponent comp, Date date) {
		List<Pair<Integer, Integer>> values = new ArrayList<Pair<Integer, Integer>>();
		ArrayList<Long> result = new ArrayList<Long>();
		for(Filter f: comp.getSelectedFilter()) {
			ArrayList<Long> partialResult = calculateFilterValue(comp.getSource(), f, date);
			partialResult.removeAll(result);
			result.addAll(partialResult);
		}
		if(comp.getSelectedFilter().isEmpty()) {
			Filter f = new Filter();
			result = calculateFilterValue(comp.getSource(), f, date);
		}
		int value;
		if (comp.getSource().equals("Terminlängen")) {
			long duration = 0;
			for (Long appointmentId: result) {
				Appointment app = getAppointment(appointmentId);
				long startTime = app.getStartTime();
				long endTime = app.getEndTime();
				duration += (endTime - startTime);
			}
			value = (int)(duration/1000/60);
		}else {
			value = result.size();
		}
		values.add(new Pair<Integer, Integer>(0, value));
		return new StatisticValues(comp.getName(), values);
	}


	public Statistic calculateAndSaveNewContinuousStatistic(String title, ArrayList<StatisticComponent> statisticComponentsList,
			Date startDate, Date endDate) {
		//calculate
		List<StatisticValues> values = new ArrayList<StatisticValues>();
		for (StatisticComponent comp: statisticComponentsList) {
			values.add(calculateContinuousStatisticValue(comp, startDate, endDate));
		}
		Statistic result = new ContinuousStatistic(title, values, startDate, endDate);

		//save
		String sql1 = "INSERT INTO statistic(title, charttype, startDate, endDate) VALUES('" + title +"', 'continuous', " + startDate.getTime() + ", " + endDate.getTime() + ")";
		String sql2 = "SELECT last_insert_rowid()";
		try (Connection conn = this.connect();
			Statement stmt = conn.createStatement())
		{
			stmt.executeUpdate(sql1);
			int id = 0;
			try (ResultSet rs = stmt.executeQuery(sql2)){
				if (rs.next()) {
					id = rs.getInt(1);
					result.setId(id);
					saveStatisticValues(id, values);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}


	private StatisticValues calculateContinuousStatisticValue(StatisticComponent comp, Date startDate, Date endDate) {
		List<Pair<Integer, Integer>> values = new ArrayList<Pair<Integer, Integer>>();
		Calendar c = Calendar.getInstance();
        c.setTime(endDate);
        c.add(Calendar.DATE, 1);
        Date nextEndDay = new Date(c.getTimeInMillis());
        int count = 0;
		for(Date date = startDate; date.before(nextEndDay);) {	
			ArrayList<Long> result = new ArrayList<Long>();
			for(Filter f: comp.getSelectedFilter()) {
				ArrayList<Long> partialResult = calculateFilterValue(comp.getSource(), f, date);
				partialResult.removeAll(result);
				result.addAll(partialResult);
			}
			if(comp.getSelectedFilter().isEmpty()) {
				Filter f = new Filter();
				result = calculateFilterValue(comp.getSource(), f, date);
			}
			int value;
			if (comp.getSource().equals("Terminlängen")) {
				long duration = 0;
				for (Long appointmentId: result) {
					Appointment app = getAppointment(appointmentId);
					long startTime = app.getStartTime();
					long endTime = app.getEndTime();
					duration += (endTime - startTime);
				}
				value = (int)(duration/1000/60);
			}else {
				value = result.size();
			}
			values.add(new Pair<Integer, Integer>(count, value));
			count++;
			
			//increment the date
	        c.setTime(date);
	        c.add(Calendar.DATE, 1);
	        date = new Date(c.getTimeInMillis());
		}
		return new StatisticValues(comp.getName(), values);
	}


	public Statistic calculateAndSaveNewIntervalStatistic(String title, ArrayList<StatisticComponent> statisticComponentsList,
			Date startDate, Date endDate, int step) {
		List<StatisticValues> values = new ArrayList<StatisticValues>();
		for (StatisticComponent comp: statisticComponentsList) {
			values.add(calculateIntervalStatisticValue(comp, startDate, endDate, step));
		}
		Statistic result = new IntervalStatistic(title, values, startDate, endDate, step);

		//save
		String sql1 = "INSERT INTO statistic(title, charttype, startDate, endDate, step) VALUES('" + title +"', 'interval', " + startDate.getTime() + ", " + endDate.getTime() + ", " + step + ")";
		String sql2 = "SELECT last_insert_rowid()";
		try (Connection conn = this.connect();
			Statement stmt = conn.createStatement())
		{
			stmt.executeUpdate(sql1);
			int id = 0;
			try (ResultSet rs = stmt.executeQuery(sql2)){
				if (rs.next()) {
					id = rs.getInt(1);
					result.setId(id);
					saveStatisticValues(id, values);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return result;
	}


	private StatisticValues calculateIntervalStatisticValue(StatisticComponent comp, Date startDate, Date endDate, int step) {
		List<Pair<Integer, Integer>> values = new ArrayList<Pair<Integer, Integer>>();
		int count = 0;
		for(Date intervStartDate = startDate; intervStartDate.before(endDate) || intervStartDate.equals(endDate);) {	
			//get End date of interval
			Calendar c = Calendar.getInstance();
	        c.setTime(intervStartDate);
	        c.add(Calendar.DATE, -1);
	        Date intervEndDate = new Date(c.getTimeInMillis());
			ArrayList<Long> result = new ArrayList<Long>();
			for(Filter f: comp.getSelectedFilter()) {
				//get result for one Filter
				ArrayList<Long> partialResult = calculateFilterValue(comp.getSource(), f, intervStartDate, intervEndDate);
				//remove all that are already in the result, than add the new ones to the result
				partialResult.removeAll(result);
				result.addAll(partialResult);
			}
			if(comp.getSelectedFilter().isEmpty()) {
				Filter f = new Filter();
				result = calculateFilterValue(comp.getSource(), f, intervStartDate, intervEndDate);
			}
			int value;
			if (comp.getSource().equals("Terminlängen")) {
				long duration = 0;
				for (Long appointmentId: result) {
					Appointment app = getAppointment(appointmentId);
					long startTime = app.getStartTime();
					long endTime = app.getEndTime();
					duration += (endTime - startTime);
				}
				value = (int)(duration/1000/60);
			}else {
				value = result.size();
			}
			values.add(new Pair<Integer, Integer>(count, value));
			count++;
			//increment the date
	        c.setTime(intervStartDate);
	        c.add(Calendar.DATE, step);
	        intervStartDate = new Date(c.getTimeInMillis());
		}
		return new StatisticValues(comp.getName(), values);
	}

	
	private ArrayList<Long> calculateFilterValue(String source, Filter f, Date date) {
		return calculateFilterValue(source, f, date, date);
	}
	private ArrayList<Long> calculateFilterValue(String source, Filter f, Date startDate, Date endDate) {
		ArrayList<Long> result = new ArrayList<Long>();
		Map<String, Object[]> filterMap = f.getFilters();
		try (Connection conn = this.connect();
				Statement stmt = conn.createStatement())
		{	
			//-----------------------student----------------------
			if(source.equals("Studenten")) {
				//create sql query
				String select = "SELECT matrNr";
				String from = " FROM student";
				String where = " WHERE created < " + startDate.getTime() + " AND (exmatr IS NULL OR exmatr > " + endDate.getTime() + ")";
				
				if (filterMap.containsKey("Geschlecht")){
					where += " AND gender = '" + filterMap.get("Geschlecht")[0] + "'";
				}
				if (filterMap.containsKey("Semester")) {
					where += " AND semester " + filterMap.get("Semester")[0] + " " + filterMap.get("Semester")[1];
				}
				if (filterMap.containsKey("PO")) {
					String sql = "SELECT id FROM po WHERE name = '" + filterMap.get("PO")[0] + "'";
					int poId = 0;
					try (ResultSet rs = stmt.executeQuery(sql)){
						if (rs.next()) {
							poId = rs.getInt("id");
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					where += " AND po = " + poId;
				}

				//Sql query
				String sql = select + from + where;
				ArrayList<Long> students = new ArrayList<Long>();
				try (ResultSet rs = stmt.executeQuery(sql)){
					while (rs.next()) {
						long id = rs.getLong("id");	
						students.add(id);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				//other filters
				if (filterMap.containsKey("ECTS")) {
					for (Long id: students) {
						Student stu = getStudent(id.intValue());
						int ECTS = calculateEcts(stu.getPassedSubjects(), stu.getPo());
						String op = (String)filterMap.get("ECTS")[0];
						if (op.equals(">")) {
							if (ECTS <= Integer.parseInt((String)filterMap.get("ECTS")[1])) {
								students.remove(id);
							}
						}else if (op.equals("=")) {
							if (ECTS != Integer.parseInt((String)filterMap.get("ECTS")[1])) {
								students.remove(id);
							}
						}else if (op.equals("<")) {
							if (ECTS >= Integer.parseInt((String)filterMap.get("ECTS")[1])) {
								students.remove(id);
							}
						}
					}
				}
				if (filterMap.containsKey("Betreuungszeit in h")) {
					for (Long id: students) {
						Student stu = getStudent(id.intValue());
						//time in milliseconds
						long betrZeit = 0;
						for (Long conId: stu.getConcernIds()) {
							Concern con = getConcern(conId);
							for (Appointment app: con.getAppointments()) {
								betrZeit += app.getDuration();
							}
						}
						//convert betrZeit to hours: /1000 -> seconds /60 -> minutes /60 -> hours
						betrZeit = betrZeit /1000 /60 /60;
						String op = (String)filterMap.get("Betreuungszeit in h")[0];
						if (op.equals(">")) {
							if (betrZeit <= Integer.parseInt((String)filterMap.get("Betreuungszeit in h")[1])) {
								students.remove(id);
							}
						}else if (op.equals("=")) {
							if (betrZeit != Integer.parseInt((String)filterMap.get("Betreuungszeit in h")[1])) {
								students.remove(id);
							}
						}else if (op.equals("<")) {
							if (betrZeit >= Integer.parseInt((String)filterMap.get("Betreuungszeit in h")[1])) {
								students.remove(id);
							}
						}
					}
				}
				if (filterMap.containsKey("Anzahl zugehöriger Anliegen")) {
					for (Long id: students) {
						Student stu = getStudent(id.intValue());
						int anzAnl = stu.getConcernIds().size();
						String op = (String)filterMap.get("Anzahl zugehöriger Anliegen")[0];
						if (op.equals(">")) {
							if (anzAnl <= Integer.parseInt((String)filterMap.get("Anzahl zugehöriger Anliegen")[1])) {
								students.remove(id);
							}
						}else if (op.equals("=")) {
							if (anzAnl != Integer.parseInt((String)filterMap.get("Anzahl zugehöriger Anliegen")[1])) {
								students.remove(id);
							}
						}else if (op.equals("<")) {
							if (anzAnl >= Integer.parseInt((String)filterMap.get("Anzahl zugehöriger Anliegen")[1])) {
								students.remove(id);
							}
						}
					}
				}
				if (filterMap.containsKey("Letzter Kontakt")) {
					for (Long id: students) {
						Student stu = getStudent(id.intValue());
						Date lastCon = getLastStudentEmail(stu).getDate();
						String op = (String)filterMap.get("Letzter Kontakt")[0];
						if (op.equals(">")) {
							if (!lastCon.after((Date)filterMap.get("Letzter Kontakt")[1])) {
								students.remove(id);
							}
						}else if (op.equals("=")) {
							if (!lastCon.equals((Date)filterMap.get("Letzter Kontakt")[1])) {
								students.remove(id);
							}
						}else if (op.equals("<")) {
							if (!lastCon.before((Date)filterMap.get("Letzter Kontakt")[1])) {
								students.remove(id);
							}
						}
					}
				}
				
				result = students;
				
				
				
			//-----------------------concern----------------------
			}else if(source.equals("Anliegen")){
				//create sql query
				String select = "SELECT id";
				String from = " FROM concern";
				String where = " WHERE created < " + startDate.getTime() + " AND (done IS NULL OR done > " + endDate.getTime() + ")";
			
				if (filterMap.containsKey("Thema")) {
					String sql = "SELECT id FROM topic WHERE title = '" + filterMap.get("Thema")[0] + "'";
					int topicId = 0;
					try (ResultSet rs = stmt.executeQuery(sql)){
						if (rs.next()) {
							topicId = rs.getInt("id");
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					where += " AND topic = " + topicId;
				}
				if (filterMap.containsKey("Status")) {
					if(filterMap.get("Status")[0].equals("Offen")) {
						where += " AND done IS NULL";
					}else if(filterMap.get("Status")[0].equals("Beendet")) {
						where += " AND done IS NOT NULL AND complete = true";
					}else {
						where += " AND done IS NOT NULL AND complete = false";
					}
				}
				
				
				//Sql query
				String sql = select + from + where;
				ArrayList<Long> concerns = new ArrayList<Long>();
				try (ResultSet rs = stmt.executeQuery(sql)){
					while (rs.next()) {
						long id = rs.getLong("id");	
						concerns.add(id);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				
				//other filters
				if (filterMap.containsKey("Anzahl der Termine")) {
					for (Long conId: concerns) {
						Concern con = getConcern(conId);
						int numApp = con.getAppointments().size();
						String op = (String)filterMap.get("Anzahl der Termine")[0];
						if (op.equals(">")) {
							if (numApp <= Integer.parseInt((String)filterMap.get("Anzahl der Termine")[1])) {
								concerns.remove(conId);
							}
						}else if (op.equals("=")) {
							if (numApp != Integer.parseInt((String)filterMap.get("Anzahl der Termine")[1])) {
								concerns.remove(conId);
							}
						}else if (op.equals("<")) {
							if (numApp >= Integer.parseInt((String)filterMap.get("Anzahl der Termine")[1])) {
								concerns.remove(conId);
							}
						}
					}
				}
				if (filterMap.containsKey("Gesamtlänge der Termine in h")) {
					for (Long conId: concerns) {
						//time in milliseconds
						long betrZeit = 0;
						Concern con = getConcern(conId);
						for (Appointment app: con.getAppointments()) {
							betrZeit += app.getDuration();
						}
						//convert betrZeit to hours: /1000 -> seconds /60 -> minutes /60 -> hours
						betrZeit = betrZeit /1000 /60 /60;
						String op = (String)filterMap.get("Gesamtlänge der Termine in h")[0];
						if (op.equals(">")) {
							if (betrZeit <= Integer.parseInt((String)filterMap.get("Gesamtlänge der Termine in h")[1])) {
								concerns.remove(conId);
							}
						}else if (op.equals("=")) {
							if (betrZeit != Integer.parseInt((String)filterMap.get("Gesamtlänge der Termine in h")[1])) {
								concerns.remove(conId);
							}
						}else if (op.equals("<")) {
							if (betrZeit >= Integer.parseInt((String)filterMap.get("Gesamtlänge der Termine in h")[1])) {
								concerns.remove(conId);
							}
						}
					}
				}
				if (filterMap.containsKey("Anzahl der Studenten")) {
					for (Long conId: concerns) {
						Concern con = getConcern(conId);
						int numStu = con.getStudents().size();
						String op = (String)filterMap.get("Anzahl der Termine")[0];
						if (op.equals(">")) {
							if (numStu <= Integer.parseInt((String)filterMap.get("Anzahl der Termine")[1])) {
								concerns.remove(conId);
							}
						}else if (op.equals("=")) {
							if (numStu != Integer.parseInt((String)filterMap.get("Anzahl der Termine")[1])) {
								concerns.remove(conId);
							}
						}else if (op.equals("<")) {
							if (numStu >= Integer.parseInt((String)filterMap.get("Anzahl der Termine")[1])) {
								concerns.remove(conId);
							}
						}
					}
				}
				
				result = concerns;
			//-----------------------Appointment length----------------------
			}else if(source.equals("Terminlängen")){
				String select = "SELECT startTime, endTime";
				String from = " FROM appointment";
				String where = " WHERE date >= " + startDate.getTime() + " AND date <= " + endDate.getTime();
				
				if (filterMap.containsKey("Thema")) {
					String sql = "SELECT id FROM topic WHERE title = '" + filterMap.get("Thema")[0] + "'";
					int topicId = 0;
					try (ResultSet rs = stmt.executeQuery(sql)){
						if (rs.next()) {
							topicId = rs.getInt("id");
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					from += ", concern";
					where += " AND concern = concern.id AND topic = " + topicId;
				}
				
				//Sql query
				String sql = select + from + where;
				ArrayList<Long> appointments = new ArrayList<Long>();
				try (ResultSet rs = stmt.executeQuery(sql)){
					while (rs.next()) {
						long id = rs.getLong("id");	
						appointments.add(id);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				result = appointments;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return result;
		
	}

	
	private void saveStatisticValues(int statId, List<StatisticValues> statValues) {
		for(StatisticValues statValue: statValues) {		
			String sql1 = "INSERT INTO statistic_component(statistic, name) VALUES(" + statId +", '" + statValue.getName() + "')";
			String sql2 = "SELECT last_insert_rowid()";
			try (Connection conn = this.connect();
				Statement stmt = conn.createStatement())
			{
				stmt.executeUpdate(sql1);
				int statCompId = 0;
				try (ResultSet rs = stmt.executeQuery(sql2)){
					if (rs.next()) {
						statCompId = rs.getInt(1);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				String sql3;
				for (Pair<Integer, Integer> value: statValue.getValues()) {
					sql3 = "INSERT INTO statistic_values(statCompId, value, valueNumber) VALUES("+ statCompId + ", " + value.getValue() + ", " + value.getKey() + ")";
					stmt.executeUpdate(sql3);
				}				
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}


	public void deleteStatistic(Statistic statisticToDelete) {
		String sql = "DELETE FROM statistic WHERE id = " + statisticToDelete.getId();
		try (Connection conn = this.connect();
				Statement stmt = conn.createStatement()) {
			stmt.executeUpdate(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	


	public void saveStudentImage(long id, Image img) {
		String sql = "UPDATE student SET image = ? WHERE id = " + id;
		try(ByteArrayOutputStream os = new ByteArrayOutputStream()){
			ImageIO.write(SwingFXUtils.fromFXImage(img, null),"png", os); 
			try(Connection conn = this.connect();
				PreparedStatement pstmt = conn.prepareStatement(sql);
				Statement stmt = conn.createStatement()){
				pstmt.setBytes(1, os.toByteArray());
				pstmt.executeUpdate();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public long saveNewStudent(Student student) {
		long studentId = 0;
		
		String sql = "INSERT INTO student(matrNr, name, firstName, semester, po, notes, gender, created) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		String sql2 = "SELECT last_insert_rowid()";
		try(Connection conn = this.connect();
				PreparedStatement pstmt = conn.prepareStatement(sql);
				Statement stmt = conn.createStatement())
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
			pstmt.setString(6, student.getNotes());
			pstmt.setString(7, student.getGender());
			pstmt.setDate(8, new Date(System.currentTimeMillis()));
			pstmt.executeUpdate();
			try (ResultSet rs = stmt.executeQuery(sql2)){
				if (rs.next()) {
					studentId = rs.getInt(1);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
				
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
		return studentId;
	}


	public void saveEditedStudent(Student student) {	
		
		String sql1 = "UPDATE student SET matrNr = ?, name = ?, firstname = ?, semester = ?, po = ?, gender = ? WHERE id = ?";
		String sql2 = "DELETE FROM concern_student WHERE student = " + student.getId();
		String sql3 = "DELETE FROM passed_subjects WHERE student = " + student.getId();
		
		Student oldStudent = getStudent(student.getId());
		//get MailAddresses to be added
		ArrayList<String> newMailAddresses = new ArrayList<String>(student.geteMailAddresses());
		newMailAddresses.removeAll(oldStudent.geteMailAddresses());
		//get MailAddresses to be deleted
		ArrayList<String> oldMailAddresses = oldStudent.geteMailAddresses();
		oldMailAddresses.removeAll(student.geteMailAddresses());
		try(Connection conn = this.connect();
				PreparedStatement pstmt = conn.prepareStatement(sql1);
				Statement stmt = conn.createStatement())
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
			pstmt.setString(6, student.getGender());
			pstmt.setLong(7, student.getId());
			pstmt.executeUpdate();
			
			stmt.executeUpdate(sql2);
			stmt.executeUpdate(sql3);
			for (String address: oldMailAddresses) {
				String sql = "DELETE FROM student_emailAddress WHERE emailAddress = '" + address +"'";
				stmt.executeUpdate(sql);
			}
			addStudentEmail(student.getId(), newMailAddresses);
			addPassedSubjects(student.getId(), student.getPassedSubjects());
			addConcernsStudent(student.getConcernIds(), student.getId());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}


	public void saveEditedStudentNotes(Student student, String notes) {
		String sql ="UPDATE student SET notes = '" + notes + "' WHERE id = " + student.getId();
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
		String sql = "SELECT * FROM student WHERE mtrNr = " + mtrNr;
		
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

	public Image getDefaultStudentImage() {
		try
		{
			return new Image(getClass().getClassLoader().getResourceAsStream("PicturePlaceholder.png"));
		}
		catch(Exception e)
		{
			return null;
		}

	}


	public void setStudentExmatr(Student s, Date d) {
		String sql = "UPDATE student SET exmatr = " + d + " WHERE id = " + s.getId();
		System.out.println(sql);
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

	public void deleteStudent(Student s) 
	{
		String sql = "DELETE FROM student WHERE id = "+ s.getId();
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


	private void addStudentEmail(long id, ArrayList<String> geteMailAddresses) {
		String sql;
		try (Connection conn = this.connect();
				Statement stmt = conn.createStatement())
			{
			for (String mail: geteMailAddresses){
				sql="INSERT INTO student_emailAddress (student, emailAddress) VALUES (" + id + ", '" + mail + "')";
				stmt.executeUpdate(sql);
			}
			}catch (Exception e) {
				e.printStackTrace();
			}
	}

	private void addPassedSubjects(long id, ObservableList<Subject> passedSubjects) {
		String sql;
		try (Connection conn = this.connect();
				Statement stmt = conn.createStatement())
			{
			for (Subject subject: passedSubjects){
				sql="INSERT INTO passed_subjects (student, subject) VALUES (" + id + ", " + subject.getId() + ")";
				stmt.executeUpdate(sql);
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
		String sql1 = "UPDATE topic SET title = '"  + topic.getTitle() + "' WHERE id = " + topic.getId();
		String sql2 = "DELETE FROM topic_forms WHERE topic = " + topic.getId();
		try (Connection conn = this.connect();
				Statement stmt = conn.createStatement())
			{
				stmt.executeUpdate(sql1);
				stmt.executeUpdate(sql2);
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
	
	public void checkMail(Student student) {
		//get Date of the last EMail Check, for filtering the eMails. If first Check, Date = minValue
		EMail lastEmail = getLastStudentEmail(student);
		Date lastEmailDate = null;
		if (lastEmail != null) {
			lastEmailDate = getLastStudentEmail(student).getDate();
		}
		if (lastEmailDate == null) {
			lastEmailDate = new Date(Long.MIN_VALUE);
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
				if (sendDate.after(lastEmailDate)) {
					// Filter eMails for the corresponding student
					ArrayList<String> fromAddresses = new ArrayList<String>();
					Address senders[] = msg.getFrom();
					for (Address address : senders) {
					    fromAddresses.add(((InternetAddress)address).getAddress().toLowerCase());
					}
					// If a MailAddress of sender and student are the same 
					String mailAddress = null;
					for (String address: student.geteMailAddresses()) {
						if (fromAddresses.contains(address.toLowerCase())){
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
				if (sendDate.after(lastEmailDate)) {
					ArrayList<String> toAddresses = new ArrayList<String>();
					Address[] recipients = msg.getAllRecipients();
					for (Address address : recipients) {
					    toAddresses.add(((InternetAddress)address).getAddress().toLowerCase());
					}
					String mailAddress = null;
					for (String address: student.geteMailAddresses()) {
						if (toAddresses.contains(address.toLowerCase())){
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
	
	//does the same as checkMail(Sudent) just for retrieving eMails from a specific eMailaddress before the last known eMail(last complete check)
	//checks beforehand if eMailAddress was already in database and just gets e-Mails after the last known Mail of that Address
	
	public void checkMail(Student student, String mailAddress) {
		EMail lastStudentEmail = getLastStudentEmail(student);
		EMail lastAddressEmail = getLastAddressEmail(mailAddress);
		Date lastStudentEmailDate = null;
		if (lastStudentEmail != null) {
			lastStudentEmailDate = lastStudentEmail.getDate();
		}
		Date lastAddressEmailDate = null;
		if (lastAddressEmail != null) {
			lastAddressEmailDate = lastAddressEmail.getDate();
		}
		try {
			Properties mailProps = new Properties();
			Session mailSession = Session.getDefaultInstance(mailProps);
			Store store = mailSession.getStore("imaps");
			store.connect("mail.fh-trier.de", options.getUserID(), options.getPassword());
			
			
			Folder inbox = store.getFolder("INBOX");
			inbox.open(Folder.READ_ONLY);
			for (int i = 0; i < inbox.getMessageCount(); i++) {
				Message msg = inbox.getMessage(i);
				Date sendDate = new Date(msg.getSentDate().getTime());
				if (sendDate.after(lastAddressEmailDate)) {
					if (sendDate.before(lastStudentEmailDate)) {
						ArrayList<String> fromAddresses = new ArrayList<String>();
						Address senders[] = msg.getFrom();
						for (Address address : senders) {
					    	fromAddresses.add(((InternetAddress)address).getAddress().toLowerCase());
						}
						if (fromAddresses.contains(mailAddress.toLowerCase())) {
							String subject = msg.getSubject();
							String content = getEmailContent(msg);
							EMail email = new EMail(content, subject, mailAddress, sendDate, true);
							saveMail(email);
						}					
					}else {
						break;
					}
				}
			}
			
			Folder outbox = store.getFolder("sent-mail");
			outbox.open(Folder.READ_ONLY);
			for (int i = 0; i < inbox.getMessageCount(); i++) {
				Message msg = outbox.getMessage(i);
				Date sendDate = new Date(msg.getSentDate().getTime());
				if (sendDate.after(lastAddressEmailDate)) {
					if (sendDate.before(lastStudentEmailDate)) {
						ArrayList<String> toAddresses = new ArrayList<String>();
						Address[] recipients = msg.getAllRecipients();
						for (Address address : recipients) {
							toAddresses.add(((InternetAddress)address).getAddress().toLowerCase());
						}
						if(toAddresses.contains(mailAddress.toLowerCase())){
							String subject = msg.getSubject();
							String content = getEmailContent(msg);
							EMail email = new EMail(content, subject, mailAddress, sendDate, false);
							saveMail(email);
						}
					}else {
						break;
					}
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
//	         int count = mp.getCount();
	         result = getEmailContent(mp.getBodyPart(0));
//	         for (int i = 0; i < count; i++)
//	            //result += getEmailContent(mp.getBodyPart(i));
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
	
	public EMail sendMail(String userID, String userName, String recipientName, String mailAddress, String subject, String content) {
		try {
			// Create a properties file containing
			// the host address of the SMTP server
			Properties mailProps = new Properties();
			mailProps.put("mail.smtp.host", "mail.fh-trier.de");
			
			// Create a session with the Java Mail API
			Session mailSession = Session.getInstance(mailProps);
			// Create a new mail message
			MimeMessage message = new MimeMessage(mailSession);
			// Set the From and the Recipient
			message.setFrom(new InternetAddress(userID + "@fh-trier.de", userName));
			message.setRecipient(Message.RecipientType.TO,
					new InternetAddress(mailAddress, recipientName));
			// Set the subject
			message.setSubject(subject);
			// Set the message text
			message.setText(content);
			// Save all the changes made to the message
			message.saveChanges();
			// Send the message
			Transport.send(message);

			// save E-mail to Database
			EMail email = new EMail(content, subject, mailAddress, new Date(System.currentTimeMillis()), false);
			saveMail(email);

			return email;
		}catch (SendFailedException e){
			Alert alert = new Alert(AlertType.INFORMATION);
	        alert.setTitle("Warnung");
	        alert.setHeaderText("VPN!");
	        alert.setContentText("Zum senden von E-Mails wird eine VPN-Verbindung zur Hochschule benötigt.");
	        alert.showAndWait();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
