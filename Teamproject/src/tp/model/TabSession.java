package tp.model;

import java.io.Serializable;
import java.util.ArrayList;

public class TabSession implements Serializable {

	private static final long serialVersionUID = 1249294932494086121L;

	private ArrayList<String> sessionTabs = new ArrayList<String>();

	public TabSession() {

	}

	public ArrayList<String> getSessionTabs() {
		return sessionTabs;
	}

	public void setSessionTabs(ArrayList<String> sessionTabs) {
		this.sessionTabs = sessionTabs;
	}
	
	public boolean existsTabAlready(String idNewView)
	{
		for (String idSessionTabs: sessionTabs)
		{
			if(idNewView.equals(idSessionTabs))
			{
				return true;
			}
			
		}
		return false;
	}

}
