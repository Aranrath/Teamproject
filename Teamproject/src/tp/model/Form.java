package tp.model;

import java.io.File;

public class Form {
	
	private String name;
	private File file;
	
	public Form (String name, String filePath)
	{
		try
		{
			file = new File(filePath);
		}
		catch(Exception e)
		{
			System.out.println("ERROR: Creating Form-File failed");
		}
		
		this.name = name;
		
	}

	public String getName() {
		return name;
	}

	public File getFile() {
		return file;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setFile(File file) {
		this.file = file;
	}
	
	
}
