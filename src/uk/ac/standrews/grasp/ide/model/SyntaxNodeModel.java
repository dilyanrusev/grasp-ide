package uk.ac.standrews.grasp.ide.model;

import java.util.ArrayList;

import grasp.lang.ISyntaxNode;

public class SyntaxNodeModel implements ISyntaxNode {
	private ArrayList<ISyntaxNode> children;
	private int column;
	private int line;
	private ISyntaxNode owner;
	private int startPosition;
	private int tokenId;
	private String tokenText;
	
	public SyntaxNodeModel(ISyntaxNode other, ISyntaxNode owner) {
		this.owner = owner;
		this.tokenId = other.getTokenId();
		this.column = other.getColumn();
		this.line = other.getLine();
		this.startPosition = other.getStartPosition();
		this.tokenText = other.getTokenText();
		this.children = new ArrayList<ISyntaxNode>();
		for (ISyntaxNode child: other.getChildren()) {
			ISyntaxNode copy = new SyntaxNodeModel(child, this);
			this.children.add(copy);
		}
	}
	
	public SyntaxNodeModel(int tokenId, ISyntaxNode owner) {
		this(tokenId, owner, null, 0, 0, 0);
	}
	
	public SyntaxNodeModel(int tokenId, ISyntaxNode owner, String text, 
			int line, int column, int startPosition) {
		this.tokenId = tokenId;
		this.owner = owner;
		this.tokenText = text;
		this.startPosition = startPosition;
		this.children = new ArrayList<ISyntaxNode>();
	}
	
	@Override
	public ArrayList<ISyntaxNode> getChildren() {
		return children;
	}

	@Override
	public int getColumn() {
		return column;
	}

	@Override
	public int getEndPosition() {
		return startPosition + (tokenText != null ? tokenText.length() : 0);
	}

	@Override
	public int getLine() {
		return line;
	}

	@Override
	public ISyntaxNode getOwner() {
		return owner;
	}

	@Override
	public int getStartPosition() {
		return startPosition;
	}

	@Override
	public int getTokenId() {
		return tokenId;
	}

	@Override
	public String getTokenText() {
		return tokenText;
	}

}
