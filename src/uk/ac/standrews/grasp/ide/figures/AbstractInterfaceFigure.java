package uk.ac.standrews.grasp.ide.figures;

import org.eclipse.draw2d.AbstractBorder;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.swt.graphics.Image;

/**
 * Base class for figures that draw Provides and Requries
 * @author Dilyan Rusev
 *
 */
public abstract class AbstractInterfaceFigure extends Label 
		implements IFirstClassFigure {
	
	/**
	 * Construct the figure
	 */
	public AbstractInterfaceFigure() {
		setIcon(createIcon());		
		setBorder(new SelectionBorder(new NullBorder()));		
		setToolTip(new Label(getIcon()));
	}

	/**
	 * Create an image to be used for the figure and its tooltip
	 * @return Image
	 */
	protected abstract Image createIcon();
	
	@Override
	public void setHeaderText(String text) {
		setText(text);
	}

	@Override
	public void setTooltipText(String text) {
		((Label) getToolTip()).setText(text);
	}

	@Override
	public void setSelected(boolean selected) {
		((SelectionBorder) getBorder()).setDrawAdditionalBorder(selected);
		repaint();
	}
	
	private static class NullBorder extends AbstractBorder {
		private static final Insets EMPTY_INSETS = new Insets();

		@Override
		public Insets getInsets(IFigure figure) {
			return EMPTY_INSETS;
		}

		@Override
		public void paint(IFigure figure, Graphics graphics, Insets insets) {
			// do nothing			
		}
	}
}
