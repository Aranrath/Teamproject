package tp.statistics;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import tp.Presenter;
import tp.model.MyTab;
import tp.model.statistics.Statistic;

public class EditStatisticView extends GridPane
{
	
	private Presenter presenter;
	private MyTab tab;
	private ToggleGroup toggleGroup;
	
	//======================================GUI
	//--------------------------------------Standard-Felder
	
	private GridPane defaultFields;
	
	private Label titleLabel;
	private TextField titleTextField;
	
	private Label typeLabel;
	private RadioButton ratioStatisticRadioButton;
	private RadioButton continuousStatisticRadioButton;
	private RadioButton intervalStatisticRadioButton;
	
	//--------------------------------------Typspezifische-Felder
	
	//angezeigteOptionen
	private GridPane typespecificOptionsGridPane;
	
	//Je Statistik-Typ
//	private GridPane ratioStatisticOptionsGridPane;
	private GridPane continuousStatisticOptionsGridPane;
	private GridPane intervalStatisticOptionsGridPane;
	
	//für Zeiträume
	private Label timePeriodLabel;
	private Label startDateLabel;
	private DatePicker startDateDatePicker;
	private Label endDateLabel;
	private DatePicker endDateDatePicker;
	
	//für Zeitabstände
	private Label timeIntervalsInDaysLabel;
	private TextField timeIntervalsInDaysTextField;
	
	//--------------------------------------Abschließen
	
	private HBox calculateHBox;
	private Button calculateButton;
	private Label errorLabel;
	
	//--------------------------------------Filter
	
	private ScrollPane statisticObjectsScrollPane;
	private VBox statisticObjectsVBox;
	private Button addStatisticObjectButton;
	
	
	//======================================

	public EditStatisticView(Presenter presenter, MyTab tab) {
		this.presenter = presenter;
		this.tab = tab;
		buildView();
	}

