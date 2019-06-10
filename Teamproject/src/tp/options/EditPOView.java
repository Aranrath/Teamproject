package tp.options;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;
import tp.Presenter;
import tp.model.PO;
import tp.model.Subject;
import tp.students.EditStudentView;

public class EditPOView extends GridPane {

	private ObservableList<Subject> allSubjects;
	private Stage stage;
	private Presenter presenter;
	
	private OptionsView optionsView;
	private EditStudentView editStudentView;
	

	private Button saveButton;
	private Label subjectErrorLabel;
	private Label poNameErrorLabel;
	private TextField poNameTextField;
	private TableView<Subject> selectSubjectsTable;
	
	//=====================================================

	//Neue PO über die OptionsView
	public EditPOView(Stage stage, Presenter presenter, OptionsView optionsView) {
		allSubjects = presenter.getSubjects();
		this.stage = stage;
		this.presenter = presenter;
		this.optionsView = optionsView;

		buildView();
		fillView();
	}
	
	//Neue PO über die EditStudentView
	public EditPOView(Stage stage, Presenter presenter, EditStudentView editStudentView) {
		allSubjects = presenter.getSubjects();
		this.stage = stage;
		this.presenter = presenter;
		this.editStudentView = editStudentView;

		buildView();
		fillView();
	}

	//Vorhandene PO bearbeiten
	public EditPOView(Stage stage, Presenter presenter, PO po) {
		allSubjects = presenter.getSubjects(po);
		this.stage = stage;
		this.presenter = presenter;

		buildView();
		fillView(po);
	}
	
	
	//===================================================

