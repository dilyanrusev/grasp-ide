package uk.ac.standrews.grasp.ide.editors.completion;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;

import uk.ac.standrews.grasp.ide.Log;
import uk.ac.standrews.grasp.ide.editors.TextUtil;

public class Context implements ICodeCompletionContext {
	private IDocument document;
	private int position;
	private String wordBeforeCursor;
	private String wordAfterCursor;
	private String wordAtCursor;
	
	@Override
	public void computeFor(IDocument doc, int position) {
		this.document = doc;
		this.position = position;
		this.wordAfterCursor = null;
		this.wordBeforeCursor = null;
		this.wordAtCursor = null;
	}

	@Override
	public IDocument getDocument() {
		return document;
	}

	@Override
	public int getOffset() {
		return position;
	}

	@Override
	public String getWordBeforeCursor() {
		Assert.isNotNull(document);
		if (wordBeforeCursor == null) {
			try {
				// crawl back until whitespace
				StringBuilder sb = new StringBuilder();
				for (int i = position - 1; i >= 0; i--) {
					char c = document.getChar(i);
					if (TextUtil.isIdentifier(c)) {
						sb.insert(0, c);
					} else {
						break; // <<-- break on whitespace
					}
				}
				wordBeforeCursor = sb.toString();
			} catch (BadLocationException e) {
				Log.error(e);
				wordBeforeCursor = "";
			}		
		}
		return wordBeforeCursor;
	}

	@Override
	public String getWordAfterCursor() {
		Assert.isNotNull(document);
		if (wordAfterCursor == null) {
			StringBuilder sb = new StringBuilder();
			try {
				char c = document.getChar(position);
				for (int i = position + 1; TextUtil.isIdentifier(c); i++) {
					sb.append(c);
					c = document.getChar(i);
				}
				wordAfterCursor = sb.toString();
			} catch (BadLocationException e) {
				Log.error(e);
				wordAfterCursor = "";
			}
		}
		return wordAfterCursor;
	}

	@Override
	public String getWordAtCursor() {
		Assert.isNotNull(document);
		if (wordAtCursor == null) {
			wordAtCursor = getWordBeforeCursor() + getWordAfterCursor();
		}
		return wordAtCursor;
	}		
}