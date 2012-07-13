package uk.ac.standrews.grasp.ide.editors.completion;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;

import uk.ac.standrews.grasp.ide.Log;

/**
 * Read a file character by character. Ignores comments and new lines. Must call <code>dispose()</code> when done.
 * @author Dilyan Rusev
 * @see #dispose()
 */
class CharacterReader {
	private LineNumberReader reader;
	private int offset;
	private int column;
		
	/**
	 * Creates a new character reader
	 * @param reader Input to parse
	 */
	public CharacterReader(Reader reader) {
		this.reader = new LineNumberReader(reader);
		// we start counting lines from 1, not 0
		this.reader.setLineNumber(1);
		// When the first char is read, proper positions will be reported
		this.offset = 0; 
		this.column = 0;
	}
	
	/**
	 * Read the next character. Skips comments and updates statistics
	 * @return -1 on EOF/error or a value that can be cast to <code>char</code>
	 * @throws IOException when any of the underlying methods encounters I/O error
	 */
	public int getNextChar() throws IOException {
		int c = readWithOffset();
		if (c == -1) return c;
		char ch = (char)c;
		if (ch != '/') {
			return c;
		} else {
			c = readWithOffset();
			if (c == -1) return c;
			ch = (char)c;
			if (ch == '/') {
				// skip until line has changed
				int startLine = reader.getLineNumber();
				while (c != -1 && startLine == reader.getLineNumber()) {
					c = readWithOffset();
				}
				return c;
			} else if (ch == '*') {
				// skip until we see the */ sequence
				char prevCh = '\0';
				while ((c = readWithOffset()) != -1) {
					prevCh = ch;
					ch = (char)c;
					if (prevCh == '*' && ch == '/') {
						break;
					}
				}
				return getNextChar();
			} else {
				return c;
			}
		}
	}
	
	private int readWithOffset() throws IOException {
		offset++;
		int startLine = reader.getLineNumber();
		int result = reader.read();		
		column = startLine == reader.getLineNumber() ? column + 1 : 0;
		return result;
	}
	
	/**
	 * Get the column after the last read character
	 * @return 0-based position of the cursor, after the last character has been read. Relative to the start of the current line. 
	 */
	public int getColumn() {
		return column;
	}
	
	/**
	 * Get the number of characters read so far
	 * @return 0-based count of characters read since the last call to <code>getNextChar()</code>
	 */
	public int getOffset() {
		return offset;
	}
	
	/**
	 * Get the 1-based line number
	 * @return 1-based line number since the least call to <code>getNextChar()</code>
	 */
	public int getLineNumber() {
		return reader.getLineNumber();
	}

	/**
	 * Closes the underlying <code>Reader</code> and logs any exceptions.
	 */
	public void dispose() {		
		try {
			reader.close();
		} catch (IOException e) {
			Log.error(e);
		}		
	}
	
	/**
	 * Returns human-readable string representing the current state of this <code>CharacterReader</code>
	 * @return Current state of the reader. Format might change.
	 */
	@Override
	public String toString() {
		if (reader != null) {
			return "CharacterScanner line=" + reader.getLineNumber() + "; column=" + column + "; offset=" + offset;
		} else {
			return "CharacterScanner line=0; column=" + column + "; offset=" + offset;
		}
	}
}