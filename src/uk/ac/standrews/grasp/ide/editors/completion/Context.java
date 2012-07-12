package uk.ac.standrews.grasp.ide.editors.completion;

import java.io.IOException;
import java.io.Reader;

import grasp.lang.IArchitecture;
import grasp.lang.ISyntaxTree;
import grasp.lang.Parser;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;

import shared.io.ISource;
import shared.logging.ILogger;
import uk.ac.standrews.grasp.ide.GraspPlugin;
import uk.ac.standrews.grasp.ide.Log;
import uk.ac.standrews.grasp.ide.builder.GraspSourceFile;
import uk.ac.standrews.grasp.ide.editors.TextUtil;

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
	private ISyntaxTree syntaxTree;
	
	@Override
	public void computeFor(IFile file, IDocument doc, int position) {
		this.file = file;
		this.document = doc;
		this.position = position;
		this.wordAfterCursor = null;
		this.wordBeforeCursor = null;
		this.wordAtCursor = null;
		this.syntaxTree = null;
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
	public IArchitecture getAst() {
		Assert.isNotNull(file);
		return GraspPlugin.getFileArchitecture(file);
	}

	@Override
	public ISyntaxTree getSyntaxTree() {
		if (syntaxTree == null) {
			Assert.isNotNull(file);
			Parser graspParser = new Parser();
			syntaxTree = graspParser.parse(new GraspSourceFile(file), NullLogger.INSTANCE);
		} 
		return syntaxTree;
	}
}

/**
 * Logger that ignores all logging requests
 * @author Dilyan Rusev
 *
 */
class NullLogger implements ILogger {
	/** Convenience instance of the null logger */
	public static final ILogger INSTANCE = new NullLogger();

	@Override
	public void compiler_error(String s, Object... aobj) {
		// ignore
	}

	@Override
	public void compiler_warn(String s, Object... aobj) {
		// ignore
	}

	@Override
	public void error(String s, Object... aobj) {
		// ignore
	}

	@Override
	public void error(String s, Exception exception) {
		// ignore
	}

	@Override
	public void info(String s, Object... aobj) {
		// ignore
	}

	@Override
	public ILogger initialize(String s, Level level, boolean flag) {
		// ignore
		return this;
	}

	@Override
	public void print() {
		// ignore
	}

	@Override
	public void print(String s, Object... aobj) {
		// ignore
	}

	@Override
	public void shutdown() {
		// nothing to do
	}

	@Override
	public void trace(String s, Object... aobj) {
		// ignore
	}

	@Override
	public void warn(String s, Object... aobj) {
		// ignore
	}	
}