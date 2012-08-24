package uk.ac.standrews.grasp.ide.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.swt.graphics.Image;

/**
 * Border that is specialized to draw around layers
 * @author Dilyan Rusev
 *
 */
public class LayerBorder extends HeaderBorder {	 
	private Insets childrenMargin;

	/**
	 * Construct a new border and associated it with an icon
	 * @param icon Initial icon
	 */
	public LayerBorder(Image icon) {
		super(icon);		
		Insets theMargin = getMargin();		
		theMargin.bottom = 0;
		setMargin(theMargin);
		childrenMargin = new Insets(5);
	}
	
	/**
	 * Set the spacing between the border and the figure's children
	 * @param newInsets New insets
	 */
	public void setChildrenMargin(Insets newInsets) {
		childrenMargin = newInsets;
	}
	
	/**
	 * Return a copy of the margin between the border and the figure's children
	 * @return Margin between the border and the figure's children
	 */
	public Insets getChildrenMargin() {
		return new Insets(childrenMargin);
	}
	
	@Override
	public Insets getInsets(IFigure figure) {
		Insets insets = super.getInsets(figure);
		insets.top += 1 + childrenMargin.top;
		insets.bottom += 1 + childrenMargin.bottom;
		insets.left += 1 + childrenMargin.left;
		insets.right += 1 + childrenMargin.right;
		return insets;
	}
	
	@Override
	public void paint(IFigure figure, Graphics g, Insets insets) {		
		super.paint(figure, g, insets);
		tempRect = getPaintRectangle(figure, insets);
		tempRect.setWidth(tempRect.width - 1);
		tempRect.setHeight(tempRect.height - 1);
		g.drawRoundRectangle(tempRect, 15, 10);
		g.setLineStyle(Graphics.LINE_DASH);
		int y = tempRect.y + getSize().height - 1;
		g.drawLine(tempRect.x, y, tempRect.x + tempRect.width - 1, y);
	}
}
