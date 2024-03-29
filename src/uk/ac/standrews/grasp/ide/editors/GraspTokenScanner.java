/**
 * 
 */
package uk.ac.standrews.grasp.ide.editors;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.Token;

/**
 * Rule-based scanner for syntax highlighting
 * @author Dilyan Rusev
 *
 */
public class GraspTokenScanner extends RuleBasedScanner {
	/**
	 * Construct the scanner and create the rules
	 */
	public GraspTokenScanner() {	
		IToken stringLiteral = new Token(new TextAttribute(TextUtil.getStringLiteralColour()));
		IToken declarativeLiteral = new Token(new TextAttribute(TextUtil.getDeclarativeLiteralColour()));
		IToken keyword = new Token(new TextAttribute(TextUtil.getKeywordColour()));
		IToken inlineComment = new Token(new TextAttribute(TextUtil.getInlineCommentColour()));
		

		setRules(new IRule[] {
				TextUtil.createKeywordsRule(keyword),
				TextUtil.createWhitespaceRule(),
				TextUtil.createSingleQuoteStringLiteralRule(stringLiteral),
				TextUtil.createDoubleQuoteStringLiteralRule(stringLiteral),
				TextUtil.createSingleQuoteDeclarativeLiteralRule(declarativeLiteral),
				TextUtil.createDoubleQuoteDeclarativeLiteralRule(declarativeLiteral),
				TextUtil.createInlineCommentsRule(inlineComment)
		});
	}
}
