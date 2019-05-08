package tp.model.statistics;

import java.util.List;

public class Statistic {
	
	private long id;
	private String title; 
	private List<StatisticValues> values;
	
	public Statistic(String title, List<StatisticValues> values)
	{
		this.title = title;
		this.setValues(values);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<StatisticValues> getValues() {
		return values;
	}

	public void setValues(List<StatisticValues> values) {
		this.values = values;
	}
	
	@Override
	public String toString() {
		return title;
	}
}
