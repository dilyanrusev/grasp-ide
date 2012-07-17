package uk.ac.standrews.grasp.ide.wizards;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.ui.actions.WorkspaceModifyOperation;

import uk.ac.standrews.grasp.ide.Log;

public class CreateArchitectureOperation extends WorkspaceModifyOperation {
	private IGraspExample example;
	private IFile file;
	private String name;
	
	public CreateArchitectureOperation(IGraspExample example, IFile file, String name) {
		this.example = example;
		this.file = file;
		this.name = name;
	}

	@Override
	protected void execute(IProgressMonitor monitor) throws CoreException,
			InvocationTargetException, InterruptedException {
		monitor.beginTask("Create grasp architecture", IProgressMonitor.UNKNOWN);
		
		ensureExists(file.getParent(), monitor);
		
		InputStream contents = new ByteArrayInputStream(example.getText(name).getBytes());
		file.create(contents, true, new SubProgressMonitor(monitor, 1));
		try {
			contents.close();
		} catch (IOException e) {
			Log.error(e);
		}
		
		monitor.done();		
	}
	
	private void ensureExists(IContainer current, IProgressMonitor monitor) throws CoreException {
		if (current == null) return;
		if (!current.exists()) {
			ensureExists(current.getParent(), monitor);
			if (current instanceof IFolder) {
				IFolder folder = (IFolder) current;
				folder.create(true, true, new SubProgressMonitor(monitor, 1));
			} 
		}
	}
}
