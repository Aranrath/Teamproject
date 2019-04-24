package tp.model.statistics;

import java.sql.Date;
import java.util.List;

public class ContinuousStatistic extends Statistic {

	private Date startDate;
	private Date endDate;
	
	public ContinuousStatistic(String title, List<StatisticValues> values, Date startDate, Date endDate) {
		super(title, values);
		this.startDate = startDate;
		this.endDate = endDate;
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
