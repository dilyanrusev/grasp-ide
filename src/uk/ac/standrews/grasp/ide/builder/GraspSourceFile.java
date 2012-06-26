package uk.ac.standrews.grasp.ide.builder;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;

import shared.io.ISource;

/**
 * Adapts <code>IFile</code> to <code>ISource</code>
 * @author Dilyan Rusev
 *
 * @see org.eclipse.core.resources.IFile
 * @see shared.io.ISource
 */
class GraspSourceFile implements ISource {
	private IFile adapted;
	
	/**
	 * Creates a new grasp source file
	 * @param adapted Eclipse file resource to adapt
	 */
	public GraspSourceFile(IFile adapted) {
		Assert.isNotNull(adapted);
		this.adapted = adapted;
	}

	@Override
	public String getFullName() {
		return adapted.getFullPath().toOSString();
	}

	@Override
	public String getName() {
		return adapted.getName();
	}

	@Override
	public Reader getReader() throws IOException {
		InputStream contents;
		try {
			contents = adapted.getContents();
		} catch (CoreException e) {
			throw new IOException(e);
		}
		Reader reader = new InputStreamReader(contents);
		return reader;
	}

	@Override
	public String toString() {
		return adapted.getName();
	}
}
