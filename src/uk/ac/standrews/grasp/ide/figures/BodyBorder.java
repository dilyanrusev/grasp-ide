package uk.ac.standrews.grasp.ide.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.swt.graphics.Image;

/**
 * Extends header border to draw a rectangle around the figure and 
 * a line below the header text and icon, before the client area
 * @author Dilyan Rusev
 *
 */
public class BodyBorder extends HeaderBorder {

	/**
	 * Construct a new body border and bind it to an icon
	 * @param icon Icon to associate with the header of the border
	 */
	public BodyBorder(Image icon) {
		super(icon);		
	}

	@Override
	public Insets getInsets(IFigure figure) {
		Insets insets = new Insets(super.getInsets(figure));
		insets.top += 1;
		return insets;
	}

	@Override
	public void paint(IFigure figure, Graphics graphics, Insets insets) {
		Insets superInsets = new Insets(insets);
		superInsets.top -= 1;
		super.paint(figure, graphics, superInsets);
		tempRect = getPaintRectangle(figure, insets);
		tempRect.width -= 1;
		tempRect.height -= 1;
		graphics.drawRectangle(tempRect);		
		tempRect.y += getSize().height;
		graphics.drawLine(tempRect.getTopLeft(), tempRect.getTopRight());
	}


}
