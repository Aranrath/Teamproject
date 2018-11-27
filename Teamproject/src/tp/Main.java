package tp;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

	Model model;
	Presenter presenter;
	MainView mainView;

	
	@Override
	public void start(Stage primaryStage) {
		try {
			
			generateMVP();
			
			//TODO screen maxen
			Scene scene = new Scene(mainView, 1200, 800);
			
			primaryStage.setScene(scene);
			primaryStage.show();
		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	
	private void generateMVP() {
		
		model = new Model();
		presenter = new Presenter (model, mainView);
		mainView = new MainView (presenter);
		
	}


	public static void main(String[] args) {
		launch(args);
	}

}
