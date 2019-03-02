package tp.students;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import tp.Presenter;
import tp.model.Options;

public class TakeImageView extends GridPane{
	
	private Presenter presenter;
	private Stage stage;
	Options options;
	
	//======================
	
	private ImageView fotoImageView;
	private Button connectionButton;
	private Label ipLabel;
	private TextField ipTextField;
	private Button saveImageButton;
	
	public TakeImageView(Stage stage, Presenter presenter)
	{
		this.presenter = presenter;
		this.stage = stage;
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
			String connectionId = ipTextField.getText();
			if(!connectionId.equals(""))
			{
				//TODO Karen
			}
		});
		
		saveImageButton.setOnAction((event)->{
			//TODO
			
			stage.close();
		});
		
	}

}
