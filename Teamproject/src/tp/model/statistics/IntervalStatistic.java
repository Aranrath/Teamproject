package tp.model.statistics;

import java.sql.Date;

import javafx.collections.ObservableList;

public class IntervalStatistic extends Statistic{

	private Date startDate;
	private Date endDate;
	private int step;
	
	public IntervalStatistic(String title, ObservableList<StatisticValues> values, int step) {
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

	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
	}
	
	
}
