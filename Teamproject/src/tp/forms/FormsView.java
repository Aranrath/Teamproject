package tp.forms;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import javax.imageio.ImageIO;

import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tp.Presenter;
import tp.concern.ConcernView;
import tp.model.Form;

public class FormsView extends GridPane
{

	private Presenter presenter;
	private ObservableList<Form> shownForms;
	private ObservableList<Form> allForms;
	
	private ConcernView concernView;
	private Stage stage;

	// =============================================GUI Elemente

	private Button addFormButton;
	private Button deleteFormButton;
	private TextField searchTextField;
	private ListView<Form> formListView;
	
	private Button renameFormButton;
	private HBox renameHBox;
	private TextField newNameTextField;
	private Button saveNewNameButton;
	private Button cancelRenameButton;
	
	
	//-----------------------------------------------------------
	//GUI Elemente Version: aus Menü heraus -> Anzeige Bilder und PDF
	
	private ImageView imageView;
	private Label noShowableFileFormatLabel;
	
	//----------------------------------------------------------
	//GUI Elemente Version 2: aus Concern heraus
	
	private Label selectedFormsLabel;
	private ListView<Form> selectedFormsListView;
	private Button toRightButton;
	private Button toLeftButton;
	private Button selectedFormsToConcernButton;
	

	// =============================================Konstruktoren

	//Version 1: View gestarten aus dem Hauptmenü
	public FormsView(Presenter presenter)
	{
		this.presenter = presenter;
		
		buildView();
		buildVersion1GUI();

	}
	

	//Version 2: View gestartet aus einem Concern heraus
	public FormsView(Presenter presenter, Stage stage, ConcernView concernView, ObservableList<Form> filesAlreadyInConcern, ObservableList<Form> topicRelatedFiles)
	{
		this.presenter = presenter;
		this.stage = stage;
		this.concernView = concernView;
		
		
		buildView();
		buildVersion2GUI(filesAlreadyInConcern, topicRelatedFiles);
	}
	
	//===============================================================
	//private Methoden
	//===============================================================
	

	private void buildView() {
		setPadding(new Insets(20));
		setHgap(20);
		setVgap(20);
		
		
		this.allForms = presenter.getTopicForms();
		this.shownForms = FXCollections.observableArrayList(allForms);

		addFormButton = new Button("Neu");
		deleteFormButton = new Button("Löschen");
		searchTextField = new TextField();
		searchTextField.setPromptText("Suche Formular");
		searchTextField.setStyle("-fx-focus-color: transparent;");
		
		formListView = new ListView<Form>(shownForms);
		formListView.setStyle("-fx-focus-color: transparent;");
		
		renameFormButton = new Button("Umbenennen");
		newNameTextField = new TextField();
		newNameTextField.setMaxWidth(Double.MAX_VALUE);
		HBox.setHgrow(newNameTextField, Priority.ALWAYS);
		
		saveNewNameButton = new Button("✔");
		cancelRenameButton = new Button("✖");
		cancelRenameButton.setMinSize(Button.USE_PREF_SIZE, Button.USE_PREF_SIZE);
		saveNewNameButton.setMinSize(Button.USE_PREF_SIZE, Button.USE_PREF_SIZE);
		renameHBox = new HBox(newNameTextField, saveNewNameButton, cancelRenameButton);
		renameHBox.setVisible(false);
		
		// =====================================================================

		add(searchTextField, 0, 0);
		GridPane.setHalignment(searchTextField, HPos.LEFT);

		add(renameFormButton, 1, 0);
		GridPane.setHalignment(renameFormButton, HPos.RIGHT);
		
		add(renameHBox, 0,0,2,1);
		GridPane.setHalignment(renameHBox, HPos.RIGHT);
		
		add(formListView, 0, 1, 2, 2);

		add(addFormButton, 1, 3);
		GridPane.setHalignment(addFormButton, HPos.RIGHT);

		add(deleteFormButton, 0, 3);
		GridPane.setHalignment(deleteFormButton, HPos.LEFT);
	
		//========================================================================
		
		addFormButton.setOnAction((event) -> {
			Stage stage = new Stage();
			stage.setAlwaysOnTop(true);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setTitle("Neues Formular hinzufügen");
			stage.setResizable(false);
			stage.setScene(new Scene(new NewFormView(stage, presenter, this, allForms), 400, 200));
			stage.show();
		});
		deleteFormButton.setOnAction((event) -> {
			for(Form f: formListView.getSelectionModel().getSelectedItems())
			{
				presenter.deleteForm(f);
			}
		});
		
		renameFormButton.setOnAction((event) -> {

			Form selectedForm = formListView.getSelectionModel().getSelectedItem();
			if(selectedForm!= null)
			{
				newNameTextField.setText(selectedForm.getName());
				
				renameFormButton.setVisible(false);
				renameHBox.setVisible(true);
				searchTextField.setVisible(false);
				
				searchTextField.setMouseTransparent(true);
				formListView.setMouseTransparent(true);
			}
		});
		
		
		saveNewNameButton.setOnAction((event) -> {
			
			Form selectedForm = formListView.getSelectionModel().getSelectedItem();
			
			if(selectedForm!= null)
			{
				String newName = newNameTextField.getText();
				
				for(Form f: allForms)
				{
					if(f.getId()!= selectedForm.getId() && f.getName().equals(newName))
					{
						return;
					}
				}
				
				if(!newName.isEmpty())
				{
					selectedForm.setName(newName);
					presenter.saveEditedForm(selectedForm);
					
					renameFormButton.setVisible(true);
					renameHBox.setVisible(false);
					searchTextField.setVisible(true);
					
					searchTextField.setMouseTransparent(false);
					formListView.setMouseTransparent(false);
					
					shownForms.remove(selectedForm);
					shownForms.add(selectedForm);
					
					allForms.remove(selectedForm);
					allForms.add(selectedForm);
					
					filterForms(searchTextField.getText());
				}
				
			}
			
		});
		
		cancelRenameButton.setOnAction((event) -> {

			renameFormButton.setVisible(true);
			renameHBox.setVisible(false);
			searchTextField.setVisible(true);
			
			searchTextField.setMouseTransparent(false);
			formListView.setMouseTransparent(false);
			
		});
		

		
		searchTextField.textProperty().addListener((obs, oldText, newText) -> {
			filterForms(newText);
		});
		

	}

