package uk.ac.standrews.grasp.ide.editors.completion;

import java.io.StringReader;

import grasp.lang.IArchitecture;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;

import uk.ac.standrews.grasp.ide.GraspPlugin;
import uk.ac.standrews.grasp.ide.Log;
import uk.ac.standrews.grasp.ide.editors.TextUtil;
import uk.ac.standrews.grasp.ide.model.GraspModel;

/**
 * Default implementation of  <code>ICodeCompletionContext</code>
 * @author Dilyan Rusev
 *
 */
public class Context implements ICodeCompletionContext {
	private IDocument document;
	private IFile file;
	private int position;
	private String wordBeforeCursor;
	private String wordAfterCursor;
	private String wordAtCursor;
	private GraspScanner scanner;
	private int line;
	private int column;
	
	@Override
	public void computeFor(IFile file, IDocument doc, int position) {
		this.file = file;
		this.document = doc;
		this.position = position;
		this.wordAfterCursor = null;
		this.wordBeforeCursor = null;
		this.wordAtCursor = null;
		this.scanner = null;
		this.line = -1;
		this.column = -1;
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
	
	@Override
	public IArchitecture getModel() {
		Assert.isNotNull(file);
		return GraspPlugin.getFileArchitecture(file);
	}

	@Override
	public GraspScanner getCodeScanner() {
		if (scanner == null) {
			scanner = GraspModel.INSTANCE.getScannerForFile(file);
			if (scanner == null) {
				scanner = new GraspScanner();
				scanner.parse(new StringReader(document.get()));
				GraspModel.INSTANCE.setScannerForFile(file, scanner);
			}
		}
		return scanner;
	}

	@Override
	public int getLine() {
		if (line == -1) {
			try {
				line = document.getLineOfOffset(position) + 1; // lines start counting at 1
			} catch (BadLocationException e) {
				Log.error(e);
				line = -1;
			}
		}
		return line;		
	}

	@Override
	public int getColumn() {
		if (column == -1) {
			try {
				int zeroLine = document.getLineOfOffset(position);
				column = position - document.getLineOffset(zeroLine);
			} catch (BadLocationException e) {
				Log.error(e);
				column = -1;
			}
		}
		return column;
	}
}