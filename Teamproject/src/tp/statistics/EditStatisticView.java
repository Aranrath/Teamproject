package tp.statistics;

import java.sql.Date;
import java.util.ArrayList;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
	
	//für Zeiträume
	private Label continuousStatistictOptionsTmePeriodLabel;
	private Label continuousStatistictOptionsStartDateLabel;
	private DatePicker continuousStatistictOptionsStartDateDatePicker;
	private Label continuousStatistictOptionsEndDateLabel;
	private DatePicker continuousStatistictOptionsEndDateDatePicker;
	
	//IntervalStatistic-------------------------------
	private GridPane intervalStatisticOptionsGridPane;
	
	//für Zeiträume
	private Label intervalStatistictOptionsTmePeriodLabel;
	private Label intervalStatistictOptionsStartDateLabel;
	private DatePicker intervalStatistictOptionsStartDateDatePicker;
	private Label intervalStatistictOptionsEndDateLabel;
	private DatePicker intervalStatistictOptionsEndDateDatePicker;
	
	//für Zeitabstände
	private Label intervalStatistictOptionsTimeIntervalsInDaysLabel;
	private TextField intervalStatistictOptionsTimeIntervalsInDaysTextField;
	
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
		
		ratioStatisticRadioButton = new RadioButton("Verhältnis-Statistik" + "\n"+ "(zusätzlich Visualisiert in einem Kuchendiagramm)");
		ratioStatisticRadioButton.setToggleGroup(toggleGroup);
		continuousStatisticRadioButton = new RadioButton("Verlaufs-Statistik" + "\n"+"(zusätzlich Visualisiert in einem Graph)");
		continuousStatisticRadioButton.setToggleGroup(toggleGroup);
		intervalStatisticRadioButton = new RadioButton("Intervall-Statistik"+ "\n"+"(zusätzlich Visualisiert in einem Balkendiagramm)");
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
		
		//für Zeiträume
		continuousStatistictOptionsTmePeriodLabel = new Label("Zeitraum");
		continuousStatistictOptionsStartDateLabel = new Label("Start-Datum");
		continuousStatistictOptionsStartDateDatePicker = new DatePicker();
		continuousStatistictOptionsEndDateLabel = new Label("End-Datum");
		continuousStatistictOptionsEndDateDatePicker = new DatePicker();
		
		//Zusammenfügen
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
		
		//für Zeiträume
		intervalStatistictOptionsTmePeriodLabel = new Label("Zeitraum");
		intervalStatistictOptionsStartDateLabel = new Label("Start-Datum");
		intervalStatistictOptionsStartDateDatePicker = new DatePicker();
		intervalStatistictOptionsEndDateLabel = new Label("End-Datum");
		intervalStatistictOptionsEndDateDatePicker = new DatePicker();
		
		//für Zeitabstände
		intervalStatistictOptionsTimeIntervalsInDaysLabel = new Label("Zeitabstände (In Tagen)");
		intervalStatistictOptionsTimeIntervalsInDaysTextField = new TextField();
		// force the field to be numeric only
		intervalStatistictOptionsTimeIntervalsInDaysTextField.textProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> observable, String oldValue, 
		        String newValue) {
		        if (!newValue.matches("\\d*")) {
		        	intervalStatistictOptionsTimeIntervalsInDaysTextField.setText(newValue.replaceAll("[^\\d]", ""));
		        }
		    }
		});
		
		//Zusammenfügen
		intervalStatisticOptionsGridPane.add(new Label("Optionen Intervall-Statistik:"), 0, 0);
		intervalStatisticOptionsGridPane.add(intervalStatistictOptionsTmePeriodLabel, 0, 1);
		intervalStatisticOptionsGridPane.add(intervalStatistictOptionsStartDateLabel, 0, 2);
		intervalStatisticOptionsGridPane.add(intervalStatistictOptionsStartDateDatePicker, 1, 2);
		intervalStatisticOptionsGridPane.add(intervalStatistictOptionsEndDateLabel, 0, 3);
		intervalStatisticOptionsGridPane.add(intervalStatistictOptionsEndDateDatePicker, 1, 3);
		intervalStatisticOptionsGridPane.add(new Label(""), 0, 4);
		intervalStatisticOptionsGridPane.add(intervalStatistictOptionsTimeIntervalsInDaysLabel, 0, 5);
		intervalStatisticOptionsGridPane.add(intervalStatistictOptionsTimeIntervalsInDaysTextField, 1, 5);
		
		
		//--------------------------------------Abschließen

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
				errorLabel.setText("Alle Felder müssen ausgefüllt sein");
				errorLabel.setTextFill(Color.RED);
				return;
			}
			
			//Fehler mit Statistik-Typen
			if(toggleGroup.getSelectedToggle() == null) {
				errorLabel.setText("Kein Statistik-Typ ausgewählt");
				errorLabel.setTextFill(Color.RED);
				return;
			}
			//Typspezifische Einstellungen unvollständig
			if(toggleGroup.getSelectedToggle().equals(continuousStatisticRadioButton) &&
					(continuousStatistictOptionsStartDateDatePicker.getValue()==null || continuousStatistictOptionsEndDateDatePicker.getValue()==null)) {
				errorLabel.setText("Alle Felder müssen asgefüllt sein");
				errorLabel.setTextFill(Color.RED);
				return;
			}
			if(toggleGroup.getSelectedToggle().equals(intervalStatisticRadioButton) &&
					(intervalStatistictOptionsStartDateDatePicker.getValue()==null || intervalStatistictOptionsEndDateDatePicker.getValue()==null ||
					intervalStatistictOptionsTimeIntervalsInDaysTextField.getText().equals(""))) {
				errorLabel.setText("Alle Felder müssen asgefüllt sein");
				errorLabel.setTextFill(Color.RED);
				return;
			}
			
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
				errorLabel.setText("Es gibt unvollständige Filter");
				errorLabel.setVisible(true);
				return;
			}
				
			//===================================================Auslesen der Oberfläche
				
			//StatisticComponents
			ArrayList<StatisticComponent> statisticComponentsList = new ArrayList<>();
			for(Node n : statisticObjectsList)
			{
				statisticComponentsList.add(((StatisticComponentView) n).getStatisticComponent());
			}
			String statisticType;
			Date startDate;
			Date endDate;
			int step;
			//typespecific Fields auslesen je nachdem welcher Radiobutton ausgewählt ist
			if(toggleGroup.getSelectedToggle().equals(ratioStatisticRadioButton)) {
				statisticType = "ratio";
				startDate = null;
				endDate = null;
				step = 0;
			}else if(toggleGroup.getSelectedToggle().equals(continuousStatisticRadioButton)) {
				statisticType = "continuous";
				startDate = Date.valueOf(continuousStatistictOptionsStartDateDatePicker.getValue());
				endDate = Date.valueOf(continuousStatistictOptionsEndDateDatePicker.getValue());
				step = 0;
			}else{
				statisticType = "interval";
				startDate = Date.valueOf(intervalStatistictOptionsStartDateDatePicker.getValue());
				endDate = Date.valueOf(intervalStatistictOptionsEndDateDatePicker.getValue());
				step = Integer.parseInt(intervalStatistictOptionsTimeIntervalsInDaysTextField.getText());
			}
			
			
			//===================================================Berechnung der Statistik
			
			errorLabel.setText("Statistic wird Berechnet");
			errorLabel.setTextFill(Color.BLUE);
				
			new Thread(() -> {
				Statistic newStatistic;
				if (statisticType.equals("ratio")) {
					newStatistic = presenter.calculateAndSaveNewRatioStatistic(statisticComponentsList);
				}else if(statisticType.equals("continuous")) {
					newStatistic = presenter.calculateAndSaveNewContinuousStatistic(statisticComponentsList, startDate, endDate);
				}else{
					newStatistic = presenter.calculateAndSaveNewIntervalStatistic(statisticComponentsList, startDate, endDate, step);
				}

				Platform.runLater(()->{
					presenter.closeThisTab(tab);
					presenter.openStatisticTab(newStatistic);
				});
					
			}).run();

		});
		
		addStatisticObjectButton.setOnAction(event -> {
			statisticObjectsVBox.getChildren().remove(addStatisticObjectButton);
			statisticObjectsVBox.getChildren().addAll(new StatisticComponentView(this, presenter));
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
