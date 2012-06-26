package uk.ac.standrews.grasp.ide.builder;

import org.eclipse.core.resources.IResource;

/**
 * Used by visitor to compile individual resources
 * @author Dilyan Rusev
 * @see GraspBuilder
 */
public interface IGraspBuilder {
	/**
	 * Builds an individual grasp architecture file. Ignores non-<code>IFile</code> resources
	 * and files not ending with ".grasp"
	 * @param resource Resource to inspect and build
	 */
	void performBuild(IResource resource);
}
