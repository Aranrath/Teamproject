package tp.statistics;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.util.Pair;
import javafx.util.StringConverter;
import tp.Presenter;
import tp.model.statistics.ContinuousStatistic;
import tp.model.statistics.IntervalStatistic;
import tp.model.statistics.RatioStatistic;
import tp.model.statistics.Statistic;
import tp.model.statistics.StatisticValues;

public class StatisticView extends HBox{

	private Presenter presenter;
	Statistic statistic;
	private TableView<StatisticValues> allStatisticsTable;
	//=====================0
	
	
	public StatisticView(Statistic statistic)
	{
		this.statistic = statistic;
		buildView();
	}
	
	
	//================================================
	
	
	private void buildView(){
		setPadding(new Insets(20));

		if (statistic instanceof RatioStatistic) {
			ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(); 
			for (StatisticValues statVal: statistic.getValues()) {
				PieChart.Data pieData = new PieChart.Data(statVal.getName(), statVal.getValues().get(0).getValue());
				pieChartData.add(pieData);		
			}
			PieChart pieChart = new PieChart(pieChartData);
			pieChart.setTitle(statistic.getTitle());
			pieChart.setLabelsVisible(true);
			pieChart.setLegendVisible(false);
			
			getChildren().add(pieChart);
			
		}else if (statistic instanceof ContinuousStatistic) {
			//TODO überarbeiten
			long start = ((ContinuousStatistic) statistic).getStartDate().getTime()/100000000;
			long end = ((ContinuousStatistic) statistic).getEndDate().getTime()/100000000;
			NumberAxis xAxis = new NumberAxis(start, end, 10);
//			CategoryAxis xAxis = new CategoryAxis();
			xAxis.setLabel("Datum"); 
			StringConverter<Number> converter = new StringConverter<Number>() {
				@Override
				public String toString(Number object) {
					Date date = new Date(object.longValue()*100000000);
					return date.toString();
				}
				@Override
				public Number fromString(String string) {
					//Not needed
					return null;
				}
			};
			xAxis.setTickLabelFormatter(converter);

			int yMax = 0;
			for (StatisticValues statVal: statistic.getValues()){
				for(Pair<Date, Integer> value: statVal.getValues()) {
					if (value.getValue() > yMax) {
						yMax = value.getValue();
					}
				}
			}
			int yAxisMax = yMax + (yMax/8) + 1;
			int step = yAxisMax/10;
			NumberAxis yAxis = new NumberAxis(0, yAxisMax, step); 
			yAxis.setLabel("Anzahl");
			
			
			LineChart<Number, Number> lineChart = new LineChart<Number, Number>(xAxis, yAxis);
			for (StatisticValues statVal: statistic.getValues()){
				XYChart.Series<Number, Number> series = new XYChart.Series<Number, Number>(); 
				series.setName(statVal.getName()); 
				for(Pair<Date, Integer> value: statVal.getValues()) {
					series.getData().add(new XYChart.Data<Number, Number>(value.getKey().getTime()/100000000, value.getValue()));
				}
				lineChart.getData().add(series);
			}
			getChildren().add(lineChart);
			
		}else if (statistic instanceof IntervalStatistic) {
			CategoryAxis xAxis = new CategoryAxis();
			
			//create possible bars   
			ObservableList<String> barChartCategories = FXCollections.observableArrayList();
			for (StatisticValues statVal: statistic.getValues()) {
				barChartCategories.add(statVal.getName());
			}
			
			xAxis.setCategories(barChartCategories); 
			xAxis.setLabel("Datum");  

			
			int yMax = 0;
			for (StatisticValues statVal: statistic.getValues()){
				for(Pair<Date, Integer> value: statVal.getValues()) {
					if (value.getValue() > yMax) {
						yMax = value.getValue();
					}
				}
			}
			int yAxisMax = yMax + (yMax/8) + 1;
			int step = yAxisMax/10;
			NumberAxis yAxis = new NumberAxis(0, yAxisMax, step); 
			yAxis.setLabel("Anzahl");
			
			
			BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);  
			barChart.setTitle(statistic.getTitle()); 
			
			//TODO date Intervals get the dates form the value Pairs keys..... dumbass
//			//create Date intervals
			ArrayList<XYChart.Series<String, Number>> chartSeriesList = new ArrayList<>();
//			for(Date date = ((IntervalStatistic) statistic).getStartDate(); date.before(((IntervalStatistic) statistic).getEndDate());) {	
//				XYChart.Series<String, Number> series = new XYChart.Series<>(); 
//				//increment the date by step days
//				Calendar c = Calendar.getInstance();
//		        c.setTime(date);
//		        c.add(Calendar.DATE, ((IntervalStatistic) statistic).getStep());
//		        Date newDate = new Date(c.getTimeInMillis());
//		        series.setName(date.toString() + " - " + newDate.toString());
//		        chartSeriesList.add(series);
//		        date = newDate;
//			}
			
			//add Data
			for (StatisticValues statVal: statistic.getValues()) {
				for(int i = 0; i < statVal.getValues().size(); i++) {
					chartSeriesList.get(i).getData().add(new XYChart.Data<String, Number>(statVal.getName(), statVal.getValues().get(i).getValue()));
				}
			}
			
			for (XYChart.Series<String, Number> series: chartSeriesList) {
				barChart.getData().add(series);
			}
		}
		
		
		allStatisticsTable = new TableView<StatisticValues>();
	}
	

}
