package tp.statistics;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import tp.Presenter;
import tp.model.statistics.Statistic;

public class AllStatisticsView extends GridPane{

	private Presenter presenter;
	private ObservableList<Statistic> allStatistics;
	private ObservableList<Statistic> shownStatistics;
	
	//-----------GUI
	
	private ListView<Statistic> allStatisticsListView;
	private TextField searchTextField;
	private Button newStatisticButton;
	private Button deleteStatisticButton;
	
	public AllStatisticsView(Presenter presenter)
	{
		this.presenter = presenter;
		buildView();
	}

	private void buildView() {
		setPadding(new Insets(20));
		setHgap(20);
		setVgap(20);

		
		allStatistics = presenter.getStatistics();
		shownStatistics = FXCollections.observableArrayList(allStatistics);
		
		allStatisticsListView = new ListView<Statistic>(shownStatistics);
		searchTextField = new TextField();
		searchTextField.setPromptText("Suche Statistik");
		newStatisticButton = new Button("Neue Statistik hinzufügen");
		deleteStatisticButton = new Button("Statistik löschen");
		
		
		// =====================================================================

		add(searchTextField, 0, 0);
		GridPane.setHalignment(searchTextField, HPos.LEFT);

		add(allStatisticsListView, 0, 1, 2, 1);
		
		add(newStatisticButton, 1, 2);
		GridPane.setHalignment(newStatisticButton, HPos.RIGHT);

		add(deleteStatisticButton, 0, 2);
		GridPane.setHalignment(deleteStatisticButton, HPos.LEFT);
		

		//===================================================================
		//constraints
		//TODO
				
		ColumnConstraints col = new ColumnConstraints();
		col.setPercentWidth(100 / 2);
				     
		getColumnConstraints().addAll(col,col);
				
		RowConstraints buttonRow = new RowConstraints();
		buttonRow.setPercentHeight(20 / 2);
		RowConstraints tableRow = new RowConstraints();
		tableRow.setPercentHeight(80);
				     
		getRowConstraints().addAll(buttonRow,tableRow,buttonRow);
				
		// ======================================================================
		newStatisticButton.setOnAction((event) -> {
			presenter.openNewStatisticTab();
		});

		deleteStatisticButton.setOnAction((event) -> {
			Statistic statisticToDelete = allStatisticsListView.getSelectionModel().getSelectedItem();
			if (statisticToDelete == null)
			{
				return;
			}
			Alert alert = new Alert(AlertType.WARNING, "Statistik \"" + statisticToDelete.getTitle() + "\" wirklich aus der Datenbank löschen?",
					ButtonType.YES, ButtonType.CANCEL);
			alert.showAndWait();

			if (alert.getResult() == ButtonType.YES) {
				
				presenter.deleteStatistic(statisticToDelete);
			}
		});
		
		searchTextField.textProperty().addListener((obs, oldText, newText) -> {
			filterStatistics(newText);
		});
		
		
		allStatisticsListView.setOnMousePressed(new EventHandler<MouseEvent>() {
		    @Override 
		    public void handle(MouseEvent event) {
		        if (event.isPrimaryButtonDown() && event.getClickCount() > 1) {
		        	Statistic selectedStatistic = allStatisticsListView.getSelectionModel().getSelectedItem();
		        	if(selectedStatistic != null)
		        	{
		        		presenter.openStatisticTab(selectedStatistic);
		        	}
		                               
		        }
		    }
		});
	}
	
	// ======================================================================
	//(private) Hilfs-Methoden
	
	private void filterStatistics(String searchTerm) {
		
		shownStatistics.clear();
		
		if(searchTerm.isEmpty())
		{
				shownStatistics.addAll(allStatistics);
		}
		else
		{
			
			String [] searchTerms = searchTerm.toLowerCase().split(" ");
			for (Statistic statistic : allStatistics)
			{
				if(Presenter.containsAll(statistic.toString().toLowerCase(), searchTerms))
				{
					shownStatistics.add(statistic);
				}	
			}
		}
	}

	// ======================================================================
	//öffentliche Methoden
	

	public void updateView() {
		allStatistics = presenter.getStatistics();
		shownStatistics = FXCollections.observableArrayList(allStatistics);
		filterStatistics(searchTextField.getText());	
	}

}
