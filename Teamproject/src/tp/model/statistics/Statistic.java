package tp.model.statistics;

public class Statistic {
	
	private long id;
	private String title;
	
	public Statistic(String title)
	{
		this.title = title;
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
}
