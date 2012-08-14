package uk.ac.standrews.grasp.ide.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.graphics.Image;

import uk.ac.standrews.grasp.ide.editParts.IconsCache;

/**
 * Figure to display the Grasp System element
 * @author Dilyan Rusev
 *
 */
public class SystemFigure extends AbstractFirstClassContainer {
	
	@Override
	protected Image createIcon() {
		return IconsCache.getDefault().getSystemIcon();
	}	
	
	@Override
	protected IHeaderBorder createBorder() {
		FoldedCornerHeaderBorder header = new FoldedCornerHeaderBorder(getIcon());
		header.setBackgroundColor(ColorConstants.white);
		return header;
	}
}
