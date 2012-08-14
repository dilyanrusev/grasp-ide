package uk.ac.standrews.grasp.ide.figures;

import org.eclipse.draw2d.AbstractBorder;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * Child figure for fist class figures that should contain the children
 * @author Dilyan Rusev
 *
 */
public class BodyFigure extends Figure {
	/**
	 * Create a new body figure
	 */
	public BodyFigure() {
		ToolbarLayout layout = new ToolbarLayout();
		layout.setMinorAlignment(ToolbarLayout.ALIGN_TOPLEFT);
		layout.setStretchMinorAxis(true);
		layout.setSpacing(2);
		setLayoutManager(layout);
		setBorder(new BodyBorder());
	}
	
	/**
	 * Paint a line on the top
	 * @author Dilyan Rusev
	 *
	 */
	private static class BodyBorder extends AbstractBorder {

		@Override
		public Insets getInsets(IFigure figure) {
			return new Insets(1, 0, 0, 0);
		}

		@Override
		public void paint(IFigure figure, Graphics graphics, Insets insets) {
			Rectangle bounds = getPaintRectangle(figure, insets);
			graphics.drawLine(bounds.getTopLeft(), bounds.getTopRight());
		}
		
	}
}
