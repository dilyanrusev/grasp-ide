package uk.ac.standrews.grasp.ide.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.swt.graphics.Image;

public class RoundedHeaderBorder extends HeaderBorder {
	private int vertSpace; 

	public RoundedHeaderBorder(Image icon) {
		super(icon);		
		Insets theMargin = getMargin();
		vertSpace = theMargin.bottom;
		theMargin.bottom = 0;
		setMargin(theMargin);		
	}
	
	/**
	 * Set the vertical space between the border and the figure's children
	 * @param newVertSpace
	 */
	public void setVertSpace(int newVertSpace) {
		vertSpace = newVertSpace;
	}
	
	/**
	 * Vertical space between the border and the figure's children
	 * @return
	 */
	public int getVertSpace() {
		return vertSpace;
	}
	
	@Override
	public Insets getInsets(IFigure figure) {
		Insets insets = super.getInsets(figure);
		insets.top += 1 + vertSpace;
		insets.bottom += 1;
		insets.left += 1;
		insets.right += 1;
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
