package tp.model.statistics;

import java.sql.Date;
import java.util.List;

public class RatioStatistic extends Statistic{
	
	private Date date;

	public RatioStatistic(String title, List<StatisticValues> values, Date date) {
		super(title, values);
		this.date = date;
	}

	
	//=====================================
	
	
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	

}
