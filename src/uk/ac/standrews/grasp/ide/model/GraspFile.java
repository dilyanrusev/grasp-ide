package uk.ac.standrews.grasp.ide.model;

import grasp.lang.IArchitecture;
import grasp.lang.ISyntaxTree;
import grasp.lang.Parser;

import java.io.InputStreamReader;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.source.ISourceViewer;

import shared.error.IErrorReport;
import shared.io.ISource;
import uk.ac.standrews.grasp.ide.builder.GraspSourceFile;
import uk.ac.standrews.grasp.ide.builder.NullLogger;
import uk.ac.standrews.grasp.ide.editors.completion.GraspScanner;
import uk.ac.standrews.grasp.ide.model.GraspFileChangedEvent.Kind;

public class GraspFile {
	private static Set<IGraspFileChangedListener> changeListeners =
			new HashSet<IGraspFileChangedListener>();
	
	private IErrorReport errorReport;
	private ArchitectureModel architecture;
	private ArchitectureModel lastArchitectureThatCompiled;
	private ISourceViewer sourceViewer;
	private ISyntaxTree syntaxTree;
	private GraspScanner scanner;
	private IFile file;
	
	public static void addChangeListener(IGraspFileChangedListener listener) {
		changeListeners.add(listener);
	}
	
	public static void removeChangeListener(IGraspFileChangedListener listener) {
		changeListeners.remove(listener);
	}
	
	private static void fireChange(GraspFile instance, EnumSet<Kind> change) {
		GraspFileChangedEvent event = new GraspFileChangedEvent(instance, change);
		for (IGraspFileChangedListener listener: changeListeners) {
			listener.fileChanged(event);
		}
	}
	
	public GraspFile(IFile file) {
		this.file = file;
	}
	
	public void compiled(IArchitecture graph, IErrorReport errorReport) {
		this.errorReport = errorReport;
		
		fireChange(this, EnumSet.of(Kind.Compiled));
	}
	
	public boolean refreshFromXml(IFile xmlFile) {
		architecture = XmlModelReader.getDefault().readFromFile(xmlFile);
	    if (architecture != null) {
		    lastArchitectureThatCompiled = architecture;
		    
		    fireChange(this, EnumSet.of(Kind.ArchiectureRefreshed));
		    return true;
	    }		
		return false;
	}
	
	private boolean refreshFromXml() {
		IPath graspPath = file.getProjectRelativePath();
		IPath xmlPath = graspPath.addFileExtension("xml");
		if (!xmlPath.equals(graspPath)) {
			IFile xmlFile = file.getProject().getFile(xmlPath);
			if (xmlFile.exists()) {
				return refreshFromXml(xmlFile);
			}
		}
		return false;
	}
	
	public void reparse() throws CoreException {
		if (scanner == null) {
			scanner = new GraspScanner();
		}
		scanner.parse(new InputStreamReader(file.getContents()));
		
		Parser parser = new Parser();
		ISource source = new GraspSourceFile(file);
		syntaxTree = parser.parse(source, NullLogger.INSTANCE);
		
		EnumSet<Kind> changes = EnumSet.of(Kind.Reparsed);
		if (syntaxTree != null) {
			changes.add(Kind.SyntaxTreeChanged);
		}
		fireChange(this, changes);		
	}
	
	public IFile getFile() {
		return file;
	}
	
	public IErrorReport getErrorReport() {
		return errorReport;
	}
	
	public ArchitectureModel getLastArchitectureThatCompiled() {
		if (lastArchitectureThatCompiled == null) {
			refreshFromXml();
		}
		return lastArchitectureThatCompiled;
	}
	
	public ArchitectureModel getArchitecture() {
		if (architecture == null) {
			refreshFromXml();
		}
		return architecture;
	}
	
	public void setSourceViewer(ISourceViewer sourceViewer) {
		this.sourceViewer = sourceViewer;
		fireChange(this, EnumSet.of(Kind.SourceViewerChanged));
	}
	
	public IDocument getDocument() {
		return sourceViewer != null ? sourceViewer.getDocument() : null;
	}

	public ISyntaxTree getSyntaxTree() {
		return syntaxTree;
	}

	public void setSyntaxTree(ISyntaxTree syntaxTree) {
		this.syntaxTree = syntaxTree;
		fireChange(this, EnumSet.of(Kind.SyntaxTreeChanged));
	}

	public GraspScanner getScanner() {
		return scanner;
	}

	public void setScanner(GraspScanner scanner) {
		this.scanner = scanner;
		fireChange(this, EnumSet.of(Kind.ScannerChanged));
	}
}