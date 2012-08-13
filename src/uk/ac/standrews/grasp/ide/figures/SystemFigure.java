package uk.ac.standrews.grasp.ide.figures;

import org.eclipse.draw2d.Border;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Label;
import org.eclipse.swt.graphics.Color;

import uk.ac.standrews.grasp.ide.editParts.IconsCache;

public class SystemFigure extends AbstractFirstClassFigure {

	public SystemFigure() {
		getBody().setBorder(null);
		setOpaque(false);
	}
	
	@Override
	protected Label createHeadLabel() {
		Label header = new Label("<<system>>", IconsCache.getDefault().getSystemIcon());
		header.setBorder(new FoldedCornerBorder());
		return header;
	}
	
	@Override
	protected Color createBackgroundColour() {
		return ColorConstants.white;
	}
	
	@Override
	protected Border createBorder() {
		return null;
	}
}
