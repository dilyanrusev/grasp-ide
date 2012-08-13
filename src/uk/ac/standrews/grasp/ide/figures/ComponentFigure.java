package uk.ac.standrews.grasp.ide.figures;

import org.eclipse.draw2d.Label;

import uk.ac.standrews.grasp.ide.editParts.IconsCache;

/**
 * Figure to draw Grasp components
 * @author Dilyan Rusev
 *
 */
public class ComponentFigure extends AbstractFirstClassFigure {

	@Override
	protected Label createHeadLabel() {
		return new Label("<<component>>", IconsCache.getDefault().getComponentIcon());		
	}
	
	/**
	 * Clear child figures
	 */
	public void clearBody() {
		getBody().removeAll();
	}
	
	/**
	 * Construct a child figure for provides and add it to the children
	 * @param name Name of the provided interface
	 */
	public void addProvides(String name) {
		Label provides = new Label(name, IconsCache.getDefault().getProvidesIcon());
		getBody().add(provides);
	}
	
	/**
	 * Construct a child figure for requires and add it to the children
	 * @param name Name of the required interface
	 */
	public void addRequires(String name) {
		Label requires = new Label(name, IconsCache.getDefault().getRequiresIcon());
		getBody().add(requires);
	}
}
