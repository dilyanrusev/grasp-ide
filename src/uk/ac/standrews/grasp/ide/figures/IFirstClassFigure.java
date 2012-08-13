package uk.ac.standrews.grasp.ide.figures;

import org.eclipse.draw2d.IFigure;

/**
 * Common interface for all figures that draw first-class elements
 * @author Dilyan Rusev
 *
 */
public interface IFirstClassFigure extends IFigure {

	/**
	 * Set the text in the header label
	 * @param text New caption
	 */
	void setHeaderText(String text);
	
	/**
	 * Insert child figures here
	 * @return Children container
	 */
	BodyFigure getBody();

}