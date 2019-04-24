package tp.model.statistics;

import java.sql.Date;
import java.util.List;

public class IntervalStatistic extends Statistic{

	private Date startDate;
	private Date endDate;
	private int step;
	
	public IntervalStatistic(String title, List<StatisticValues> values, Date startDate, Date endDate, int step) {
		super(title, values);
		this.startDate = startDate;
		this.endDate = endDate;
		this.step = step;
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
