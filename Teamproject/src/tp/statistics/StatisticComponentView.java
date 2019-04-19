package tp.statistics;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import tp.model.statistics.StatisticComponent;

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
		basicOptionsGridPane = new GridPane();
		nameLabel = new Label("Name:");
		nameTextField = new TextField();
		sourceLabel = new Label("Quelle");
		sourceComboBox = new ComboBox<String>(FXCollections.observableArrayList("Studenten","Anliegen"));
		colorLabel = new Label("Farbe:");
		colorPicker = new ColorPicker();
		deleteStatisticComponentButton = new Button("Statistik-Objekt löschen");
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
		
		getChildren().add(basicOptionsGridPane);
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
				getChildren().remove(addFilterButton);
				FilterView newFilterGridPane = new FilterView();
				filterGridPanes.add(newFilterGridPane);
				getChildren().addAll(newFilterGridPane, addFilterButton);
			}

		});
		
		sourceComboBox.setOnAction(event ->{
			if(!filterGridPanes.isEmpty())
			{
				event.consume();
				errorLabel.setVisible(true);
				errorLabel.setText("Lösche erst die Filter auf dieser Quelle");
			}
		});
		
		deleteStatisticComponentButton.setOnAction(event -> {
			editStatisticView.deleteStatisticComponent(this);
		});
		
	}
	
	public boolean checkStatisticComponentAvailability()
	{
		//TODO kontrolliert ob alle nötigen Felder (richtig) ausgefüllt sind um ein StatisticComponent zu erstellen
		return true;
	}
	
	public StatisticComponent getStatisticComponent()
	{
		//TODO Erstellt ein StatisticComponent und liefert dieses zurück
		return null;
	}
	
	
	//========================================================innere Klasse
	
	class FilterView extends GridPane
	{
		
		private StatisticComponent.Filter filter;
		
		//=======================================
		
		private VBox filterMethodsVBox;
		private Button deleteFilterMethodButton;
		private Button deleteFilterViewButton;
		private ComboBox<String> filterMethodsComboBox;
		private GridPane filterMethodParamGridPane;
		private Label errorLabel;
		private Button addFilterMethodButton;
		
		public FilterView()
		{
			filter = new StatisticComponent.Filter();
			buildFilterGridPane();	
		}
		
		private void buildFilterGridPane()
		{
			filterMethodsVBox = new VBox();
			deleteFilterViewButton = new Button("Filter löschen");
			deleteFilterMethodButton = new Button("-");
			if(sourceComboBox.getSelectionModel().getSelectedItem().equals("Studenten"))
			{
				filterMethodsComboBox = new ComboBox<String>(FXCollections.observableArrayList(StatisticComponent.STUDENT_FILTER_METHODS));
			}
			else if(sourceComboBox.getSelectionModel().getSelectedItem().equals("Anliegen"))
			{
				filterMethodsComboBox = new ComboBox<String>(FXCollections.observableArrayList(StatisticComponent.CONCERN_FILTERS_METHODS));
			}	
			filterMethodParamGridPane = new GridPane();
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
			
			//TODO Button Anbindung
		}	
		
		public StatisticComponent.Filter getFilter()
		{
			return filter;
		}
	}

}

