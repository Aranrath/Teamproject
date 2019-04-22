package tp.concern;

import java.sql.Date;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import tp.Presenter;
import tp.model.Concern;

public class AllConcernsView extends GridPane
{
	private Presenter presenter;
	ObservableList<Concern> allConcerns;
	ObservableList<Concern> shownConcerns;
	
	private TableView<Concern> allConcernsTable;
	private TextField searchTextField;
	private Button newConcernButton;
	private Button deleteConcernButton;
	private CheckBox showClosedConcernsCheckBox;
	
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
		
		allConcerns = presenter.getConcerns();
		shownConcerns = FXCollections.observableArrayList(allConcerns);
		

		allConcernsTable = new TableView<Concern>(shownConcerns);
		searchTextField = new TextField();
		searchTextField.setPromptText("Suche Anliegen");
		newConcernButton = new Button("Neues Anliegen hinzufügen");
		deleteConcernButton = new Button("Anliegen löschen");
		showClosedConcernsCheckBox = new CheckBox("Zeige abgeschlossene Anliegen");
		
		// =====================================================================

		add(searchTextField, 0, 0);
		GridPane.setHalignment(searchTextField, HPos.LEFT);
		
		add(newConcernButton, 1, 0);
		GridPane.setHalignment(newConcernButton, HPos.RIGHT);

		add(allConcernsTable, 0, 1, 2, 1);
		
		add(showClosedConcernsCheckBox, 1, 2);
		GridPane.setHalignment(showClosedConcernsCheckBox, HPos.RIGHT);

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
			if (concernToDelete != null)
			{
				Alert alert = new Alert(AlertType.WARNING, "Anliegen \"" + concernToDelete.getTitle() + "\" wirklich aus der Datenbank löschen?",
						ButtonType.YES, ButtonType.CANCEL);
				alert.showAndWait();

				if (alert.getResult() == ButtonType.YES) {
					
					presenter.deleteConcern(concernToDelete);
					allConcernsTable.getItems().remove(concernToDelete);
					
				}
			}

		});
		
		showClosedConcernsCheckBox.setOnAction(event -> {
			filterConcerns(searchTextField.getText());
		});
		
		searchTextField.textProperty().addListener((obs, oldText, newText) -> {
			filterConcerns(newText);
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
	


	private void filterConcerns(String searchTerm) {
		
		shownConcerns.clear();
		
		if(searchTerm.isEmpty())
		{
			
			if(showClosedConcernsCheckBox.isSelected())
			{
				shownConcerns.addAll(allConcerns);
			}
			else
			{
				for (Concern concern : allConcerns)
				{
					if(!concern.isClosed())
					{
						shownConcerns.add(concern);
					}
					
				}
			}
	
		}
		else
		{
			
			String [] searchTerms = searchTerm.toLowerCase().split(" ");
			
			if(showClosedConcernsCheckBox.isSelected())
			{
				for (Concern concern : allConcerns)
				{
					if(ConcernView.containsAll(concern.toString().toLowerCase(), searchTerms))
					{
						shownConcerns.add(concern);
					}
					
				}
			}
			else
			{
				for (Concern concern : allConcerns)
				{
					if(!concern.isClosed() && ConcernView.containsAll(concern.toString().toLowerCase(), searchTerms))
					{
						shownConcerns.add(concern);
					}
					
				}
			}
			
			
			
		}
		
		
		
	}

	public void updateView() {
		allConcerns = presenter.getConcerns();
		shownConcerns = FXCollections.observableArrayList(allConcerns);
		filterConcerns(searchTextField.getText());
		
	}

	
}
