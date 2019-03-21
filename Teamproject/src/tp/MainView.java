package tp;

import java.util.ArrayList;
import java.util.Date;

import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import tp.appointment.WeekScheduleView;
import tp.concern.AllConcernsView;
import tp.concern.ConcernView;
import tp.forms.FormsView;
import tp.model.Appointment;
import tp.model.Concern;
import tp.model.MyTab;
import tp.model.Options;
import tp.model.Reminder;
import tp.model.Statistic;
import tp.model.Student;
import tp.options.EditUserDataView;
import tp.options.OptionsView;
import tp.reminders.NewReminderAlertView;
import tp.reminders.RemindersView;
import tp.statistics.AllStatisticsView;
import tp.statistics.EditStatisticView;
import tp.statistics.StatisticView;
import tp.students.AllStudentsView;
import tp.students.EditStudentView;
import tp.students.StudentView;

public class MainView extends BorderPane {

	private Presenter presenter;
	private ArrayList<String> sessionTabsIds;
	private ArrayList<Appointment> next24hourAppointments;
	
	//=======================================0
	
	private ToolBar leftToolBar;
	private ToolBar rightToolBar;
	private TabPane tabPane;


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
		Button allStatisticsButton = new Button("Alle Statistiken");
		Button newStatisticButton = new Button("Neue Statistik");
		Button remindersButton = new Button("Erinnerungen");
		
		//========================================================
		
		optionsButton.setMaxWidth(Double.MAX_VALUE);
		allConcernsButton.setMaxWidth(Double.MAX_VALUE);
		newConcernButton.setMaxWidth(Double.MAX_VALUE);
		allStudentensButton.setMaxWidth(Double.MAX_VALUE);
		newStudentButton.setMaxWidth(Double.MAX_VALUE);
		formsButton.setMaxWidth(Double.MAX_VALUE);
		allStatisticsButton.setMaxWidth(Double.MAX_VALUE);
		newStatisticButton.setMaxWidth(Double.MAX_VALUE);
		remindersButton.setMaxWidth(Double.MAX_VALUE);
		
		//========================================================

		optionsButton.setOnAction((event) -> {
			openOptionsTab();
		});
		allConcernsButton.setOnAction((event) -> {
			openAllConcernsTab();
		});
		newConcernButton.setOnAction((event) -> {
			openNewConcernTab();
		});
		allStudentensButton.setOnAction((event) -> {
			openAllStudentsTab();
		});
		newStudentButton.setOnAction((event) -> {
			openNewStudentTab();
		});
		formsButton.setOnAction((event) -> {
			openFormsTab();
		});
		allStatisticsButton.setOnAction((event) -> {
			openAllStatisticsTab();
		});
		newStatisticButton.setOnAction((event) -> {
			openNewStatisticTab();
		});
		remindersButton.setOnAction((event) -> {
			openRemindersTab();
		});

		// Toolbars
		leftToolBar = new ToolBar();
		leftToolBar.setOrientation(Orientation.VERTICAL);
		rightToolBar = new ToolBar();
		rightToolBar.setOrientation(Orientation.VERTICAL);

		leftToolBar.getItems().addAll(optionsButton, new Separator(), allConcernsButton, newConcernButton,
				new Separator(), allStudentensButton, newStudentButton, new Separator(), allStatisticsButton,
				newStatisticButton, new Separator(), formsButton, new Separator(), remindersButton);

		// -------------------Zusammenfügen---------------------------------------

		setLeft(leftToolBar);
		setRight(rightToolBar);

		tabPane = new TabPane();
		setCenter(tabPane);

