package uk.ac.standrews.grasp.ide.figures;

import org.eclipse.draw2d.Border;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.ToolbarLayout;

import uk.ac.standrews.grasp.ide.GraspPlugin;

/**
 * Base class for first-class figures
 * @author Dilyan Rusev
 *
 */
public abstract class AbstractFirstClassFigure extends Figure {
	private BodyFigure body;
	private Label header;
	
	/**
	 * Create a new figure. Override {@link #createBorder()} and {@link #createHeadLabel()} 
	 * to customise behaviour. Add child figures via {@link #getBody()}
	 */
	public AbstractFirstClassFigure() {
		body = new BodyFigure();
		ToolbarLayout layout = new ToolbarLayout();
		setLayoutManager(layout);
		setBorder(createBorder());
		setBackgroundColor(GraspPlugin.getDefault().getColour(255, 255, 206));
		setOpaque(true);
		
		header = createHeadLabel();
		add(header);
		add(body);
	}
	
	/**
	 * Create the label to be displayed at the top of the figure
	 * @return Text and Icon for the header of the figure
	 */
	protected abstract Label createHeadLabel();
	
	/**
	 * Create a custom border to encompass the figure. By default, creates
	 * a 1-pixel black line border
	 * @return Border for the outline of the figure
	 */
	protected Border createBorder() {
		return new LineBorder(ColorConstants.black, 1);
	}
	
	/**return header;
	 * Insert child figures here
	 * @return Children container
	 */
	protected BodyFigure getBody() {
		return body;
	}
	
	/**
	 * Set the text in the header label
	 * @param text New caption
	 */
	public void setHeaderText(String text) {
		header.setText(text);
	}
}
