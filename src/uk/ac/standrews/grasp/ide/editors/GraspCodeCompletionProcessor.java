package uk.ac.standrews.grasp.ide.editors;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;

import uk.ac.standrews.grasp.ide.Log;

/**
 * Code completion for Grasp
 * @author Dilyan Rusev
 *
 */
class GraspCodeCompletionProcessor implements IContentAssistProcessor {
	private static final IContextInformation[] EMPTY_CONTEXT_LIST = { };
	//private static final ICompletionProposal[] EMPTY_COMPLETION_LIST = { };
	private static final char[] PROPOSAL_ACTIVATION_CHARS = {};
	private static final char[] INFORMATION_ACTIVATION_CHARS = {};

	@Override
	public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer,	int offset) {
		IDocument doc = viewer.getDocument();
		String wordSoFar = getWordSoFar(doc, offset);
		List<ICompletionProposal> completions = new ArrayList<ICompletionProposal>();
		for (String keyword: TextUtil.KEYWORDS) {
			int lenDiff = keyword.length() - wordSoFar.length();
			if (lenDiff > 0 && keyword.startsWith(wordSoFar)) {
				String addition = keyword.substring(wordSoFar.length());
				
				completions.add(new CompletionProposal(
						addition,    // string that is actually inserted
						offset,      // position in document at which we insert new text
						0,           // we don't replace text; we insert new text => length of replaced text is 0
						lenDiff,     // the cursor position should advance after the completion
						null,        // no image
						keyword,     // what is actually displayed to the user
						null,        // no context
						null         // no additional propositions
						));
			}
		}
		
		return completions.toArray(new ICompletionProposal[completions.size()]);
	}

	@Override
	public IContextInformation[] computeContextInformation(ITextViewer viewer, int offset) {
		return EMPTY_CONTEXT_LIST;
	}

	@Override
	public char[] getCompletionProposalAutoActivationCharacters() {
		return PROPOSAL_ACTIVATION_CHARS;
	}

	@Override
	public char[] getContextInformationAutoActivationCharacters() {
		return INFORMATION_ACTIVATION_CHARS;
	}

	@Override
	public String getErrorMessage() {
		return "Something went terribly wrong :D";
	}

	@Override
	public IContextInformationValidator getContextInformationValidator() {
		return null;
	}
	
	private String getWordSoFar(IDocument doc, int position) {		
		try {
			// crawl back until whitespace
			StringBuilder sb = new StringBuilder();
			for (int i = position - 1; i >= 0; i--) {
				char c = doc.getChar(i);
				if (TextUtil.isIdentifier(c)) {
					sb.insert(0, c);
				} else {
					break; // <<-- break on whitespace
				}
			}
			return sb.toString();
		} catch (BadLocationException e) {
			Log.error(e);
			return "";
		}		
	}
}
