package tp.concern;

import java.sql.Date;

import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
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
	}

	//TODO ?
	@SuppressWarnings("unchecked")
	private void buildView() {
		setPadding(new Insets(20));
		setHgap(20);
		setVgap(20);

		allConcernsTable = new TableView<Concern>(presenter.getConcerns());
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


		//===================================================================

		ColumnConstraints col0 = new ColumnConstraints();
		col0.setPercentWidth(30);
		ColumnConstraints col1 = new ColumnConstraints();
		col1.setPercentWidth(30);
		     
		getColumnConstraints().addAll(col0,col1);
		
		// ======================================================================
		
		TableColumn<Concern, String> titleCol = new TableColumn<Concern, String>("Titel");
		titleCol.setCellValueFactory(new PropertyValueFactory<Concern, String>("title"));
		
		TableColumn<Concern, String> topicNameCol = new TableColumn<Concern, String>("Thema");
		topicNameCol.setCellValueFactory(new PropertyValueFactory<Concern, String>("topic"));
		
		TableColumn<Concern, Date> nextAppointmentDateAndTimeCol = new TableColumn<Concern, Date>("Nächster Termin");
		nextAppointmentDateAndTimeCol.setCellValueFactory(new PropertyValueFactory<Concern, Date>("nextAppointment"));
		
		TableColumn<Concern, String> students = new TableColumn<Concern, String>("Studenten");
		students.setCellValueFactory(new PropertyValueFactory<Concern, String>("studentsString"));
		
		double width = titleCol.widthProperty().get() + topicNameCol.widthProperty().get() + nextAppointmentDateAndTimeCol.widthProperty().get();
		students.prefWidthProperty().bind(allConcernsTable.widthProperty().subtract(width));
		
		
		allConcernsTable.getColumns().addAll(titleCol, topicNameCol, nextAppointmentDateAndTimeCol, students);

		// =====================================================================

		newConcernButton.setOnAction((event) -> {
			presenter.openNewConcernTab();
		});

		deleteConcernButton.setOnAction((event) -> {
			Concern concernToDelete = allConcernsTable.getSelectionModel().getSelectedItem();
			if (concernToDelete == null)
			{
				return;
			}
			Alert alert = new Alert(AlertType.WARNING, "Anliegen \"" + concernToDelete.getTitle() + "\" wirklich aus der Datenbank löschen?",
					ButtonType.YES, ButtonType.CANCEL);
			alert.showAndWait();

			if (alert.getResult() == ButtonType.YES) {
				
				presenter.deleteConcern(concernToDelete);
			}


		});
		
		allConcernsTable.setOnMousePressed(new EventHandler<MouseEvent>() {
		    @Override 
		    public void handle(MouseEvent event) {
		        if (event.isPrimaryButtonDown() && event.getClickCount() > 1) {
		        	Concern selectedConcern = allConcernsTable.getSelectionModel().getSelectedItem();
		        	if(selectedConcern != null)
		        	{
		        		presenter.openConcernTab(selectedConcern);
		        	}
		                               
		        }
		    }
		});
		
		
	}
	


	public void updateView() {
		allConcernsTable.setItems(presenter.getConcerns());
		//TODO updateSearchbar
	}

	
}
