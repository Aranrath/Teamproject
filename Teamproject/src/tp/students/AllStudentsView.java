package tp.students;

import java.sql.Date;
import java.util.List;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import tp.Presenter;
import tp.model.Student;

public class AllStudentsView extends GridPane {

	private Presenter presenter;

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
		fillView();
	}

	//TODO ?
	@SuppressWarnings("unchecked")
	private void buildView() {
		
		setPadding(new Insets(20));
		setHgap(20);
		setVgap(20);

		allStudentsTable = new TableView<Student>();
		selectedStudentsTable = new TableView<Student>();
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
		TableColumn<Student, String> firstNameCol = new TableColumn<Student, String>("Vorname");
		TableColumn<Student, Integer> mtrNrCol = new TableColumn<Student, Integer>("Matrikelnr.");
		TableColumn<Student,Date> lastContactCol = new TableColumn<Student, Date>("Letzter Kontakt");
		TableColumn<Student, String> emailCol = new TableColumn<Student, String>("Email");

		allStudentsTable.getColumns().addAll(mtrNrCol, lastNameCol,firstNameCol,lastContactCol, emailCol);
		selectedStudentsTable.getColumns().addAll(mtrNrCol,lastNameCol, firstNameCol);

		// =====================================================================

		newStudentButton.setOnAction((event) -> {
			presenter.openNewStudentTab();
		});
		toRightButton.setOnAction((event) -> {
			List<Student> selectedStudents = allStudentsTable.getSelectionModel().getSelectedItems();
			
			//für jeden Studenten der der Auswahl hinzugefügt werden soll, gleiche mit jedem bereits ausgewählten Studenten ab, ob bereits ausgewählt. Wenn nicht, füge ihn der Auswahl hinzu; wenn ja, überspringe diesen Studenten
			for (Student unselectedS : selectedStudents)
			{
				boolean alreadySelected = false;
				
				for(Student selectedS : selectedStudentsTable.getItems())
				{		
					if(unselectedS.getMtrNr() == selectedS.getMtrNr())
					{
						alreadySelected = true;
						break;
					}
				}
				
				if(!alreadySelected)
				{
					//Studenten  zur Auswahl (rechts) hinzufügen, NICHT aus linker Auswahl entfernen (?)
					selectedStudentsTable.getItems().add(unselectedS);
				}
					
			}
		});
		toLeftButton.setOnAction((event) -> {
		});
		allToLeftButton.setOnAction((event) -> {
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

			updateStudents();

		});
		studentToNewConcernButton.setOnAction((event) -> {
			presenter.openNewConcernTab(selectedStudentsTable.getItems());
		});

	}



	private void fillView() {
		//TODO

	}
	
	private void updateStudents() {
		/*TODO Alle Tabs die Studenten betreffen könnten behandel
		 *  Student in Tab offen -> Tab schließen
		 *  Student in Anliegen -> löschen? / vermerken???
		 */
		
		
	}

}
