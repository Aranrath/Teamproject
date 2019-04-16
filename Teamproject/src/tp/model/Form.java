package tp.model;

import java.io.File;

public class Form {
	
	private long id;
	private String name;
	private File file;
	
	public Form (String name, File file)
	{
		this.file = file;
		this.name = name;
	}
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
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

	@Override
	public String toString() {
		return name;
	}
	
	
}
