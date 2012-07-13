package uk.ac.standrews.grasp.ide.editors.completion;

/**
 * Default implementation of <code>IChunk</code>. Immutable.
 * @author Dilyan Rusev
 *
 */
class Chunk implements IChunk {
	// can cache hash code because Chunk is immutable
	private volatile int hashCode;
	private final int offset;
	private final int line;
	private final int column;
	private final int columnEnd;
	private final String text;
	
	/**
	 * Create an immutable chunk
	 * @param offset 0-based offset for the first character, inclusive
	 * @param line 1-based line number for the first character
	 * @param column 0-based position in <code>line</code> for the first character
	 * @param columnEnd 0-based position in <code>line</code> for the last character, inclusive
	 * @param text Textual representation of the chunk
	 */
	public Chunk(int offset, int line, int column, int columnEnd, String text) {
		this.offset = offset;
		this.line = line;
		this.column = column;
		this.columnEnd = columnEnd;
		this.text = text;
	}

	@Override
	public int getOffset() {
		return offset;
	}

	@Override
	public int getLine() {
		return line;
	}

	@Override
	public int getColumn() {
		return column;
	}

	@Override
	public int getColumnEnd() {
		return columnEnd;
	}

	@Override
	public String getText() {
		return text;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (!(obj instanceof Chunk)) return false;
		Chunk other = (Chunk)obj;
		
		return offset == other.offset
				&& line == other.line
				&& column == other.column
				&& columnEnd == other.columnEnd
				&& (text == null ? other.text == null : text.equals(other.text));
	}
	
	@Override
	public int hashCode() {
		int result = hashCode;
		if (result == 0) {
			result = 17;
			result = 31 * result + offset;
			result = 31 * result + line;
			result = 31 * result + column;
			result = 31 * result + columnEnd;
			result = 31 * result + (text != null ? text.hashCode() : 0);
		}
		return result;
	}
	
	@Override
	public String toString() {
		return String.format("\"%s\" [offset=%d; line=%d; col=%d; col_end=%d]", text, offset, line, column, columnEnd);
	}
}