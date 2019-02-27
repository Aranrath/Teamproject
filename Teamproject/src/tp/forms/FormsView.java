package tp.forms;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tp.Presenter;

public class FormsView extends GridPane{
	
	Presenter presenter;
	
	private Button addFormButton;
	private Button deleteFormButton;
	
	public FormsView(Presenter presenter)
	{
		this.presenter = presenter;
		buildView();
		fillView();
	}
	
	private void buildView()
	{
		addFormButton = new Button ("Formular hinzuf�gen");
		deleteFormButton = new Button ("Ausgew�hlte Formulare entfernen");
	}
	
	private void fillView()
	{
		addFormButton.setOnAction((event)-> {
			Stage stage = new Stage();
			stage.setAlwaysOnTop(true);
			stage.initModality(Modality.APPLICATION_MODAL);
	        stage.setTitle("Formular hinzuf�gen");
	        stage.setScene(new Scene(new FormEditView(stage, presenter), 450, 450));
	        stage.show();
		});
		deleteFormButton.setOnAction((event)-> {
			
		});
	}
	
	

}
