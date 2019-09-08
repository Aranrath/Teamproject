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
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import tp.Presenter;
import tp.model.Student;

public class AllStudentsView extends GridPane {

	private Presenter presenter;
	private ObservableList<Student> selectedStudents;
	private ObservableList<Student> allStudents;
	private ObservableList<Student> shownStudents;

	//-------------------GUI
	
	private TableView<Student> allStudentsTable;
	private TableView<Student> selectedStudentsTable;
	private Label selectionLabel;
	private TextField searchTextField;
	private Button newStudentButton;
	private Button toRightButton;
	private Button toLeftButton;
	private Button allToLeftButton;
	private Button deleteStudentButton;
	private Button exmatrStudentButton;
	private Button studentToNewConcernButton;
	private DatePicker datePicker;
	private CheckBox showExmatrStudentsCheckBox;

	public AllStudentsView(Presenter presenter) {
		this.presenter = presenter;
		buildView();
		// to filter out all exmatrikulated Students
		filterStudents("");
	}

	@SuppressWarnings("unchecked")
	private void buildView() {
		
		setPadding(new Insets(20));
		setHgap(20);
		setVgap(20);
		
		allStudents = presenter.getStudents();
		shownStudents = FXCollections.observableArrayList(allStudents);
		allStudentsTable = new TableView<Student>(shownStudents);
		
		selectedStudents = FXCollections.observableArrayList();
		selectedStudentsTable = new TableView<Student>(selectedStudents);
		
		searchTextField = new TextField();
		searchTextField.setPromptText("Suche Student");
		showExmatrStudentsCheckBox = new CheckBox("Zeige exmatrikulierte Studenten");
		
		selectionLabel = new Label("Auswahl");
		newStudentButton = new Button("Neuen Studenten hinzufügen");
		toRightButton = new Button(">");
		toLeftButton = new Button("<");
		allToLeftButton = new Button("<<");
		deleteStudentButton = new Button("Ausgewählte löschen");
		exmatrStudentButton = new Button("Ausgewählte exmatrikulieren");
		studentToNewConcernButton = new Button("Zu neuem Anliegen hinzufügen");
		datePicker = new DatePicker();

		// =====================================================================

		add(searchTextField, 0, 0);
		GridPane.setHalignment(searchTextField, HPos.LEFT);

		add(newStudentButton, 1, 0);
		GridPane.setHalignment(newStudentButton, HPos.RIGHT);
		
		add(showExmatrStudentsCheckBox, 1, 1);
		GridPane.setHalignment(showExmatrStudentsCheckBox, HPos.RIGHT);

		add(selectionLabel, 3, 0);

		add(deleteStudentButton, 4, 0);
		GridPane.setHalignment(deleteStudentButton, HPos.RIGHT);
		
		add(datePicker, 3, 1);
		
		add(exmatrStudentButton, 4, 1);
		GridPane.setHalignment(exmatrStudentButton, HPos.RIGHT);
		
		VBox leftRightButtonBox = new VBox();
		leftRightButtonBox.getChildren().addAll(toLeftButton, toRightButton, allToLeftButton);
		leftRightButtonBox.setSpacing(10);
		toRightButton.setMaxWidth(Double.MAX_VALUE);
		toLeftButton.setMaxWidth(Double.MAX_VALUE);
		allToLeftButton.setMaxWidth(Double.MAX_VALUE);
		toRightButton.setMinSize(Button.USE_PREF_SIZE, Button.USE_PREF_SIZE);
		toLeftButton.setMinSize(Button.USE_PREF_SIZE, Button.USE_PREF_SIZE);
		allToLeftButton.setMinSize(Button.USE_PREF_SIZE, Button.USE_PREF_SIZE);
		
		add(leftRightButtonBox, 2, 2);
		leftRightButtonBox.setAlignment(Pos.CENTER);

		add(allStudentsTable, 0, 2, 2, 3);
		add(selectedStudentsTable, 3, 2, 2, 1);

		add(studentToNewConcernButton, 3, 3, 2, 1);
		GridPane.setHalignment(studentToNewConcernButton, HPos.RIGHT);

		// ======================================================================
		

		TableColumn<Student, String> lastNameCol = new TableColumn<Student, String>("Nachname");
		lastNameCol.setCellValueFactory(new PropertyValueFactory<Student, String>("name"));
		
		TableColumn<Student, String> firstNameCol = new TableColumn<Student, String>("Vorname");
		firstNameCol.setCellValueFactory(new PropertyValueFactory<Student, String>("firstName"));
		
		TableColumn<Student, String> mtrNrCol = new TableColumn<Student, String>("Matrikelnr.");
		mtrNrCol.setCellValueFactory(new PropertyValueFactory<Student, String>("mtrNrString"));
		
		TableColumn<Student, Date> poCol = new TableColumn<Student, Date>("Studiengang");
		poCol.setCellValueFactory(new PropertyValueFactory<>("po"));
		
		TableColumn<Student, Date> lastContactCol = new TableColumn<Student, Date>("Letzter Kontakt");
		lastContactCol.setCellValueFactory(new PropertyValueFactory<>("lastContact"));
		
		TableColumn<Student, Date> exmatrCol = new TableColumn<Student, Date>("Exmatrikuliert");
		exmatrCol.setCellValueFactory(new PropertyValueFactory<>("exmatr"));
		
		allStudentsTable.getColumns().addAll(mtrNrCol, lastNameCol,firstNameCol,poCol, lastContactCol, exmatrCol);
		
		TableColumn<Student, String> lastNameCol2 = new TableColumn<Student, String>("Nachname");
		lastNameCol2.setCellValueFactory(new PropertyValueFactory<Student, String>("name"));
		
		TableColumn<Student, String> firstNameCol2 = new TableColumn<Student, String>("Vorname");
		firstNameCol2.setCellValueFactory(new PropertyValueFactory<Student, String>("firstName"));
		
		TableColumn<Student, String> mtrNrCol2 = new TableColumn<Student, String>("Matrikelnr.");
		mtrNrCol2.setCellValueFactory(new PropertyValueFactory<Student, String>("mtrNrString"));
		
		selectedStudentsTable.getColumns().addAll(mtrNrCol2, lastNameCol2, firstNameCol2);

		
		//===================================================================
		//Constraints
				
		ColumnConstraints leftTableCol = new ColumnConstraints();
		leftTableCol.setPercentWidth(55 / 2);
		ColumnConstraints buttonCol = new ColumnConstraints();
		buttonCol.setPercentWidth(5);
		ColumnConstraints rightTableCol = new ColumnConstraints();
		rightTableCol.setPercentWidth(40 / 2);
		
		getColumnConstraints().addAll(leftTableCol, leftTableCol,buttonCol, rightTableCol, rightTableCol);
				
		//-------------------------------------------------
		
		RowConstraints buttonRow = new RowConstraints();
		buttonRow.setPercentHeight(20 / 4);
		
		RowConstraints tableRow = new RowConstraints();
		tableRow.setPercentHeight(80);
				     
		getRowConstraints().addAll(buttonRow, buttonRow, tableRow, buttonRow, buttonRow);
		
		// =====================================================================

		newStudentButton.setOnAction((event) -> {
			presenter.openNewStudentTab();
		});
		
		toRightButton.setOnAction((event) -> {
			List<Student> studentsToMove = allStudentsTable.getSelectionModel().getSelectedItems();
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
		
		deleteStudentButton.setOnAction((event) -> {
			Alert alert = new Alert(AlertType.WARNING, "Ausgewählte Studenten aus der Datenbank löschen? \n Dieser ist damit für zukünftige Statistiken nicht mehr verfügbar.",
					ButtonType.YES, ButtonType.CANCEL);
			alert.showAndWait();

			if (alert.getResult() == ButtonType.YES) {
				for (Student s : selectedStudents) {
					presenter.deleteStudent(s);
					presenter.closeRelatedTabs(s);
				}
				selectedStudents.clear();
			}


		});
		
		exmatrStudentButton.setOnAction((event)-> {
			if(datePicker.getValue() == null) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Fehlendes Datum");
				alert.setHeaderText("Bitte wählen sie ein Datum aus.");
				alert.showAndWait();
			}else {
				for(Student student: selectedStudents) {
					presenter.setStudentExmatr(student, Date.valueOf(datePicker.getValue()));
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
		
		showExmatrStudentsCheckBox.setOnAction(event -> {
			filterStudents(searchTextField.getText());
		});
		
		searchTextField.textProperty().addListener((obs, oldText, newText) -> {
			filterStudents(newText);
		});

	}
	
	//============================================================
	//(private) Hilfs-Methoden

	
	private void filterStudents(String searchTerm)
	{
		shownStudents.clear();
		
		if(searchTerm.isEmpty())
		{
			if(showExmatrStudentsCheckBox.isSelected()) {
				shownStudents.addAll(allStudents);
			}else {
				for(Student student: allStudents) {
					if(student.getExmatr() == null) {
						shownStudents.add(student);
					}
				}
			}
		}
		else
		{
			String [] searchTerms = searchTerm.toLowerCase().split(" ");
			
			if(showExmatrStudentsCheckBox.isSelected()) {
				for (Student student : allStudents)
				{
					if(Presenter.containsAll(student.toString().toLowerCase(), searchTerms))
					{
						shownStudents.add(student);
					}	
				}
			}else {
				for (Student student : allStudents)
				{
					if(student.getExmatr() == null && Presenter.containsAll(student.toString().toLowerCase(), searchTerms))
					{
						shownStudents.add(student);
					}	
				}
			}
		}
		shownStudents.removeAll(selectedStudents);
	}
	
	//============================================================
	//(public) Hilfs-Methoden

	public void updateView()
	{
		allStudents = presenter.getStudents();
		shownStudents.clear();
		for (Student student: allStudents) {
			shownStudents.add(student);
		}
		filterStudents(searchTextField.getText());	
	}

}
