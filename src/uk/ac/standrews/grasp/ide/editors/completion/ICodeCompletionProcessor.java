package uk.ac.standrews.grasp.ide.editors.completion;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.eclipse.jface.text.contentassist.ICompletionProposal;

/**
 * Represents a rule in <code>ICodeCompletionHost</code> that returns completion suggestions
 * given a position in the document, or <code>NO_PROPOSALS</code> if no completion is available
 * @author Dilyan Rusev
 *
 */
public interface ICodeCompletionProcessor {
	/**
	 * Empty collection of proposals, to be returned if the processor did not find any proposals for the current context
	 */
	static final Collection<ICompletionProposal> NO_PROPOSALS = Collections.unmodifiableCollection(new ArrayList<ICompletionProposal>());
	
	/**
	 * Evaluate a code completion context and return a list of suggestions
	 * @param context Code completion context
	 * @return List of proposals or <code>NO_PROPOSALS</code>; do not return <code>null</code>
	 */
	Collection<ICompletionProposal> evaluateContext(ICodeCompletionContext context);
}
