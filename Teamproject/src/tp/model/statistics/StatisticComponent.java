package tp.model.statistics;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;

public class StatisticComponent
{
	public static final ObservableList<String> STUDENT_FILTER_METHODS = FXCollections.observableArrayList("Geschlecht", "ECTS", "Betreuungszeit in h","Semester", "Anzahl zugehöriger Anliegen", "PO", "Letzter Kontakt");
	public static final ObservableList<String> CONCERN_FILTERS_METHODS = FXCollections.observableArrayList("Thema", "Anzahl der Termine", "Gesamtlänge der Termine in h", "Status", "Anzahl der Studenten");

	//=====================================================
	
	private String name;
	private String source;
	private List<Filter> selectedFilter;

	public StatisticComponent (String name, String source, List<Filter> selectedFilter)
	{
		this.name = name;
		this.source = source;
		this.selectedFilter = selectedFilter;
	}

	public String getName() {
		return name;
	}

	public String getSource() {
		return source;
	}

	public List<Filter> getSelectedFilter() {
		return selectedFilter;
	}
	
	//====================================innere Klasse

	public static class Filter
	{
		
		private Map<String, Object[]> filters;
		
		//========================================
		
		public Filter() {
			filters = new HashMap<String, Object[]>();
		}
		
		public void addFilter(String name, Object...objects)
		{
			filters.put(name, objects);
		}

		public void deleteFilter(String name) {
			filters.remove(name);
		}
		
		public Map<String, Object[]> getFilters() {
			return filters;
		}
	}
	
}


