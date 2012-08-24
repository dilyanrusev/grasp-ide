package uk.ac.standrews.grasp.ide.compiler;

/**
 * Exposes minimum statistics needed to represent a compilation error so that the IDE can create problem markers
 * @author Dilyan Rusev
 *
 */
public class CompilationError {
	private int line;
	private String message;
	private int column;
	private int columnEnd;
	
	/**
	 * Set the error message
	 * @param message Message given by the compiler
	 * @return <code>this</code>, use for method chaining
	 */
	CompilationError setMessage(String message) {
		this.message = message;
		return this;
	}	
	
	/**
	 * Describe where the error occurred in the file, as reported by the compiler
	 * @param line Line, starts from 1
	 * @param column Column where the error starts
	 * @param columnEnd Column where the error ends
	 * @return <code>this</code>, use for method chaining
	 */
	CompilationError setLocation(int line, int column, int columnEnd) {
		this.line = line;
		this.column = column;
		this.columnEnd = columnEnd;
		return this;
	}
	
	/**
	 * Line number, starts from 1
	 * @return Line number, starts from 1
	 */
	public int getLine() {
		return line;
	}
	
	/**
	 * Error message, as reported by the compiler
	 * @return Error message, as reported by the compiler
	 */
	public String getMessage() {
		return message;
	}
	
	/**
	 * Column where the error starts
	 * @return Column where the error starts
	 */
	public int getColumn() {
		return column;
	}
	
	/**
	 * Column where the error ends
	 * @return Column where the error ends
	 */
	public int getColumnEnd() {
		return columnEnd;
	}
	
	@Override
	public String toString() {
		return new StringBuilder()
			.append('"').append(message)
			.append("\" line=").append(line)
			.append(", col=").append(column)
			.append(", end=").append(columnEnd)
			.toString();
	}
}
