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
	private Operations lastOperation;
	
	/**
	 * Create a new formatter
	 */
	public StatementBuilder() {		
		sb = new StringBuilder();
		lastOperation = Operations.NULL;
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
		if (lastOperation == Operations.END_BODY || lastOperation == Operations.START_BODY) {
			newLine();
		}
		sb.append(currentIndent);
		lastOperation = Operations.OPEN_STATEMENT;
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
		lastOperation = Operations.CLOSE_STATEMENT;
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
		if (lastOperation == Operations.KEYWORD ||
				lastOperation == Operations.IDENTIFIER) {
			sb.append(' ');
		}
		if (model.getExpressionType() == ExpressionType.STRING) {
			sb.append("\"");
			sb.append(model.getText());
			sb.append("\"");
		} else if (model.getExpressionType() == ExpressionType.BOOLEAN) {
			boolean isTrue = "true".equals(model.getText());
			boolean isFalse = "false".equals(model.getText());
			if (!isTrue && !isFalse) {
				// declarative
				sb.append("#\"");
				sb.append(model.getText());
				sb.append("\"");
			} else {
				sb.append(model.getText());
			}
		} else {
			sb.append(model.getText());
		}
		lastOperation = Operations.EXPRESSION;
		return this;
	}
	
	/**
	 * Append an identifier to the buffer
	 * @param identifier Valid Grasp identifier. Does not perform checks
	 * @return This
	 */
	public StatementBuilder identifier(String identifier) {		
		if (lastOperation == Operations.IDENTIFIER 
				|| lastOperation == Operations.KEYWORD
				|| lastOperation == Operations.EXPRESSION) {
			sb.append(' ');
		}
		sb.append(identifier);
		lastOperation = Operations.IDENTIFIER;
		return this;
	}
	
	/**
	 * Open a bracket (parenthesis)
	 * @return This
	 */
	public StatementBuilder openBracket() {			
		sb.append('(');
		lastOperation = Operations.OPEN_BRACKET;
		return this;
	}
	
	/**
	 * Close a bracket (parenthesis)
	 * @return This
	 */
	public StatementBuilder closeBracket() {			
		sb.append(')');
		lastOperation = Operations.CLOSE_BRACKET;
		return this;
	}
	
	/**
	 * Start the body of an element and increase indentation
	 * @return This
	 */
	public StatementBuilder startBody() {
		sb.append(" {");
		increaseIndent();
		lastOperation = Operations.START_BODY;
		return this;
	}
	
	/**
	 * Close the body of an element and decrease indentation
	 * @return This
	 */
	public StatementBuilder endBody() {		
		decreaseIndent();
		sb.append(currentIndent);
		sb.append('}');		
		newLine();
		lastOperation = Operations.END_BODY;
		return this;
	}
	
	/**
	 * Add a comma to the buffer
	 * @return This
	 */
	public StatementBuilder comma() {
		sb.append(", ");
		lastOperation = Operations.COMMA;
		return this;
	}	
	
	/**
	 * Add the equals sign to the buffer 
	 * @return This
	 */
	public StatementBuilder equals() {
		sb.append(" = ");
		lastOperation = Operations.EQUALS;
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
		lastOperation = Operations.STRING;
		return this;
	}
	
	/**
	 * Add a keyword to the buffer
	 * @param forModel Will insert the keyword which is appropriate for this model
	 * @return This
	 */
	public StatementBuilder keyword(ElementModel forModel) {
		if (lastOperation == Operations.IDENTIFIER || lastOperation == Operations.EXPRESSION) {
			sb.append(' ');
		}
		sb.append(forModel.getType().getKeyword());		
		lastOperation = forModel.getType() != ElementType.ANNOTATION ? Operations.KEYWORD : Operations.KEYWORD_ANNOTATION;
		return this;
	}
	
	/**
	 * Add a keyword to the buffer
	 * @param kw Keyword to insert into the buffer
	 * @return
	 */
	public StatementBuilder keyword(String kw) {
		if (lastOperation == Operations.IDENTIFIER || lastOperation == Operations.EXPRESSION) {
			sb.append(' ');
		}
		sb.append(kw);		
		lastOperation = Operations.KEYWORD;
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
	
	private enum Operations {
		NULL,
		OPEN_STATEMENT,
		CLOSE_STATEMENT,
		EXPRESSION,
		IDENTIFIER,
		OPEN_BRACKET,
		CLOSE_BRACKET,
		START_BODY,
		STRING,
		END_BODY,
		COMMA,
		EQUALS,
		KEYWORD,
		KEYWORD_ANNOTATION
		;
		
		
	}
}