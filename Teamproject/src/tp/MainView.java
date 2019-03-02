package tp;

import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import tp.appointment.WeekScheduleView;
import tp.concern.AllConcernsView;
import tp.concern.ConcernView;
import tp.concern.EditConcernView;
import tp.forms.FormsView;
import tp.model.Appointment;
import tp.model.Concern;
import tp.model.MyTab;
import tp.model.Statistic;
import tp.model.Student;
import tp.options.OptionsView;
import tp.statistics.AllStatisticsView;
import tp.statistics.EditStatisticView;
import tp.statistics.StatisticView;
import tp.students.AllStudentsView;
import tp.students.EditStudentView;
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
		Button allStatisticsButton = new Button("Alle Statistiken");
		Button newStatisticButton = new Button("Neue Statistik");
		
		//========================================================
		
		optionsButton.setMaxWidth(Double.MAX_VALUE);
		allConcernsButton.setMaxWidth(Double.MAX_VALUE);
		newConcernButton.setMaxWidth(Double.MAX_VALUE);
		allStudentensButton.setMaxWidth(Double.MAX_VALUE);
		newStudentButton.setMaxWidth(Double.MAX_VALUE);
		formsButton.setMaxWidth(Double.MAX_VALUE);
		allStatisticsButton.setMaxWidth(Double.MAX_VALUE);
		newStatisticButton.setMaxWidth(Double.MAX_VALUE);
		
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

		// Toolbars
		leftToolBar = new ToolBar();
		leftToolBar.setOrientation(Orientation.VERTICAL);
		rightToolBar = new ToolBar();
		rightToolBar.setOrientation(Orientation.VERTICAL);

		leftToolBar.getItems().addAll(optionsButton, new Separator(), allConcernsButton, newConcernButton,
				new Separator(), allStudentensButton, newStudentButton, new Separator(), allStatisticsButton,
				newStatisticButton, new Separator(), formsButton);

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
						+ a.getConcern().getTitle() + "\n" + a.getRoomNmb());
				newAppointmentButton.setOnAction((event) -> {
					openConcernTab(a.getConcern());
				});
				rightToolBar.getItems().addAll(newAppointmentButton);
			}
		}
	}

	/**
	 * naming convention: s<mtrNr> = student, c<id> = concern, o = options, t<id> =
	 * statistic, i = all statistics, a = all students, l = all concerns, f = forms,
	 * w = weekly, n<ew> = new(unsaved) appointment Schedule
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
				if (firstLetter == 'c') {
					openConcernTab(presenter.getConcern(Integer.parseInt(s.substring(1))));
				}
				if (firstLetter == 'o') {
					openOptionsTab();
				}
				if (firstLetter == 't') {
					openStatisticTab(presenter.getStatistic(Integer.parseInt(s.substring(1))));
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
					openWeekScheduleTab();
				}
			}
		}

	}

	private MyTab tabAlreadyOpen(String tabId) {

		for (int i = 0; i < tabPane.getTabs().size(); i++) {
			MyTab t = (MyTab) tabPane.getTabs().get(i);
			if (t.getViewId().equals(tabId)) {
				return t; // Tab already exists

			}
		}
		// Tab doesnt exist
		return null;
	}

	// ------------------------openingTabs-----------------------------------

	private void openAllStudentsTab() {
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

	private void openStudentTab(Student student) {
		MyTab newTab = tabAlreadyOpen("a" + student.getMtrNr());
		if (newTab == null) {
			newTab = new MyTab("a" + student.getMtrNr());

			newTab.setText(student.getName() + ", " + student.getFirstName());

			newTab.setContent(new StudentView(student));

			tabPane.getTabs().addAll(newTab);
		}
		SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
		selectionModel.select(newTab);

	}

	private void openOptionsTab() {
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

	private void openWeekScheduleTab() {
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

	private void openFormsTab() {
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

	private void openAllConcernsTab() {
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

	private void openStatisticTab(Statistic statistic) {
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

	private void openAllStatisticsTab() {
		MyTab newTab = tabAlreadyOpen("i");
		if (newTab == null) {
			newTab = new MyTab("i");

			newTab.setText("Alle Statistiken");

			newTab.setContent(new AllStatisticsView());

			tabPane.getTabs().addAll(newTab);
		}
		SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
		selectionModel.select(newTab);

	}

	private void openConcernTab(Concern concern) {
		MyTab newTab = tabAlreadyOpen("c" + concern.getId());
		if (newTab == null) {
			newTab = new MyTab("c" + concern.getId());

			newTab.setText(concern.getTitle());

			newTab.setContent(new ConcernView(concern));

			tabPane.getTabs().addAll(newTab);
		}
		SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
		selectionModel.select(newTab);

	}

	public void openNewConcernTab() {
		MyTab newTab = new MyTab("new");

		newTab.setText("Neues Anliegen");

		newTab.setContent(new EditConcernView());

		tabPane.getTabs().addAll(newTab);

		SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
		selectionModel.select(newTab);

	}
	
	public void openNewConcernTab(ObservableList<Student> students) {
		MyTab newTab = new MyTab("new");

		newTab.setText("Neues Anliegen");

		newTab.setContent(new EditConcernView(students));

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
	

	private void openNewStatisticTab() {
		MyTab newTab = new MyTab("new");

		newTab.setText("Neue Statistik");

		newTab.setContent(new EditStatisticView());

		tabPane.getTabs().addAll(newTab);

		SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
		selectionModel.select(newTab);
	}

	public void updateSubjectRelatedTabs() {
		// TODO Auto-generated method stub
		
	}

}
