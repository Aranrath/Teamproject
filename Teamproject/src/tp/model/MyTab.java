package tp.model;

import javafx.scene.control.Tab;

public class MyTab extends Tab{
	
	String tabId;
	
	public MyTab(String viewId)
	{
		super();
		this.tabId = viewId;
	}

	public String getTabId() {
		return tabId;
	}

	public void setTabId(String viewId) {
		this.tabId = viewId;
	}
	

}
