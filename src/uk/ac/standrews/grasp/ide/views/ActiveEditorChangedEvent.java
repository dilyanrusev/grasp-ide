package uk.ac.standrews.grasp.ide.views;

import org.eclipse.core.resources.IFile;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;

import uk.ac.standrews.grasp.ide.model.ArchitectureModel;
import uk.ac.standrews.grasp.ide.model.GraspModel;

public class ActiveEditorChangedEvent {
	private final IEditorPart editor;
	
	public ActiveEditorChangedEvent(IEditorPart editor) {
		this.editor = editor;
	}
	
	public IEditorPart getEditor() {
		return editor;
	}
	
	public ArchitectureModel getEditorArchitecture() {
		if (editor.getEditorInput() instanceof IFileEditorInput) {
			IFile file = ((IFileEditorInput) editor.getEditorInput()).getFile();
			return GraspModel.INSTANCE.ensureFileStats(file).getArchitecture();
		}
		return null;
	}
}
