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

/**
 * Maintains mappings between Eclipse files and the Grasp model parsed/compiled from them
 * @author Dilyan Rusev
 *
 */
public class GraspFile {
	private static Set<IGraspFileChangedListener> changeListeners =
			new HashSet<IGraspFileChangedListener>();
	
	private ArchitectureModel architecture;
	private ArchitectureModel lastArchitectureThatCompiled;
	private ISourceViewer sourceViewer;	
	private GraspScanner scanner;
	private CompilationResult compilationResult;
	private IFile file;
	
	/**
	 * Add a listener that will be notified whenever there is a change to file-model mapping
	 * @param listener Event listener. Has no effect if already added
	 */
	public static void addChangeListener(IGraspFileChangedListener listener) {
		changeListeners.add(listener);
	}
	
	/**
	 * Remove the listener
	 * @param listener Event listener. Has no effect if not added
	 */
	public static void removeChangeListener(IGraspFileChangedListener listener) {
		changeListeners.remove(listener);
	}
	
	private static void fireChange(GraspFile instance, EnumSet<Kind> change) {
		GraspFileChangedEvent event = new GraspFileChangedEvent(instance, change);
		for (IGraspFileChangedListener listener: changeListeners) {
			listener.fileChanged(event);
		}
	}
	
	/**
	 * Construct a new file-model mapping
	 * @param file File to maintain statistics for
	 */
	public GraspFile(IFile file) {
		this.file = file;
	}
	
	/**
	 * Notify subscribers that a file has been compiled
	 * @param compilationResult Results of the compilation
	 */
	public void compiled(CompilationResult compilationResult) {
		this.compilationResult = compilationResult;
		
		fireChange(this, EnumSet.of(Kind.Compiled));
	}
	
	/**
	 * Notify subscribers that the model associated with this file has been reloaded from the XML-serialized graph
	 * @param xmlFile File containing the XML serialized graph
	 * @return True if the model was upgraded
	 */
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
	
	/**
	 * Get the file for this mapping
	 * @return file for this mapping
	 */
	public IFile getFile() {
		return file;
	}
	
	/**
	 * Get the result of the last compilation
	 * @return last compilation result
	 */
	public CompilationResult getCompilationResult() {
		return compilationResult;
	}
	
	/**
	 * Return the last successfully compiled model for this file
	 * @return last successfully compiled model for this file
	 */
	public ArchitectureModel getLastArchitectureThatCompiled() {
		if (lastArchitectureThatCompiled == null) {
			refreshFromXml();
		}
		return lastArchitectureThatCompiled;
	}
	
	/**
	 * Get the model associated with this file
	 * @return model associated with this file
	 */
	public ArchitectureModel getArchitecture() {
		if (architecture == null) {
			refreshFromXml();
		}
		return architecture;
	}
	
	/**
	 * Set the source viewer associated with this file
	 * @param sourceViewer Source viewer that shows the source code of this file
	 */
	public void setSourceViewer(ISourceViewer sourceViewer) {
		this.sourceViewer = sourceViewer;
		fireChange(this, EnumSet.of(Kind.SourceViewerChanged));
	}
	
	/**
	 * Get the document that displays the textual contents of this file
	 * @return Document
	 */
	public IDocument getDocument() {
		return sourceViewer != null ? sourceViewer.getDocument() : null;
	}	

	/**
	 * Get the token scanner for this file
	 * @return Scanner
	 */
	public GraspScanner getScanner() {
		return scanner;
	}

	/**
	 * Set the token scanner for this document
	 * @param scanner New scanner
	 */
	public void setScanner(GraspScanner scanner) {
		this.scanner = scanner;
		fireChange(this, EnumSet.of(Kind.ScannerChanged));
	}
}