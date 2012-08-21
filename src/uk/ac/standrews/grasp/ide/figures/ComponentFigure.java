package uk.ac.standrews.grasp.ide.figures;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

import uk.ac.standrews.grasp.ide.GraspPlugin;
import uk.ac.standrews.grasp.ide.editParts.IconsCache;

/**
 * Figure to draw Grasp components
 * @author Dilyan Rusev
 *
 */
public class ComponentFigure extends AbstractNodeFigure {	
	
	@Override
	protected Color createBackgroundColour() {
		return GraspPlugin.getDefault().getColour(200, 200, 255);
	}
	
	@Override
	protected Image createIcon() {
		return IconsCache.getDefault().getComponentIcon();
	}
}
