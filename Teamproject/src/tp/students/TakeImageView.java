package tp.students;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import tp.Presenter;

public class TakeImageView extends GridPane {

	private Presenter presenter;
	private Stage stage;
	private Image image;
	private EditStudentView editStudentParentView;
	private StudentView studentParentView;
	private boolean serverOpen;
	private ServerSocket serverSocket;

	// ======================

	private ImageView fotoImageView;
	private Button ServerButton;
	private Label ipLabel;
	private Label currentIpLabel;
	private Button saveImageButton;

	public TakeImageView(Stage stage, Presenter presenter, EditStudentView parentView) {
		this.presenter = presenter;
		this.stage = stage;
		this.editStudentParentView = parentView;
		serverOpen = false;
		buildView();

	}

	public TakeImageView(Stage stage, Presenter presenter, StudentView parentView) {
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
		fotoImageView.setImage(presenter.getDefaultStudentImage());
		fotoImageView.setFitWidth(300);
		fotoImageView.setPreserveRatio(true);
		fotoImageView.setSmooth(true);
		fotoImageView.setCache(true);

		ipLabel = new Label("IP des Gerätes: ");
		ServerButton = new Button("Verbindung öffnen.");
		currentIpLabel = new Label();
		saveImageButton = new Button("Bild speichern");

		// ======================================

		add(fotoImageView, 0, 0, 2, 1);
		add(ipLabel, 0, 1);
		GridPane.setHalignment(ipLabel, HPos.LEFT);
		add(currentIpLabel, 1, 1);
		GridPane.setHalignment(currentIpLabel, HPos.LEFT);
		add(ServerButton, 1, 2, 1, 2);
		GridPane.setValignment(ServerButton, VPos.BOTTOM);
		add(saveImageButton, 0, 4, 2, 1);
		GridPane.setHalignment(saveImageButton, HPos.CENTER);

		ServerButton.setOnAction((event) -> {
			startServer();
		});

		saveImageButton.setOnAction((event) -> {
			if (editStudentParentView != null) {
				editStudentParentView.updateImage(image);

			} else if (studentParentView != null) {
				studentParentView.updateImage(image);
			}
			stage.close();
		});

	}

	// connect to the App, get the Picture and load it into the Image View
	private void startServer() {
		// String serverIp = currentIpLabel.getText();
		if (serverOpen) {
			try {
				serverSocket.close();
			} catch (IOException e) {}
			serverOpen = false;
			ServerButton.setText("Verbindung öffnen");
			currentIpLabel.setText("");
		} else {
			serverOpen = true;
			ServerButton.setText("Verbindung schließen");
			Thread serverThread = new Thread(new ServerThread());
			serverThread.start();
		}
	}

	private class ServerThread implements Runnable {

		public void run() {
			try {
				serverSocket = new ServerSocket(8080);
				 try {
				        InetAddress iAddress = InetAddress.getLocalHost();
				        String server_IP = iAddress.getHostAddress();
						Platform.runLater(() -> currentIpLabel.setText(server_IP));
				 } catch (UnknownHostException e) {}

				while (!serverSocket.isClosed()) {
					try (Socket socket = serverSocket.accept()) {
						System.out.println("I'm here");
						// Get The Picture from InputStream
						InputStream istream = new BufferedInputStream(socket.getInputStream());
						image = new Image(istream);
						// show image in ImageView
						fotoImageView.setImage(image);
					} catch (IOException e) {}
				}
			} catch (IOException e1) {
			} finally {
				if(serverSocket!= null) {
					try {
						serverSocket.close();
					} catch (IOException e) {}
				}
			}
		}
	}

}
