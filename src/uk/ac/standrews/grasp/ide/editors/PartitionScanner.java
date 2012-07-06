package uk.ac.standrews.grasp.ide.editors;

import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.Token;

/**
 * Set-up a grasp content file into partitions to aid code completion and code colouring
 * @author Dilyan Rusev
 *
 */
public class PartitionScanner extends RuleBasedPartitionScanner {
	public static final String[] PARTITIONS = {
		"__grasp_block_comment",
	};
	public static final String BLOCK_COMMENT = PARTITIONS[0];
	
	public static final PartitionScanner INSTANCE = new PartitionScanner();
	
	private PartitionScanner() {
		IToken undefined = Token.UNDEFINED;
		IToken blockComment = new Token(BLOCK_COMMENT);
		setPredicateRules(new IPredicateRule[] {
				TextUtil.createBlockCommentRule(blockComment),
				TextUtil.createDoubleQuoteDeclarativeLiteralRule(undefined),
				TextUtil.createDoubleQuoteStringLiteralRule(undefined),
				TextUtil.createInlineCommentsRule(undefined),
				TextUtil.createSingleQuoteDeclarativeLiteralRule(undefined),
				TextUtil.createSingleQuoteStringLiteralRule(undefined)			
		});
	}
}
