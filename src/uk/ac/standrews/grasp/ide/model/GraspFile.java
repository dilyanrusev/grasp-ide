package uk.ac.standrews.grasp.ide.model;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.source.ISourceViewer;

import uk.ac.standrews.grasp.ide.compiler.CompilationResult;
import uk.ac.standrews.grasp.ide.editors.completion.GraspScanner;
import uk.ac.standrews.grasp.ide.model.GraspFileChangedEvent.Kind;

public class GraspFile {
	private static Set<IGraspFileChangedListener> changeListeners =
			new HashSet<IGraspFileChangedListener>();
	
	private ArchitectureModel architecture;
	private ArchitectureModel lastArchitectureThatCompiled;
	private ISourceViewer sourceViewer;	
	private GraspScanner scanner;
	private CompilationResult compilationResult;
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
	
	public void compiled(CompilationResult compilationResult) {
		this.compilationResult = compilationResult;
		
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
	
	public IFile getFile() {
		return file;
	}
	
	public CompilationResult getCompilationResult() {
		return compilationResult;
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

	public GraspScanner getScanner() {
		return scanner;
	}

	public void setScanner(GraspScanner scanner) {
		this.scanner = scanner;
		fireChange(this, EnumSet.of(Kind.ScannerChanged));
	}
}