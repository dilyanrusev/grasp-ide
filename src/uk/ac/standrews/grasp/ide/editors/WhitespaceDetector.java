package uk.ac.standrews.grasp.ide.editors;

import org.eclipse.jface.text.rules.IWhitespaceDetector;

class WhitespaceDetector implements IWhitespaceDetector {
	static final char TAB = Character.toChars(0x0009)[0];
	static final char VERT_TAB = Character.toChars(0x000B)[0];
	static final char FORM_FEED = Character.toChars(0x000C)[0];
	static final char SPACE = Character.toChars(0x0020)[0];
	
	@Override
	public boolean isWhitespace(char c) {
		return c == SPACE || c == TAB || c == FORM_FEED || c == VERT_TAB;
	}
}
