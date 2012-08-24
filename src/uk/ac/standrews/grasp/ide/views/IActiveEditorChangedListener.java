package uk.ac.standrews.grasp.ide.views;

/**
 * Clients receive notifications when the active editor has been changed
 * @author Dilyan Rusev
 *
 */
public interface IActiveEditorChangedListener {
	/**
	 * Notification that the active editor has been changed
	 * @param event Describes the changed editor
	 */
	void activeEditorChanged(ActiveEditorChangedEvent event);
}
