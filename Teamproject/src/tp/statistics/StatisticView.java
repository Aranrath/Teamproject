package tp.statistics;

import java.sql.Date;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
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
			
			for(int i = 0; i < pieChartData.size(); i++) {
				String color = "#" + statistic.getValues().get(i).getColor().toString().substring(2, 8).toUpperCase(); 
				pieChartData.get(i).getNode().setStyle("-fx-pie-color: " + color + ";");
				//TODO legendenFarbe....
//				Node pie = pieChart.lookup(".default-color" + i + ".chart-pie");
//				pie.setStyle("-fx-pie-color: " + color + ";");
//				Node legend = pieChart.lookup(".default-color" + i + ".chart-legend-item");
//				legend.setStyle("-fx-backround-color: " + color + ";");
			}
			
			getChildren().add(pieChart);
			
		}else if (statistic instanceof ContinuousStatistic) {
			//TODO überarbeiten
			long start = ((ContinuousStatistic) statistic).getStartDate().getTime()/100000000;
			long end = ((ContinuousStatistic) statistic).getEndDate().getTime()/100000000;
			NumberAxis xAxis = new NumberAxis(start, end, 10);
//			CategoryAxis xAxis = new CategoryAxis();
			xAxis.setLabel("Zeit"); 
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
			//TODO Überarbeiten
			int step = 10;
			NumberAxis yAxis = new NumberAxis(0, yMax, step); 
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
			ObservableList<String> barChartCategories = FXCollections.observableArrayList();
			// barChartCategories .... startTime... startTime+step.... startTime+2step.... endTime
			
			for (StatisticValues statVal: statistic.getValues()) {
				
			}
			
			xAxis.setCategories(barChartCategories); 
//			xAxis.setLabel("category");  
//
//			//Defining the y axis 
//			NumberAxis yAxis = new NumberAxis(); 
//			yAxis.setLabel("score");
//			
//			BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);  
//			barChart.setTitle("Comparison between various cars"); 
//			
//			
//			//Prepare XYChart.Series objects by setting data        
//			XYChart.Series<String, Number> series1 = new XYChart.Series<>(); 
//			series1.setName("Fiat"); 
//			series1.getData().add(new XYChart.Data<>("Speed", 1.0)); 
//			series1.getData().add(new XYChart.Data<>("User rating", 3.0)); 
//			series1.getData().add(new XYChart.Data<>("Milage", 5.0)); 
//			series1.getData().add(new XYChart.Data<>("Safety", 5.0));   
//
//			XYChart.Series<String, Number> series2 = new XYChart.Series<>(); 
//			series2.setName("Audi"); 
//			series2.getData().add(new XYChart.Data<>("Speed", 5.0)); 
//			series2.getData().add(new XYChart.Data<>("User rating", 6.0));  
//
//			series2.getData().add(new XYChart.Data<>("Milage", 10.0)); 
//			series2.getData().add(new XYChart.Data<>("Safety", 4.0));  
//
//			XYChart.Series<String, Number> series3 = new XYChart.Series<>(); 
//			series3.setName("Ford"); 
//			series3.getData().add(new XYChart.Data<>("Speed", 4.0)); 
//			series3.getData().add(new XYChart.Data<>("User rating", 2.0)); 
//			series3.getData().add(new XYChart.Data<>("Milage", 3.0)); 
//			series3.getData().add(new XYChart.Data<>("Safety", 6.0));
//			
//			//Setting the data to bar chart        
//			barChart.getData().addAll(series1, series2, series3);
		}
		
		
		allStatisticsTable = new TableView<StatisticValues>();
	}
	

}