	@SuppressWarnings("unchecked")
	private void buildView() {
		setPadding(new Insets(10, 10, 10, 10));
		setHgap(10);
		setVgap(10);

		poNameTextField = new TextField();
		saveButton = new Button("Speichern");
		subjectErrorLabel = new Label("");
		poNameErrorLabel = new Label("");
		selectSubjectsTable = new TableView<Subject>(allSubjects);

		//====================================================================
		
		add(new Label("PO Name"), 0, 0);
		add(poNameTextField, 1, 0);
		add(poNameErrorLabel, 1, 1);

		add(selectSubjectsTable, 0, 2, 2, 1);

		add(subjectErrorLabel, 0, 3, 2, 1);
		add(saveButton, 0, 4, 2, 1);
		GridPane.setHalignment(saveButton, HPos.RIGHT);

		// ==================================================================
		selectSubjectsTable.setEditable(true);

		TableColumn<Subject, String> titleCol = new TableColumn<Subject, String>("Modul");
		titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));

		TableColumn<Subject, Boolean> mandatoryCol = new TableColumn<Subject, Boolean>("Pflicht");
		mandatoryCol.setCellValueFactory(new PropertyValueFactory<>("mandatory"));
		mandatoryCol.setCellFactory(new Callback<TableColumn<Subject, Boolean>, TableCell<Subject, Boolean>>() {
			@Override
			public TableCell<Subject, Boolean> call(TableColumn<Subject, Boolean> p) {
				final CheckBoxTableCell<Subject, Boolean> manCell = new CheckBoxTableCell<>();
				manCell.setSelectedStateCallback(new Callback<Integer, ObservableValue<Boolean>>() {
					@Override
					public ObservableValue<Boolean> call(Integer index) {
						if (selectSubjectsTable.getItems().get(index).mandatoryProperty().get()) {
							selectSubjectsTable.getItems().get(index).setOptional(false);
						}
						return selectSubjectsTable.getItems().get(index).mandatoryProperty();
					}
				});
				return manCell;
			}
		});

		TableColumn<Subject, Boolean> optionalCol = new TableColumn<Subject, Boolean>("Wahl");
		optionalCol.setCellValueFactory(new PropertyValueFactory<>("optional"));
		optionalCol.setCellFactory(new Callback<TableColumn<Subject, Boolean>, TableCell<Subject, Boolean>>() {
			@Override
			public TableCell<Subject, Boolean> call(TableColumn<Subject, Boolean> p) {
				final CheckBoxTableCell<Subject, Boolean> opCell = new CheckBoxTableCell<>();
				opCell.setSelectedStateCallback(new Callback<Integer, ObservableValue<Boolean>>() {
					@Override
					public ObservableValue<Boolean> call(Integer index) {
						if (selectSubjectsTable.getItems().get(index).optionalProperty().get()) {
							selectSubjectsTable.getItems().get(index).setMandatory(false);
						}
						return selectSubjectsTable.getItems().get(index).optionalProperty();
					}
				});
				return opCell;
			}
		});

		selectSubjectsTable.getColumns().addAll(titleCol, mandatoryCol, optionalCol);

		// ======================================================================

	}

	private void fillView() {

		saveButton.setOnAction((event) -> {
			if (poNameTextField.getText().equals("")) {
				poNameErrorLabel.setText("Titel muss ausgefüllt sein");
				poNameErrorLabel.setTextFill(Color.RED);
				return;

			}
			if (nameAlreadyExists(poNameTextField.getText())) {
				poNameErrorLabel.setText("Name bereits vorhanden");
				poNameErrorLabel.setTextFill(Color.RED);
				return;
			} else {
				ObservableList<Subject> selectedOptionalSubjects = FXCollections.observableArrayList();
				ObservableList<Subject> selectedMandatorySubjects = FXCollections.observableArrayList();
				for (Subject s : selectSubjectsTable.getItems()) {
					if (s.optionalProperty().get()) {
						selectedOptionalSubjects.add(s);
					} else if (s.mandatoryProperty().get())
						selectedMandatorySubjects.add(s);
				}
				// UNTERSCHIED: speicher das geänderte Thema
				PO po = presenter.saveNewPo(new PO(poNameTextField.getText(), selectedOptionalSubjects, selectedMandatorySubjects));
				
				if(optionsView != null)
				{
					optionsView.addNewPO(po);
				}
				else if(editStudentView != null)
				{
					editStudentView.addNewPO(po);
				}
				stage.close();
			}
		});

	}

	private void fillView(PO po) {
		
		poNameTextField.setText(po.getName());
		
		saveButton.setOnAction((event) -> {
			if (poNameTextField.getText().equals("")) {
				poNameErrorLabel.setText("Titel muss ausgefüllt sein");
				poNameErrorLabel.setTextFill(Color.RED);
				return;

			}
			if (nameAlreadyExists(po, poNameTextField.getText())) {
				poNameErrorLabel.setText("Name bereits vorhanden");
				poNameErrorLabel.setTextFill(Color.RED);
				return;
			} else {
				ObservableList<Subject> selectedOptionalSubjects = FXCollections.observableArrayList();
				ObservableList<Subject> selectedMandatorySubjects = FXCollections.observableArrayList();
				for (Subject s : selectSubjectsTable.getItems()) {
					if (s.optionalProperty().get()) {
						selectedOptionalSubjects.add(s);
					} else if (s.mandatoryProperty().get())
						selectedMandatorySubjects.add(s);
				}
				// UNTERSCHIED: speicher das geänderte Thema
				presenter.saveEditedPO(poNameTextField.getText(), selectedMandatorySubjects, selectedOptionalSubjects,
						po);
				stage.close();
			}
		});
	}



	// =================================================================

	private boolean nameAlreadyExists(String newPOName) {
		for (PO p : presenter.getPOs()) {
			if (p.getName().equals(newPOName)) {
				return true;
			}
		}
		return false;
	}
	
	private boolean nameAlreadyExists(PO po, String newPOName) {
		for (PO p : presenter.getPOs())
		{
			if (po.getId() != p.getId() && p.getName().equals(newPOName))
			{
				return true;
			}
		}
		return false;
	}

}
