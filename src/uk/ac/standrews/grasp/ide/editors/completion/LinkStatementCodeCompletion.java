package uk.ac.standrews.grasp.ide.editors.completion;

import java.util.Collection;

import org.eclipse.jface.text.contentassist.ICompletionProposal;

public class LinkStatementCodeCompletion implements ICodeCompletionProcessor {

	@Override
	public Collection<ICompletionProposal> evaluateContext(
			ICodeCompletionContext context) {
		int line = context.getLine();
		int column = context.getColumn();
		GraspScanner scanner = context.getCodeScanner();
		IChunk chunkAtCursor = scanner.getChunkAtPosition(line, column);
		System.out.printf("Chunk at cursor (%d:%d:%d): %s%n", context.getOffset(), line, column, chunkAtCursor);
		return NO_PROPOSALS;
	}
}
