package uk.ac.standrews.grasp.ide.model;

import grasp.lang.IArchitecture;
import grasp.lang.ISyntaxTree;
import grasp.lang.Parser;

import java.io.InputStreamReader;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.source.ISourceViewer;

import shared.error.IErrorReport;
import shared.io.ISource;
import uk.ac.standrews.grasp.ide.builder.GraspSourceFile;
import uk.ac.standrews.grasp.ide.builder.NullLogger;
import uk.ac.standrews.grasp.ide.editors.completion.GraspScanner;

public class GraspFile {
	private IErrorReport errorReport;
	private ArchitectureModel architecture;
	private ArchitectureModel lastArchitectureThatCompiled;
	private ISourceViewer sourceViewer;
	private ISyntaxTree syntaxTree;
	private GraspScanner scanner;
	private IFile file;
	
	public GraspFile(IFile file) {
		this.file = file;
	}
	
	public void compiled(IArchitecture graph, IErrorReport errorReport) {
		if (graph != null && !errorReport.isAny()) {
			architecture = new ArchitectureModel(graph, file);
			lastArchitectureThatCompiled = architecture;
		} else {
			architecture = null;
		}
	}
	
	public void reparse() throws CoreException {
		if (scanner == null) {
			scanner = new GraspScanner();
		}
		scanner.parse(new InputStreamReader(file.getContents()));
		
		Parser parser = new Parser();
		ISource source = new GraspSourceFile(file);
		syntaxTree = parser.parse(source, NullLogger.INSTANCE);
	}
	
	public IFile getFile() {
		return file;
	}
	
	public IErrorReport getErrorReport() {
		return errorReport;
	}
	
	public ArchitectureModel getLastArchitectureThatCompiled() {
		return lastArchitectureThatCompiled;
	}
	
	public ArchitectureModel getArchitecture() {
		return architecture;
	}
	
	public void setSourceViewer(ISourceViewer sourceViewer) {
		this.sourceViewer = sourceViewer;
	}
	
	public IDocument getDocument() {
		return sourceViewer != null ? sourceViewer.getDocument() : null;
	}

	public ISyntaxTree getSyntaxTree() {
		return syntaxTree;
	}

	public void setSyntaxTree(ISyntaxTree syntaxTree) {
		this.syntaxTree = syntaxTree;
	}

	public GraspScanner getScanner() {
		return scanner;
	}

	public void setScanner(GraspScanner scanner) {
		this.scanner = scanner;
	}
}