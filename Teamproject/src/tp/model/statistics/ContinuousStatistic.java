package tp.model.statistics;

import java.sql.Date;

import javafx.collections.ObservableList;

public class ContinuousStatistic extends Statistic {

	private Date startDate;
	private Date endDate;
	
	public ContinuousStatistic(String title, ObservableList<StatisticValues> values) {
		super(title, values);
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
}