	private void buildView() {
		setPadding(new Insets(20));
		setHgap(20);
		setVgap(20);
		
		//======================================GUI Elemente erstellen
		//--------------------------------------Standard-Felder
		
		defaultFields = new GridPane();
		defaultFields.setHgap(20);
		defaultFields.setVgap(20);
		
		titleLabel = new Label("Titel:");
		titleTextField = new TextField();
		
		typeLabel = new Label("Statistik-Typ:");
		toggleGroup = new ToggleGroup();
		
		ratioStatisticRadioButton = new RadioButton("Verhältnis-Statistik (zusätzlich Visualisiert in einem Kuchendiagramm)");
		ratioStatisticRadioButton.setToggleGroup(toggleGroup);
		continuousStatisticRadioButton = new RadioButton("Verlaufs-Statistik (zusätzlich Visualisiert in einem Graph)");
		continuousStatisticRadioButton.setToggleGroup(toggleGroup);
		intervalStatisticRadioButton = new RadioButton("Intervall-Diagramm (zusätzlich Visualisiert in einem Balkendiagramm)");
		intervalStatisticRadioButton.setToggleGroup(toggleGroup);
		
		defaultFields.add(titleLabel, 0, 0);
		defaultFields.add(titleTextField, 1, 0);
		defaultFields.add(typeLabel, 0, 1);
		defaultFields.add(ratioStatisticRadioButton, 0, 2,2,1);
		defaultFields.add(continuousStatisticRadioButton, 0, 3, 2,1);
		defaultFields.add(intervalStatisticRadioButton, 0,4,2,1);
		
		//--------------------------------------Typspezifische-Felder
		
		//angezeigteOptionen
		typespecificOptionsGridPane = new GridPane();
		
		//Je 
//		ratioStatisticOptionsGridPane = new GridPane();
		
		continuousStatisticOptionsGridPane = new GridPane();
		continuousStatisticOptionsGridPane.setPadding(new Insets(20));
		continuousStatisticOptionsGridPane.setHgap(20);
		continuousStatisticOptionsGridPane.setVgap(20);
		
		intervalStatisticOptionsGridPane = new GridPane();
		intervalStatisticOptionsGridPane.setPadding(new Insets(20));
		intervalStatisticOptionsGridPane.setHgap(20);
		intervalStatisticOptionsGridPane.setVgap(20);
		
		//für Zeiträume
		timePeriodLabel = new Label("Zeitraum");
		startDateLabel = new Label("Start-Datum");
		startDateDatePicker = new DatePicker();
		endDateLabel = new Label("End-Datum");
		endDateDatePicker = new DatePicker();
		
		//für Zeitabstände
		timeIntervalsInDaysLabel = new Label("Zeitabstände (In Tagen)");
		timeIntervalsInDaysTextField = new TextField();
		
		continuousStatisticOptionsGridPane.add(timePeriodLabel, 0, 0);
		continuousStatisticOptionsGridPane.add(startDateLabel, 0, 1);
		continuousStatisticOptionsGridPane.add(startDateDatePicker, 1, 1);
		continuousStatisticOptionsGridPane.add(endDateLabel, 0, 2);
		continuousStatisticOptionsGridPane.add(endDateDatePicker, 1, 2);
		
		intervalStatisticOptionsGridPane.add(timePeriodLabel, 0, 0);
		intervalStatisticOptionsGridPane.add(startDateLabel, 0, 1);
		intervalStatisticOptionsGridPane.add(startDateDatePicker, 1, 1);
		intervalStatisticOptionsGridPane.add(endDateLabel, 0, 2);
		intervalStatisticOptionsGridPane.add(endDateDatePicker, 1, 2);
		intervalStatisticOptionsGridPane.add(new Label(""), 0, 3);
		intervalStatisticOptionsGridPane.add(timeIntervalsInDaysLabel, 0, 4);
		intervalStatisticOptionsGridPane.add(timeIntervalsInDaysTextField, 1, 4);
		
		
		//--------------------------------------Abschließen

		calculateButton = new Button("Statistik berechnen");
		errorLabel = new Label("ERROR");
		errorLabel.setTextFill(Color.RED);
		errorLabel.setVisible(false);
		
		calculateHBox = new HBox(errorLabel, calculateButton);
		calculateHBox.setPadding(new Insets(20));
		calculateHBox.setSpacing(20);
		calculateHBox.setBackground(
				new Background(new BackgroundFill(Color.CORNFLOWERBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
		calculateHBox.setAlignment(Pos.CENTER_RIGHT);
		
		
		//--------------------------------------Filter
		
		addStatisticObjectButton = new Button("+");
		statisticObjectsVBox = new VBox(addStatisticObjectButton);
		statisticObjectsScrollPane = new ScrollPane();
		statisticObjectsScrollPane.setContent(statisticObjectsVBox);
		
		statisticObjectsVBox.setPadding(new Insets(20));
		statisticObjectsVBox.setSpacing(20);
		
		//=====================================

		
		add(defaultFields,0,0);
		add(typespecificOptionsGridPane,0,1);
		add(calculateHBox,0,2);
		add(statisticObjectsScrollPane, 1,0,1,3);
		
		//====================================
		
		calculateButton.setOnAction(event -> {
			String title = titleTextField.getText();
			errorLabel.setVisible(true);
			if(title.equals(""))
			{
				errorLabel.setText("Alle Felder müssen ausgefüllt sein");
				errorLabel.setTextFill(Color.RED);
			}
			else
			{
				//TODO Anzeige auf Oberfläche: wird berechnet
				
				new Thread(() -> {

					//TODO statt Statistik ausgelesene Oberflächenelemente ausgeben
					Statistic newStatistic = presenter.calculateAndSaveNewStatistic(new Statistic("hi"));

					Platform.runLater(()->{
						presenter.closeThisTab(tab);
						presenter.openStatisticTab(newStatistic);
					});
					
				}).run();
				
				
			}
			
		});
		
		addStatisticObjectButton.setOnAction(event -> {
			statisticObjectsVBox.getChildren().add(new StatisticComponentView(this));
		});
		
		ratioStatisticRadioButton.setOnAction(event ->
		{
			typespecificOptionsGridPane = null;
		});
		
		continuousStatisticRadioButton.setOnAction(event ->
		{
			typespecificOptionsGridPane = continuousStatisticOptionsGridPane;
		});
		
		intervalStatisticRadioButton.setOnAction(event ->
		{
			typespecificOptionsGridPane = intervalStatisticOptionsGridPane;
		});

		
	}

	public void deleteStatisticComponent(StatisticComponentView statisticComponentViewToDelete) {
		statisticObjectsVBox.getChildren().remove(statisticComponentViewToDelete);
		
	}

}
