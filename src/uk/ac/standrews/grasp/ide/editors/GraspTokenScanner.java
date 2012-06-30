/**
 * 
 */
package uk.ac.standrews.grasp.ide.editors;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IWhitespaceDetector;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.jface.text.rules.WordRule;
import org.eclipse.swt.SWT;

import uk.ac.standrews.grasp.ide.GraspPlugin;
import uk.ac.standrews.grasp.ide.model.GraspManager;

/**
 * @author Dilyan Rusev
 *
 */
public class GraspTokenScanner extends RuleBasedScanner {

	public GraspTokenScanner() {
		GraspPlugin plugin = GraspPlugin.getDefault();
		IRule[] rules = new IRule[2];
		WordRule keywordRule = new WordRule(new KeywordDetector());
		TextAttribute keywordStyle = new TextAttribute(
				plugin.getColour(GraspTextEditor.RGB_KEYWORD),
				null, SWT.BOLD);
		Token keywordToken = new Token(keywordStyle);
		for (String keyword: GraspManager.KEYWORDS) {
			keywordRule.addWord(keyword, keywordToken);
		}
		rules[0] = keywordRule;
		rules[1] = new WhitespaceRule(new WhitespaceDetector());
		setRules(rules);
	}
	
}
