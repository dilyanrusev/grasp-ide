package uk.ac.standrews.grasp.ide.editors;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWhitespaceDetector;
import org.eclipse.jface.text.rules.Token;

/**
 * Scans the text file for opening and closing pairs of comments
 * @author Dilyan Rusev
 *
 */
class BlockCommentsRule implements IRule {
	private IWhitespaceDetector whitespaceDetector = new WhitespaceDetector();

	@Override
	public IToken evaluate(ICharacterScanner scanner) {
		/**
		 * Roughly speaking:
		 * 1. Scan backwards to find the opening character
		 * 2. Scan forwards to find the closing character
		 */
		if (scanForOpening(scanner) && true) {
			return Token.OTHER;
		} else {
			return Token.UNDEFINED;
		}
	}
	
	private boolean scanForOpening(ICharacterScanner scanner) {
//		int current;
//		char ch = '\0', prevCh = '\0';
//		
//		current = scanner.read();
//		ch = current != -1 ? (char) current : '\0';
//		if (ch == '/') { 
//			prevCh = ch;
//			current = scanner.read();
//			ch = current != -1 ? (char) current : '\0';
//			if (ch == '*')
//		}
//		while ((ch != '*' && prevCh != '/') ||
//				current != ICharacterScanner.EOF) {
//			prevCh = ch;
//			current = scanner.read();
//			ch = current != -1 ? (char) current : '\0';
//		}
//		return ch == '*' && prevCh == '/';
		return false;
	}
	
	private void scanForWhitespaceBackwards(ICharacterScanner scanner) {
		scanner.unread();
		int current = scanner.read();
		while (current != -1 && !whitespaceDetector.isWhitespace((char)current)) {
			scanner.unread();
			current = scanner.read();
		}
	}
	
	private boolean scanForClosing(ICharacterScanner scanner) {
		return false;
	}
	
	private enum States {
		SearchingForOpeningBracket,
		SearchingForClosingBracket
	}

}
