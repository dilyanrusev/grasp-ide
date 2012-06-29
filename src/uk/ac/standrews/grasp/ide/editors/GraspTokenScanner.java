/**
 * 
 */
package uk.ac.standrews.grasp.ide.editors;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.ITokenScanner;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.WordRule;

/**
 * @author Dilyan Rusev
 *
 */
public class GraspTokenScanner extends RuleBasedScanner {

	public GraspTokenScanner() {
		List<IRule> rules = new ArrayList<IRule>();
		for (KeywordDetectors keywordDetector: KeywordDetectors.values()) {
			rules.add(new WordRule(keywordDetector, GraspTokens.KEYWORD, false));
		}
		setRules(rules.toArray(new IRule[rules.size()]));
	}

	@Override
	public char[][] getLegalLineDelimiters() {
		char[][] delimiters = new char[5][];
		delimiters[0] = Character.toChars(0x000A); // LF
		delimiters[1] = Character.toChars(0x000D); // CR	
		delimiters[2] = Character.toChars(0x0085); // Next line
		delimiters[3] = Character.toChars(0x2028); // Line separator
		delimiters[4] = Character.toChars(0x2029); // Paragraph separator
		return delimiters;
	}
}
