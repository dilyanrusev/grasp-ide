package uk.ac.standrews.grasp.ide.editors.completion;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.text.IDocument;

import uk.ac.standrews.grasp.ide.model.ArchitectureModel;

/**
 * Represents the <code>IContentAssistProcessor</code> which provides additional context to aid code completion
 * @author Dilyan Rusev
 *
 */
public interface ICodeCompletionContext {
	/**
	 * Reset the context with a new document and position
	 * @param doc Document to compute the new values for
	 * @param offset Position (offset) in the document
	 * @param file File that contains the document
	 */
	void computeFor(IFile file, IDocument doc, int offset);
	/**
	 * Get the document that contains the text to be evaluated
	 * @return the document
	 */
	IDocument getDocument();
	/**
	 * Get the cursor position in the document at the time code completion was requested
	 * @return Position in the document
	 */
	int getOffset();
	
	/**
	 * Get line in the document relative to the cursor position
	 * @return Line in the document
	 */
	int getLine();
	
	/**
	 * Get column relative to the line in which the cursor is
	 * @return Column relative to cursor position's line
	 */
	int getColumn();
	
	/**
	 * Get the word (anything up until invalid identifier character is seen) before the cursor position
	 * @return Word before cursor position
	 */
	String getWordBeforeCursor();
	/**
	 * Get the word (anything up until invalid identifier character is seen) after the cursor position
	 * @return Word after cursor position
	 */
	String getWordAfterCursor();
	/**
	 * Get the word at the cursor position (scans both back and forward until invalid identifier character is detected)
	 * @return Word at the cursor
	 */
	String getWordAtCursor();
	
	/**
	 * Returns the AST for the document, if it was compiled successfully, or null if it wasn't compiled or there were compilation errors
	 * @return AST for the document. May be <code>null</code>
	 */
	ArchitectureModel getModel();
	
	/**
	 * Returns a grasp source code scanner
	 * @return Lets the user parse the code chunk by chunk
	 */
	GraspScanner getCodeScanner();
}
