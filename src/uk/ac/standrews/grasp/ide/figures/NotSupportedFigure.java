package uk.ac.standrews.grasp.ide.figures;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.StackLayout;

public class NotSupportedFigure extends Figure {
	//private static final Dimension SIZE = new Dimension(100, 50);
	
	public NotSupportedFigure() {
		setLayoutManager(new StackLayout());
	}
	
}
