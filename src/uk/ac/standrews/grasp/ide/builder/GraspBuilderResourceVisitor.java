/**
 * 
 */
package uk.ac.standrews.grasp.ide.builder;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;

/**
 * Visits project structure/listens to resource changes and builds grasp files
 * @author Dilyan Rusev
 *
 */
class GraspBuilderResourceVisitor implements IResourceDeltaVisitor,
		IResourceVisitor {
	private IGraspBuilder builder;
	
	/**
	 * Creates a visitor and binds it to an builder
	 * @param builder Class that can build individual files
	 */
	public GraspBuilderResourceVisitor(IGraspBuilder builder) {
		this.builder = builder;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.resources.IResourceVisitor#visit(org.eclipse.core.resources.IResource)
	 */
	@Override
	public boolean visit(IResource resource) throws CoreException {
		// Invoked by full build
		builder.performBuild(resource);
		return true;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.resources.IResourceDeltaVisitor#visit(org.eclipse.core.resources.IResourceDelta)
	 */
	@Override
	public boolean visit(IResourceDelta delta) throws CoreException {
		// invoked by incremental build
		IResource resource = delta.getResource();
		switch (delta.getKind()) {
		case IResourceDelta.ADDED:
			// do not build on add
			break;
		case IResourceDelta.REMOVED:
			// do not build on remove
			break;
		case IResourceDelta.CHANGED:
			// do build on change
			builder.performBuild(resource);
			break;
		}
		//return true to continue visiting children.
		return true;
	}

}
