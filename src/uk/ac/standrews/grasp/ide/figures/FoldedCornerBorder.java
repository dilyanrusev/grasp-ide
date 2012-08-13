package uk.ac.standrews.grasp.ide.figures;

import org.eclipse.draw2d.AbstractBorder;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

public class FoldedCornerBorder extends AbstractBorder {
	private static final int FOLD_WIDTH = 15;
	private static final int FOLD_HEIGHT = 10;

	@Override
	public Insets getInsets(IFigure ifigure) {
		return new Insets(0, 0, FOLD_HEIGHT, FOLD_WIDTH);
	}

	@Override
	public void paint(IFigure figure, Graphics g, Insets insets) {
		Rectangle bounds = getPaintRectangle(figure, insets);
		Point foldStart = bounds.getBottomRight().getTranslated(-FOLD_WIDTH - 1, -1);
		Point foldEnd = bounds.getBottomRight().getTranslated(-1, -FOLD_HEIGHT - 1);
		
		g.drawLine(bounds.getBottomLeft(), foldStart);		
		g.drawLine(foldStart, foldEnd);		
		g.drawLine(foldEnd, bounds.getTopRight());		
	}
}
