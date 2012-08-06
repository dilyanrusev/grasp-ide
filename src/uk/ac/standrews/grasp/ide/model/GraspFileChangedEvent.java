package uk.ac.standrews.grasp.ide.model;

import java.util.EnumSet;

public class GraspFileChangedEvent {
	public enum Kind {
		Compiled,
		ArchiectureRefreshed,
		ScannerChanged,
		SourceViewerChanged,
	}
	
	private final EnumSet<Kind> kind;
	private final GraspFile source;
	
	public GraspFileChangedEvent(GraspFile source, EnumSet<Kind> kind) {
		this.source = source;
		this.kind = kind;
	}
	
	public GraspFile getSource() {
		return source;
	}
	
	public EnumSet<Kind> getKind() {
		return kind;
	}
}
