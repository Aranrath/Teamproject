package tp;

import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TabPane;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import tp.model.Appointment;
import tp.model.MyTab;
import tp.students.AllStudentsView;

public class MainView extends BorderPane {

	private Presenter presenter;

	private ToolBar leftToolBar;
	private ToolBar rightToolBar;
	TabPane tabPane;
	private String[] sessionTabsIds;
	private Appointment[] next24hourAppointments;

	public MainView(Presenter presenter) {
		this.presenter = presenter;
		this.sessionTabsIds = presenter.getSessionTabsIds();
		buildView();
		updateRightToolBar();
	}

	private void buildView() {

		// Buttons
		Button optionsButton = new Button("Optionen");
		Button allConcernsButton = new Button("Alle Anliegen");
		Button newConcernButton = new Button("Neues Anliegen");
		Button allStudentensButton = new Button("Alle Studenten");
		Button newStudentButton = new Button("Neuer Student");
		Button formsButton = new Button("Formulare");
		Button allStatisticsButton = new Button("Statistiken");
		Button newStatisticButton = new Button("Neue Statistik");
		
		optionsButton.setOnAction((event)-> {
			openOptionsTab();
		});

		// Toolbars
		leftToolBar = new ToolBar();
		leftToolBar.setOrientation(Orientation.VERTICAL);
		rightToolBar = new ToolBar();
		rightToolBar.setOrientation(Orientation.VERTICAL);

		leftToolBar.getItems().addAll(optionsButton, new Separator(), allConcernsButton, newConcernButton,
				new Separator(), allStudentensButton, newStudentButton, new Separator(), 
				allStatisticsButton, newStatisticButton, new Separator(), formsButton);

		// -------------------Zusammenfügen---------------------------------------

		setLeft(leftToolBar);
		setRight(rightToolBar);

		tabPane = new TabPane();
		setCenter(tabPane);

		openSessionTabs();
		

	}

	public void updateRightToolBar() {
		this.next24hourAppointments = presenter.getNext24hourAppointments();
		rightToolBar.getItems().clear();
		rightToolBar.getItems().addAll(new Label("Nächste Termine")); // TODO Zeiten hinzufügen: letzter
																		// Aktualisierungszeitpunkt
		for (Appointment a : next24hourAppointments) {
			Button newAppointmentButton = new Button(a.getConcern().getTitle());
			newAppointmentButton.setOnAction((event) -> {
				openConcernTab(a.getConcern().getId());
			});
			rightToolBar.getItems().addAll(newAppointmentButton);
		}
	}

	/**
	 * Prefixes: s = student, c = concern, o = options, t = statistic, i = all
	 * statistics, a = all students, l = all concerns, f = forms, w = weekly
	 * appointment Schedule
	 */
	private void openSessionTabs() {
		if (sessionTabsIds == null) {
			sessionTabsIds = presenter.getSessionTabsIds();
		}
		for (String s : sessionTabsIds) {
			char firstLetter = s.charAt(0);

			if (firstLetter == 's') {
				openStudentTab(Integer.parseInt(s.substring(1)));
			}
			if (firstLetter == 'c') {
				openConcernTab(Integer.parseInt(s.substring(1)));
			}
			if (firstLetter == 'o') {
				openOptionsTab();
			}
			if (firstLetter == 't') {
				openStatisticTab(Integer.parseInt(s.substring(1)));
			}
			if (firstLetter == 'i') {
				openAllStatisticsTab();
			}
			if (firstLetter == 'a') {
				openAllStudentsTab();
			}
			if (firstLetter == 'l') {
				openAllConcernsTab();
			}
			if (firstLetter == 'f') {
				openFormsTab();
			}
			if (firstLetter == 'w') {
				opemWeeklyAppointmentSchedule();
			}
		}

	}
	


	//----------------------------------------------opening Tabs-----------------------------------

	
	private void openConcernTab(int id) {
//		SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
		
		//Tab bereits offen?
		// wenn Tab bereits existiert
		//wechsel aktiven Tab dahin
		
		// //wenn Tab noch nicht existiert
		//Öffne neuen Tab (im Vordergrund)
	}

	private void openAllStudentsTab() {
		MyTab newTab = new MyTab("a");
		
		newTab.setContent(new AllStudentsView());
		
		tabPane.getTabs().addAll(newTab);
	}

	private void openStudentTab(int mtrNr) {
		// TODO Auto-generated method stub

	}
	
	private void openOptionsTab() {
		// TODO Auto-generated method stub
		
	}
	private void opemWeeklyAppointmentSchedule() {
		// TODO Auto-generated method stub
		
	}

	private void openFormsTab() {
		// TODO Auto-generated method stub
		
	}

	private void openAllConcernsTab() {
		// TODO Auto-generated method stub
		
	}

	private void openStatisticTab(int parseInt) {
		// TODO Auto-generated method stub
		
	}

	private void openAllStatisticsTab() {
		// TODO Auto-generated method stub
		
	}

}
