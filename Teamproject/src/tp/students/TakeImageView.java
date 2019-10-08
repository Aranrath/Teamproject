package tp.students;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import javafx.event.EventHandler;
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
import javafx.stage.WindowEvent;
import tp.Presenter;
import tp.model.Options;
import tp.model.Student;

public class TakeImageView extends GridPane {

	private Presenter presenter;
	private Stage stage;
	private Options options;
	private Image image;
	private EditStudentView editStudentParentView;
	private StudentView studentParentView;
	private Student student;
	private ServerSocket serverSocket;

	// ======================

	private ImageView fotoImageView;
	private Button connectionButton;
	private Label ipLabel;
	private TextField ipTextField;
	private Button saveImageButton;

	public TakeImageView(Stage stage, Presenter presenter, EditStudentView parentView) {
		this.presenter = presenter;
		this.stage = stage;
		this.editStudentParentView = parentView;
		buildView();
	}

	public TakeImageView(Stage stage, Presenter presenter, StudentView parentView, Student student) {
		this.presenter = presenter;
		this.stage = stage;
		this.studentParentView = parentView;
		this.student = student;
		buildView();
	}

	private void buildView() {

		setPadding(new Insets(10, 10, 10, 10));
		setHgap(10);
		setVgap(10);

		fotoImageView = new ImageView();
		fotoImageView.setImage(presenter.getDefaultStudentImage());
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

		// ======================================

		add(fotoImageView, 0, 0, 2, 1);
		add(ipLabel, 0, 1);
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

		saveImageButton.setOnAction((event) -> {
			if (editStudentParentView != null) {
				editStudentParentView.updateImage(image);

			} else if (studentParentView != null) {
				studentParentView.updateImage(image);
				student.setImage(image);
				presenter.saveEditedStudent(student);
			}
			stage.close();
		});

		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
		      public void handle(WindowEvent we) {
		          if(serverSocket!= null) {
						try {
							serverSocket.close();
						} catch (IOException e) {}
					}
		      }
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
