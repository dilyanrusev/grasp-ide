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
	private static final char LF = Character.toChars(0x000A)[0];
	private static final char CR = Character.toChars(0x000D)[0];
	private static final char NL = Character.toChars(0x0085)[0];
	private static final char LS1 = Character.toChars(0x2028)[0];
	//private static final char LS2 = Character.toChars(0x2028)[1];
	private static final char PS1 = Character.toChars(0x2029)[0];
	//private static final char PS2 = Character.toChars(0x2029)[1];
	
	@Override
	public boolean isWhitespace(char c) {
		return c == SPACE || c == TAB || c == FORM_FEED || c == VERT_TAB 
				|| c == LF || c == CR || c == NL || c == LS1 //|| c == LS2
				|| c == PS1 //|| c == PS2
				;
	}
}
