package uk.ac.standrews.grasp.ide.editors;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.rules.IPartitionTokenScanner;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;

import uk.ac.standrews.grasp.ide.GraspPlugin;

/**
 * Set-up a grasp content file into partitions to aid code completion and code colouring
 * @author Dilyan Rusev
 *
 */
public class PartitionScanner extends RuleBasedPartitionScanner {
	public static final String[] PARTITIONS = {
		"__grasp_block_comment",
		"__grasp_default_text"
	};
	public static final String BLOCK_COMMENT = PARTITIONS[0];
	public static final String DEFAULT_TEXT = PARTITIONS[1];
	
	public PartitionScanner() {
		GraspPlugin plugin = GraspPlugin.getDefault();
		
	}
}
