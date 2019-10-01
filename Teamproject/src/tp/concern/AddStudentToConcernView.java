package tp.concern;

import java.sql.Date;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tp.Presenter;
import tp.model.Student;

public class AddStudentToConcernView extends GridPane{
	
	private Stage stage;
	private ConcernView concernView;
	private ObservableList<Student> selectedStudents;
	private ObservableList<Student> addableStudents;
	private ObservableList<Student> shownStudents;
	private ObservableList<Student> allStudents;
	
	//-------------------------GUI
	
	private TableView<Student> addableStudentsTable;
	private TableView<Student> selectedStudentsTable;
	private Label selectionLabel;
	private Button toRightButton;
	private Button toLeftButton;
	private Button allToLeftButton;
	private Button saveButton;
	private TextField searchTextField;
	
	public AddStudentToConcernView(Presenter presenter, Stage stage, ConcernView concernView, ObservableList<Student> alreadyInConcernStudents)
	{
		this.stage = stage;
		this.concernView = concernView;
		
		//get nur nich exmatrikulierte Studenten
		this.allStudents = presenter.getStudents();
		this.addableStudents = FXCollections.observableArrayList();
		for (Student student : allStudents)
		{
			if(student.getExmatr() == null)
			{
				addableStudents.add(student);
			}	
		}
		this.selectedStudents = FXCollections.observableArrayList(alreadyInConcernStudents);
		/*
		 * Remove all funktioniert an dieser Stelle nicht.
		 * Da die Studentenobjekte getrennt aus der Datenbank ausgelesen wurden sind es nicht die selben Objekte
		 * -> manuelles Vergleichen der Student-id und löschen
		*/
		for (Student inStu: selectedStudents) {
			for (Student allStu: addableStudents) {
				if(inStu.getMtrNr() == allStu.getMtrNr()) {
					addableStudents.remove(allStu);
					break;
				}
			}
		}
		this.shownStudents = FXCollections.observableArrayList(addableStudents);
		
		buildView();
	}

