package uk.ac.standrews.grasp.ide.figures;


/**
 * Base class for figures that are nodes rather than containers
 * @author Dilyan Rusev
 *
 */
abstract class AbstractNodeFigure extends AbstractElementFigure {
	
	@Override
	protected IHeaderBorder createBorder() {
		return new BodyBorder(getIcon());
	}
	
}
