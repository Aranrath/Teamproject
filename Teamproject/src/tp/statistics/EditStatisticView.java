package tp.statistics;

import java.util.ArrayList;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import tp.Presenter;
import tp.model.MyTab;
import tp.model.statistics.Statistic;
import tp.model.statistics.StatisticComponent;

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

	//ContinuousStatistic-------------------------------
	private GridPane continuousStatisticOptionsGridPane;
	
	//f�r Zeitr�ume
	private Label continuousStatistictOptionsTmePeriodLabel;
	private Label continuousStatistictOptionsStartDateLabel;
	private DatePicker continuousStatistictOptionsStartDateDatePicker;
	private Label continuousStatistictOptionsEndDateLabel;
	private DatePicker continuousStatistictOptionsEndDateDatePicker;
	
	//IntervalStatistic-------------------------------
	private GridPane intervalStatisticOptionsGridPane;
	
	//f�r Zeitr�ume
	private Label intervalStatistictOptionsTmePeriodLabel;
	private Label intervalStatistictOptionsStartDateLabel;
	private DatePicker intervalStatistictOptionsStartDateDatePicker;
	private Label intervalStatistictOptionsEndDateLabel;
	private DatePicker intervalStatistictOptionsEndDateDatePicker;
	
	//f�r Zeitabst�nde
	private Label intervalStatistictOptionsTimeIntervalsInDaysLabel;
	private TextField intervalStatistictOptionsTimeIntervalsInDaysTextField;
	
	//--------------------------------------Abschlie�en
	
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
		
		setPadding(new Insets(10));
		setHgap(10);
		setVgap(10);
		
		//======================================GUI Elemente erstellen
		//--------------------------------------Standard-Felder
		
		defaultFields = new GridPane();
		defaultFields.setHgap(10);
		defaultFields.setVgap(10);
		
		titleLabel = new Label("Titel:");
		titleTextField = new TextField();
		
		typeLabel = new Label("Statistik-Typ:");
		toggleGroup = new ToggleGroup();
		
		ratioStatisticRadioButton = new RadioButton("Verh�ltnis-Statistik" + "\n"+ "(zus�tzlich Visualisiert in einem Kuchendiagramm)");
		ratioStatisticRadioButton.setToggleGroup(toggleGroup);
		continuousStatisticRadioButton = new RadioButton("Verlaufs-Statistik" + "\n"+"(zus�tzlich Visualisiert in einem Graph)");
		continuousStatisticRadioButton.setToggleGroup(toggleGroup);
		intervalStatisticRadioButton = new RadioButton("Intervall-Statistik"+ "\n"+"(zus�tzlich Visualisiert in einem Balkendiagramm)");
		intervalStatisticRadioButton.setToggleGroup(toggleGroup);
		
		defaultFields.add(titleLabel, 0, 0);
		defaultFields.add(titleTextField, 1, 0);
		defaultFields.add(typeLabel, 0, 1);
		defaultFields.add(ratioStatisticRadioButton, 0, 2,2,1);
		defaultFields.add(continuousStatisticRadioButton, 0, 3, 2,1);
		defaultFields.add(intervalStatisticRadioButton, 0,4,2,1);
		
		//--------------------------------------Typspezifische-Felder

		
		//------------------------------------------------------ContinuousStatisticOptionsGridPane
		continuousStatisticOptionsGridPane = new GridPane();
		continuousStatisticOptionsGridPane.setHgap(10);
		continuousStatisticOptionsGridPane.setVgap(10);
		continuousStatisticOptionsGridPane.setVisible(false);
		
		//f�r Zeitr�ume
		continuousStatistictOptionsTmePeriodLabel = new Label("Zeitraum");
		continuousStatistictOptionsStartDateLabel = new Label("Start-Datum");
		continuousStatistictOptionsStartDateDatePicker = new DatePicker();
		continuousStatistictOptionsEndDateLabel = new Label("End-Datum");
		continuousStatistictOptionsEndDateDatePicker = new DatePicker();
		
		//Zusammenf�gen
		continuousStatisticOptionsGridPane.add(new Label("Optionen Verlaufs-Statistik:"), 0	, 0);
		continuousStatisticOptionsGridPane.add(continuousStatistictOptionsTmePeriodLabel, 0, 1);
		continuousStatisticOptionsGridPane.add(continuousStatistictOptionsStartDateLabel, 0, 2);
		continuousStatisticOptionsGridPane.add(continuousStatistictOptionsStartDateDatePicker, 1, 2);
		continuousStatisticOptionsGridPane.add(continuousStatistictOptionsEndDateLabel, 0, 3);
		continuousStatisticOptionsGridPane.add(continuousStatistictOptionsEndDateDatePicker, 1, 3);
		
		//------------------------------------------------------IntervalStatisticOptionsGridPane
		
		intervalStatisticOptionsGridPane = new GridPane();
		intervalStatisticOptionsGridPane.setHgap(10);
		intervalStatisticOptionsGridPane.setVgap(10);
		intervalStatisticOptionsGridPane.setVisible(false);
		
		//f�r Zeitr�ume
		intervalStatistictOptionsTmePeriodLabel = new Label("Zeitraum");
		intervalStatistictOptionsStartDateLabel = new Label("Start-Datum");
		intervalStatistictOptionsStartDateDatePicker = new DatePicker();
		intervalStatistictOptionsEndDateLabel = new Label("End-Datum");
		intervalStatistictOptionsEndDateDatePicker = new DatePicker();
		
		//f�r Zeitabst�nde
		intervalStatistictOptionsTimeIntervalsInDaysLabel = new Label("Zeitabst�nde (In Tagen)");
		intervalStatistictOptionsTimeIntervalsInDaysTextField = new TextField();
		
		//Zusammenf�gen
		intervalStatisticOptionsGridPane.add(new Label("Optionen Intervall-Statistik:"), 0, 0);
		intervalStatisticOptionsGridPane.add(intervalStatistictOptionsTmePeriodLabel, 0, 1);
		intervalStatisticOptionsGridPane.add(intervalStatistictOptionsStartDateLabel, 0, 2);
		intervalStatisticOptionsGridPane.add(intervalStatistictOptionsStartDateDatePicker, 1, 2);
		intervalStatisticOptionsGridPane.add(intervalStatistictOptionsEndDateLabel, 0, 3);
		intervalStatisticOptionsGridPane.add(intervalStatistictOptionsEndDateDatePicker, 1, 3);
		intervalStatisticOptionsGridPane.add(new Label(""), 0, 4);
		intervalStatisticOptionsGridPane.add(intervalStatistictOptionsTimeIntervalsInDaysLabel, 0, 5);
		intervalStatisticOptionsGridPane.add(intervalStatistictOptionsTimeIntervalsInDaysTextField, 1, 5);
		
		
		//--------------------------------------Abschlie�en

		calculateButton = new Button("Statistik berechnen");
		errorLabel = new Label("ERROR");
		errorLabel.setTextFill(Color.RED);
		errorLabel.setVisible(false);
		
		calculateHBox = new HBox(errorLabel, calculateButton);
		calculateHBox.setPadding(new Insets(10));
		calculateHBox.setSpacing(10);
		calculateHBox.setBackground(
				new Background(new BackgroundFill(Color.CORNFLOWERBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
		calculateHBox.setAlignment(Pos.CENTER_RIGHT);
		
		
		//--------------------------------------Filter
		
		addStatisticObjectButton = new Button("+");
		statisticObjectsVBox = new VBox(addStatisticObjectButton);
		statisticObjectsVBox.setAlignment(Pos.TOP_CENTER);
		statisticObjectsScrollPane = new ScrollPane();
		statisticObjectsScrollPane.setContent(statisticObjectsVBox);
		statisticObjectsVBox.prefWidthProperty().bind(statisticObjectsScrollPane.widthProperty());
		statisticObjectsVBox.prefHeightProperty().bind(statisticObjectsScrollPane.heightProperty());
		statisticObjectsVBox.setBackground(
				new Background(new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY)));
		statisticObjectsVBox.setSpacing(10);
		
		//=====================================

		
		add(defaultFields,0,0);
		add(continuousStatisticOptionsGridPane,0,1);
		add(intervalStatisticOptionsGridPane,0,1);
		add(calculateHBox,0,2);
		add(statisticObjectsScrollPane, 1,0,1,3);
		
		//-------------------------------------
		
		ColumnConstraints column1 = new ColumnConstraints();
		column1.setPercentWidth(30);
		ColumnConstraints column2 = new ColumnConstraints();
		column2.setPercentWidth(70);
		
		getColumnConstraints().addAll(column1, column2);
		
		RowConstraints row1 = new RowConstraints();
		row1.setPercentHeight(45);
		
		RowConstraints row2 = new RowConstraints();
		row2.setPercentHeight(45);
		
		RowConstraints row3 = new RowConstraints();
		row3.setPercentHeight(10);
		
		getRowConstraints().addAll(row1,row2,row3);
		
		
		//====================================
		
		calculateButton.setOnAction(event -> {
			
			//===================================================Error Suche
			
			//Fehler in den defaultFields
			String title = titleTextField.getText();
			errorLabel.setVisible(true);
			if(title.equals(""))
			{
				errorLabel.setText("Alle Felder m�ssen ausgef�llt sein");
				errorLabel.setTextFill(Color.RED);
				return;
			}
			
			//Fehler mit Statistik-Typen
			//TODO Kein RadioButton ausgew�hlt
			//TODO Typspezifische Einstellungen unvollst�ndig
			
			//Fehler in den Filtern
			ArrayList<Node> statisticObjectsList = new ArrayList<Node>(statisticObjectsVBox.getChildren());
			statisticObjectsList.remove(addStatisticObjectButton);
			boolean allCreatable = true;
			for(Node n : statisticObjectsList)
			{
				//wird nicht abgebrochen sobald ein Fehler auftritt, damit in isCreatable() ALLE fehlerhaften Filter ihr eigenes Error-Label setzen
				if(!((StatisticComponentView) n).isCreatable())
				{
					allCreatable = false;
				}	
			}
			if(!allCreatable)
			{
				errorLabel.setText("Es gibt unvollst�ndige Filter");
				errorLabel.setVisible(true);
				return;
			}
				
			//===================================================Auslesen der Oberfl�che
				
			//StatisticComponents
			ArrayList<StatisticComponent> statisticComponentsList = new ArrayList<>();
				
			for(Node n : statisticObjectsList)
			{
				statisticComponentsList.add(((StatisticComponentView) n).getStatisticComponent());
			}
			
			//TODO defaultFields auslesen
			//TODO typespecific Fields auslesen je nachdem welcher Radiobutton ausgew�hlt ist
			
			
			//===================================================Berechnung der Statistik
				
			//TODO Anzeige auf Oberfl�che: wird berechnet
				
			new Thread(() -> {

				//TODO statt Statistik ausgelesene Oberfl�chenelemente �bergeben und Methode dementsprechend anpassen
				Statistic newStatistic = presenter.calculateAndSaveNewStatistic(new Statistic("hi"));

				Platform.runLater(()->{
					presenter.closeThisTab(tab);
					presenter.openStatisticTab(newStatistic);
				});
					
			}).run();

		});
		
		addStatisticObjectButton.setOnAction(event -> {
			statisticObjectsVBox.getChildren().remove(addStatisticObjectButton);
			statisticObjectsVBox.getChildren().addAll(new StatisticComponentView(this));
			statisticObjectsVBox.getChildren().add(addStatisticObjectButton);
		});
		
		ratioStatisticRadioButton.setOnAction(event ->
		{
			continuousStatisticOptionsGridPane.setVisible(false);
			intervalStatisticOptionsGridPane.setVisible(false);
		});
		
		continuousStatisticRadioButton.setOnAction(event ->
		{
			continuousStatisticOptionsGridPane.setVisible(true);
			intervalStatisticOptionsGridPane.setVisible(false);
		});
		
		intervalStatisticRadioButton.setOnAction(event ->
		{
			continuousStatisticOptionsGridPane.setVisible(false);
			intervalStatisticOptionsGridPane.setVisible(true);
		});

		
	}

	public void deleteStatisticComponent(StatisticComponentView statisticComponentViewToDelete) {
		statisticObjectsVBox.getChildren().remove(statisticComponentViewToDelete);
		
	}

}