	@SuppressWarnings("unchecked")
	private void buildView() {
		setPadding(new Insets(10, 10, 10, 10));
		setHgap(10);
		setVgap(10);
		
		addableStudentsTable = new TableView<Student>(shownStudents);
		selectedStudentsTable = new TableView<Student>(selectedStudents);
		searchTextField = new TextField();
		searchTextField.setPromptText("Suche Studenten");
		selectionLabel = new Label("Auswahl");
		toRightButton = new Button(">");
		toLeftButton = new Button("<");
		allToLeftButton = new Button("<<");
		saveButton = new Button("Studenten im Anliegen übernehmen");
		
		// =====================================================================

				add(searchTextField, 0, 0);
				GridPane.setHalignment(searchTextField, HPos.LEFT);
				
				add(addableStudentsTable, 0, 1, 1, 2);

				VBox leftRightButtonBox = new VBox();
				leftRightButtonBox.getChildren().addAll(toLeftButton, toRightButton, allToLeftButton);
				leftRightButtonBox.setSpacing(10);
				toRightButton.setMaxWidth(Double.MAX_VALUE);
				toLeftButton.setMaxWidth(Double.MAX_VALUE);
				allToLeftButton.setMaxWidth(Double.MAX_VALUE);
				toRightButton.setMinSize(Button.USE_PREF_SIZE, Button.USE_PREF_SIZE);
				toLeftButton.setMinSize(Button.USE_PREF_SIZE, Button.USE_PREF_SIZE);
				allToLeftButton.setMinSize(Button.USE_PREF_SIZE, Button.USE_PREF_SIZE);
				leftRightButtonBox.setAlignment(Pos.CENTER);
				add(leftRightButtonBox, 1, 1);

				add(selectionLabel, 2, 0);
				selectionLabel.setMinSize(Label.USE_PREF_SIZE, Label.USE_PREF_SIZE);
				
				add(selectedStudentsTable, 2, 1);
				
				add(saveButton, 2, 2);
				saveButton.setMinSize(Button.USE_PREF_SIZE, Button.USE_PREF_SIZE);
				GridPane.setHalignment(saveButton, HPos.RIGHT);

				// ======================================================================

				TableColumn<Student, String> lastNameCol = new TableColumn<Student, String>("Nachname");
				lastNameCol.setCellValueFactory(new PropertyValueFactory<Student, String>("name"));
				
				TableColumn<Student, String> firstNameCol = new TableColumn<Student, String>("Vorname");
				firstNameCol.setCellValueFactory(new PropertyValueFactory<Student, String>("firstName"));
				
				TableColumn<Student, Integer> mtrNrCol = new TableColumn<Student, Integer>("Matrikelnr.");
				mtrNrCol.setCellValueFactory(new PropertyValueFactory<>("mtrNr"));
				
				TableColumn<Student, Date> lastContactCol = new TableColumn<Student, Date>("Letzter Kontakt");
				lastContactCol.setCellValueFactory(new PropertyValueFactory<>("lastContact"));
				
				addableStudentsTable.getColumns().addAll(mtrNrCol, lastNameCol,firstNameCol,lastContactCol);
				
				
				TableColumn<Student, String> lastNameCol2 = new TableColumn<Student, String>("Nachname");
				lastNameCol2.setCellValueFactory(new PropertyValueFactory<Student, String>("name"));
				
				TableColumn<Student, String> firstNameCol2 = new TableColumn<Student, String>("Vorname");
				firstNameCol2.setCellValueFactory(new PropertyValueFactory<Student, String>("firstName"));
				
				TableColumn<Student, Integer> mtrNrCol2 = new TableColumn<Student, Integer>("Matrikelnr.");
				mtrNrCol2.setCellValueFactory(new PropertyValueFactory<>("mtrNr"));
				
				selectedStudentsTable.getColumns().addAll(mtrNrCol2, lastNameCol2, firstNameCol2);

				// =====================================================================
				//Constraints
				
				ColumnConstraints tableCol = new ColumnConstraints();
				tableCol.setHgrow(Priority.ALWAYS);
				ColumnConstraints buttonCol = new ColumnConstraints();
				
				getColumnConstraints().addAll(tableCol, buttonCol,tableCol);
						
				RowConstraints buttonRow = new RowConstraints();
				RowConstraints tableRow = new RowConstraints();
				tableRow.setVgrow(Priority.ALWAYS);
				
				setMinHeight(leftRightButtonBox.getPrefHeight() + selectionLabel.getPrefHeight() + saveButton.getPrefHeight());
						     
				getRowConstraints().addAll(buttonRow,tableRow,buttonRow);
				
				// ======================================================================
				
				toRightButton.setOnAction((event) -> {
					List<Student> studentsToMove = addableStudentsTable.getSelectionModel().getSelectedItems();
					// First addAll, because studentsToMove will be empty after they have been deleted from allStudents
					selectedStudents.addAll(studentsToMove);
					shownStudents.removeAll(studentsToMove);
				});
				
				toLeftButton.setOnAction((event) -> {
					List<Student> studentsToMove = selectedStudentsTable.getSelectionModel().getSelectedItems();
					shownStudents.addAll(studentsToMove);
					selectedStudents.removeAll(studentsToMove);
				});
				
				allToLeftButton.setOnAction((event) -> {
					shownStudents.addAll(selectedStudents);
					selectedStudents.clear();
					
				});
				
				saveButton.setOnAction((event) -> {
					concernView.addStudentsToConcern(selectedStudentsTable.getItems());
					stage.close();
				});
				
				searchTextField.textProperty().addListener((obs, oldText, newText) -> {
					filterStudents(newText);
				});
	}

	private void filterStudents(String searchTerm) {
		shownStudents.clear();
		
		if(searchTerm.isEmpty())
		{
				shownStudents.addAll(addableStudents);
		}
		else
		{
			
			String [] searchTerms = searchTerm.toLowerCase().split(" ");
			for (Student student : addableStudents)
			{
				if(Presenter.containsAll(student.toString().toLowerCase(), searchTerms))
				{
					shownStudents.add(student);
				}	
			}
		}
		shownStudents.removeAll(selectedStudents);
		
	}

	

}
