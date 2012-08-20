package uk.ac.standrews.grasp.ide.figures;

import org.eclipse.draw2d.AbstractBorder;
import org.eclipse.draw2d.Border;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.graphics.Color;

/**
 * Wraps another border and draws selection over it
 * @author Dilyan Rusev
 *
 */
public class SelectionBorder extends AbstractBorder {
	private final Border wrapped;
	private boolean draw;
	private static final int THICKNESS = 5;
	private Color color;
	private int thickness;
	
	/**
	 * Create a new selection border
	 * @param wrapped Border to wrap
	 */
	public SelectionBorder(Border wrapped) {
		this.wrapped = wrapped;
		this.draw = false;
		this.color = ColorConstants.black;
		this.thickness = THICKNESS;
	}
	
	/**
	 * Turn on or off the selection
	 * @param flag True to draw selection over wrapped border
	 */
	public void setDrawAdditionalBorder(boolean flag) {
		this.draw = flag;		
	}

	@Override
	public Insets getInsets(IFigure figure) {
		Insets insets = new Insets(wrapped.getInsets(figure));
		insets.top += thickness;
		insets.left += thickness;
		insets.right += thickness;
		insets.bottom += thickness;
		return insets;
	}
	
	@Override
	public Dimension getPreferredSize(IFigure f) {
		Dimension preferred = new Dimension(wrapped.getPreferredSize(f));
		return preferred.expand(2 * thickness, 2 * thickness);	
	}

	@Override
	public void paint(IFigure figure, Graphics graphics, Insets insets) {
		Insets wrappedInsets = new Insets(Math.max(0, insets.top - thickness)
				, Math.max(0, insets.left - thickness)
				, Math.max(0, insets.bottom - thickness)
				, Math.max(0, insets.right - thickness));
		wrapped.paint(figure, graphics, wrappedInsets);
		if (draw) {
			tempRect = getPaintRectangle(figure, insets);
			tempRect.width -= 1;
			tempRect.height -= 1;
			graphics.setForegroundColor(color);			
			graphics.setLineWidth(thickness);		
			graphics.setLineStyle(Graphics.LINE_DASHDOT);
			Point[] points = { tempRect.getTopLeft(), tempRect.getTopRight(),
					tempRect.getBottomRight(), tempRect.getBottomLeft()};
			graphics.drawLine(points[0], points[1]);
			graphics.drawLine(points[1], points[2]);
			graphics.drawLine(points[2], points[3]);
			graphics.drawLine(points[3], points[0]);
		}
	}

}
