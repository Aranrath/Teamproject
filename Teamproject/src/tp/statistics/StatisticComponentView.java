package tp.statistics;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import tp.Presenter;
import tp.model.PO;
import tp.model.Topic;
import tp.model.statistics.StatisticComponent;
import tp.model.statistics.StatisticComponent.Filter;

public class StatisticComponentView extends HBox
{
	private Presenter presenter;
	private ArrayList<FilterView> filterGridPanes;
	private EditStatisticView editStatisticView;
	
	//=====================================
	
	private GridPane basicOptionsGridPane;
	private Label nameLabel;
	private TextField nameTextField;
	private Label sourceLabel;
	private ComboBox<String> sourceComboBox;
	private Label errorLabel;
	private Button deleteStatisticComponentButton;
	private Button addFilterButton;
	
	public StatisticComponentView(EditStatisticView editStatisticView, Presenter presenter)
	{
		this.editStatisticView = editStatisticView;
		this.presenter = presenter;
		filterGridPanes = new ArrayList<FilterView>();
		buildView();
	}

	private void buildView()
	{
		setPadding(new Insets(5));
		setSpacing(5);
		
		setAlignment(Pos.CENTER_LEFT);
		
		setBackground(
				new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
		
		basicOptionsGridPane = new GridPane();
		nameLabel = new Label("Name ");
		nameTextField = new TextField();
		sourceLabel = new Label("Quelle ");
		sourceComboBox = new ComboBox<String>(FXCollections.observableArrayList("Studenten","Anliegen","Terminlängen"));
		sourceComboBox.setMaxWidth(Double.MAX_VALUE);
		HBox.setHgrow(sourceComboBox, Priority.ALWAYS);
				
		deleteStatisticComponentButton = new Button("Löschen");
		errorLabel = new Label("ERROR");
		errorLabel.setVisible(false);
		errorLabel.setTextFill(Color.RED);
		addFilterButton = new Button("+");
			
		basicOptionsGridPane.add(nameLabel, 0, 0);
		basicOptionsGridPane.add(nameTextField, 1, 0);
		basicOptionsGridPane.add(sourceLabel, 0, 1);
		basicOptionsGridPane.add(sourceComboBox, 1, 1);
		basicOptionsGridPane.add(errorLabel, 0, 3,2,1);
		basicOptionsGridPane.add(deleteStatisticComponentButton, 0, 4,2,1);
		GridPane.setHalignment(deleteStatisticComponentButton, HPos.RIGHT);
		
		getChildren().addAll(basicOptionsGridPane, new Separator(Orientation.VERTICAL));
		getChildren().add(addFilterButton);
		
		
		//===========================================
		
		addFilterButton.setOnAction(event -> {
			if(sourceComboBox.getSelectionModel().getSelectedItem() == null)
			{
				errorLabel.setVisible(true);
				errorLabel.setText("Wähle zunächst eine Quelle aus");
			}
			else
			{
				if(errorLabel.getText().equals("Wähle zunächst eine Quelle aus"))
				{
					errorLabel.setVisible(false);
				}
				
				getChildren().remove(addFilterButton);
				FilterView newFilterGridPane = new FilterView(this);
				filterGridPanes.add(newFilterGridPane);
				getChildren().addAll(newFilterGridPane, new Separator(Orientation.VERTICAL),addFilterButton);
				if(filterGridPanes.size()==1) {
					sourceComboBox.setDisable(true);
				}
			}

		});
		
		
		deleteStatisticComponentButton.setOnAction(event -> {
			editStatisticView.deleteStatisticComponent(this);
		});
		
	}

	public void deleteFilter(FilterView filterView) {
		filterGridPanes.remove(filterView);
		getChildren().remove(getChildren().indexOf(filterView) + 1); 	//delete seperator
		getChildren().remove(filterView);
		if(filterGridPanes.size()==0) {
			sourceComboBox.setDisable(false);
		}
	}
	
	public boolean isCreatable()
	{
		//kontrolliert ob alle nötigen Felder ausgefüllt sind um ein StatisticComponent zu erstellen
		if (nameTextField.getText().equals("") || sourceComboBox.getValue() == null) {
			errorLabel.setVisible(true);
			errorLabel.setText("Name und Quelle müssen ausgefüllt sein");
			return false;
		}
		return true;
	}
	
	public StatisticComponent getStatisticComponent()
	{
		String name = nameTextField.getText();
		String source = sourceComboBox.getSelectionModel().getSelectedItem();
		ArrayList<Filter> selectedFilter = new ArrayList<Filter>();
		for(FilterView filter: filterGridPanes) {
			selectedFilter.add(filter.getFilter());
		}
		StatisticComponent result = new StatisticComponent(name, source, selectedFilter);
		return result;
	}
	
	
	//========================================================innere Klasse
	
	class FilterView extends GridPane
	{
		private StatisticComponentView parentView;
		private Filter filter;
		
		//=======================================
		
		private VBox filterMethodsVBox;
		private Button deleteFilterMethodButton;
		private Button deleteFilterViewButton;
		private ComboBox<String> filterMethodsComboBox;
		private HBox filterMethodParamHBox;
		private Label errorLabel;
		private Button addFilterMethodButton;
		
		private List<String> filterMethods;
		private ComboBox<?> filterMethodParamComboBox;
		private ComboBox<String> filterMethodParamPrefixComboBox;
		private DatePicker filterMethodParamDatePicker;
		private TextField filterMethodParamTextField;
		
		public FilterView(StatisticComponentView parentView)
		{
			filter = new Filter();
			this.parentView = parentView;
			filterMethods = new ArrayList<String>();
			buildFilterGridPane();	
		}
		
		
		private void buildFilterGridPane()
		{
			filterMethodsVBox = new VBox();
			deleteFilterViewButton = new Button("Filter löschen");
			GridPane.setHalignment(deleteFilterViewButton, HPos.RIGHT);
			ObservableList<String> studentFilters = FXCollections.observableArrayList(StatisticComponent.STUDENT_FILTER_METHODS);
			ObservableList<String> concernFilters = FXCollections.observableArrayList(StatisticComponent.CONCERN_FILTER_METHODS);
			ObservableList<String> appointmentLengthFilters = FXCollections.observableArrayList(StatisticComponent.APPOINTMENT_LENGTH_FILTER_METHODS);
			
			deleteFilterMethodButton = new Button("-");
			deleteFilterMethodButton.setVisible(false);
			
			if(sourceComboBox.getSelectionModel().getSelectedItem().equals("Studenten"))
			{
				filterMethodsComboBox = new ComboBox<String>(studentFilters);
			}
			else if(sourceComboBox.getSelectionModel().getSelectedItem().equals("Anliegen"))
			{
				filterMethodsComboBox = new ComboBox<String>(concernFilters);
			}
			else if(sourceComboBox.getSelectionModel().getSelectedItem().equals("Terminlängen"))
			{
				filterMethodsComboBox = new ComboBox<String>(appointmentLengthFilters);
			}
			
			filterMethodsComboBox.setMaxWidth(Double.MAX_VALUE);
			GridPane.setHgrow(filterMethodsComboBox, Priority.ALWAYS);
			
			filterMethodParamHBox = new HBox();
			filterMethodParamHBox.setVisible(false);
			errorLabel = new Label("ERROR");
			errorLabel.setTextFill(Color.RED);
			errorLabel.setVisible(false);
			addFilterMethodButton = new Button("+");
			addFilterMethodButton.setVisible(false);
			
			//=====================================
			
			add(deleteFilterMethodButton,1,0);
			GridPane.setHalignment(deleteFilterMethodButton, HPos.RIGHT);
			GridPane.setValignment(deleteFilterMethodButton, VPos.BOTTOM);
			add(filterMethodsVBox,0,0);
			add(filterMethodsComboBox,0,1,2,1);
			add(addFilterMethodButton,2,1);
			add(filterMethodParamHBox,0,2,3,1);
			add(deleteFilterViewButton, 1,3);
			add(errorLabel,0,3);
			
			//=====================================
			
			addFilterMethodButton.setOnAction(event -> {
				//wenn alle richtig ausgefüllt ein Label damit hinzufügen + bei Filter hinzufügen
				errorLabel.setVisible(false);
				String filterMethodName = filterMethodsComboBox.getSelectionModel().getSelectedItem();
				if(!filterMethodParamComboBox.getSelectionModel().isEmpty()){
					filterMethods.add(filterMethodName);
					Label filterLabel = new Label(filterMethodName + ": " + filterMethodParamComboBox.getSelectionModel().getSelectedItem()); 
					filterMethodsVBox.getChildren().add(filterLabel);
					filter.addFilter(filterMethodName, filterMethodParamComboBox.getSelectionModel().getSelectedItem());
				}else if(!filterMethodParamPrefixComboBox.getSelectionModel().isEmpty() && !filterMethodParamTextField.getText().equals("")) {
					filterMethods.add(filterMethodName);
					Label filterLabel = new Label(filterMethodName + " " + filterMethodParamPrefixComboBox.getSelectionModel().getSelectedItem() + " " + filterMethodParamTextField.getText());
					filterMethodsVBox.getChildren().add(filterLabel);
					filter.addFilter(filterMethodName, filterMethodParamPrefixComboBox.getSelectionModel().getSelectedItem(), filterMethodParamTextField.getText());
				}else if(!filterMethodParamPrefixComboBox.getSelectionModel().isEmpty() && (filterMethodParamDatePicker.getValue()!=null)) {
					filterMethods.add(filterMethodName);
					Label filterLabel = new Label(filterMethodName + " " + filterMethodParamPrefixComboBox.getSelectionModel().getSelectedItem() + " " + filterMethodParamDatePicker.getValue());
					filterMethodsVBox.getChildren().add(filterLabel);
					filter.addFilter(filterMethodName, filterMethodParamPrefixComboBox.getSelectionModel().getSelectedItem(), filterMethodParamDatePicker.getValue());
				}else {
					errorLabel.setVisible(true);
					errorLabel.setText("Es gibt unvollständige Filter");
					return;
				}
				deleteFilterMethodButton.setVisible(true);
				//Methode aus der Combobox löschen
				//wenn ComboBox leer ausblenden
				//wenn ComboBox nicht leer nichts auswählen und MethodGridPane ausblenden
				if (sourceComboBox.getValue().equals("Studenten")) {
					studentFilters.remove(filterMethodsComboBox.getValue());
					if(studentFilters.isEmpty()) {
						filterMethodsComboBox.setVisible(false);
					}
					filterMethodsComboBox.getSelectionModel().clearSelection();
					filterMethodParamHBox.setVisible(false);
					
				}else if (sourceComboBox.getValue().equals("Anliegen")) {
					concernFilters.remove(filterMethodsComboBox.getValue());
					if(concernFilters.isEmpty()) {
						filterMethodsComboBox.setVisible(false);
					}
					filterMethodsComboBox.getSelectionModel().clearSelection();
					filterMethodParamHBox.setVisible(false);
					
				}else if (sourceComboBox.getValue().equals("Terminlängen")) {
					appointmentLengthFilters.remove(filterMethodsComboBox.getValue());
					if(appointmentLengthFilters.isEmpty()) {
						filterMethodsComboBox.setVisible(false);
					}
					filterMethodsComboBox.getSelectionModel().clearSelection();
					filterMethodParamHBox.setVisible(false);
					
				}
			});
			
			
			filterMethodsComboBox.setOnAction(event -> {
				addFilterMethodButton.setVisible(true);
				//filterMethodParamGridPane anpassen je möglichen Combobox-Eintrag
				String method = filterMethodsComboBox.getSelectionModel().getSelectedItem();
				filterMethodParamComboBox = new ComboBox<Object>();
				filterMethodParamDatePicker = new DatePicker();
				filterMethodParamPrefixComboBox = new ComboBox<String>(FXCollections.observableArrayList("<", ">", "="));
				filterMethodParamTextField = new TextField();
				// force the field to be numeric only
				filterMethodParamTextField.textProperty().addListener(new ChangeListener<String>() {
				    @Override
				    public void changed(ObservableValue<? extends String> observable, String oldValue, 
				        String newValue) {
				        if (!newValue.matches("\\d*")) {
				            filterMethodParamTextField.setText(newValue.replaceAll("[^\\d]", ""));
				        }
				    }
				});
				filterMethodParamHBox.getChildren().clear();
				if(method!=null) {
					switch (method) {
					case "Thema":
						filterMethodParamComboBox = new ComboBox<Topic>(presenter.getTopics());
						filterMethodParamHBox.getChildren().add(filterMethodParamComboBox);
						break;
					case "Status":
						filterMethodParamComboBox = new ComboBox<String>(FXCollections.observableArrayList("Offen", "Beendet", "Abgebrochen"));
						filterMethodParamHBox.getChildren().add(filterMethodParamComboBox);
						break;
					case "Geschlecht":
						filterMethodParamComboBox = new ComboBox<String>(FXCollections.observableArrayList("unbekannt", "männlich", "weiblich", "divers"));
						filterMethodParamHBox.getChildren().add(filterMethodParamComboBox);
						break;
					case "PO":
						filterMethodParamComboBox = new ComboBox<PO>(presenter.getPOs());
						filterMethodParamHBox.getChildren().add(filterMethodParamComboBox);
						break;
					case "Letzter Kontakt":
						filterMethodParamPrefixComboBox = new ComboBox<String>(FXCollections.observableArrayList("vor", "nach", "am"));
						filterMethodParamHBox.getChildren().add(filterMethodParamPrefixComboBox);
						filterMethodParamHBox.getChildren().add(filterMethodParamDatePicker);
						break;
					case "Anzahl der Termine":
					case "Gesamtlänge der Termine in h":
					case "Anzahl der Studenten":
					case "ECTS":
					case "Betreuungszeit in h":
					case "Semester":
					case "Anzahl zugehöriger Anliegen":
						filterMethodParamHBox.getChildren().add(filterMethodParamPrefixComboBox);
						filterMethodParamHBox.getChildren().add(filterMethodParamTextField);
					}
				}
				//filterMethodParamGridPane einblenden
				filterMethodParamHBox.setVisible(true);
			});
			
			
			deleteFilterViewButton.setOnAction(event -> {
				parentView.deleteFilter(this);
			});
			
			
			deleteFilterMethodButton.setOnAction(event -> {
				//Methode wieder der ComboBox hinzufügen und letzte FilterMethod löschen
				int filterMethodSize = filterMethods.size();
				if (sourceComboBox.getValue().equals("Studenten")) {
					studentFilters.add(filterMethods.get(filterMethodSize-1));
				}else if (sourceComboBox.getValue().equals("Anliegen")) {
					concernFilters.add(filterMethods.get(filterMethodSize-1));
				}else if (sourceComboBox.getValue().equals("Terminlängen")) {
					appointmentLengthFilters.add(filterMethods.get(filterMethodSize-1));
				}
				filter.deleteFilter(filterMethods.get(filterMethodSize-1));
				filterMethods.remove(filterMethodSize-1);
				filterMethodsVBox.getChildren().remove(filterMethodSize-1);
				
				//wenn keine Filtermethoden mehr da, deleteButton ausblenden
				if (filterMethodSize == 1) {
					deleteFilterMethodButton.setVisible(false);
				}
			});
			
			
		}
		
		public Filter getFilter()
		{
			return filter;
		}
	}



}

