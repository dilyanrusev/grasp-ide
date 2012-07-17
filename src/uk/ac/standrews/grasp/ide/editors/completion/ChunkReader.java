package uk.ac.standrews.grasp.ide.editors.completion;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.runtime.Assert;

import uk.ac.standrews.grasp.ide.Log;
import uk.ac.standrews.grasp.ide.editors.TextUtil;

/**
 * Read chunks. A chunk is everything but whitespace. Must call <code>dispose()</code> when done.
 * @author Dilyan Rusev
 * @see #dispose()
 */
class ChunkReader {
	private CharacterReader reader;
	private StringBuilder builder;
	private int c;
	private char ch;
	private int startOffset;
	private int startColumn;
	private int line;
	private static final Set<Character> ONE_SYMBOL_CHUNKS;
	
	static {
		Character[] syms = new Character[] {
				'(', ')', '@', ';', 
		};
		ONE_SYMBOL_CHUNKS = new HashSet<Character>(Arrays.asList(syms));
	}
	
	/**
	 * Creates a new chunk reader
	 * @param reader 
	 */
	public ChunkReader(CharacterReader reader) {
		Assert.isNotNull(reader, "CharacterReader must not be null");
		this.reader = reader;
		this.builder = new StringBuilder();
	}
	
	/**
	 * Parse a whole chunk. Declarations and strings are treated as single chunks
	 * @return Parsed chunk or <code>null</code> on error/end of file
	 */
	public IChunk nextChunk() {
		builder.setLength(0);		
		
		try {
			c = reader.getNextChar();
			if (c == -1) return null;
			ch = (char)c;		
			
			// skip whitespace
			if (TextUtil.isWhitespace(ch) && !handleWhitespace()) {
				return null;
			} 			
			// must read at least 1 character to return accurate statistics
			// proper start is before first non-whitespace
			line = reader.getLineNumber();
			startOffset = reader.getOffset() - 1;
			startColumn = reader.getColumn() - 1;
			// append first non-whitespace character
			builder.append(ch);		
			
			// handle declarations
			if (ch == '#') {
				return handleDeclaration();
			}
			
			// handle strings
			if (ch == '\'' || ch == '"'){
				return handleString(ch);				
			}			
			
			// hande one-symbol statements
			if (ONE_SYMBOL_CHUNKS.contains(ch)) {
				return buildChunkFromBuffer();
			}
			
			return handleCommonCase();
		} catch (IOException e) {
			Log.error(e);
			return null;
		}
	}

	private IChunk handleDeclaration() throws IOException {
		c = reader.getNextChar();
		if (c == -1) return buildChunkFromBuffer();
		ch = (char)c;
		if (ch == '\'' || ch == '"') {
			char quot = ch;			
			builder.append(quot);
			return handleString(quot);
		} else {			
			builder.append(ch);
			return handleCommonCase();
		}
	}

	private IChunk handleString(char quot) throws IOException {
		while ((c = reader.getNextChar()) != -1 && (ch = (char)c) != quot) {
			builder.append(ch);
		}
		if (c != -1) builder.append(ch);
		return buildChunkFromBuffer();
	}
	
	private boolean handleWhitespace() throws IOException {
		while ((c = reader.getNextChar()) != -1 && TextUtil.isWhitespace((char)c)) {
			// skip
		}
		if (c == -1) return false;
		ch = (char)c;
		return true;
	}
	
	private IChunk handleCommonCase() throws IOException {
		// read non-whitespace
		while ((c = reader.getNextChar()) != -1 && !TextUtil.isWhitespace((char)c)) {
			builder.append((char)c);
		}
		if (builder.length() > 0) {
			return buildChunkFromBuffer();
		} else {
			return null;
		}	
	}	
	
	private IChunk buildChunkFromBuffer() {
		String txt = builder.toString();
		int columnEnd = startColumn + txt.length();
		return new Chunk(startOffset, line, startColumn, columnEnd, txt);
	}
	
	/**
	 * Frees system resources. Must be called when done.
	 */
	public void dispose() {
		reader.dispose();
	}
}