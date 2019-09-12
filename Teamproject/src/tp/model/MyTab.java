package tp.model;

import javafx.scene.control.Tab;

public class MyTab extends Tab{
	
	private String tabId;
	
	public MyTab(String tabId)
	{
		super();
		this.tabId = tabId;
		setStyle("-fx-background-color: " + Options.TAB_BACKGROUND_COLOR + ";");
	}

	public String getTabId() {
		return tabId;
	}

	public void setTabId(String viewId) {
		this.tabId = viewId;
	}

}
