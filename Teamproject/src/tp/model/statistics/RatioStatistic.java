package tp.model.statistics;

import javafx.collections.ObservableMap;

public class RatioStatistic extends Statistic{

	private ObservableMap<String, Integer> values;
	
	public RatioStatistic(String title, ObservableMap<String, Integer> values) {
		super(title);
		this.values = values;
	}

	public ObservableMap<String, Integer> getValues() {
		return values;
	}

	public void setValues(ObservableMap<String, Integer> values) {
		this.values = values;
	}

}
