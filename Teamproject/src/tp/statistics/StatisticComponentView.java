package tp.statistics;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
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
import tp.model.statistics.StatisticComponent;
import tp.model.statistics.StatisticComponent.Filter;

public class StatisticComponentView extends HBox
{
	private ArrayList<FilterView> filterGridPanes;
	private EditStatisticView editStatisticView;
	
	//=====================================
	
	private GridPane basicOptionsGridPane;
	private Label nameLabel;
	private TextField nameTextField;
	private Label sourceLabel;
	private ComboBox<String> sourceComboBox;
	private Label colorLabel;
	private ColorPicker colorPicker;
	private Label errorLabel;
	private Button deleteStatisticComponentButton;
	private Button addFilterButton;
	
	public StatisticComponentView(EditStatisticView editStatisticView)
	{
		this.editStatisticView = editStatisticView;
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
		sourceComboBox = new ComboBox<String>(FXCollections.observableArrayList("Studenten","Anliegen"));
		sourceComboBox.setMaxWidth(Double.MAX_VALUE);
		HBox.setHgrow(sourceComboBox, Priority.ALWAYS);
		
		colorLabel = new Label("Farbe ");
		colorPicker = new ColorPicker();
		colorPicker.setMaxWidth(Double.MAX_VALUE);
		HBox.setHgrow(colorPicker, Priority.ALWAYS);
		
		deleteStatisticComponentButton = new Button("Löschen");
		errorLabel = new Label("ERROR");
		errorLabel.setVisible(false);
		errorLabel.setTextFill(Color.RED);
		addFilterButton = new Button("+");
			
		basicOptionsGridPane.add(nameLabel, 0, 0);
		basicOptionsGridPane.add(nameTextField, 1, 0);
		basicOptionsGridPane.add(sourceLabel, 0, 1);
		basicOptionsGridPane.add(sourceComboBox, 1, 1);
		basicOptionsGridPane.add(colorLabel, 0, 2);
		basicOptionsGridPane.add(colorPicker, 1, 2);
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
			}

		});
		
		sourceComboBox.setOnAction(event ->{
			
			if(!filterGridPanes.isEmpty())
			{
				//TODO BUUGS
				event.consume();
				errorLabel.setVisible(true);
				errorLabel.setText("Lösche erst die Filter auf dieser Quelle");
				return;
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
		Color color = colorPicker.getValue();
		ArrayList<Filter> selectedFilter = new ArrayList<Filter>();
		for(FilterView filter: filterGridPanes) {
			selectedFilter.add(filter.getFilter());
		}
		StatisticComponent result = new StatisticComponent(name, source, color, selectedFilter);
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
		private GridPane filterMethodParamGridPane;
		private Label errorLabel;
		private Button addFilterMethodButton;
		
		public FilterView(StatisticComponentView parentView)
		{
			filter = new Filter();
			this.parentView = parentView;
			buildFilterGridPane();	
		}
		
		
		private void buildFilterGridPane()
		{
			filterMethodsVBox = new VBox();
			deleteFilterViewButton = new Button("Filter löschen");
			GridPane.setHalignment(deleteFilterViewButton, HPos.RIGHT);
			ObservableList<String> studentFilters = FXCollections.observableArrayList(StatisticComponent.STUDENT_FILTER_METHODS);
			ObservableList<String> concernFilters = FXCollections.observableArrayList(StatisticComponent.CONCERN_FILTERS_METHODS);
			
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
			
			filterMethodsComboBox.setMaxWidth(Double.MAX_VALUE);
			GridPane.setHgrow(filterMethodsComboBox, Priority.ALWAYS);
			
			filterMethodParamGridPane = new GridPane();
			filterMethodParamGridPane.setVisible(false);
			errorLabel = new Label("ERROR");
			errorLabel.setTextFill(Color.RED);
			errorLabel.setVisible(false);
			addFilterMethodButton = new Button("+");
			addFilterMethodButton.setVisible(false);
			
			//=====================================
			
			add(deleteFilterMethodButton,0,0);
			GridPane.setHalignment(deleteFilterMethodButton, HPos.RIGHT);
			GridPane.setValignment(deleteFilterMethodButton, VPos.BOTTOM);
			add(filterMethodsVBox,1,0);
			add(filterMethodsComboBox,0,1);
			add(addFilterMethodButton,1,1);
			add(filterMethodParamGridPane,0,2,2,1);
			add(deleteFilterViewButton, 0,3);
			add(errorLabel,1,3);
			
			//=====================================
			
			addFilterMethodButton.setOnAction(event -> {
				deleteFilterMethodButton.setVisible(true);
				//TODO wenn alle richtig ausgefüllt ein Label damit hinzufügen + bei Filter hinzufügen
				
				//Methode aus der Combobox löschen
				//wenn ComboBox leer ausblenden
				//wenn ComboBox nicht leer nichts auswählen und MethodGridPane ausblenden
				if (sourceComboBox.getValue().equals("Studenten")) {
					studentFilters.remove(filterMethodsComboBox.getValue());
					if(studentFilters.isEmpty()) {
						sourceComboBox.setVisible(false);
					}
					sourceComboBox.getSelectionModel().clearSelection();
					filterMethodParamGridPane.setVisible(false);
					
				}else if (sourceComboBox.getValue().equals("Anliegen")) {
					concernFilters.remove(filterMethodsComboBox.getValue());
					if(concernFilters.isEmpty()) {
						sourceComboBox.setVisible(false);
					}
					sourceComboBox.getSelectionModel().clearSelection();
					filterMethodParamGridPane.setVisible(false);
					
				}
			});
			
			filterMethodsComboBox.setOnAction(event -> {
				addFilterMethodButton.setVisible(true);
				
				//filterMethodParamGridPane einblenden
				filterMethodParamGridPane.setVisible(true);
				//TODO filterMethodParamGridPane anpassen je möglichen Combobox-Eintrag
			});
			
			deleteFilterViewButton.setOnAction(event -> {
				parentView.deleteFilter(this);
			});
			
			deleteFilterMethodButton.setOnAction(event -> {
				//TODO letzte FilterMethod löschen und Methode wieder der ComboBox hinzufügen
				if (sourceComboBox.getValue().equals("Studenten")) {
					
				}else if (sourceComboBox.getValue().equals("Anliegen")) {
					
				}
				//TODO wenn keine Filtermethoden mehr da, deleteButton ausblenden
			});
			
			
		}
		
		public Filter getFilter()
		{
			//TODO is filter geupdated??
			return filter;
		}
	}



}

