package tp.concern;

import java.sql.Date;
import java.util.List;

import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tp.Presenter;
import tp.model.Student;

public class AddStudentToConcernView extends GridPane{
	
	private Presenter presenter;
	private Stage stage;
	private ConcernView concernView;
	private ObservableList<Student> selectedStudents;
	private ObservableList<Student> allStudents;
	
	//=====================================
	
	TableView<Student> allStudentsTable;
	TableView<Student> selectedStudentsTable;
	Label searchBar;
	Label selectionLabel;
	Button newStudentButton;
	Button toRightButton;
	Button toLeftButton;
	Button allToLeftButton;
	Button studentToConcernButton;
	
	public AddStudentToConcernView(Presenter presenter, Stage stage, ConcernView concernView, ObservableList<Student> alreadyInConcernStudents)
	{
		this.presenter = presenter;
		this.stage = stage;
		this.concernView = concernView;
		this.selectedStudents = alreadyInConcernStudents;
		this.allStudents = presenter.getStudents();
		allStudents.removeAll(selectedStudents);
		
		buildView();
	}

	@SuppressWarnings("unchecked")
	private void buildView() {
		setPadding(new Insets(10, 10, 10, 10));
		setHgap(10);
		setVgap(10);
		
		allStudentsTable = new TableView<Student>(allStudents);
		selectedStudentsTable = new TableView<Student>(selectedStudents);
		searchBar = new Label("Hier sollte die SearchBar sein");
		selectionLabel = new Label("Auswahl");
		newStudentButton = new Button("Neuen Studenten hinzuf�gen");
		toRightButton = new Button(">");
		toLeftButton = new Button("<");
		allToLeftButton = new Button("<<");
		studentToConcernButton = new Button("Zum Anliegen hinzuf�gen");
		
		// =====================================================================

				add(searchBar, 0, 0);
				GridPane.setHalignment(searchBar, HPos.LEFT);

				add(newStudentButton, 1, 0);
				GridPane.setHalignment(newStudentButton, HPos.RIGHT);

				add(selectionLabel, 3, 0);

				VBox leftRightButtonBox = new VBox();
				leftRightButtonBox.getChildren().addAll(toLeftButton, toRightButton, allToLeftButton);
				leftRightButtonBox.setSpacing(10);
				toRightButton.setMaxWidth(Double.MAX_VALUE);
				toLeftButton.setMaxWidth(Double.MAX_VALUE);
				allToLeftButton.setMaxWidth(Double.MAX_VALUE);
				add(leftRightButtonBox, 2, 1,1,3);
				leftRightButtonBox.setAlignment(Pos.CENTER);

				add(studentToConcernButton, 3, 4, 2, 1);
				GridPane.setHalignment(studentToConcernButton, HPos.RIGHT);

				add(allStudentsTable, 0, 1, 2, 5);
				add(selectedStudentsTable, 3, 1, 2, 3);

				// ======================================================================

				TableColumn<Student, String> lastNameCol = new TableColumn<Student, String>("Nachname");
				lastNameCol.setCellValueFactory(new PropertyValueFactory<Student, String>("name"));
				
				TableColumn<Student, String> firstNameCol = new TableColumn<Student, String>("Vorname");
				firstNameCol.setCellValueFactory(new PropertyValueFactory<Student, String>("firstName"));
				
				TableColumn<Student, Integer> mtrNrCol = new TableColumn<Student, Integer>("Matrikelnr.");
				mtrNrCol.setCellValueFactory(new PropertyValueFactory<>("mtrNr"));
				
				TableColumn<Student, Date> lastContactCol = new TableColumn<Student, Date>("Letzter Kontakt");
				lastContactCol.setCellValueFactory(new PropertyValueFactory<>("lastContact"));
				
				allStudentsTable.getColumns().addAll(mtrNrCol, lastNameCol,firstNameCol,lastContactCol);
				
				
				TableColumn<Student, String> lastNameCol2 = new TableColumn<Student, String>("Nachname");
				lastNameCol2.setCellValueFactory(new PropertyValueFactory<Student, String>("name"));
				
				TableColumn<Student, String> firstNameCol2 = new TableColumn<Student, String>("Vorname");
				firstNameCol2.setCellValueFactory(new PropertyValueFactory<Student, String>("firstName"));
				
				TableColumn<Student, Integer> mtrNrCol2 = new TableColumn<Student, Integer>("Matrikelnr.");
				mtrNrCol2.setCellValueFactory(new PropertyValueFactory<>("mtrNr"));
				
				selectedStudentsTable.getColumns().addAll(mtrNrCol2, lastNameCol2, firstNameCol2);

				// =====================================================================

				newStudentButton.setOnAction((event) -> {
					//TODO Student is immernoch im Hintergrnd, kann nicht drauf zugegriffen werden
					presenter.openNewStudentTab();
				});
				
				toRightButton.setOnAction((event) -> {
					List<Student> studentsToMove = allStudentsTable.getSelectionModel().getSelectedItems();
					// First addAll, because studentsToMove will be empty after they have been deleted from allStudents
					selectedStudents.addAll(studentsToMove);
					allStudents.removeAll(studentsToMove); 
				});
				
				toLeftButton.setOnAction((event) -> {
					List<Student> studentsToMove = selectedStudentsTable.getSelectionModel().getSelectedItems();
					allStudents.addAll(studentsToMove);
					selectedStudents.removeAll(studentsToMove);
				});
				
				allToLeftButton.setOnAction((event) -> {
					allStudents.addAll(selectedStudents);
					selectedStudents.clear();
					
				});
				
				studentToConcernButton.setOnAction((event) -> {
					concernView.addStudentsToConcern(selectedStudentsTable.getItems());
					stage.close();
				});
		
		
		
	}

	

}
