package tp;

import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import tp.model.Session;
import tp.model.UpdateTimer;

public class MainView extends BorderPane{
	
	Presenter presenter;
	
	ToolBar leftToolBar;
	ToolBar	rightToolBar;
	Session session;
	UpdateTimer updateTimer;

	public MainView(Presenter presenter) {
		this.presenter = presenter;
		this.session = presenter.getSession();
		buildView();
	}

	private void buildView() {
		
		//Buttons
		Button optionenButton = new Button ("Optionen");
		Button alleAnliegenButton = new Button ("Alle Anliegen");
		Button neuesAnliegenButton = new Button ("Neues Anliegen");
		Button alleStudentenButton = new Button ("Alle Studenten");
		Button neuerStudentButton = new Button ("Neuer Student");
		Button formulareButton = new Button ("Formulare");
		Button statistikenButton = new Button ("Statistiken");	
		
		//Toolbars
		leftToolBar = new ToolBar();
		leftToolBar.setOrientation(Orientation.VERTICAL);
		
		ToolBar rightToolBar = new ToolBar();
		rightToolBar.setOrientation(Orientation.VERTICAL);
		rightToolBar.getItems().addAll(new Label ("Nächste Termine"));
		leftToolBar.getItems().addAll(optionenButton, new Separator(), alleAnliegenButton, neuesAnliegenButton, new Separator(), alleStudentenButton, neuerStudentButton, new Separator(), formulareButton, statistikenButton );
		


		//Time aktualisierungszeitpunkt; //damit nach Termin seitenleiste aktualisiert
		//for every Termin des RESTTages (jetzt >= aktualisierungszeitpunkt)
		Button anliegenButton = new Button("Anliegen 1");
		rightToolBar.getItems().addAll(anliegenButton);
		
		
		setLeft(leftToolBar);
		setRight(rightToolBar);
		
		TabPane tabPane = new TabPane();
		setCenter(tabPane);
		Tab tab1 = new Tab("Alle Studenten");
		Tab tab2 = new Tab("Max Mustermann");
		tabPane.getTabs().addAll(tab1, tab2);
		Pane pane1 = new Pane();
		Pane pane2 = new Pane();
		tab1.setContent(pane1);
		tab2.setContent(pane2);
		pane1.getChildren().addAll(new Button("hi"));
		
	
	
		
	}

}
