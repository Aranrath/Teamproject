package tp;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import tp.model.Model;
import tp.model.Options;

public class Main extends Application {

	private Model model;
	private Presenter presenter;
	private MainView mainView;

	
	
	@Override
	public void start(Stage primaryStage) {
		try {
			
			generateMVP();
			
			primaryStage.setMaximized(true);
			
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
	
	
private void generateMVP() {
		
		model = new Model();
		presenter = new Presenter (model);
		mainView = new MainView (presenter);
		presenter.setMainView(mainView);
		
	}


	public static void main(String[] args) {
		launch(args);
	}

}