	private void buildVersion1GUI()
	{
		imageView = new ImageView();
		imageView.setVisible(false);
		imageView.preserveRatioProperty().set(true);
    	imageView.fitHeightProperty().bind(formListView.heightProperty());
		noShowableFileFormatLabel = new Label("Kein Formular mit anzeigbarem Datei-Format ausgewählt");
		noShowableFileFormatLabel.setVisible(false);
		
		//------------------------------------------------------------------------------------
		
		add(imageView, 2,0,2,4);
		GridPane.setHalignment(imageView, HPos.CENTER);
		GridPane.setValignment(imageView, VPos.CENTER);
		add(noShowableFileFormatLabel, 2,0,2,4);
		GridPane.setHalignment(noShowableFileFormatLabel, HPos.CENTER);
		GridPane.setValignment(noShowableFileFormatLabel, VPos.CENTER);
		
		//========================================================================
		//Column Constraints
				
		ColumnConstraints column1 = new ColumnConstraints();
		column1.setPercentWidth(35/ 2);
		getColumnConstraints().add(column1);
		getColumnConstraints().add(column1);

		ColumnConstraints column2 = new ColumnConstraints();
		column2.setPercentWidth(65 / 2);
		getColumnConstraints().add(column2);
		getColumnConstraints().add(column2);
		
		//--------------------------------------------------
		//Row Constraints
				
		RowConstraints buttonRow = new RowConstraints();
		buttonRow.setPercentHeight(15/2);
				
		RowConstraints formListRow = new RowConstraints();
		formListRow.setPercentHeight(85/2);
				
				
		getRowConstraints().add(buttonRow);
		getRowConstraints().add(formListRow);
		getRowConstraints().add(formListRow);
		getRowConstraints().add(buttonRow);
				
		//=========================================================================
		
		formListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Form>() {
		    //@Override
		    public void changed(ObservableValue<? extends Form> observable, Form oldValue, Form newValue) {  	
		       if(newValue!= null && newValue.formIsImage())
		       {
		    	   imageView.setImage(newValue.getImage());
		    	   imageView.setVisible(true);
		    	   noShowableFileFormatLabel.setVisible(false);
		       }
		       else if(newValue != null && newValue.formIsPdf()) {
		    	   //if PDF, convert first page of PDF to image
				try (RandomAccessFile raf = new RandomAccessFile(newValue.getFile(), "r");){
					FileChannel channel = raf.getChannel();
					ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
					PDFFile pdf = new PDFFile(buf);
					PDFPage page = pdf.getPage(0);
					
					Rectangle rect = new Rectangle(0, 0, (int) page.getBBox().getWidth(),
					                                 (int) page.getBBox().getHeight());
					BufferedImage bufferedImage = new BufferedImage(rect.width, rect.height,
					                                  BufferedImage.TYPE_INT_RGB);

					Image image = page.getImage(rect.width, rect.height, rect, null, true, true);
					File file = File.createTempFile("tempImage", ".bmp");
					Graphics2D bufImageGraphics = bufferedImage.createGraphics();
					bufImageGraphics.drawImage(image, 0, 0, null);
					ImageIO.write(bufferedImage, "bmp", file);
					javafx.scene.image.Image img = new javafx.scene.image.Image(file.toURI().toString());
					
					
					imageView.setImage(img);
			    	imageView.setVisible(true);
			    	noShowableFileFormatLabel.setVisible(false);
//					
					
					} catch (Exception e) {
						e.printStackTrace();
					}
		       }
		       else
		       {
		    	   imageView.setVisible(false);
		    	   noShowableFileFormatLabel.setVisible(true);
		       }

		    }
		});
		
		
//		File pdfFile = new File("/path/to/pdf.pdf");
//		RandomAccessFile raf = new RandomAccessFile(pdfFile, "r");
//		FileChannel channel = raf.getChannel();
//		ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
//		PDFFile pdf = new PDFFile(buf);
//		PDFPage page = pdf.getPage(0);
//
//		// create the image
//		Rectangle rect = new Rectangle(0, 0, (int) page.getBBox().getWidth(),
//		                                 (int) page.getBBox().getHeight());
//		BufferedImage bufferedImage = new BufferedImage(rect.width, rect.height,
//		                                  BufferedImage.TYPE_INT_RGB);
//
//		Image image = page.getImage(rect.width, rect.height,    // width & height
//		                            rect,                       // clip rect
//		                            null,                       // null for the ImageObserver
//		                            true,                       // fill background with white
//		                            true                        // block until drawing is done
//		);
//		Graphics2D bufImageGraphics = bufferedImage.createGraphics();
//		bufImageGraphics.drawImage(image, 0, 0, null);
//		ImageIO.write(bufferedImage, format, new File( "/path/to/image.jpg" ));
		
	}
	
	private void buildVersion2GUI(ObservableList<Form> filesAlreadyInConcern, ObservableList<Form> topicRelatedFiles)
	{
		selectedFormsLabel = new Label("Ausgewählte Dateien");
		selectedFormsListView = new ListView<Form>();
		toRightButton = new Button(">");
		toLeftButton = new Button("<");
		selectedFormsToConcernButton = new Button("Dateien zum Concern hinzufügen");
		
		
		//Funktioniert so nicht, Dateien in den jeweiligen nicht die gleichen Objekte sind -> Vergleich über id!
		//allForms.removeAll(filesAlreadyInConcern);
		//shownForms.removeAll(filesAlreadyInConcern);
		for(Form fileAlreadyInConcern : filesAlreadyInConcern)
		{
			//for(Form shownForm : shownForms)
			for(int i = 0; i<shownForms.size();i++)
			{
				if(fileAlreadyInConcern.getId() == shownForms.get(i).getId())
				{
					shownForms.remove(shownForms.get(i));
				}
			}
			
			//for(Form form : allForms)
			for(int i = 0; i<allForms.size();i++)
			{
				if(fileAlreadyInConcern.getId() == allForms.get(i).getId())
				{
					allForms.remove(allForms.get(i));
				}
			}
		}

		
		//Themenbezogene Dateien ausschließen
		filesAlreadyInConcern.removeAll(topicRelatedFiles);

		selectedFormsListView.getItems().addAll(filesAlreadyInConcern);
		
		//------------------------------------------------------------------------------------
		
		add(selectedFormsLabel,3,0);
		GridPane.setHalignment(selectedFormsLabel, HPos.LEFT);
		
		add(selectedFormsListView,3,1);

		add(selectedFormsToConcernButton,3,2);
		GridPane.setHalignment(selectedFormsToConcernButton, HPos.RIGHT);

		VBox leftRightButtonBox = new VBox();
		leftRightButtonBox.getChildren().addAll(toLeftButton, toRightButton);
		leftRightButtonBox.setSpacing(10);
		toRightButton.setMinSize(Button.USE_PREF_SIZE, Button.USE_PREF_SIZE);
		toLeftButton.setMinSize(Button.USE_PREF_SIZE, Button.USE_PREF_SIZE);
		add(leftRightButtonBox, 2, 1);
		leftRightButtonBox.setAlignment(Pos.CENTER);
		
		//===================================================================================
		//TODO Constraints für diese Version => Fenster hat noch feste Größe...
		
		//===================================================================================
		
		toRightButton.setOnAction(event -> {
			ObservableList<Form> itemsToMove = formListView.getSelectionModel().getSelectedItems();

			selectedFormsListView.getItems().addAll(itemsToMove);
			allForms.removeAll(itemsToMove);
			shownForms.removeAll(itemsToMove);
		});
		
		toLeftButton.setOnAction(event -> {
			ObservableList<Form> itemsToMove = selectedFormsListView.getSelectionModel().getSelectedItems();
			allForms.addAll(itemsToMove);
			selectedFormsListView.getItems().removeAll(itemsToMove);
			
			filterForms(searchTextField.getText());
		});

		
		selectedFormsToConcernButton.setOnAction(event -> {
			concernView.addFilesToConcern(selectedFormsListView.getItems());
			stage.close();
		});
	}
	
	
	private void filterForms(String searchTerm) {
		
		if(searchTerm.isEmpty())
		{
			shownForms.clear();
			shownForms.addAll(allForms);
		}
		else
		{
			
			shownForms.clear();
			String [] searchTerms = searchTerm.toLowerCase().split(" ");
			
			
			for (Form form : allForms)
			{
				if(Presenter.containsAll(form.getName().toLowerCase(), searchTerms))
				{
					shownForms.add(form);
				}
				
			}
			
		}
		
	}
	
	
	//===============================================================
	//public Methoden
	//===============================================================
	
	
	public void addNewForm(Form newForm)
	{
		formListView.getItems().add(newForm);
		
	}


	public void updateView() {
		allForms = presenter.getTopicForms();
		shownForms.clear();
		for (Form form: allForms) {
			shownForms.add(form);
		}
		filterForms(searchTextField.getText());
	}


}
