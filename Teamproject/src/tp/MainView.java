package tp;

import com.sun.xml.internal.ws.wsdl.writer.document.OpenAtts;

import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import tp.model.Appointment;
import tp.model.TabSession;

public class MainView extends BorderPane {

	private Presenter presenter;

	private ToolBar leftToolBar;
	private ToolBar rightToolBar;
	TabPane tabPane;
	private TabSession tabSession;
	private Appointment[] next24hourAppointments;

	public MainView(Presenter presenter) {
		this.presenter = presenter;
		this.tabSession = presenter.getTabSession();
		buildView();
		updateRightToolBar();
	}

	private void buildView() {

		// Buttons
		Button optionenButton = new Button("Optionen");
		Button alleAnliegenButton = new Button("Alle Anliegen");
		Button neuesAnliegenButton = new Button("Neues Anliegen");
		Button alleStudentenButton = new Button("Alle Studenten");
		Button neuerStudentButton = new Button("Neuer Student");
		Button formulareButton = new Button("Formulare");
		Button statistikenButton = new Button("Statistiken");

		// Toolbars
		leftToolBar = new ToolBar();
		leftToolBar.setOrientation(Orientation.VERTICAL);
		rightToolBar = new ToolBar();
		rightToolBar.setOrientation(Orientation.VERTICAL);

		leftToolBar.getItems().addAll(optionenButton, new Separator(), alleAnliegenButton, neuesAnliegenButton,
				new Separator(), alleStudentenButton, neuerStudentButton, new Separator(), formulareButton,
				statistikenButton);

		// -------------------Zusammenfügen---------------------------------------

		setLeft(leftToolBar);
		setRight(rightToolBar);

		tabPane = new TabPane();
		setCenter(tabPane);

		// -----------------TabSession in TabPane laden-------------------------------

		Tab tab1 = new Tab("Alle Studenten");
		Tab tab2 = new Tab("Max Mustermann");
		tabPane.getTabs().addAll(tab1, tab2);
		Pane pane1 = new Pane();
		Pane pane2 = new Pane();
		tab1.setContent(pane1);
		tab2.setContent(pane2);
		pane1.getChildren().addAll(new Button("hi"));

	}

	public void updateRightToolBar() {
		this.next24hourAppointments = presenter.getNext24hourAppointments();
		rightToolBar.getItems().clear();
		rightToolBar.getItems().addAll(new Label("Nächste Termine")); // TODO Zeiten hinzufügen: letzter
																		// Aktualisierungszeitpunkt
		for (Appointment a : next24hourAppointments) {
			Button newAppointmentButton = new Button(a.getConcern().getTitle());
			newAppointmentButton.setOnAction((event) -> {
				openConcernView(a.getConcern().getId());
			});
			rightToolBar.getItems().addAll(newAppointmentButton);
		}
	}

	private void openConcernView(int id) {
		//wenn Tab bereits existiert
		if(tabSession.existsTabAlready("c" + id))
		{
			SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
			
		}
		//wenn Tab noch nicht existiert
		else
		{
			
		}
	}

	private void openAllStudentView() {
		// TODO Auto-generated method stub

	}

	private void openStudentView(int mtrNr) {
		// TODO Auto-generated method stub

	}
	


}
