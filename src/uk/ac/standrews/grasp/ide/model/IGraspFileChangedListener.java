package uk.ac.standrews.grasp.ide.model;

/**
 * Contract for classes that want to observe changes to the file-model mapping
 * @author Dilyan Rusev
 *
 */
public interface IGraspFileChangedListener {
	/**
	 * Notification about a change in the file-model mapping
	 * @param event Information about the actual change
	 */
	void fileChanged(GraspFileChangedEvent event);
}
