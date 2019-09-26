package tp.statistics;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javafx.util.Pair;
import javafx.util.StringConverter;
import tp.model.statistics.ContinuousStatistic;
import tp.model.statistics.IntervalStatistic;
import tp.model.statistics.RatioStatistic;
import tp.model.statistics.Statistic;
import tp.model.statistics.StatisticValues;

public class StatisticView extends VBox{

	Statistic statistic;
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
			
			getChildren().addAll(pieChart, createPieChartTableView(pieChart));
			
		}else if (statistic instanceof ContinuousStatistic) {
			Date startDate = ((ContinuousStatistic) statistic).getStartDate();
			Date endDate = ((ContinuousStatistic) statistic).getEndDate();
			long difference = endDate.getTime() - startDate.getTime();
		    long daysBetween = (difference / (1000*60*60*24));
			NumberAxis xAxis = new NumberAxis(0, daysBetween, 10);
			xAxis.setLabel("Datum"); 
			StringConverter<Number> converter = new StringConverter<Number>() {
				@Override
				public String toString(Number object) {
					Calendar c = Calendar.getInstance();
					c.setTime(startDate);
			        c.add(Calendar.DATE, object.intValue());
			        Date date = new Date(c.getTimeInMillis());
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
				for(Pair<Integer, Integer> value: statVal.getValues()) {
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
				for(Pair<Integer, Integer> value: statVal.getValues()) {
					series.getData().add(new XYChart.Data<Number, Number>(value.getKey(), value.getValue()));
				}
				lineChart.getData().add(series);
			}
			getChildren().addAll(lineChart, createLineChartTableView(lineChart));
			
		}else if (statistic instanceof IntervalStatistic) {
			CategoryAxis xAxis = new CategoryAxis();
			
			//create Date intervals
			ObservableList<String> barChartCategories = FXCollections.observableArrayList();
			Date startDate = ((IntervalStatistic) statistic).getStartDate();
			StatisticValues statValue = statistic.getValues().get(0);
			
			for (int i = 1; i < statValue.getValues().size(); i++) {
				Calendar c = Calendar.getInstance();
				c.setTime(startDate);
		        c.add(Calendar.DATE, ((IntervalStatistic) statistic).getStep());
		        Date newDate = new Date(c.getTimeInMillis());
				barChartCategories.add(startDate.toString() + " - " + newDate.toString());
		        startDate = newDate;
			}
			barChartCategories.add(startDate.toString() + " - " + ((IntervalStatistic) statistic).getEndDate().toString());
			
			xAxis.setCategories(barChartCategories); 
			xAxis.setLabel("Datum");  

			//get max of y Axis
			int yMax = 0;
			for (StatisticValues statVal: statistic.getValues()){
				for(Pair<Integer, Integer> value: statVal.getValues()) {
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
			
			
			//create bars and add data  
			ArrayList<XYChart.Series<String, Number>> chartSeriesList = new ArrayList<>();
			for (StatisticValues statVal: statistic.getValues()) {
				XYChart.Series<String, Number> series = new XYChart.Series<>();
				series.setName(statVal.getName());
				for (int i = 0; i < statVal.getValues().size(); i++) {
					series.getData().add(new XYChart.Data<String, Number>(barChartCategories.get(i), statVal.getValues().get(i).getValue()));
				}
				chartSeriesList.add(series);
			}
			
			for (XYChart.Series<String, Number> series: chartSeriesList) {
				barChart.getData().add(series);
			}
			getChildren().addAll(barChart, createBarChartTableView(barChart));
		}
	}

	private TableView<Data> createPieChartTableView(PieChart chart) {
		TableView<PieChart.Data> chartTable = new TableView<Data>();
		
		if(!chart.getData().isEmpty()) {
			TableColumn<Data, String> legendCol = new TableColumn<Data, String>("Name");
			legendCol.setCellValueFactory(new Callback<CellDataFeatures<PieChart.Data, String>, ObservableValue<String>>() {
			     public ObservableValue<String> call(CellDataFeatures<PieChart.Data, String> param) {
			    	 return new SimpleStringProperty(param.getValue().getName());
			     }
			  });
			chartTable.getColumns().add(legendCol);
			
			TableColumn<Data, Integer> valueCol = new TableColumn<Data, Integer>("Wert");
			valueCol.setCellValueFactory(new Callback<CellDataFeatures<PieChart.Data, Integer>, ObservableValue<Integer>>() {
			     public ObservableValue<Integer> call(CellDataFeatures<PieChart.Data, Integer> param) {
			    	 return new SimpleObjectProperty<Integer>((int) param.getValue().getPieValue());
			     }
			  });
			chartTable.getColumns().add(valueCol);
			
		}
		for (Data d: chart.getData()) {
			chartTable.getItems().add(d);
		}
		
		return chartTable;
	}
	
	private TableView<Series<Number, Number>> createLineChartTableView(LineChart<Number, Number> chart) {
		TableView<XYChart.Series<Number, Number>> chartTable = new TableView<Series<Number, Number>>();
		
		if(!chart.getData().isEmpty()) {
			TableColumn<XYChart.Series<Number, Number>, String> legendCol = new TableColumn<XYChart.Series<Number, Number>, String>("Name");
			legendCol.setCellValueFactory(new Callback<CellDataFeatures<XYChart.Series<Number, Number>, String>, ObservableValue<String>>() {
			     public ObservableValue<String> call(CellDataFeatures<XYChart.Series<Number, Number>, String> param) {
			    	 return new SimpleStringProperty(param.getValue().getName());
			     }
			  });
			chartTable.getColumns().add(legendCol);
			
			final ObservableList<XYChart.Data<Number, Number>> firstSeriesData = chart.getData().get(0).getData();
			Date date = ((ContinuousStatistic) statistic).getStartDate();
		    for (final XYChart.Data<Number, Number> item: firstSeriesData) {
//		    	Date itemDate = new Date(item.getXValue().longValue()*100000000);
		    	TableColumn<Series<Number, Number>, Number> col = new TableColumn<Series<Number, Number>, Number>(date.toString());
		    	col.setSortable(false);
		    	col.setCellValueFactory(new Callback<CellDataFeatures<XYChart.Series<Number,Number>, Number>, ObservableValue<Number>>() {
		    		public ObservableValue<Number> call(CellDataFeatures<XYChart.Series<Number,Number>, Number> param) {
		    			for (XYChart.Data<Number, Number> currentItem: param.getValue().getData()) {
		    				if (currentItem.getXValue().equals(item.getXValue())) {
		    					return currentItem.YValueProperty();
		    				}
		    			}
		    			return null;
		    		}
		    	 });
		    	// increase date by 1
		    	chartTable.getColumns().add(col);
		    	Calendar c = Calendar.getInstance();
				c.setTime(date);
			    c.add(Calendar.DATE,1);
			    date = new Date(c.getTimeInMillis());
		      }
		}
		for (XYChart.Series<Number,Number> series: chart.getData()) {
		  	  chartTable.getItems().add(series);
		}  
		
		return chartTable;
	}
	
	private TableView<XYChart.Series<String, Number>> createBarChartTableView(BarChart<String, Number> chart) {
		TableView<XYChart.Series<String, Number>> chartTable = new TableView<Series<String, Number>>();
		
		if(!chart.getData().isEmpty()) {
			TableColumn<XYChart.Series<String, Number>, String> legendCol = new TableColumn<XYChart.Series<String, Number>, String>("Name");
			legendCol.setCellValueFactory(new Callback<CellDataFeatures<XYChart.Series<String, Number>, String>, ObservableValue<String>>() {
			     public ObservableValue<String> call(CellDataFeatures<XYChart.Series<String, Number>, String> param) {
			    	 return new SimpleStringProperty(param.getValue().getName());
			     }
			  });
			chartTable.getColumns().add(legendCol);
			
			final ObservableList<XYChart.Data<String, Number>> firstSeriesData = chart.getData().get(0).getData();
		    for (final XYChart.Data<String, Number> item: firstSeriesData) {
		    	TableColumn<Series<String, Number>, Number> col = new TableColumn<Series<String, Number>, Number>(item.getXValue().toString());
		    	col.setSortable(false);
		    	col.setCellValueFactory(new Callback<CellDataFeatures<XYChart.Series<String,Number>, Number>, ObservableValue<Number>>() {
		    		public ObservableValue<Number> call(CellDataFeatures<XYChart.Series<String,Number>, Number> param) {
		    			for (XYChart.Data<String, Number> currentItem: param.getValue().getData()) {
		    				if (currentItem.getXValue().equals(item.getXValue())) {
		    					return currentItem.YValueProperty();
		    				}
		    			}
		    			return null;
		    		}
		    	 });
		    	 chartTable.getColumns().add(col);
		      }
		}
		for (XYChart.Series<String,Number> series: chart.getData()) {
		  	  chartTable.getItems().add(series);
		}  
		
		return chartTable;
	}

}
