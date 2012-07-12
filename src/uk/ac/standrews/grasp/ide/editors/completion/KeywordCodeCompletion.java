package uk.ac.standrews.grasp.ide.editors.completion;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;

import uk.ac.standrews.grasp.ide.editors.TextUtil;

/**
 * Provides code completion for keywords
 * @author Dilyan Rusev
 *
 */
public class KeywordCodeCompletion implements ICodeCompletionProcessor {

	@Override
	public Collection<ICompletionProposal> evaluateContext(ICodeCompletionContext context) {
		String wordSoFar = context.getWordBeforeCursor();
		int replaceLen = context.getWordAfterCursor().length();
		int offset = context.getOffset();
		List<ICompletionProposal> completions = new ArrayList<ICompletionProposal>();
		for (String keyword: TextUtil.KEYWORDS) {
			int lenDiff = keyword.length() - wordSoFar.length();
			if (lenDiff > 0 && keyword.startsWith(wordSoFar)) {
				String addition = keyword.substring(wordSoFar.length());
				
				completions.add(new CompletionProposal(
						addition,    // string that is actually inserted
						offset,      // position in document at which we insert new text
						replaceLen,  // replace whatever we had after the cursor position
						lenDiff,     // the cursor position should advance after the completion
						null,        // no image
						keyword,     // what is actually displayed to the user
						null,        // no context
						null         // no additional propositions
						));
			}
		}
		
		return completions;
	}

}
