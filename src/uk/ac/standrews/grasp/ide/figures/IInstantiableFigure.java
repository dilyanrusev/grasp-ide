package uk.ac.standrews.grasp.ide.figures;

/**
 * Common functionality for connector and component figures
 * @author Dilyan Rusev
 *
 */
public interface IInstantiableFigure extends IFirstClassFigure {

	/**
	 * Clear child figures
	 */
	public abstract void clearBody();

	/**
	 * Construct a child figure for provides and add it to the children
	 * @param name Name of the provided interface
	 */
	public abstract void addProvides(String name);

	/**
	 * Construct a child figure for requires and add it to the children
	 * @param name Name of the required interface
	 */
	public abstract void addRequires(String name);

}