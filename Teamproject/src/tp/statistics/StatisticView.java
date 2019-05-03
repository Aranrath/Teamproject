package tp.statistics;

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
				PieChart.Data pieData = new PieChart.Data(statVal.getName(), statVal.getValues().get(0));
				pieChartData.add(pieData);		
			}
			PieChart pieChart = new PieChart(pieChartData);
			pieChart.setTitle(statistic.getTitle());
			pieChart.setLabelsVisible(true);
			pieChart.setLegendVisible(false);
			
			for(int i = 0; i < pieChartData.size(); i++) {
				String color = "#" + statistic.getValues().get(i).getColor().toString().substring(2, 8).toUpperCase(); 
				pieChartData.get(i).getNode().setStyle("-fx-pie-color: " + color + ";");
//				Node pie = pieChart.lookup(".default-color" + i + ".chart-pie");
//				pie.setStyle("-fx-pie-color: " + color + ";");
//				Node legend = pieChart.lookup(".default-color" + i + ".chart-legend-item");
//				legend.setStyle("-fx-backround-color: " + color + ";");
			}
			
			getChildren().add(pieChart);
			
		}else if (statistic instanceof ContinuousStatistic) {
			//TODO überarbeiten
			int end = 100; // zu anz Tage zw startDate und EndDate...
			NumberAxis xAxis = new NumberAxis(0, end, 10);
			xAxis.setLabel("Zeit"); 
			
			//TODO Überarbeiten
			int max = 100;
			int step = 10;
			NumberAxis yAxis = new NumberAxis(0, max, step); 
			yAxis.setLabel("Anzahl");
			LineChart lineChart = new LineChart(xAxis, yAxis);
			for (StatisticValues statVal: statistic.getValues()){
				XYChart.Series series = new XYChart.Series(); 
				series.setName(statVal.getName()); 
				for (int i = 0; i < statVal.getValues().size(); i++) {
					series.getData().add(new XYChart.Data(i, statVal.getValues().get(i))); 
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
