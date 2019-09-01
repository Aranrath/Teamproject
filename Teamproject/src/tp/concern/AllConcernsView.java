package tp.concern;

import java.sql.Date;
import java.util.Calendar;
import java.util.Optional;

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
import javafx.scene.layout.RowConstraints;
import tp.Presenter;
import tp.model.Concern;

public class AllConcernsView extends GridPane
{
	private Presenter presenter;
	private ObservableList<Concern> allConcerns;
	private ObservableList<Concern> shownConcerns;
	
	//-----------GUI
	
	private TableView<Concern> allConcernsTable;
	private TextField searchTextField;
	private Button newConcernButton;
	private Button closeConcernButton;
	private CheckBox showClosedConcernsCheckBox;
	
	public AllConcernsView(Presenter presenter)
	{
		this.presenter = presenter;
		buildView();
		// to filter out all finished concerns
		filterConcerns("");
	}

	
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
		closeConcernButton = new Button("Anliegen schließen");
		showClosedConcernsCheckBox = new CheckBox("Zeige abgeschlossene Anliegen");
		
		// =====================================================================

		add(searchTextField, 0, 0);
		GridPane.setHalignment(searchTextField, HPos.LEFT);
		
		add(showClosedConcernsCheckBox, 1, 0);
		GridPane.setHalignment(showClosedConcernsCheckBox, HPos.RIGHT);

		add(allConcernsTable, 0, 1, 2, 1);
		
		add(newConcernButton, 1, 2);
		GridPane.setHalignment(newConcernButton, HPos.RIGHT);

		add(closeConcernButton, 0, 2);
		GridPane.setHalignment(closeConcernButton, HPos.LEFT);


		//===================================================================
		//constraints
		
		ColumnConstraints col = new ColumnConstraints();
		col.setPercentWidth(100 / 2);
		     
		getColumnConstraints().addAll(col,col);
		
		RowConstraints buttonRow = new RowConstraints();
		buttonRow.setPercentHeight(20 / 2);
		RowConstraints tableRow = new RowConstraints();
		tableRow.setPercentHeight(80);
		     
		getRowConstraints().addAll(buttonRow,tableRow,buttonRow);
		
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

		closeConcernButton.setOnAction((event) -> {
			Concern concernToClose = allConcernsTable.getSelectionModel().getSelectedItem();
			if (concernToClose != null)
			{

				Alert alert = new Alert(AlertType.CONFIRMATION);
		        alert.setTitle("Anliegen " +  concernToClose.getTitle() + " abschließen");
		        alert.setHeaderText("Bitte wählen sie den korrekten Abschluss-Status des Anliegen"
		        						+"\n" + "INFO: Abgeschlossene Anliegen sind (mit Ausnahme des Fehleintrages) weiterhin einsehbar."
		        						+"\n" + "ACHTUNG: Das Schließen eines Anliegens ist nicht umkehrbar");
		 
		        ButtonType completed = new ButtonType("Schließen mit Status \"Erledigt\"");
		        ButtonType uncompleted = new ButtonType("Schließen mit Status \"Abgebrochen\"");
		        ButtonType deletable = new ButtonType("Löschen als Fehleintrag");
		        ButtonType abortMission = new ButtonType("Abbrechen");
		 
		        // Standard ButtonTypes entfernen
		        alert.getButtonTypes().clear();
		 
		        //Eigene ButtonTypes hinzufügen
		        alert.getButtonTypes().addAll(completed, uncompleted, deletable,abortMission);
		 
		        //Alert anzeigen
		        Optional<ButtonType> option = alert.showAndWait();
		 
		        //Resultat verarbeiten
		        if (option.get() == completed)
		        {
		        	concernToClose.setCompleted(true);
		        	concernToClose.setClosingDate(new java.sql.Date(Calendar.getInstance().getTime().getTime()));
		            presenter.saveEditedConcern(concernToClose);
		            
		        }
		        else if (option.get() == uncompleted)
		        {
		        	concernToClose.setClosingDate(new java.sql.Date(Calendar.getInstance().getTime().getTime()));
		        	presenter.saveEditedConcern(concernToClose);
		        } 
		        else if (option.get() == deletable)
		        {
		        	presenter.deleteConcern(concernToClose);
		        	presenter.closeRelatedTabs(concernToClose);
		        }
		        else if (option.get() == abortMission)
		        {
		        	
		        }
			}
			updateView();

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
	
	// ======================================================================
	//(private) Hilfs-Methoden

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
					if(!concern.isCompleted())
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
					if(Presenter.containsAll(concern.getFilters().toLowerCase(), searchTerms))
					{
						shownConcerns.add(concern);
					}
					
				}
			}
			else
			{
				for (Concern concern : allConcerns)
				{
					if(!concern.isCompleted() && Presenter.containsAll(concern.getFilters().toLowerCase(), searchTerms))
					{
						shownConcerns.add(concern);
					}
					
				}
			}	
		}
	}
	
	// ======================================================================
	//öffentliche Methoden

	public void updateView() {
		allConcerns = presenter.getConcerns();
		shownConcerns.clear();
		for (Concern concern: allConcerns) {
			shownConcerns.add(concern);
		}
		filterConcerns(searchTextField.getText());
		
	}

	
}
