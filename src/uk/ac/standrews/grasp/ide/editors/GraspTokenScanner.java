/**
 * 
 */
package uk.ac.standrews.grasp.ide.editors;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.jface.text.rules.WordRule;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.RGB;

import uk.ac.standrews.grasp.ide.GraspPlugin;
import uk.ac.standrews.grasp.ide.model.GraspManager;

/**
 * @author Dilyan Rusev
 *
 */
public class GraspTokenScanner extends RuleBasedScanner {

	public GraspTokenScanner() {		
		IRule[] rules = {		
			createKeywordRule(),
			new WhitespaceRule(new WhitespaceDetector()),
			createCommentsRule(),
			new SingleLineRule("\"", "\"", getToken(GraspTextEditor.RGB_STRING_LITERAL, 0), '\\'),
			new SingleLineRule("'", "'", getToken(GraspTextEditor.RGB_STRING_LITERAL, 0), '\\'),
			new SingleLineRule("#\"", "\"", getToken(GraspTextEditor.RGB_DECLARATIVE_LITERAL, 0), '\\'),
			new SingleLineRule("#'", "'", getToken(GraspTextEditor.RGB_DECLARATIVE_LITERAL, 0), '\\'),
			new MultiLineRule("/*", "*/", getToken(GraspTextEditor.RGB_BLOCK_COMMENT, 0))
		};
		setRules(rules);
	}
	
	private static IToken getToken(RGB colour, int style) {
		return new Token(new TextAttribute(
				GraspPlugin.getDefault().getColour(colour),  // colour
				null,                                        // background
				style));                                     // SWT.BOLD, SWT.ITALIC, etc
	}
	
	private IRule createKeywordRule() {
		WordRule keywordRule = new WordRule(new KeywordDetector());
		IToken keywordToken = getToken(GraspTextEditor.RGB_KEYWORD, SWT.BOLD);
		for (String keyword: GraspManager.KEYWORDS) {
			keywordRule.addWord(keyword, keywordToken);
		}
		return keywordRule;
	}
	
	private IRule createCommentsRule() {
		IToken token = getToken(GraspTextEditor.RGB_INLINE_COMMENT, SWT.NONE);
		SingleLineRule rule = new SingleLineRule("//", null, token);
		return rule;
	}
	
}
