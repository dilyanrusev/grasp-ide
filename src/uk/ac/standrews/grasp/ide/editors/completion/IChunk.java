package uk.ac.standrews.grasp.ide.editors.completion;

/**
 * Represents a chunk of grasp code. A chunk is either declaration, string, or any sequence of non-whitespace characters
 * @author Dilyan Rusev
 *
 */
public interface IChunk {
	/**
	 * Get the count of characters in the document before the chunk was encountered
	 * @return Offset, 0-based, from start of the document, in characters, until the first char of this chunk
	 */
	int getOffset();
	/**
	 * Get the line at which the first symbol of the chunk was encountered
	 * @return Line, 1-based, from the start of the document, until the first char of this chunk
	 */
	int getLine();
	/**
	 * Get the column at which the first symbol of the chunk was encountered
	 * @return Position in the line, 0-based, of the first character in the chunk, inclusive
	 */
	int getColumn();
	/**
	 * Shortcut for <code>getColumn() + getText().length()</code>
	 * @return Position in the line, 0-based, of the last character in the chunk, inclusive
	 */
	int getColumnEnd();
	/**
	 * Text of the chunk
	 * @return All characters in this chunk
	 */
	String getText();
}