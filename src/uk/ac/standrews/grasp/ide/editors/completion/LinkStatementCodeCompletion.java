package uk.ac.standrews.grasp.ide.editors.completion;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;

public class LinkStatementCodeCompletion implements ICodeCompletionProcessor {

	@Override
	public Collection<ICompletionProposal> evaluateContext(
			ICodeCompletionContext context) {
		Collection<ICompletionProposal> proposals = new ArrayList<ICompletionProposal>();
		int line = context.getLine();
		int column = context.getColumn();
		
		GraspScanner scanner = context.getCodeScanner();
		printCursorPosition(context);
		IChunk chunkAtCursor = scanner.getChunkAtPosition(line, column);
		printChunk(chunkAtCursor, "at cursor");
		IChunk prev = scanner.getChunkBeforePosition(line, column);
		printChunk(prev, "previous");
		IChunk next = scanner.getChunkAfterPosition(line, column);
		printChunk(next, "next");
		
		if (chunkAtCursor == null && prev.getText().equals("link")) {
			proposals.add(buildProposal(context, "to"));
		}
		
		return proposals;
	}
	
	private ICompletionProposal buildProposal(ICodeCompletionContext ctx, String proposal) {
		int offset = ctx.getOffset();
		return new CompletionProposal(
				proposal,    // string that is actually inserted
				offset,      // position in document at which we insert new text
				0,  // replace nothing
				proposal.length(),     // the cursor position should advance after the completion
				null,        // no image
				proposal,     // what is actually displayed to the user
				null,        // no context
				null         // no additional propositions
		);
	}
	
	private void printCursorPosition(ICodeCompletionContext ctx) {
		System.out.printf("cursor (%d:%d:%d)%n", ctx.getOffset(), ctx.getLine(), ctx.getColumn());
	}
	
	private void printChunk(IChunk chunk, String msg) {
		String chunkTxt = chunk != null ? chunk.toString() : "<null>";
		if (msg != null) {
			System.out.printf("%s: %s%n", msg, chunkTxt);
		} else {
			System.out.println(chunkTxt);
		}
	}
	
}
