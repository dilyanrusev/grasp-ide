package uk.ac.standrews.grasp.ide.editors.completion;

import grasp.lang.ISyntaxNode;
import grasp.lang.Parser;

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
	
	private ISyntaxNode getNodeAtPosition(ICodeCompletionContext ctx) {
		return getNodeAtPosition(ctx.getSyntaxTree().getRoot(), ctx.getLine(), ctx.getColumn());
	}
	
	private ISyntaxNode getNodeAtPosition(ISyntaxNode start, int line, int column) {
		if (start == null) return null;
		if (start.getLine() == line
				&& column >= start.getStartPosition() 
				&& column < start.getEndPosition()) {
			return start;
		}
		for (ISyntaxNode child: start.getChildren()) {
			ISyntaxNode found = getNodeAtPosition(child, line, column);
			if (found != null) return found;
		}
		return null;
	}
	
	private String getTokenIdString(int id) {
		switch (id) {
		   case Parser.TOKEN_ARCHITECTURE: return "TOKEN_ARCHITECTURE";
		   case Parser.TOKEN_REQUIREMENT: return "TOKEN_REQUIREMENT";
		   case Parser.TOKEN_QATTRIBUTE: return "TOKEN_QATTRIBUTE";
		   case Parser.TOKEN_RATIONALE : return "TOKEN_RATIONALE";
		   case Parser.TOKEN_REASON : return "TOKEN_REASON";
		   case Parser.TOKEN_TEMPLATE : return "TOKEN_TEMPLATE";
		   case Parser.TOKEN_SYSTEM : return "TOKEN_SYSTEM";
		   case Parser.TOKEN_LAYER : return "TOKEN_LAYER";
		   case Parser.TOKEN_OVER : return "TOKEN_OVER";
		   case Parser.TOKEN_COMPONENT : return "TOKEN_COMPONENT";
		   case Parser.TOKEN_CONNECTOR : return "TOKEN_CONNECTOR";
		   case Parser.TOKEN_PROVIDES : return "TOKEN_PROVIDES";
		   case Parser.TOKEN_REQUIRES : return "TOKEN_REQUIRES";
		   case Parser.TOKEN_CHECK : return "TOKEN_CHECK";
		   case Parser.TOKEN_LINK : return "TOKEN_LINK";
		   case Parser.TOKEN_EXPR : return "TOKEN_EXPR";
		   case Parser.TOKEN_ANNOTATION : return "TOKEN_ANNOTATION";
		   case Parser.TOKEN_NAMEDVALUE : return "TOKEN_NAMEDVALUE";
		   case Parser.TOKEN_PROPERTY : return "TOKEN_PROPERTY";
		   case Parser.TOKEN_SUPPORTS : return "TOKEN_SUPPORTS";
		   case Parser.TOKEN_INHIBITS : return "TOKEN_INHIBITS";
		   case Parser.TOKEN_BECAUSE : return "TOKEN_BECAUSE";
		   case Parser.TOKEN_EXTENDS : return "TOKEN_EXTENDS";
		   case Parser.TOKEN_TRUE : return "TOKEN_TRUE";
		   case Parser.TOKEN_FALSE : return "TOKEN_FALSE";
		   case Parser.TOKEN_HANDLER : return "TOKEN_HANDLER";
		   case Parser.TOKEN_PROVIDER : return "TOKEN_PROVIDER";
		   case Parser.TOKEN_CONSUMER : return "TOKEN_CONSUMER";
		   case Parser.TOKEN_NAME : return "TOKEN_NAME";
		   case Parser.TOKEN_ALIAS : return "TOKEN_ALIAS";
		   case Parser.TOKEN_BASE : return "TOKEN_BASE";
		   case Parser.TOKEN_ARGS : return "TOKEN_ARGS";
		   case Parser.TOKEN_BODY : return "TOKEN_BODY";
		   case Parser.TOKEN_PAYLOAD : return "TOKEN_PAYLOAD";
		   case Parser.TOKEN_CALL : return "TOKEN_CALL";
		   case Parser.TOKEN_PARMS : return "TOKEN_PARMS";
		   case Parser.TOKEN_MEMB : return "TOKEN_MEMB";
		   case Parser.TOKEN_SUBSETOF : return "TOKEN_SUBSETOF";
		   case Parser.TOKEN_ACCEPTS : return "TOKEN_ACCEPTS";
		   case Parser.TOKEN_MAXDEG : return "TOKEN_MAXDEG";
		   case Parser.TOKEN_DIS : return "TOKEN_DIS";
		   case Parser.TOKEN_CON : return "TOKEN_CON";
		   case Parser.TOKEN_IOR : return "TOKEN_IOR";
		   case Parser.TOKEN_XOR : return "TOKEN_XOR";
		   case Parser.TOKEN_AND : return "TOKEN_AND";
		   case Parser.TOKEN_EQL : return "TOKEN_EQL";
		   case Parser.TOKEN_NEQ : return "TOKEN_NEQ";
		   case Parser.TOKEN_GTN : return "TOKEN_GTN";
		   case Parser.TOKEN_GTE : return "TOKEN_GTE";
		   case Parser.TOKEN_LTN : return "TOKEN_LTN";
		   case Parser.TOKEN_LTE : return "TOKEN_LTE";
		   case Parser.TOKEN_AUG : return "TOKEN_AUG";
		   case Parser.TOKEN_NAG : return "TOKEN_NAG";
		   case Parser.TOKEN_ADD : return "TOKEN_ADD";
		   case Parser.TOKEN_SUB: return "TOKEN_SUB";
		   case Parser.TOKEN_MUL : return "TOKEN_MUL";
		   case Parser.TOKEN_DIV : return "TOKEN_DIV";
		   case Parser.TOKEN_MOD : return "TOKEN_MOD";
		   case Parser.TOKEN_CMP : return "TOKEN_CMP";
		   case Parser.TOKEN_NOT: return "TOKEN_NOT";
		   case Parser.TOKEN_POS : return "TOKEN_POS";
		   case Parser.TOKEN_NEG : return "TOKEN_NEG";
		   case Parser.TOKEN_SET : return "TOKEN_SET";
		   case Parser.TOKEN_PAIR : return "TOKEN_PAIR";
		   case Parser.TOKEN_INTEGER : return "TOKEN_INTEGER";
		   case Parser.TOKEN_REAL : return "TOKEN_REAL";
		   case Parser.TOKEN_BOOLEAN : return "TOKEN_BOOLEAN";
		   case Parser.TOKEN_STRING : return "TOKEN_STRING";
		   case Parser.TOKEN_DECL : return "TOKEN_DECL";
		   default:
			   return "unknown token id ("+id+")";
		}
	}
}
