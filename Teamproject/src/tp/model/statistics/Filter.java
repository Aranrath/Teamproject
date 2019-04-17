package tp.model.statistics;

import javafx.collections.ObservableList;
import javafx.util.Pair;

public class Filter {

	private String source;
	//First List of 'or' components, Second List of 'and' components, Type (ClassName, ObjectToBeFiltered)  
	//ex: {{()and()and()} or {()and()}}
	private ObservableList<ObservableList<Pair<String, Object>>> filter;
	
	public Filter(String source, ObservableList<ObservableList<Pair<String, Object>>> filter) {
		super();
		this.source = source;
		this.filter = filter;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public ObservableList<ObservableList<Pair<String, Object>>> getFilter() {
		return filter;
	}

	public void setFilter(ObservableList<ObservableList<Pair<String, Object>>> filter) {
		this.filter = filter;
	}
	
}
