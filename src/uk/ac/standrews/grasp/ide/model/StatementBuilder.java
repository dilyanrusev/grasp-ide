package uk.ac.standrews.grasp.ide.model;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * Internal class that helps formatting Grasp source code
 * @author Dilyan Rusev
 *
 */
class StatementBuilder {
	private StringBuilder sb;
	private int indentLevel;
	private String indent;
	private StringBuilder currentIndent;
	private boolean pendingSpace;		
	
	/**
	 * Create a new formatter
	 */
	public StatementBuilder() {
		sb = new StringBuilder();
		indentLevel = 0;
		indent = "  ";			
		currentIndent = new StringBuilder();
	}
	
	/**
	 * Add indentation and keyword for the model to the buffer
	 * @param forModel Model whose keyword to append to the buffer
	 * @return This
	 */
	public StatementBuilder openStatement(ElementModel forModel) {			
		sb.append(currentIndent);
		return keyword(forModel);
	}
	
	/**
	 * May append semicolon depending on element. Appends new line to the buffer.
	 * @param forModel Type of element to close. Determines whether to put semicolon
	 * @return This
	 */
	public StatementBuilder closeStatement(ElementModel forModel) {
		if (forModel.getType().endsWithSemicolon()) {
			sb.append(';');
		}
		return newLine();
	}
	
	/**
	 * Increase the current indent. Does not touch the buffer
	 */
	private void increaseIndent() {
		indentLevel++;
		currentIndent.append(indent);		
	}
	
	/**
	 * Decrease the current indent. Does not touch the buffer
	 */
	private void decreaseIndent() {
		indentLevel--;
		currentIndent.delete(indentLevel * indent.length(), 
				currentIndent.length());			
	}
	
	/**
	 * Appends platform-specific line terminator to the buffer
	 * @return ThisThis
	 */
	private StatementBuilder newLine() {
		sb.append(System.getProperty("line.separator"));
		return this;
	}
	
	/**
	 * Appends an expression to the buffer, properly escaping it
	 * @param model Expression to append to the buffer
	 * @return This
	 */
	public StatementBuilder expression(ExpressionModel model) {
		if (model.getExpressionType() == ExpressionType.STRING) {
			sb.append("\"");
			sb.append(model.getText());
			sb.append("\"");
		} else {
			sb.append(model.getText());
		}
		return this;
	}
	
	/**
	 * Append an identifier to the buffer
	 * @param identifier Valid Grasp identifier. Does not perform checks
	 * @return This
	 */
	public StatementBuilder identifier(String identifier) {
		if (pendingSpace) {
			sb.append(' ');
			pendingSpace = false;
		}
		sb.append(identifier);
		return this;
	}
	
	/**
	 * Open a bracket (parenthesis)
	 * @return This
	 */
	public StatementBuilder openBracket() {			
		sb.append('(');
		return this;
	}
	
	/**
	 * Close a bracket (parenthesis)
	 * @return This
	 */
	public StatementBuilder closeBracket() {			
		sb.append(')');
		return this;
	}
	
	/**
	 * Start the body of an element and increase indentation
	 * @return This
	 */
	public StatementBuilder startBody() {
		sb.append(" {");
		increaseIndent();
		return this;
	}
	
	/**
	 * Close the body of an element and decrease indentation
	 * @return This
	 */
	public StatementBuilder endBody() {		
		decreaseIndent();
		sb.append('}');		
		return newLine();
	}
	
	/**
	 * Add a comma to the buffer
	 * @return This
	 */
	public StatementBuilder comma() {
		sb.append(", ");
		return this;
	}	
	
	/**
	 * Add the equals sign to the buffer 
	 * @return This
	 */
	public StatementBuilder equals() {
		sb.append(" = ");
		return this;
	}
	
	/**
	 * Add and escape a string literal to the buffer
	 * @param literal String literalThis
	 * @return This
	 */
	public StatementBuilder string(String literal) {
		sb.append('\"');
		sb.append(literal);
		sb.append('\"');
		return this;
	}
	
	/**
	 * Add a keyword to the buffer
	 * @param forModel Will insert the keyword which is appropriate for this model
	 * @return This
	 */
	public StatementBuilder keyword(ElementModel forModel) {
		sb.append(forModel.getType().getKeyword());
		this.pendingSpace = true;
		return this;
	}
	
	/**
	 * Add a keyword to the buffer
	 * @param kw Keyword to insert into the buffer
	 * @return
	 */
	public StatementBuilder keyword(String kw) {
		sb.append(kw);
		this.pendingSpace = true;
		return this;
	}
	
	/**
	 * Current buffer state
	 */
	@Override
	public String toString() {
		return sb.toString();
	}
	
	/**
	 * Convert the current buffer state to an array of bytes
	 * @param charset Specific encoding, or null to use the default
	 * @return Buffer's contents encoded in charset
	 */
	public byte[] toByteArray(Charset charset) {
		return sb.toString().getBytes(charset != null ? charset : Charset.defaultCharset()); 
	}
	
	/**
	 * Convert the current buffer state to an input stream
	 * @param charset Specific encoding, or null to use the default
	 * @return Buffer's contents encoded in charset
	 */
	public InputStream toStream(Charset charset) {
		return new ByteArrayInputStream(toByteArray(charset));
	}
}