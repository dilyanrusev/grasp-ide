package uk.ac.standrews.grasp.ide.figures;


/**
 * Common interface for all figures that draw first-class elements
 * @author Dilyan Rusev
 *
 */
public interface IFirstClassFigure {

	/**
	 * Set the text in the header label
	 * @param text New caption
	 */
	void setHeaderText(String text);
	
	/**
	 * Set the tooltip text
	 * @param text Tooltip text
	 */
	void setTooltipText(String text);
}