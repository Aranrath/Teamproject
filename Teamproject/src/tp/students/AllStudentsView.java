package tp.students;

import java.sql.Date;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import tp.Presenter;
import tp.model.Student;

public class AllStudentsView extends GridPane {

	private Presenter presenter;
	private ObservableList<Student> selectedStudents;
	private ObservableList<Student> allStudents;

	//================================
	
	private TableView<Student> allStudentsTable;
	private TableView<Student> selectedStudentsTable;
	private Label searchBar;
	private Label selectionLabel;
	private Button newStudentButton;
	private Button toRightButton;
	private Button toLeftButton;
	private Button allToLeftButton;
	private Button deleteStudentButton;
	private Button studentToNewConcernButton;

	public AllStudentsView(Presenter presenter) {
		this.presenter = presenter;
		buildView();
	}

	//TODO ?
	@SuppressWarnings("unchecked")
	private void buildView() {
		
		setPadding(new Insets(20));
		setHgap(20);
		setVgap(20);
		allStudents = presenter.getStudents();
		selectedStudents = FXCollections.observableArrayList();
		allStudentsTable = new TableView<Student>(allStudents);
		selectedStudentsTable = new TableView<Student>(selectedStudents);
		searchBar = new Label("Hier sollte die SearchBar sein");
		selectionLabel = new Label("Auswahl");
		newStudentButton = new Button("Neuen Studenten hinzufügen");
		toRightButton = new Button(">");
		toLeftButton = new Button("<");
		allToLeftButton = new Button("<<");
		deleteStudentButton = new Button("Studenten löschen");
		studentToNewConcernButton = new Button("Zu neuem Anliegen hinzufügen");

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

		add(deleteStudentButton, 4, 0);
		GridPane.setHalignment(deleteStudentButton, HPos.RIGHT);

		add(studentToNewConcernButton, 3, 4, 2, 1);
		GridPane.setHalignment(studentToNewConcernButton, HPos.RIGHT);

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
		
		deleteStudentButton.setOnAction((event) -> {
			Alert alert = new Alert(AlertType.WARNING, "Ausgewählte Studenten aus der Datenbank löschen?",
					ButtonType.YES, ButtonType.CANCEL);
			alert.showAndWait();

			if (alert.getResult() == ButtonType.YES) {
				for (Student s : selectedStudentsTable.getItems()) {
					presenter.deleteStudent(s);
				}
			}


		});
		studentToNewConcernButton.setOnAction((event) -> {
			presenter.openNewConcernTab(selectedStudentsTable.getItems());
		});
		
		
		allStudentsTable.setOnMousePressed(new EventHandler<MouseEvent>() {
		    @Override 
		    public void handle(MouseEvent event) {
		        if (event.isPrimaryButtonDown() && event.getClickCount() > 1) {
		        	Student selectedStudent = allStudentsTable.getSelectionModel().getSelectedItem();
		        	if(selectedStudent!=null)
		        	{
		        		presenter.openStudenTab(selectedStudent);  
		        	}
		                             
		        }
		    }
		});
		
		selectedStudentsTable.setOnMousePressed(new EventHandler<MouseEvent>() {
		    @Override 
		    public void handle(MouseEvent event) {
		        if (event.isPrimaryButtonDown() && event.getClickCount() > 1) {
		        	
		        	Student selectedStudent = selectedStudentsTable.getSelectionModel().getSelectedItem();
		        	if(selectedStudent!=null)
		        	{
		        		presenter.openStudenTab(selectedStudent);  
		        	}                 
		        }
		    }
		});

	}

	
	public void updateView()
	{
		allStudentsTable.setItems(presenter.getStudents());
		allStudentsTable.getItems().removeAll(selectedStudentsTable.getItems());
		
	}

}
