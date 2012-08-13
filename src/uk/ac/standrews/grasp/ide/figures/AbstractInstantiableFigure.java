package uk.ac.standrews.grasp.ide.figures;

import org.eclipse.draw2d.Label;

import uk.ac.standrews.grasp.ide.editParts.IconsCache;

/**
 * Default implementation of {@link IInstantiableFigure}
 * @author Dilyan Rusev
 *
 */
abstract class AbstractInstantiableFigure extends AbstractFirstClassFigure 
	implements IInstantiableFigure {
	
	@Override
	public void clearBody() {
		getBody().removeAll();
	}
	
	@Override
	public void addProvides(String name) {
		Label provides = new Label(name, IconsCache.getDefault().getProvidesIcon());
		getBody().add(provides);
	}
	
	@Override
	public void addRequires(String name) {
		Label requires = new Label(name, IconsCache.getDefault().getRequiresIcon());
		getBody().add(requires);
	}
}
