package uk.ac.standrews.grasp.ide.figures;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.KeyEvent;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LayoutManager;
import org.eclipse.draw2d.OrderedLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

/**
 * Base class for Grasp figures
 * @author Dilyan Rusev
 *
 */
abstract class AbstractElementFigure extends Figure implements IFirstClassFigure {
	private IHeaderBorder header;
	private SelectionBorder selectionBorder;
	private Image icon;

	/**
	 * Create a new figure
	 */
	public AbstractElementFigure() {		
		setLayoutManager(createLayout());
		header = createBorder();
		selectionBorder = new SelectionBorder(header);
		setBorder(selectionBorder);
		setToolTip(createTooltip());
		Color bg = createBackgroundColour();
		if (bg != null) {
			setBackgroundColor(bg);
			setOpaque(true);
		}
	}
	
	/**
	 * Create the child layout. By default, creates a FlowLayout.
	 * @return
	 */
	protected LayoutManager createLayout() {
		FlowLayout layout = new FlowLayout(false);		
		layout.setMinorSpacing(10);
		layout.setMajorAlignment(OrderedLayout.ALIGN_CENTER);
		layout.setMinorAlignment(OrderedLayout.ALIGN_CENTER);
		return layout;
	}
	
	/**
	 * Create the background colour. If != null, will be opaque 
	 * and background color will be the color returned
	 * @return
	 */
	protected Color createBackgroundColour() {
		return null;
	}

	
	/**
	 * Create a border. By default, creates a HeaderBorder
	 * @return
	 */
	protected IHeaderBorder createBorder() {
		IHeaderBorder border = 
				new HeaderBorder(getIcon());		
		return border;
	}
	
	/**
	 * Create a tooltip. By default, creates a {@link org.eclipse.draw2d.Label}. If you change to other figure type,
	 * override {@link #setTooltipText(String)} as well
	 * @return
	 */
	protected IFigure createTooltip() {
		Label tooltip = new Label(getIcon());
		return tooltip;
	}
	
	/**
	 * Create an icon for this figure. Cached value can be accessed via {@link #getIcon()}
	 * @return
	 */
	protected abstract Image createIcon();
	
	/**
	 * Return the cached image returned by {@link #createIcon()}
	 * @return
	 */
	protected Image getIcon() {
		if (icon == null) {
			icon = createIcon();
		}
		return icon;
	}

	@Override
	public void setHeaderText(String text) {
		header.setText(text);
	}
	
	@Override
	public void setTooltipText(String text) {
		if (getToolTip() instanceof Label) {
			Label tooltip = (Label) getToolTip();
			tooltip.setText(text);
		}
	}
	
	@Override
	public void setSelected(boolean selected) {
		selectionBorder.setDrawAdditionalBorder(selected);		
		repaint();
	}
	
	@Override
	public void handleKeyPressed(KeyEvent event) {
		if (event.character == SWT.DEL) {
			System.out.printf("%s => DELETE%n", this);
		} else {
			System.out.println("" + this + " => " + event.character);
		}
		super.handleKeyPressed(event);
	}

}