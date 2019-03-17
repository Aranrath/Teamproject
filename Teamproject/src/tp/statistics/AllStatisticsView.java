package tp.statistics;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import tp.Presenter;
import tp.model.Statistic;

public class AllStatisticsView extends GridPane{
	

	private Presenter presenter;
	
	private TableView<Statistic> allStatisticsTable;
	private Label searchBar;
	private Button newConcernButton;
	private Button deleteConcernButton;
	
	public AllStatisticsView(Presenter presenter)
	{
		this.presenter = presenter;
		buildView();
		fillView();
	}

	//TODO ?
	@SuppressWarnings("unchecked")
	private void buildView() {
		setPadding(new Insets(20));
		setHgap(20);
		setVgap(20);

		allStatisticsTable = new TableView<Statistic>();
		searchBar = new Label("Hier sollte die SearchBar sein");
		newConcernButton = new Button("Neues Anliegen hinzufügen");
		deleteConcernButton = new Button("Anliegen löschen");
		
		
		// =====================================================================

		add(searchBar, 0, 0);
		GridPane.setHalignment(searchBar, HPos.LEFT);

		add(allStatisticsTable, 0, 1, 2, 1);
		
		add(newConcernButton, 1, 2);
		GridPane.setHalignment(newConcernButton, HPos.RIGHT);

		add(deleteConcernButton, 0, 2);
		GridPane.setHalignment(deleteConcernButton, HPos.LEFT);



		// ======================================================================
		
		TableColumn<Statistic, String> titleCol = new TableColumn<Statistic, String>("Titel");
//		TableColumn<Statistic, String> topicNameCol = new TableColumn<Concern, String>("Thema");
//		TableColumn<Statistic, Date> nextAppointmentDateAndTimeCol = new TableColumn<Concern, Date>("Nächster Termin");
//		TableColumn<Statistic, String> students = new TableColumn<Concern, String>("Studenten");
	

		allStatisticsTable.getColumns().addAll(titleCol);
		

		// =====================================================================

		newConcernButton.setOnAction((event) -> {
			presenter.openNewStatisticTab();
		});

		deleteConcernButton.setOnAction((event) -> {
			Statistic statisticToDelete = allStatisticsTable.getSelectionModel().getSelectedItem();
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
	}
	

	private void fillView() {
		// TODO Auto-generated method stub
		
	}

}
