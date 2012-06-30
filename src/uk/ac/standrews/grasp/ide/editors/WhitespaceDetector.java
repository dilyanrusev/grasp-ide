package uk.ac.standrews.grasp.ide.editors;

import org.eclipse.jface.text.rules.IWhitespaceDetector;

/**
 * Determines whether a character is whitespace 
 * @author Dilyan Rusev
 *
 */
class WhitespaceDetector implements IWhitespaceDetector {
	private static final char TAB = Character.toChars(0x0009)[0];
	private static final char VERT_TAB = Character.toChars(0x000B)[0];
	private static final char FORM_FEED = Character.toChars(0x000C)[0];
	private static final char SPACE = Character.toChars(0x0020)[0];
	
	@Override
	public boolean isWhitespace(char c) {
		return c == SPACE || c == TAB || c == FORM_FEED || c == VERT_TAB;
	}
}
