package tp.concern;

import java.sql.Date;

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
import tp.model.Concern;

public class AllConcernsView extends GridPane
{
	private Presenter presenter;
	
	private TableView<Concern> allConcernsTable;
	private Label searchBar;
	private Button newConcernButton;
	private Button deleteConcernButton;
	
	public AllConcernsView(Presenter presenter)
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

		allConcernsTable = new TableView<Concern>();
		searchBar = new Label("Hier sollte die SearchBar sein");
		newConcernButton = new Button("Neues Anliegen hinzufügen");
		deleteConcernButton = new Button("Anliegen löschen");
		
		
		// =====================================================================

		add(searchBar, 0, 0);
		GridPane.setHalignment(searchBar, HPos.LEFT);

		add(allConcernsTable, 0, 1, 2, 1);
		
		add(newConcernButton, 1, 2);
		GridPane.setHalignment(newConcernButton, HPos.RIGHT);

		add(deleteConcernButton, 0, 2);
		GridPane.setHalignment(deleteConcernButton, HPos.LEFT);



		// ======================================================================
		
		TableColumn<Concern, String> titleCol = new TableColumn<Concern, String>("Titel");
		TableColumn<Concern, String> topicNameCol = new TableColumn<Concern, String>("Thema");
		TableColumn<Concern, Date> nextAppointmentDateAndTimeCol = new TableColumn<Concern, Date>("Nächster Termin");
		TableColumn<Concern, String> students = new TableColumn<Concern, String>("Studenten");
	

		allConcernsTable.getColumns().addAll(titleCol, topicNameCol,nextAppointmentDateAndTimeCol, students);
		

		// =====================================================================

		newConcernButton.setOnAction((event) -> {
			presenter.openNewConcernTab();
		});

		deleteConcernButton.setOnAction((event) -> {
			Concern concernToDelete = allConcernsTable.getSelectionModel().getSelectedItem();
			Alert alert = new Alert(AlertType.WARNING, "Anliegen \"" + concernToDelete.getTitle() + "\" wirklich aus der Datenbank löschen?",
					ButtonType.YES, ButtonType.CANCEL);
			alert.showAndWait();

			if (alert.getResult() == ButtonType.YES) {
				
				presenter.deleteConcern(concernToDelete);
			}

			updateConcerns();

		});
	}
	

	private void fillView() {
		// TODO Auto-generated method stub
		
	}
	
	private void updateConcerns() {
		/*TODO Alle Tabs die Aliegen betreffen könnten behandel
		 *  Anliegen in Tab offen -> Tab schließen
		 *	Kalendare updaten
		 */
		
	}
	
}
