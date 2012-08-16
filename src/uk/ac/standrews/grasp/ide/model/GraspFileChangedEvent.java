package uk.ac.standrews.grasp.ide.model;

import java.util.EnumSet;

/**
 * Contains information about changes to the Eclipse file - Grasp model mapping
 * @author Dilyan Rusev
 *
 */
public class GraspFileChangedEvent {
	/**
	 * Defines what can change about the file-model mapping
	 * @author Dilyan Rusev
	 *
	 */
	public enum Kind {
		/** A file was compiled (though not necessarily succesfully) */
		Compiled,
		/** The architecture was successfully re-read from the XML produced by a successful compilation */
		ArchiectureRefreshed,
		/** The scanner for this file has been changed */
		ScannerChanged,
		/** The source viewer responsible for displaying this file's source code has changed */
		SourceViewerChanged,
	}
	
	private final EnumSet<Kind> kind;
	private final GraspFile source;
	
	/** 
	 * Construct a new event
	 * @param source Mapping that has changed
	 * @param kind Describes the kind of changes
	 */
	public GraspFileChangedEvent(GraspFile source, EnumSet<Kind> kind) {
		this.source = source;
		this.kind = kind;
	}
	
	/**
	 * Get the file-model mapping that has changed
	 * @return
	 */
	public GraspFile getSource() {
		return source;
	}
	
	/**
	 * Get the description of what exactly has changed. Query {@link #getSource()} for the new data.
	 * @return
	 */
	public EnumSet<Kind> getKind() {
		return kind;
	}
}
