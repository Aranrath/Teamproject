package tp.model.statistics;

import javafx.collections.ObservableMap;

public class IntervalStatistic extends Statistic{

	//Map(name, Map(step, value))
	private ObservableMap<String, ObservableMap<Integer, Integer>> values;
	
	public IntervalStatistic(String title, ObservableMap<String, ObservableMap<Integer, Integer>> values, int step) {
		super(title);
		this.values = values;
	}

	public ObservableMap<String, ObservableMap<Integer, Integer>> getValues() {
		return values;
	}

	public void setValues(ObservableMap<String, ObservableMap<Integer, Integer>> values) {
		this.values = values;
	}
	
	
}
