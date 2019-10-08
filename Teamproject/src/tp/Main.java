package tp;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import tp.model.Model;
import tp.model.Options;

public class Main extends Application {

	//MVP-Pattern Komponenten
	private Model model;
	private Presenter presenter;
	private MainView mainView;

	
	@Override
	public void start(Stage primaryStage) {
		try {
			
			generateMVP();
			
			primaryStage.setMaximized(true);

			primaryStage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("Icon.png")));
			
			Platform.setImplicitExit(false);

			primaryStage.setOnCloseRequest(e -> {
				e.consume();
				presenter.handleUnsavedTabs();
			});
	
			Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
			Scene scene = new Scene(mainView, primaryScreenBounds.getWidth()*0.9, primaryScreenBounds.getHeight()*0.9);
			
			primaryStage.setScene(scene);
			primaryStage.show();
			Options options = presenter.getOptions();
			
			
			
			if(options.getUserName() == null || options.getUserID() == null || options.getPassword()== null)
			{  
				presenter.showEditUserDataView(options);
	
			}
			else
			{
				presenter.showNewReminderView(options);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	
private void generateMVP()
	{
		model = new Model();
		presenter = new Presenter (model);
		mainView = new MainView (presenter);
		presenter.setMainView(mainView);
	}


	public static void main(String[] args) {
		
		//check if application is already running
		if(isApplicationAlreadyRunning()){
	        return;
	    } 
		
		launch(args);
	}

	
	/**
	 * Source:
	 * https://stackoverflow.com/questions/177189/how-to-implement-a-single-instance-java-application/36789247
	 */
	private static boolean isApplicationAlreadyRunning() {
	    try {
	        final File file = new File(Model.getStandardDirectory() + "InstanceLock.txt");
	        final RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
	        final FileLock fileLock = randomAccessFile.getChannel().tryLock();
	        if (fileLock != null) {
	            Runtime.getRuntime().addShutdownHook(new Thread() {
	                public void run() {
	                    try {
	                        fileLock.release();
	                        randomAccessFile.close();
	                        file.delete();
	                    } catch (Exception e) {
	                        //log.error("Unable to remove lock file: " + lockFile, e);
	                    }
	                }
	            });
	            return false;
	        }
	    } catch (Exception e) {
	       // log.error("Unable to create and/or lock file: " + lockFile, e);
	    }
	    return true;
	}
	
	
}




