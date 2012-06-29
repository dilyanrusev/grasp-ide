package uk.ac.standrews.grasp.ide.editors;

import org.eclipse.jface.text.rules.IToken;

public enum GraspTokens implements IToken {
	IDENTIFIER(true, false, false)
	, WHITESPACE(false, true, false)
	, INLINE_COMMENT(true, false, false)
	, BLOCK_COMMENT(true, false, false)
	, NEW_LINE(true, false, false)
	, REAL_LITERAL(true, false, false)
	, INTEGER_LITERAL(true, false, false)
	, BOOLEAN_LITERAL(true, false, false)
	, STRING_LITERAL(true, false, false)
	, DECLARATIVE_LITERAL(true, false, false)
	, ANNOTATION(true, false, false)
	, KEYWORD(true, false, false)
	;

	private boolean undefined;
	private boolean whitespace;
	private boolean other;
	
	GraspTokens(boolean other, boolean whitespace, boolean undefined) {
		this.undefined = undefined;
		this.whitespace = whitespace;
		this.other = other;
	}
	
	@Override
	public boolean isUndefined() {
		return undefined;
	}

	@Override
	public boolean isWhitespace() {
		return whitespace;
	}

	@Override
	public boolean isEOF() {
		return false;
	}

	@Override
	public boolean isOther() {
		return other;
	}

	@Override
	public Object getData() {
		return null;
	}
}
