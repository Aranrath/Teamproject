package tp.students;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import tp.Presenter;
import tp.model.Options;
import tp.model.Student;

public class TakeImageView extends GridPane{
	
	private Presenter presenter;
	private Stage stage;
	private Options options;
	private Image image;
	private EditStudentView editStudentParentView;
	private StudentView studentParentView;
	
	
	//======================
	
	private ImageView fotoImageView;
	private Button connectionButton;
	private Label ipLabel;
	private TextField ipTextField;
	private Button saveImageButton;
	
	
	public TakeImageView(Stage stage, Presenter presenter, EditStudentView parentView)
	{
		this.presenter = presenter;
		this.stage = stage;
		this.editStudentParentView = parentView;
		buildView();
		
	}
	
	public TakeImageView(Stage stage, Presenter presenter, StudentView parentView)
	{
		this.presenter = presenter;
		this.stage = stage;
		this.studentParentView = parentView;
		buildView();
	}

	private void buildView() {
		
		setPadding(new Insets(10, 10, 10, 10));
		setHgap(10);
		setVgap(10);
		
		fotoImageView = new ImageView();
		//TODO standartBild einfügen
//		fotoImageView.setImage(standartimage);
		fotoImageView.setFitWidth(300);
		fotoImageView.setPreserveRatio(true);
		fotoImageView.setSmooth(true);
		fotoImageView.setCache(true);
		
		ipLabel = new Label("IP des Mobilgerätes");
		connectionButton = new Button ("Mit wartendem " + "\n" + "Mobilgerät verbinden");
		ipTextField = new TextField();
		options = presenter.getOptions();
		if(options!=null && options.getLastUsedIP()!= null)
		{
			ipTextField.setText(options.getLastUsedIP());
		}
		saveImageButton = new Button("Bild speichern");
		
		//======================================
		
		add(fotoImageView,0,0,2,1);
		add(ipLabel, 0,1);
		GridPane.setHalignment(ipLabel, HPos.LEFT);
		add(ipTextField,0,2);
		GridPane.setHalignment(ipTextField, HPos.LEFT);
		add(connectionButton, 1,1,1,2);
		GridPane.setValignment(connectionButton, VPos.BOTTOM);
		add(saveImageButton,0,3,2,1);
		GridPane.setHalignment(saveImageButton, HPos.CENTER);
		
		connectionButton.setOnAction((event)-> {
			connectToApp();
		});
		
		saveImageButton.setOnAction((event)->{
			//TODO nur wenn nicht standartImage?
//			presenter.saveImageToStudent(fotoImageView.getImage(), student);
			//TODO update Student View, von dem dies gestartet wurde
			
			if(editStudentParentView != null)
			{
				editStudentParentView.updateImage(image);
				
			}
			else if(studentParentView!= null)
			{
				studentParentView.updateImage(image);
			}
			stage.close();
		});
		
	}
	
	//connect to the App, get the Picture and load it into the Image View
	private void connectToApp() {
		String serverIp = ipTextField.getText();
		int serverPort = 8080;
		try {
			InetAddress serverAddress = InetAddress.getByName(serverIp);
			try (Socket socket = new Socket(serverAddress, serverPort)) {

				//Get The Picture from InputStream
				InputStream istream = new BufferedInputStream(socket.getInputStream());
				image = new Image(istream);				
				//show image in ImageView
				fotoImageView.setImage(image);
				
				//save last used IP in options 
				options.setLastUsedIP(serverIp);
				

			} catch (Exception e) {
				ipTextField.setText("Ip Addresse inkorrekt");
				return;
			}
		} catch (UnknownHostException e1) {
			ipTextField.setText("Ip Addresse ungültig");
			return;
		}
	}

}
