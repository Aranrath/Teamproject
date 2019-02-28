package tp.model;

import java.io.File;

public class Form {
	
	private String name;
	private File file;
	
	public Form (String name, File file)
	{
		this.file = file;
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
