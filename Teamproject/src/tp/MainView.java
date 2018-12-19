package tp;

import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import tp.model.Appointment;
import tp.model.MyTab;
import tp.students.AllStudentsView;
import tp.students.StudentView;

public class MainView extends BorderPane {

	private Presenter presenter;

	private ToolBar leftToolBar;
	private ToolBar rightToolBar;
	private TabPane tabPane;
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
			openOptionsTab("o");
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
				openConcernTab("c" + a.getConcern().getId());
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
				openStudentTab(s);
			}
			if (firstLetter == 'c') {
				openConcernTab(s);
			}
			if (firstLetter == 'o') {
				openOptionsTab(s);
			}
			if (firstLetter == 't') {
				openStatisticTab(s);
			}
			if (firstLetter == 'i') {
				openAllStatisticsTab(s);
			}
			if (firstLetter == 'a') {
				openAllStudentsTab(s);
			}
			if (firstLetter == 'l') {
				openAllConcernsTab(s);
			}
			if (firstLetter == 'f') {
				openFormsTab(s);
			}
			if (firstLetter == 'w') {
				openWeeklyAppointmentSchedule(s);
			}
		}

	}
	
	private boolean switchIfTabAlreadyOpen(String tabId) {
		
		for(int i = 0; i< tabPane.getTabs().size(); i++)
		{
			MyTab t = (MyTab) tabPane.getTabs().get(i);
			//wenn: Tab gibt es schon
			if(t.getViewId().equals(tabId))
			{
				//switch zu dem
				SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
				selectionModel.select(t);
				return true;
				
			}
		}
		//Tab gibts noch nich
		return false;
	}
	


	//----------------------------------------------opening Tabs-----------------------------------

	
	private void openConcernTab(String tabId) {
		
		//Integer.parseInt(s.substring(1)
//		
		
		//Tab bereits offen?
		// wenn Tab bereits existiert
		//wechsel aktiven Tab dahin
		
		// //wenn Tab noch nicht existiert
		//Öffne neuen Tab (im Vordergrund)
	}

	private void openAllStudentsTab(String tabId) {
		if(!switchIfTabAlreadyOpen(tabId))
		{
			MyTab newTab = new MyTab("a");
			
			newTab.setContent(new AllStudentsView());
			
			tabPane.getTabs().addAll(newTab);
		}
	}
	

	private void openStudentTab(String tabId) {
		MyTab newTab = new MyTab("s");
		
		newTab.setContent(new StudentView(Integer.parseInt(tabId.substring(1)), presenter));
		
		tabPane.getTabs().addAll(newTab);

	}
	
	private void openOptionsTab(String tabId) {
		MyTab newTab = new MyTab("o");
		
		newTab.setContent(new AllStudentsView());
		
		tabPane.getTabs().addAll(newTab);
		
	}
	private void openWeeklyAppointmentSchedule(String tabId) {
		// TODO Auto-generated method stub
		
	}

	private void openFormsTab(String tabId) {
		// TODO Auto-generated method stub
		
	}

	private void openAllConcernsTab(String tabId) {
		// TODO Auto-generated method stub
		
	}

	private void openStatisticTab(String tabId) {
		//Integer.parseInt(s.substring(1)
		// TODO Auto-generated method stub
		
	}

	private void openAllStatisticsTab(String tabId) {
		// TODO Auto-generated method stub
		
	}

}
