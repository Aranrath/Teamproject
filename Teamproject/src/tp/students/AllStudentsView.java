package tp.students;

import java.sql.Date;

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

	Presenter presenter;

	TableView<Student> allStudentsTable;
	TableView<Student> selectedStudentsTable;
	Label searchBar;
	Label selectionLabel;
	Button newStudentButton;
	Button toRightButton;
	Button toLeftButton;
	Button allToLeftButton;
	Button deleteStudentButton;
	Button studentToNewConcernButton;

	public AllStudentsView(Presenter presenter) {
		this.presenter = presenter;
		buildView();
		updateViewContent();
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
		newStudentButton = new Button("Neuen Studenten hinzuf�gen");
		toRightButton = new Button(">");
		toLeftButton = new Button("<");
		allToLeftButton = new Button("<<");
		deleteStudentButton = new Button("Studenten l�schen");
		studentToNewConcernButton = new Button("Zu neuem Anliegen hinzuf�gen");

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
//			allStudentsTable.getSelectionModel().getSelectedItems();
		});
		toLeftButton.setOnAction((event) -> {
		});
		allToLeftButton.setOnAction((event) -> {
		});
		deleteStudentButton.setOnAction((event) -> {
			Alert alert = new Alert(AlertType.WARNING, "Ausgew�hlte Studenten aus der Datenbank l�schen?",
					ButtonType.YES, ButtonType.CANCEL);
			alert.showAndWait();

			if (alert.getResult() == ButtonType.YES) {
				for (Student s : selectedStudentsTable.getItems()) {
					presenter.deleteStudent(s);
				}
			}

			updateViewContent();

		});
		studentToNewConcernButton.setOnAction((event) -> {
			presenter.openNewConcernTab(selectedStudentsTable.getItems());
		});

	}

	private void updateViewContent() {
		//TODO

	}

}
