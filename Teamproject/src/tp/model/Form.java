package tp.model;

import java.io.File;

import javafx.scene.image.Image;

public class Form {
	
	private long id;
	private String name;
	private File file;
	private String fileExtension;
	
	//------- for new Forms, extension not needed
	public Form (String name, File file)
	{
		this.file = file;
		this.name = name;		
	}  
	
	//------- for previews of Files extension needed to identify wich type
	public Form (String name, File file, String fileExtension)
	{
		this.file = file;
		this.name = name;
		this.fileExtension = fileExtension;
		
	}    
    
  //================================================public Methods
	//Generic Getter/Setter
    
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

	
	//---------------------------------------
    
	
	public Image getImage()
	{
		try
		{
			return new Image(file.toURI().toString());
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
	
	public boolean formIsImage()
	{
		fileExtension.toLowerCase();
		
		if(fileExtension.equals(".bmp") || fileExtension.equals(".gif") || fileExtension.equals(".png")|| fileExtension.equals(".jpeg")|| fileExtension.equals(".jpg"))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public boolean formIsPdf()
	{
		fileExtension.toLowerCase();
		if(fileExtension.equals(".pdf"))
		{
			return true;
		}
		else {
			return false;
		}
	}
	
	
}
