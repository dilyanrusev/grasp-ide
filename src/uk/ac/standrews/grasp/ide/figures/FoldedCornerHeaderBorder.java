package uk.ac.standrews.grasp.ide.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.swt.graphics.Image;

/**
 * Draws text + icon on top of the figure, and underlines them with a folded corner
 * @author Dilyan Rusev
 *
 */
public class FoldedCornerHeaderBorder extends HeaderBorder {
	private static final int FOLD_WIDTH = 15;
	private static final int FOLD_HEIGHT = 10;
	
	private int vertSpace; 

	/**
	 * Create a new border
	 * @param icon Icon to display. May be null.
	 */
	public FoldedCornerHeaderBorder(Image icon) {
		super(icon);
		Insets theMargin = getMargin();
		theMargin.bottom = 0;
		setMargin(theMargin);
		vertSpace = 10;
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
	protected Dimension calculateSize() {
		Dimension size = super.calculateSize();
		size.setWidth(Math.max(size.width, FOLD_WIDTH) + 1);
		size.setHeight(Math.max(size.height, FOLD_HEIGHT) + 1);
		return size;
	}
	
	@Override
	public Insets getInsets(IFigure figure) {		
		Insets result = super.getInsets(figure);
		result.top += vertSpace;
		return result;
	}
	
	@Override
	public void paint(IFigure figure, Graphics g, Insets insets) {
		super.paint(figure, g, insets);
		tempRect = getPaintRectangle(figure, insets);
		
		int x1 = tempRect.x;
		int y1 = tempRect.y + getSize().height - 1;
		int x2 = x1 + tempRect.width - FOLD_WIDTH;
		int y2 = y1;
		g.drawLine(x1, y1, x2, y2); // bottom before fold
		x1 = tempRect.x + tempRect.width - 1;
		y1 = tempRect.y + getSize().height - FOLD_HEIGHT - 1;		
		g.drawLine(x1, y1, x2, y2); // fold
		x2 = x1;
		y2 = tempRect.y;
		g.drawLine(x1, y1, x2, y2); // finish
	}
}
