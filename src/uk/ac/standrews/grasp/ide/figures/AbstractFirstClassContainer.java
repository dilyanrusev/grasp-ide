package uk.ac.standrews.grasp.ide.figures;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LayoutManager;
import org.eclipse.draw2d.OrderedLayout;
import org.eclipse.swt.graphics.Image;

/**
 * Base class for figures whose main duty is to contain other figures (e.g. layers & system)
 * @author Dilyan Rusev
 *
 */
abstract class AbstractFirstClassContainer extends Figure implements IFirstClassFigure {
	private IHeaderBorder header;
	private Image icon;

	/**
	 * Create a new container figure
	 */
	public AbstractFirstClassContainer() {		
		setLayoutManager(createLayout());
		header = createBorder();
		setBorder(header);
		setToolTip(createTooltip());
	}
	
	/**
	 * Create the child layout. By default, creates a FlowLayout.
	 * @return
	 */
	protected LayoutManager createLayout() {
		FlowLayout layout = new FlowLayout(false);		
		layout.setMajorSpacing(10);
		layout.setMinorSpacing(10);
		layout.setMajorAlignment(OrderedLayout.ALIGN_CENTER);
		layout.setMinorAlignment(OrderedLayout.ALIGN_CENTER);
		return layout;
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

}