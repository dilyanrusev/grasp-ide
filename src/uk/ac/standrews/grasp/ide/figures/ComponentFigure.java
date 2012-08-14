package uk.ac.standrews.grasp.ide.figures;

import org.eclipse.draw2d.Label;
import org.eclipse.swt.graphics.Color;

import uk.ac.standrews.grasp.ide.GraspPlugin;
import uk.ac.standrews.grasp.ide.editParts.IconsCache;

/**
 * Figure to draw Grasp components
 * @author Dilyan Rusev
 *
 */
public class ComponentFigure extends AbstractInstantiableFigure {

	@Override
	protected Label createHeadLabel() {
		return new Label(IconsCache.getDefault().getComponentIcon());		
	}
	
	@Override
	protected Color createBackgroundColour() {
		return GraspPlugin.getDefault().getColour(200, 200, 255);
	}
}
