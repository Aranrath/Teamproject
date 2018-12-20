package tp.concern;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import tp.model.Concern;

public class EditConcernView extends HBox{
	
	Concern concern;
	
	//new
	public EditConcernView()
	{
		buildView();
	}
	
	//change existing
	public EditConcernView(Concern concern)
	{
		this.concern = concern;
		buildView();
		fillView(concern);
	}
	
	private void buildView()
	{
		getChildren().addAll(new Label("EditConernView"));
	}

	private void fillView(Concern concern)
	{
		
	}
}
