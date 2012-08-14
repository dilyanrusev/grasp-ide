package uk.ac.standrews.grasp.ide.figures;

import org.eclipse.draw2d.Label;
import org.eclipse.swt.graphics.Color;

import uk.ac.standrews.grasp.ide.GraspPlugin;
import uk.ac.standrews.grasp.ide.editParts.IconsCache;

/**
 * Figure to draw Grasp connectors
 * @author Dilyan Rusev
 *
 */
public class ConnectorFigure extends AbstractInstantiableFigure {
	
	@Override
	protected Label createHeadLabel() {
		return new Label(IconsCache.getDefault().getConnectorIcon());
	}
	
	@Override
	protected Color createBackgroundColour() {
		return GraspPlugin.getDefault().getColour(255, 200, 200);
	}
}
