package uk.ac.standrews.grasp.ide.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.OrderedLayout;

import uk.ac.standrews.grasp.ide.editParts.IconsCache;

/**
 * Figure to display the Grasp System element
 * @author Dilyan Rusev
 *
 */
public class SystemFigure extends Figure {
	private HeaderBorder header;

	/**
	 * Construct the figure
	 */
	public SystemFigure() {
		FlowLayout layout = new FlowLayout(false);		
		layout.setMajorSpacing(2000);
		layout.setMinorSpacing(10);
		layout.setMajorAlignment(OrderedLayout.ALIGN_CENTER);
		layout.setMinorAlignment(OrderedLayout.ALIGN_CENTER);		
		setLayoutManager(layout);		
		header = new FoldedCornerHeaderBorder(IconsCache.getDefault().getSystemIcon());
		header.setBackgroundColor(ColorConstants.white);
		setBorder(header);	
		
	}

	/**
	 * Set the figure's text
	 * @param text Text of the figure's header
	 */
	public void setHeaderText(String text) {
		header.setText(text);
	}
}
