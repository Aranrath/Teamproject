package tp.model.statistics;

import java.sql.Date;
import javafx.collections.ObservableMap;

public class ContinuousStatistic extends Statistic {

	// Map(name, Map(date, value))
	private ObservableMap<String, ObservableMap<Date, Integer>> values;

	public ContinuousStatistic(String title, ObservableMap<String, ObservableMap<Date, Integer>> values)
	{
		super(title);
		this.values = values;
	}

	public ObservableMap<String, ObservableMap<Date, Integer>> getValues() {
		return values;
	}

	public void setValues(ObservableMap<String, ObservableMap<Date, Integer>> values) {
		this.values = values;
	}

}
