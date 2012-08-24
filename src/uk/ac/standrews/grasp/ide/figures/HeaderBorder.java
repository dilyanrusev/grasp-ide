package uk.ac.standrews.grasp.ide.figures;

import org.eclipse.draw2d.AbstractBorder;
import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;

/**
 * Draws the header of figures - text + icon
 * The icon, if present, is drawn before (to the left of) the text
 * @author Dilyan Rusev
 *
 */
public class HeaderBorder extends AbstractBorder implements IHeaderBorder {
	private Image icon;
	private String text;
	private Font font;
	private int spacing;
	private Insets margin;
	private Color foreground;
	private Color background;
	private Dimension size;
	
	/**
	 * Create a new figure header, with both text and icon
	 * @param text Text of the header
	 * @param icon Icon of the header
	 */
	public HeaderBorder(String text, Image icon) {
		this.text = text;
		this.icon = icon;		
		this.margin = new Insets(5, 10, 5, 10);
		this.spacing = 5;		
	}
	
	/**
	 * Create a new figure with default text
	 * @param icon Icon of the header
	 */
	public HeaderBorder(Image icon) {
		this("<<header>>", icon);
	}
	
	@Override
	public void setMargin(Insets newMargin) {
		margin = newMargin;
		invalidate();
	}
	
	@Override
	public Insets getMargin() {
		return new Insets(margin);
	}
	
	@Override
	public void setSpacing(int w) {
		spacing = w;
		invalidate();
	}
	
	@Override
	public int getSpacing() {
		return spacing;
	}
	
	@Override
	public void setFont(Font newFont) {
		font = newFont;
		invalidate();
	}
	
	/* (non-Javadoc)
	 * @see uk.ac.standrews.grasp.ide.figures.IHeaderBorder#setText(java.lang.String)
	 */
	@Override
	public void setText(String newText) {
		text = newText;
		invalidate();
	}
	
	/* (non-Javadoc)
	 * @see uk.ac.standrews.grasp.ide.figures.IHeaderBorder#getText()
	 */
	@Override
	public String getText() {
		return text;
	}
	
	/* (non-Javadoc)
	 * @see uk.ac.standrews.grasp.ide.figures.IHeaderBorder#setIcon(org.eclipse.swt.graphics.Image)
	 */
	@Override
	public void setIcon(Image newIcon) {
		icon = newIcon;
		invalidate();
	}
	
	/* (non-Javadoc)
	 * @see uk.ac.standrews.grasp.ide.figures.IHeaderBorder#getIcon()
	 */
	@Override
	public Image getIcon() {
		return icon;
	}
	
	/**
	 * Set foreground colour. By default, reuses the figure's colour
	 * @param color
	 */
	public void setForegroundColor(Color color) {
		foreground = color;
	}
	
	/**
	 * Set the background colour. By default, reuses the figure's colour
	 * @param color
	 */
	public void setBackgroundColor(Color color) {
		background = color;
	}
	
	/**
	 * Make sure the next call to {@link #getSize()} will call {@link #calculateSize()}
	 */
	protected void invalidate() {
		size = null;
	}
	
	/**
	 * Get a cached copy of the desired size of the header
	 * @return Cached copy
	 */
	protected Dimension getSize() {
		if (size == null) {			
			size = calculateSize();
		}
		return size;
	}
	
	/**
	 * Calculate the preferred size of the header's contents
	 * @return Preferred size
	 */
	protected Dimension calculateSize() {
		Dimension textSize = FigureUtilities.getTextExtents(text, font);
		int w = textSize.width + margin.left + margin.right;
		int h = textSize.height + margin.top + margin.bottom;
		if (icon != null) {
			org.eclipse.swt.graphics.Rectangle imageSize = icon.getBounds();
			w += imageSize.width + spacing;
			h = Math.max(textSize.height, imageSize.height) + margin.top + margin.bottom;
		}
		return new Dimension(w, h);
	}

	@Override
	public Insets getInsets(IFigure figure) {
		if (font == null) {
			font = figure.getFont();
		}	
		int top = getSize().height;		
		return new Insets(top, 0, 0, 0);
	}
	
	@Override
	public Dimension getPreferredSize(IFigure f) {
		if (font == null) {
			font = f.getFont();
		}
		return getSize();
	}

	@Override
	public void paint(IFigure figure, Graphics g, Insets insets) {
		if (background == null) {
			background = figure.getBackgroundColor();
		}
		if (foreground == null) {
			foreground = figure.getForegroundColor();
		}
		if (font == null) {
			font = figure.getFont();
		}
		tempRect = getPaintRectangle(figure, insets);
		int x = tempRect.x + margin.left;
		int y = tempRect.y + margin.top;
		if (icon != null) {
			g.drawImage(icon, x, y);
			x += icon.getBounds().width + spacing;
		}
		g.setForegroundColor(foreground);
		if (figure.isOpaque()) {
			g.setBackgroundColor(background);
			g.fillString(text, x, y);
		} else {
			g.drawString(text, x, y);
		}
	}

}
