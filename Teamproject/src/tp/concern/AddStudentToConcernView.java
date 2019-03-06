package tp.concern;

import java.sql.Date;
import java.util.List;

import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import tp.Presenter;
import tp.model.Student;

public class AddStudentToConcernView extends GridPane{
	
	private Presenter presenter;
	
	//=====================================
	
	TableView<Student> allStudentsTable;
	TableView<Student> selectedStudentsTable;
	Label searchBar;
	Label selectionLabel;
	Button newStudentButton;
	Button toRightButton;
	Button toLeftButton;
	Button allToLeftButton;
	Button deleteStudentButton;
	Button studentToConcernButton;
	
	public AddStudentToConcernView(Presenter presenter, ObservableList<Student> alreadyInConcernStudents)
	{
		this.presenter = presenter;
		buildView();
		fillView(alreadyInConcernStudents);
	}


	private void buildView() {
		setPadding(new Insets(10, 10, 10, 10));
		setHgap(10);
		setVgap(10);
		
		allStudentsTable = new TableView<Student>();
		selectedStudentsTable = new TableView<Student>();
		searchBar = new Label("Hier sollte die SearchBar sein");
		selectionLabel = new Label("Auswahl");
		newStudentButton = new Button("Neuen Studenten hinzufügen");
		toRightButton = new Button(">");
		toLeftButton = new Button("<");
		allToLeftButton = new Button("<<");
		deleteStudentButton = new Button("Studenten löschen");
		studentToConcernButton = new Button("Zu Anliegen hinzufügen");
		
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

				add(studentToConcernButton, 3, 4, 2, 1);
				GridPane.setHalignment(studentToConcernButton, HPos.RIGHT);

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
				studentToConcernButton.setOnAction((event) -> {
					presenter.openNewConcernTab(selectedStudentsTable.getItems());
				});
		
		
		
	}
	
	private void fillView(ObservableList<Student> alreadyInConcernStudents) {
		// TODO Auto-generated method stub
		
	}

}
