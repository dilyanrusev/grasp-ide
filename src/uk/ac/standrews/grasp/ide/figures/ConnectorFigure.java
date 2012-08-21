package uk.ac.standrews.grasp.ide.figures;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

import uk.ac.standrews.grasp.ide.GraspPlugin;
import uk.ac.standrews.grasp.ide.editParts.IconsCache;

/**
 * Figure to draw Grasp connectors
 * @author Dilyan Rusev
 *
 */
public class ConnectorFigure extends AbstractNodeFigure {
	
	@Override
	protected Image createIcon() {
		return IconsCache.getDefault().getConnectorIcon();
	}
	
	@Override
	protected Color createBackgroundColour() {
		return GraspPlugin.getDefault().getColour(255, 200, 200);
	}
}
