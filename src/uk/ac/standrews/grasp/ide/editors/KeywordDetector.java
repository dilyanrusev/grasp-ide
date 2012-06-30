package uk.ac.standrews.grasp.ide.editors;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jface.text.rules.IWordDetector;

import uk.ac.standrews.grasp.ide.model.GraspManager;

/**
 * Fast-and-dirty way of detecting whether a character can be a part of a keyword. Accuracy is
 * delegated to the <code>WordRule.addWord(String, IToken)</code> for keywords.
 * @author Dilyan Rusev
 *
 */
public class KeywordDetector implements IWordDetector {
	static final Set<Character> FIRST_LETTERS;
	static final Set<Character> ALL_LETTERS;
	
	static {
		Set<Character> firstLetters = new HashSet<Character>();
		Set<Character> allLetters = new HashSet<Character>();
		for (String keyword: GraspManager.KEYWORDS) {
			firstLetters.add(keyword.charAt(0));
			for (int i = 0; i < keyword.length(); i++) {
				allLetters.add(keyword.charAt(i));
			}
		}
		FIRST_LETTERS = firstLetters;
		ALL_LETTERS = allLetters;		
	}

	@Override
	public boolean isWordStart(char c) {
		return FIRST_LETTERS.contains(c);
	}

	@Override
	public boolean isWordPart(char c) {
		return ALL_LETTERS.contains(c);
	}

}
