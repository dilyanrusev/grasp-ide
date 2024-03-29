/**
 * 
 */
package uk.ac.standrews.grasp.ide.wizards;

/**
 * Grasp examples description
 * @author Dilyan Rusev
 *
 */
public interface IGraspExample {
	/**
	 * Retrieve the source code for this example
	 * @param architectureName Name of the architecture
	 * @return Example source code
	 */
	String getText(String architectureName);
	
	/**
	 * Retrieve the example name
	 * @return Name of the example
	 */
	String getName();
	
	/**
	 * Retrieve short description of the example
	 * @return Short description of the example
	 */
	String getDescription();
}
