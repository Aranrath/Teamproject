package tp.model;

import javafx.scene.control.Tab;

public class MyTab extends Tab{
	
	String viewId;
	
	public MyTab(String viewId)
	{
		super();
		this.viewId = viewId;
	}

	public String getViewId() {
		return viewId;
	}

	public void setViewId(String viewId) {
		this.viewId = viewId;
	}
	

}
