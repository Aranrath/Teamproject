package tp;

import java.sql.Date;
import java.util.ArrayList;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import tp.appointment.WeekScheduleView;
import tp.concern.AllConcernsView;
import tp.concern.ConcernView;
import tp.forms.FormsView;
import tp.model.Appointment;
import tp.model.Concern;
import tp.model.DraggingTabPaneSupport;
import tp.model.MyTab;
import tp.model.Options;
import tp.model.Reminder;
import tp.model.Student;
import tp.model.statistics.Statistic;
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
	
	public static final String TOOLBAR_COLOR = Color.LIGHTBLUE.toString();
	public static final String BUTTON_COLOR = "#FFFFFF";
	
	//=======================================
	
	private ToolBar leftToolBar;
	private ToolBar rightToolBar;
	private Timeline rightToolBarUpdater;
	private TabPane tabPane;
	
	//======================================
	
	private Button optionsButton;
	private Button allConcernsButton;
	private Button newConcernButton;
	private Button allStudentensButton;
	private Button newStudentButton;
	private Button formsButton;
	private Button allStatisticsButton;
	private Button newStatisticButton;
	private Button remindersButton;
	private Button weeklyScheduleButton;
	private Label nextAppointmentsLabel;


	public MainView(Presenter presenter) {
		this.presenter = presenter;
		this.sessionTabsIds = presenter.getSessionTabsIds();
		buildView();
		updateRightToolBar();
		
	}

	private void buildView() {

		// Buttons
		optionsButton = new Button("Optionen");
		allConcernsButton = new Button("Alle Anliegen");
		newConcernButton = new Button("Neues Anliegen");
		allStudentensButton = new Button("Alle Studenten");
		newStudentButton = new Button("Neuer Student");
		formsButton = new Button("Formulare");
		allStatisticsButton = new Button("Alle Statistiken");
		newStatisticButton = new Button("Neue Statistik");
		remindersButton = new Button("Erinnerungen");
		
		
		//-------------------------------------------------------
		//rightToolbar
		
		weeklyScheduleButton = new Button("Wochenkalendar");
		weeklyScheduleButton.setMaxWidth(Double.MAX_VALUE);
		weeklyScheduleButton.setStyle("-fx-base: #ee2211");
		
		 nextAppointmentsLabel = new Label("Nächste Termine");
		 nextAppointmentsLabel.setMaxWidth(Double.MAX_VALUE);;
		nextAppointmentsLabel.setAlignment(Pos.CENTER);
		
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
		
		
		//----------------------------------------------------
		
		optionsButton.setStyle("-fx-base: "+ BUTTON_COLOR);
		allConcernsButton.setStyle("-fx-base: "+ BUTTON_COLOR);
		newConcernButton.setStyle("-fx-base: "+ BUTTON_COLOR);
		allStudentensButton.setStyle("-fx-base: "+ BUTTON_COLOR);
		newStudentButton.setStyle("-fx-base: "+ BUTTON_COLOR);
		formsButton.setStyle("-fx-base: "+ BUTTON_COLOR);
		allStatisticsButton.setStyle("-fx-base: "+ BUTTON_COLOR);
		newStatisticButton.setStyle("-fx-base: "+ BUTTON_COLOR);
		remindersButton.setStyle("-fx-base: "+ BUTTON_COLOR);
		
		
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
		
		weeklyScheduleButton.setOnAction((event) -> {
			openWeekScheduleTab();
		});

		//===========================================================
		// Toolbars
		
		leftToolBar = new ToolBar();
		leftToolBar.setOrientation(Orientation.VERTICAL);
		leftToolBar.setBackground(
				new Background(new BackgroundFill(Color.valueOf(TOOLBAR_COLOR), CornerRadii.EMPTY, Insets.EMPTY)));
		rightToolBar = new ToolBar();
		rightToolBar.setBackground(
				new Background(new BackgroundFill(Color.valueOf(TOOLBAR_COLOR), CornerRadii.EMPTY, Insets.EMPTY)));
		rightToolBar.setOrientation(Orientation.VERTICAL);

		
//		Separator sep_1 = new Separator();
//		Separator sep_2 = new Separator();
//		Separator sep_3 = new Separator();
//		Separator sep_4 = new Separator();
//		Separator sep_5 = new Separator();
//		
//		sep_1.setVisible(false);
//		sep_2.setVisible(false);
//		sep_3.setVisible(false);
//		sep_4.setVisible(false);
//		sep_5.setVisible(false);
//		
//		//Zusammenfügen
//		leftToolBar.getItems().addAll(optionsButton, sep_1, allConcernsButton, newConcernButton,
//				sep_2, allStudentensButton, newStudentButton, sep_3, allStatisticsButton,
//				newStatisticButton, sep_4, formsButton, sep_5, remindersButton);
		
		//Zusammenfügen
		leftToolBar.getItems().addAll(optionsButton, allConcernsButton, newConcernButton,
				allStudentensButton, newStudentButton, allStatisticsButton,
				newStatisticButton, formsButton, remindersButton);
		

		//===========================================================
		//View zusammenfügen

		setLeft(leftToolBar);
		setRight(rightToolBar);
		
		rightToolBarUpdater = new Timeline(new KeyFrame(Duration.minutes(1), new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		        updateRightToolBar();
		    }
		}));
		rightToolBarUpdater.setCycleCount(Timeline.INDEFINITE);
		rightToolBarUpdater.play();

		tabPane = new TabPane();
		setCenter(tabPane);
		
		DraggingTabPaneSupport support = new DraggingTabPaneSupport();
		support.addSupport(tabPane);
		
		openSessionTabs();

	}


	public void updateRightToolBar() {
		rightToolBar.getItems().clear();
		rightToolBar.getItems().addAll(weeklyScheduleButton,nextAppointmentsLabel);
		// -----------------------------------------------------------
		ArrayList<Appointment> next24hourAppointments = presenter.getNext24hourAppointments();
		
		if (next24hourAppointments != null) {
			for (Appointment a : next24hourAppointments)
			{
				Button newAppointmentButton = new Button(a.getStartTimeString().substring(0, 5) + " - " + a.getEndTimeString().substring(0, 5) + "\n"
						+ presenter.getConcern(a.getConcernId()).getTitle() + "\n" + a.getRoomNmb());
				newAppointmentButton.setMaxWidth(Double.MAX_VALUE);
				newAppointmentButton.setStyle("-fx-base: "+ BUTTON_COLOR);
				newAppointmentButton.setOnAction((event) -> {
					openConcernTab(presenter.getConcern(a.getConcernId()));
				});
				rightToolBar.getItems().addAll(newAppointmentButton);
			}
		}
		
		
	}

	/**
	 * naming convention: b<mtrNr> = student, s<mtrNr> = editStudent, c<id> = concern, o = options, t<id> =
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

				if (firstLetter == 's' || firstLetter == 'b') {
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

	//=====================================================================
	//open_Tab Methoden

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
		
		
		//updateView wenn zum Tab gewechselt wird
		final MyTab innerTab = newTab;
		newTab.setOnSelectionChanged((event) -> {
			if(innerTab.isSelected())
			{
				AllStudentsView view = (AllStudentsView) innerTab.getContent();
				view.updateView();
				
			}
		});
	}

	public void openStudentTab(Student student) {
		//StudentView bereits offen?
		MyTab newTab = tabAlreadyOpen("b" + student.getMtrNr());
		//Student als EditStudentView offen?
		if (newTab == null)
		{
			newTab = tabAlreadyOpen("s" + student.getMtrNr());
		}
		//wenn nicht:
		if (newTab == null) {
			newTab = new MyTab("b" + student.getMtrNr());

			newTab.setText(student.getName() + ", " + student.getFirstName());

			newTab.setContent(new StudentView(student, presenter, newTab));

			tabPane.getTabs().addAll(newTab);
		}
		SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
		selectionModel.select(newTab);
		
		//updateView wenn zum Tab gewechselt wird
		final MyTab innerTab = newTab;
		newTab.setOnSelectionChanged((event) -> {
			if(innerTab.isSelected())
			{
				StudentView view = (StudentView) innerTab.getContent();
				view.updateView();
				
			}
		});
		
	}
	
	public void openStudentTabFromEditStudentView(Student student, ArrayList<String> changedMailAddresses) {
		MyTab newTab = tabAlreadyOpen("b" + student.getMtrNr());
		if (newTab == null) {
			newTab = new MyTab("b" + student.getMtrNr());

			newTab.setText(student.getName() + ", " + student.getFirstName());

			newTab.setContent(new StudentView(student, presenter, newTab, changedMailAddresses));

			tabPane.getTabs().addAll(newTab);
		}
		SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
		selectionModel.select(newTab);
		
		//updateView wenn zum Tab gewechselt wird
		final MyTab innerTab = newTab;
		newTab.setOnSelectionChanged((event) -> {
			if(innerTab.isSelected())
			{
				StudentView view = (StudentView) innerTab.getContent();
				view.updateView();
				
			}
		});
		
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
		
		//updateView wenn zum Tab gewechselt wird
		final MyTab innerTab = newTab;
		newTab.setOnSelectionChanged((event) -> {
			if(innerTab.isSelected())
			{
				OptionsView view = (OptionsView) innerTab.getContent();
				view.updateView();
				
			}
		});

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
		
		//updateView wenn zum Tab gewechselt wird
		final MyTab innerTab = newTab;
		newTab.setOnSelectionChanged((event) -> {
			if(innerTab.isSelected())
			{
				WeekScheduleView view = (WeekScheduleView) innerTab.getContent();
				view.updateView();
						
			}
		});
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
		
		//updateView wenn zum Tab gewechselt wird
		final MyTab innerTab = newTab;
		newTab.setOnSelectionChanged((event) -> {
			if(innerTab.isSelected())
			{
				FormsView view = (FormsView) innerTab.getContent();
				view.updateView();
						
			}
		});

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
		
		//updateView wenn zum Tab gewechselt wird
		final MyTab innerTab = newTab;
		newTab.setOnSelectionChanged((event) -> {
			if(innerTab.isSelected())
			{
				AllConcernsView view = (AllConcernsView) innerTab.getContent();
				view.updateView();
				
			}
		});

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

		//updateView wenn zum Tab gewechselt wird
		final MyTab innerTab = newTab;
		newTab.setOnSelectionChanged((event) -> {
			if(innerTab.isSelected())
			{
				AllStatisticsView view = (AllStatisticsView) innerTab.getContent();
				view.updateView();
				
			}
		});
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
		
		//updateView wenn zum Tab gewechselt wird
		final MyTab innerTab = newTab;
		
		newTab.setOnSelectionChanged((event) -> {
			if(innerTab.isSelected())
			{
				ConcernView view = (ConcernView) innerTab.getContent();
				view.updateView();
				
			}
		});
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
		
		//updateView wenn zum Tab gewechselt wird
		final MyTab innerTab = newTab;
		newTab.setOnSelectionChanged((event) -> {
			if(innerTab.isSelected())
			{
				RemindersView view = (RemindersView) innerTab.getContent();
				view.updateView();
				
			}
		});
	}
	
	//--------------------Tabs für die Eingabe neuer(!!!) Objekte. Diese Tabs werden nicht gemerkt für die nächste Sitzung

	public void openNewConcernTab() {
		MyTab newTab = new MyTab("new");

		newTab.setText("Neues Anliegen");

		newTab.setContent(new ConcernView(presenter, newTab));

		tabPane.getTabs().addAll(newTab);

		SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
		selectionModel.select(newTab);

		//updateView wenn zum Tab gewechselt wird
		final MyTab innerTab = newTab;
		newTab.setOnSelectionChanged((event) -> {
			if(innerTab.isSelected())
			{
				ConcernView view = (ConcernView) innerTab.getContent();
				view.updateView();
				
			}
		});
	}
	
	public void openNewConcernTab(ObservableList<Student> students) {
		MyTab newTab = new MyTab("new");

		newTab.setText("Neues Anliegen");

		newTab.setContent(new ConcernView(presenter, newTab, students));

		tabPane.getTabs().addAll(newTab);

		SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
		selectionModel.select(newTab);

		//updateView wenn zum Tab gewechselt wird
		final MyTab innerTab = newTab;
		newTab.setOnSelectionChanged((event) -> {
			if(innerTab.isSelected())
			{
				ConcernView view = (ConcernView) innerTab.getContent();
				view.updateView();
				
			}
		});
	}

	public void openNewStudentTab() {
		MyTab newTab = new MyTab("new");

		newTab.setText("Neuer Student");

		newTab.setContent(new EditStudentView(presenter, newTab));

		tabPane.getTabs().addAll(newTab);

		SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
		selectionModel.select(newTab);
		
		//updateView wenn zum Tab gewechselt wird
		final MyTab innerTab = newTab;
		newTab.setOnSelectionChanged((event) -> {
			if(innerTab.isSelected())
			{
				EditStudentView view = (EditStudentView) innerTab.getContent();
				view.updateView();
				
			}
		});

	}
	
	public void openEditStudentTab(Student student) {
		MyTab newTab = new MyTab("s" + student.getMtrNr());

		newTab.setText(student.getName() + ", " + student.getFirstName());

		newTab.setContent(new EditStudentView(presenter, student, newTab));

		tabPane.getTabs().addAll(newTab);

		SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
		selectionModel.select(newTab);
		
		//updateView wenn zum Tab gewechselt wird
		final MyTab innerTab = newTab;
		newTab.setOnSelectionChanged((event) -> {
			if(innerTab.isSelected())
			{
				EditStudentView view = (EditStudentView) innerTab.getContent();
				view.updateView();
				
			}
		});

	}

	public void openNewStatisticTab() {
		MyTab newTab = new MyTab("new");

		newTab.setText("Neue Statistik");

		newTab.setContent(new EditStatisticView(presenter, newTab));

		tabPane.getTabs().addAll(newTab);

		SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
		selectionModel.select(newTab);
		
		//updateView wenn zum Tab gewechselt wird
		final MyTab innerTab = newTab;
		newTab.setOnSelectionChanged((event) -> {
			if(innerTab.isSelected())
			{
				EditStatisticView view = (EditStatisticView) innerTab.getContent();
				view.updateView();
				
			}
		});
	}

	//ENDE: open_Tab Methoden
	//=========================================================================
	

	public void showEditUserDataView(Options options) {
		Stage stage = new Stage();
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setAlwaysOnTop(true);
		stage.initStyle(StageStyle.UNDECORATED);
        stage.setTitle("Nutzerdaten");
        stage.setResizable(false);
        stage.setScene(new Scene(new EditUserDataView(presenter, stage, options), getWidth()*(0.6), getHeight()*(0.7)));
        stage.show();
		
	}

	public void showNewReminderView(Options options) {
		
		ObservableList<Reminder> newReminders = presenter.getNewReminders(options.getLastReminderCheck());
		
//		//--------------------------------------------------------------
//		//TEST
//		Date sqlDate = options.getLastReminderCheck();
//		System.out.println("ursprüngliches sqlDate: " + sqlDate);
//		
//		//10 Tage abziehen im LocalDate Format 
//		LocalDate localDate = sqlDate.toLocalDate();
//		localDate = localDate.minusDays(10);
//		sqlDate = java.sql.Date.valueOf( localDate );
//		
//		//Ausm Model holen
//		ObservableList<Reminder> newReminders = presenter.getNewReminders(sqlDate);
//		
//		System.out.println("New Reminders since: " + sqlDate + " : " + newReminders);
//		
//		//--------------------------------------------------------------
		
		if(newReminders!= null)
		{
			Stage stage = new Stage();
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setAlwaysOnTop(true);
	        stage.setTitle("Neue Erinnerungen");
	        stage.setResizable(false);
	        stage.setScene(new Scene(new NewReminderAlertView(presenter,stage, newReminders), getWidth()*(0.6), getHeight()*(0.7)));
	        stage.show();
		}
		options.setLastReminderCheck(new Date(System.currentTimeMillis()));
		presenter.saveEditedOptions(options);
	}

	public void closeThisTab(MyTab tab) {
		tabPane.getTabs().remove(tab);
	}



	public void closeRelatedTabs(Student student)
	{
		//Tab kann nicht in der Iteration geschlossen werden
		MyTab tabToClose = null;
		
		for(Tab tab : tabPane.getTabs())
		{
			MyTab myTab = (MyTab) tab;
			
			//Student kann als StudentView oder EditStudentView geöffnet sein
			if(myTab.getTabId().equals("b" + student.getMtrNr()) ||  myTab.getTabId().equals("s" + student.getMtrNr())   )
			{
				tabToClose = myTab;
				break;
			}
		}
		
		if(tabToClose!= null)
		{
			closeThisTab(tabToClose);
		}
	
	}
	
	public void closeRelatedTabs(Concern concern)
	{
		MyTab tabToClose = null;
		
		for(Tab tab : tabPane.getTabs())
		{
			MyTab myTab = (MyTab) tab;
			
			if(myTab.getTabId().equals("c" + concern.getId()))
			{
				tabToClose = myTab;
				break;
			}
		}
		
		if(tabToClose!= null)
		{
			closeThisTab(tabToClose);
		}

	}
	
	public void closeRelatedTabs(Statistic statistic) {
		
		MyTab tabToClose = null;
		
		for(Tab tab : tabPane.getTabs())
		{
			MyTab myTab = (MyTab) tab;
			
			if(myTab.getTabId().equals("t" + statistic.getId()))
			{
				tabToClose = myTab;
				break;
			}
		}
		
		if(tabToClose!= null)
		{
			closeThisTab(tabToClose);
		}
		
	}
	
	
	
	
	public ArrayList<String> getCurrentTabsIds()
	{
		ArrayList<String> currentTabsIds = new ArrayList<>();
		
		for(Tab tab : tabPane.getTabs())
		{
			MyTab myTab = (MyTab) tab;
			
			if(!myTab.getTabId().equals("new"))
			{
				currentTabsIds.add(myTab.getTabId());
			}
		}
		
		return currentTabsIds;
	}
	
	

}
