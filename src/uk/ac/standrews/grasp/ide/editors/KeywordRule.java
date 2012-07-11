package uk.ac.standrews.grasp.ide.editors;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

/**
 * Scans text for keywords
 * @author Dilyan Rusev
 *
 */
class KeywordRule implements IRule {
	private IToken token;
	
	public KeywordRule(IToken token) {
		Assert.isNotNull(token);
		this.token = token;
	}

	@Override
	public IToken evaluate(ICharacterScanner scanner) {
		int c = scanner.read();
		if (c != ICharacterScanner.EOF && TextUtil.isIdentifier((char)c)) {
			StringBuilder txt = new StringBuilder();
			do {
				txt.append((char)c);
				c = scanner.read();
			} while (c != ICharacterScanner.EOF && TextUtil.isIdentifier((char)c));
			
			if (TextUtil.KEYWORDS.contains(txt.toString())) {
				return token;
			}
		}
		scanner.unread();
		return Token.UNDEFINED;
	}

}
