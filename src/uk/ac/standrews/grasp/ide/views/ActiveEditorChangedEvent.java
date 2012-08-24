package uk.ac.standrews.grasp.ide.views;

import org.eclipse.core.resources.IFile;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;

import uk.ac.standrews.grasp.ide.model.ArchitectureModel;
import uk.ac.standrews.grasp.ide.model.GraspModel;

/**
 * Event data for the edior change notification
 * @author Dilyan Rusev
 *
 */
public class ActiveEditorChangedEvent {
	private final IEditorPart editor;
	
	/**
	 * Construct new event data
	 * @param editor New editor
	 */
	public ActiveEditorChangedEvent(IEditorPart editor) {
		this.editor = editor;
	}
	
	/**
	 * Get the active editor
	 * @return Active editor
	 */
	public IEditorPart getEditor() {
		return editor;
	}
	
	/**
	 * Attempt to extract the architecture associated with the editor input's file
	 * @return Architecture or null
	 */
	public ArchitectureModel getEditorArchitecture() {
		if (editor.getEditorInput() instanceof IFileEditorInput) {
			IFile file = ((IFileEditorInput) editor.getEditorInput()).getFile();
			return GraspModel.INSTANCE.ensureFileStats(file).getArchitecture();
		}
		return null;
	}
}
