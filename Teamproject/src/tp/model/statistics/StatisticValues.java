package tp.model.statistics;

import java.util.List;

import javafx.scene.paint.Color;
import javafx.util.Pair;

public class StatisticValues {
	
	private String name;
	private Color color;
	private List<Pair<Integer, Integer>> values;
	
	public StatisticValues(String name, Color color, List<Pair<Integer, Integer>> values) {
		super();
		this.name = name;
		this.color = color;
		this.values = values;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public List<Pair<Integer, Integer>> getValues() {
		return values;
	}

	public void setValues(List<Pair<Integer, Integer>> values) {
		this.values = values;
	}

}
