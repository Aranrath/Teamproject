package tp.statistics;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import tp.model.Statistic;

public class StatisticView extends HBox{
	
	Statistic statistic;
	
	public StatisticView(Statistic statistic)
	{
		this.statistic = statistic;
		buildView();
		fillView();
	}
	
	private void buildView()
	{
		getChildren().addAll(new Label("Dies ist eine Statistic View"));
	}
	
	private void fillView()
	{
		
	}

}
