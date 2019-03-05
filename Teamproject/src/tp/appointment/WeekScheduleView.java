package tp.appointment;



import java.sql.Date;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import tp.Presenter;
import tp.model.Appointment;

public class WeekScheduleView extends GridPane
{
	Presenter presenter;
	

	int shownKw;
	Date[] shownWorkWeek;
	Appointment[] weeksAppointments;

	
	Label kwLabel;
	Button leftButton;
	Button rightButton;
	Button showCurrentWeekButton;
	DatePicker datePicker;
	TableView<Appointment> timeTable;
	
	
	public WeekScheduleView(Presenter presenter)
	{
		this.presenter = presenter;
		buildView();
		//fillView with today's date
		fillView(new Date(new java.util.Date().getTime()));
	}



	private void buildView()
	{
		setPadding(new Insets(20,20,20,20));
		setHgap(20);
		setVgap(20);
		
		//initialize
		kwLabel = new Label("<Some strange>. KW von <whenever> bis <forever>");
		leftButton = new Button("<");
		rightButton = new Button(">");
		showCurrentWeekButton = new Button("Zurück zur aktuellen Woche");
		timeTable = new TableView<Appointment>();
		datePicker = new DatePicker();
		
		//add to Pane
		add(showCurrentWeekButton, 0,0);
		HBox datePickerAndLabelBox = new HBox(new Label("Zeige Woche des Datums: "), datePicker);
		add(datePickerAndLabelBox,1,0);
		HBox navigationBox = new HBox(leftButton, kwLabel, rightButton);
		navigationBox.setAlignment(Pos.CENTER);
		add(navigationBox,0,1,2,1);
		add(timeTable,0,2,2,1);
		
		leftButton.setOnAction((event)->{
			Date date = presenter.getEndOfPreviousWeek(shownWorkWeek[0]);
			fillView(date);
		});
		rightButton.setOnAction((event)->{
			Date date = presenter.getStartOfNextWeek(shownWorkWeek[shownWorkWeek.length-1]);
			fillView(date);
		});
		showCurrentWeekButton.setOnAction((event)->{});
		datePicker.setOnAction((event)->{
			fillView(new Date(datePicker.getValue().toEpochDay()));
		});
		
	}
	
	private void fillView(Date date)
	{
		shownKw = presenter.getKwOfDate(date);
		shownWorkWeek = presenter.getWorkWeekOfDate(date);
		weeksAppointments = presenter.getWeeksAppointments(shownKw);

		
	}


}
