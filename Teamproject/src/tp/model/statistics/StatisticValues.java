package tp.model.statistics;

import java.util.List;

import javafx.scene.paint.Color;

public class StatisticValues {
	
	private String name;
	private Color color;
	private List<Integer> values;
	
	public StatisticValues(String name, Color color, List<Integer> values) {
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

	public List<Integer> getValues() {
		return values;
	}

	public void setValues(List<Integer> values) {
		this.values = values;
	}

}
