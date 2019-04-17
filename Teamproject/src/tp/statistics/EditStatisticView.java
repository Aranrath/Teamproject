package tp.statistics;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import tp.Presenter;
import tp.model.MyTab;
import tp.model.statistics.Statistic;

public class EditStatisticView extends GridPane{
	
	private Presenter presenter;
	private MyTab tab;
	
	//============================
	
	private Label titleLabel;
	private TextField titleTextField;
	private Button saveButton;
	private Label errorLabel;

	public EditStatisticView(Presenter presenter, MyTab tab) {
		this.presenter = presenter;
		this.tab = tab;
		buildView();
	}

	private void buildView() {
		setPadding(new Insets(20));
		setHgap(20);
		setVgap(20);
		
		titleLabel = new Label("Titel");
		titleTextField = new TextField("");
		saveButton = new Button("Speichern");
		errorLabel = new Label("Alle Felder müssen ausgefüllt sein");
		errorLabel.setVisible(false);
		
		//=====================================
		
		add(titleLabel,0,0);
		add(titleTextField,1,0);
		add(saveButton,1,1);
		GridPane.setHalignment(saveButton, HPos.RIGHT);
		add(errorLabel,1,2);
		GridPane.setHalignment(errorLabel, HPos.RIGHT);
		
		
		//====================================
		
		saveButton.setOnAction(event -> {
			String title = titleTextField.getText();
			errorLabel.setVisible(true);
			if(title.equals(""))
			{
				errorLabel.setText("Alle Felder müssen ausgefüllt sein");
				errorLabel.setTextFill(Color.RED);
			}
			else
			{
				Statistic newStatistic = new Statistic(title);
				presenter.saveNewStatistic(newStatistic);
				presenter.openStatisticTab(newStatistic);
				presenter.closeThisTab(tab);
			}
			
		});
		
	}

}
