package uk.ac.standrews.grasp.ide.figures;

import org.eclipse.draw2d.Border;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;

/**
 * Common interfaces for headers that draw icon + text
 * @author Dilyan Rusev
 *
 */
public interface IHeaderBorder extends Border {

	/**
	 * Set the text. Should not be null at the time of rendering
	 * @param newText Text of the header
	 */
	void setText(String newText);

	/**
	 * Get the header text
	 * @return
	 */
	String getText();

	/**
	 * Set the icon that is displayed before the text in the header
	 * @param newIcon Icon to display
	 */
	void setIcon(Image newIcon);

	/**
	 * Gets the icon to be displayed before the text
	 * @return
	 */
	Image getIcon();
	
	/**
	 * Get margin between header contents (icon + text) and figure contents
	 * @param newMargin
	 */
	void setMargin(Insets newMargin);
	
	/**
	 * Set header margin
	 * @return a copy
	 */
	Insets getMargin();
	
	/**
	 * Set the horizontal spacing between icon and text
	 * @param w Horizontal spacing
	 */
	void setSpacing(int w);
	
	/**
	 * Get the horizontal spacing between icon and text
	 * @return
	 */
	int getSpacing();
	
	/**
	 * Set the font. Uses figure's font by default
	 * @param newFont Font to use
	 */
	void setFont(Font newFont);
	
	/**
	 * Set foreground colour. By default, reuses the figure's colour
	 * @param color
	 */
	void setForegroundColor(Color color);
	
	/**
	 * Set the background colour. By default, reuses the figure's colour
	 * @param color
	 */
	void setBackgroundColor(Color color);

}