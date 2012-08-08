package uk.ac.standrews.grasp.ide.editors;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWhitespaceDetector;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.swt.graphics.Color;

import uk.ac.standrews.grasp.ide.GraspPlugin;
import uk.ac.standrews.grasp.ide.preferences.Preferences;

public final class TextUtil {
	/** Lists all keywords in Grasp */
	public static final Set<String> KEYWORDS = 
			Collections.unmodifiableSet(new TreeSet<String>(Arrays.asList(new String[] {
			"architecture", "requirement", "quality_attribute", "property",
			"rationale", "reason", "template", "system", "layer", "over",
			"component", "connector", "provides", "requires", "check",
			"link", "extends", "because", "supports", "inhibits", "to", 
			"true", "false", "subsetof", "accepts"
	})));	
	
	private static final String ALPHA_CHAR = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String DECIMAL_DIGIT = "0123456789";
	private static final String ALPHANUMERIC_CHAR = ALPHA_CHAR + DECIMAL_DIGIT;
	private static final String IDENTIFIER_CHAR = ALPHANUMERIC_CHAR + "_";
	private static final Pattern IDENTIFIER_PATTERN = 
			Pattern.compile("[_a-zA-Z]\\w*");
	
	private static final IWhitespaceDetector WHITESPACE_DETECTOR = new WhitespaceDetector();
	
	private TextUtil() {		
	}
	
	public static boolean isSyntaxColouringEnabled() {
		return true;
	}
	
	public static Color getKeywordColour() {
		return GraspPlugin.getDefault().getColour(Preferences.getKeywordRgb());
	}
	
	public static Color getInlineCommentColour() {
		return GraspPlugin.getDefault().getColour(Preferences.getInlineCommentRgb());
	}
	
	public static Color getBlockCommentColour() {
		return GraspPlugin.getDefault().getColour(Preferences.getBlockCommentRgb());
	}
	
	public static Color getStringLiteralColour() {
		return GraspPlugin.getDefault().getColour(Preferences.getStringLiteralRgb());
	}
	
	public static Color getDeclarativeLiteralColour() {
		return GraspPlugin.getDefault().getColour(Preferences.getDeclarativeLiteralRgb());
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
		return new KeywordRule(token);
	}
	
	public static boolean isWhitespace(char c) {
		return WHITESPACE_DETECTOR.isWhitespace(c);
	}
	
	public static boolean isIdentifier(char c) {
		return IDENTIFIER_CHAR.indexOf(c) != -1;
	}
	
	public static boolean isIdentifier(String expression) {
		return expression != null && IDENTIFIER_PATTERN.matcher(expression).matches();
	}
	
	public static boolean isNullOrEmpty(String expression) {
		return expression == null || expression.length() == 0;
	}
	
	public static boolean isNullOrWhitespace(String expression) {
		return isNullOrEmpty(expression) || expression.trim().length() == 0;
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
