package uk.ac.standrews.grasp.ide.editors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;

import uk.ac.standrews.grasp.ide.editors.completion.Context;
import uk.ac.standrews.grasp.ide.editors.completion.ICodeCompletionContext;
import uk.ac.standrews.grasp.ide.editors.completion.ICodeCompletionProcessor;
import uk.ac.standrews.grasp.ide.editors.completion.KeywordCodeCompletion;

/**
 * Code completion for Grasp
 * @author Dilyan Rusev
 *
 */
class GraspCodeCompletionProcessor implements IContentAssistProcessor {
	private static final IContextInformation[] EMPTY_CONTEXT_LIST = { };
	private static final char[] PROPOSAL_ACTIVATION_CHARS = {};
	private static final char[] INFORMATION_ACTIVATION_CHARS = {};
	private ICodeCompletionContext context = new Context();
	private ICodeCompletionProcessor[] rules;
	
	public GraspCodeCompletionProcessor() {
		this.rules = new ICodeCompletionProcessor[] {
				new KeywordCodeCompletion()
		};
	}

	@Override
	public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer,	int offset) {
		IDocument doc = viewer.getDocument();
		context.computeFor(doc, offset);
		Collection<ICompletionProposal> completions = ICodeCompletionProcessor.NO_PROPOSALS;
		for (ICodeCompletionProcessor rule: rules) {
			Collection<ICompletionProposal> props = rule.evaluateContext(context);
			if (props.size() > 0) {
				completions = props;
				break;
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
}
