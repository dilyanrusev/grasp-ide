package uk.ac.standrews.grasp.ide.editors;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWhitespaceDetector;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.jface.text.rules.WordRule;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

import uk.ac.standrews.grasp.ide.GraspPlugin;

final class TextUtil {
	/** Lists all keywords in Grasp */
	public static final Set<String> KEYWORDS = 
			Collections.unmodifiableSet(new HashSet<String>(Arrays.asList(new String[] {
			"architecture", "requirement", "quality_attribute", "property",
			"rationale", "reason", "template", "system", "layer", "over",
			"component", "connector", "provides", "requires", "check",
			"link", "extends", "because", "supports", "inhibits", "to", 
			"true", "false"
	})));
	public static final RGB RGB_KEYWORD = new RGB(127, 0, 85);
	public static final RGB RGB_INLINE_COMMENT = new RGB(63, 127, 95);
	public static final RGB RGB_BLOCK_COMMENT = new RGB(63, 127, 95);
	public static final RGB RGB_STRING_LITERAL = new RGB(42, 0, 255);
	public static final RGB RGB_DECLARATIVE_LITERAL = new RGB(63, 95, 191);
	
	private static final String ALPHA_CHAR = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String DECIMAL_DIGIT = "0123456789";
	private static final String ALPHANUMERIC_CHAR = ALPHA_CHAR + DECIMAL_DIGIT;
	private static final String IDENTIFIER_CHAR = ALPHANUMERIC_CHAR + "_";
	
	private static final IWhitespaceDetector WHITESPACE_DETECTOR = new WhitespaceDetector();
	
	private TextUtil() {		
	}
	
	public static boolean isSyntaxColouringEnabled() {
		return true;
	}
	
	public static Color getKeywordColour() {
		return GraspPlugin.getDefault().getColour(RGB_KEYWORD);
	}
	
	public static Color getInlineCommentColour() {
		return GraspPlugin.getDefault().getColour(RGB_INLINE_COMMENT);
	}
	
	public static Color getBlockCommentColour() {
		return GraspPlugin.getDefault().getColour(RGB_BLOCK_COMMENT);
	}
	
	public static Color getStringLiteralColour() {
		return GraspPlugin.getDefault().getColour(RGB_STRING_LITERAL);
	}
	
	public static Color getDeclarativeLiteralColour() {
		return GraspPlugin.getDefault().getColour(RGB_DECLARATIVE_LITERAL);
	}	
	
	public static IPredicateRule createInlineCommentsRule(IToken token) {
		return new EndOfLineRule("//", token);
	}
	
	public static IPredicateRule createBlockCommentRule(IToken token) {
		return new MultiLineRule("/*", "*/", token, (char) 0, true);
	}
	
	public static IPredicateRule createSingleQuoteStringLiteralRule(IToken token) {
		return new SingleLineRule("'", "'", token, '\\');
	}
	
	public static IPredicateRule createDoubleQuoteStringLiteralRule(IToken token) {
		return new SingleLineRule("\"", "\"", token, '\\');
	}
	
	public static IPredicateRule createSingleQuoteDeclarativeLiteralRule(IToken token) {
		return new SingleLineRule("#'", "'", token, '\\');
	}
	
	public static IPredicateRule createDoubleQuoteDeclarativeLiteralRule(IToken token) {
		return new SingleLineRule("#\"", "\"", token, '\\');
	}	
	
	public static IRule createWhitespaceRule() {
		return new WhitespaceRule(new WhitespaceDetector());
	}
	
	public static IRule createKeywordsRule(IToken token) {
		WordRule rule = new WordRule(new IWordDetector() {			
			@Override
			public boolean isWordStart(char c) {
				return Character.isLetter(c);
			}
			
			@Override
			public boolean isWordPart(char c) {
				return Character.isLetter(c);
			}
		});
		
		for (String keyword: KEYWORDS) {
			rule.addWord(keyword, token);
		}
		
		return rule;
	}
	
	public static boolean isWhitespace(char c) {
		return WHITESPACE_DETECTOR.isWhitespace(c);
	}
	
	public static boolean isIdentifier(char c) {
		return IDENTIFIER_CHAR.indexOf(c) != -1;
	}
	
	/**
	 * Determines whether a character is whitespace 
	 * @author Dilyan Rusev
	 *
	 */
	private static class WhitespaceDetector implements IWhitespaceDetector {
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
}