		openSessionTabs();

	}


	public void updateRightToolBar() {
		rightToolBar.getItems().clear();
		//------------------------------------------------------------
		Button weeklyScheduleButton = new Button("Wochenkalendar");
		weeklyScheduleButton.setMaxWidth(Double.MAX_VALUE);
		weeklyScheduleButton.setOnAction((event) -> {
			openWeekScheduleTab();
		});
		Label nextAppointmentsLabel = new Label("Nächste Termine");
		nextAppointmentsLabel.setMaxWidth(Double.MAX_VALUE);;
		nextAppointmentsLabel.setAlignment(Pos.CENTER);
		rightToolBar.getItems().addAll(weeklyScheduleButton,nextAppointmentsLabel);
		// -----------------------------------------------------------
		this.next24hourAppointments = presenter.getNext24hourAppointments();
		if (next24hourAppointments != null) {
			for (Appointment a : next24hourAppointments) {
				Button newAppointmentButton = new Button(a.getStartTime() + " - " + a.getEndTime() + "\n"
						+ presenter.getConcern(a.getConcernId()).getTitle() + "\n" + a.getRoomNmb());
				newAppointmentButton.setOnAction((event) -> {
					openConcernTab(presenter.getConcern(a.getConcernId()));
				});
				rightToolBar.getItems().addAll(newAppointmentButton);
			}
		}
	}

	/**
	 * naming convention: s<mtrNr> = student, c<id> = concern, o = options, t<id> =
	 * statistic, i = all statistics, a = all students, l = all concerns, f = forms,
	 * w = weekly, r = all reminders, n<ew> = new(unsaved)ObjectViews //diese Tabs werden nicht gespeichert
	 */
	private void openSessionTabs() {
		if (sessionTabsIds == null) {
			sessionTabsIds = presenter.getSessionTabsIds();
		}
		if (sessionTabsIds != null) {
			for (String s : sessionTabsIds) {
				char firstLetter = s.charAt(0);

				if (firstLetter == 's') {
					openStudentTab(presenter.getStudent(Integer.parseInt(s.substring(1))));
				}
				else if (firstLetter == 'c') {
					openConcernTab(presenter.getConcern(Integer.parseInt(s.substring(1))));
				}
				else if (firstLetter == 'o') {
					openOptionsTab();
				}
				else if (firstLetter == 't') {
					openStatisticTab(presenter.getStatistic(Integer.parseInt(s.substring(1))));
				}
				else if (firstLetter == 'i') {
					openAllStatisticsTab();
				}
				else if (firstLetter == 'a') {
					openAllStudentsTab();
				}
				else if (firstLetter == 'l') {
					openAllConcernsTab();
				}
				else if (firstLetter == 'f') {
					openFormsTab();
				}
				else if (firstLetter == 'w') {
					openWeekScheduleTab();
				}
				else if (firstLetter == 'r') {
					openRemindersTab();
				}
			}
		}

	}

	private MyTab tabAlreadyOpen(String tabId) {

		for (int i = 0; i < tabPane.getTabs().size(); i++) {
			MyTab t = (MyTab) tabPane.getTabs().get(i);
			if (t.getTabId().equals(tabId)) {
				return t; // Tab already exists

			}
		}
		// Tab doesnt exist
		return null;
	}

	// ------------------------openingTabs-----------------------------------

	public void openAllStudentsTab() {
		MyTab newTab = tabAlreadyOpen("a");
		if (newTab == null) {

			newTab = new MyTab("a");

			newTab.setText("Alle Studenten");

			newTab.setContent(new AllStudentsView(presenter));

			tabPane.getTabs().addAll(newTab);
		}
		SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
		selectionModel.select(newTab);
	}

	public void openStudentTab(Student student) {
		MyTab newTab = tabAlreadyOpen("a" + student.getMtrNr());
		if (newTab == null) {
			newTab = new MyTab("a" + student.getMtrNr());

			newTab.setText(student.getName() + ", " + student.getFirstName());

			newTab.setContent(new StudentView(student, presenter));

			tabPane.getTabs().addAll(newTab);
		}
		SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
		selectionModel.select(newTab);

	}

	public void openOptionsTab() {
		MyTab newTab = tabAlreadyOpen("o");
		if (newTab == null) {
			newTab = new MyTab("o");

			newTab.setText("Optionen");

			newTab.setContent(new OptionsView(presenter));

			tabPane.getTabs().addAll(newTab);
		}
		SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
		selectionModel.select(newTab);

	}

	public void openWeekScheduleTab() {
		MyTab newTab = tabAlreadyOpen("w");
		if (newTab == null) {

			newTab = new MyTab("w");

			newTab.setText("Wochenplan");

			newTab.setContent(new WeekScheduleView(presenter));

			tabPane.getTabs().addAll(newTab);
		}

		SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
		selectionModel.select(newTab);
	}

	public void openFormsTab() {
		MyTab newTab = tabAlreadyOpen("f");
		if (newTab == null) {
			newTab = new MyTab("f");

			newTab.setText("Formulare");

			newTab.setContent(new FormsView(presenter));

			tabPane.getTabs().addAll(newTab);
		}
		SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
		selectionModel.select(newTab);

	}

	public void openAllConcernsTab() {
		MyTab newTab = tabAlreadyOpen("l");
		if (newTab == null) {
			newTab = new MyTab("l");

			newTab.setText("Alle Anliegen");

			newTab.setContent(new AllConcernsView(presenter));

			tabPane.getTabs().addAll(newTab);
		}
		SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
		selectionModel.select(newTab);

	}

	public void openStatisticTab(Statistic statistic) {
		MyTab newTab = tabAlreadyOpen("t" + statistic.getId());
		if (newTab == null) {
			newTab = new MyTab("t" + statistic.getId());

			newTab.setText(statistic.getTitle());

			newTab.setContent(new StatisticView(statistic));

			tabPane.getTabs().addAll(newTab);
		}
		SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
		selectionModel.select(newTab);

	}

	public void openAllStatisticsTab() {
		MyTab newTab = tabAlreadyOpen("i");
		if (newTab == null) {
			newTab = new MyTab("i");

			newTab.setText("Alle Statistiken");

			newTab.setContent(new AllStatisticsView(presenter));

			tabPane.getTabs().addAll(newTab);
		}
		SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
		selectionModel.select(newTab);

	}

	public void openConcernTab(Concern concern) {
		MyTab newTab = tabAlreadyOpen("c" + concern.getId());
		if (newTab == null) {
			newTab = new MyTab("c" + concern.getId());

			newTab.setText(concern.getTitle());

			newTab.setContent(new ConcernView(presenter, newTab, concern));

			tabPane.getTabs().addAll(newTab);
		}
		SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
		selectionModel.select(newTab);
	}
	
	public void openRemindersTab() {
		MyTab newTab = tabAlreadyOpen("r");
		if (newTab == null) {
			newTab = new MyTab("r");

			newTab.setText("Erinnerungen");

			newTab.setContent(new RemindersView(presenter));

			tabPane.getTabs().addAll(newTab);
		}
		SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
		selectionModel.select(newTab);
	}
	
	//--------------------Tabs für die Eingabe neuer(!!!) Objekte. Diese Tabs werden nicht gemerkt für die nächste Sitzung

	public void openNewConcernTab() {
		MyTab newTab = new MyTab("new");

		newTab.setText("Neues Anliegen");

		newTab.setContent(new ConcernView(presenter, newTab));

		tabPane.getTabs().addAll(newTab);

		SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
		selectionModel.select(newTab);

	}
	
	public void openNewConcernTab(ObservableList<Student> students) {
		MyTab newTab = new MyTab("new");

		newTab.setText("Neues Anliegen");

		newTab.setContent(new ConcernView(presenter, newTab, students));

		tabPane.getTabs().addAll(newTab);

		SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
		selectionModel.select(newTab);

	}

	public void openNewStudentTab() {
		MyTab newTab = new MyTab("new");

		newTab.setText("Neuer Student");

		newTab.setContent(new EditStudentView(presenter));

		tabPane.getTabs().addAll(newTab);

		SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
		selectionModel.select(newTab);

	}
	

	public void openNewStatisticTab() {
		MyTab newTab = new MyTab("new");

		newTab.setText("Neue Statistik");

		newTab.setContent(new EditStatisticView(presenter, newTab));

		tabPane.getTabs().addAll(newTab);

		SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
		selectionModel.select(newTab);
	}

	//=========================================================================00
	
	public void updateSubjectRelatedTabs() {
		// TODO Auto-generated method stub
		
	}

	public void showEditUserDataView(Options options) {
		Stage stage = new Stage();
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setAlwaysOnTop(true);
		stage.initStyle(StageStyle.UNDECORATED);
        stage.setTitle("Nutzerdaten");
        stage.setScene(new Scene(new EditUserDataView(presenter, stage, options), 450, 450));
        stage.show();
		
	}

	public void showNewReminderView(Options options) {
		ObservableList<Reminder> newReminders = presenter.getNewReminders(options.getLastReminderCheck());
		if(newReminders!= null)
		{
			Stage stage = new Stage();
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setAlwaysOnTop(true);
	        stage.setTitle("Neue Erinnerungen");
	        stage.setScene(new Scene(new NewReminderAlertView(presenter,stage, newReminders), 450, 450));
	        stage.show();
		}
		options.setLastReminderCheck(new Date());
		presenter.saveEditedOptions(options);
	}

	public void closeThisTab(MyTab tab) {
		tabPane.getTabs().remove(tab);
	}
	
	

}
