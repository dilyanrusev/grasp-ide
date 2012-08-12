package uk.ac.standrews.grasp.ide.wizards;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.ui.actions.WorkspaceModifyOperation;

import uk.ac.standrews.grasp.ide.builder.GraspNature;

/**
 * This class contains the project creation logic that requires progress monitors and batches them to be ran together
 * @author Dilyan Rusev
 *
 */
class CreateProjectOperation extends WorkspaceModifyOperation {
	private String projectName;
	private IProject theProject;
	private IWorkspaceRoot workspaceRoot;
	
	/**
	 * Create a new batch project creation operation
	 * @param projectName Name of the project to create.
	 * @see #getProject()
	 */
	public CreateProjectOperation(String projectName) {
		this.projectName = projectName;
		this.workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
	}

	@Override
	protected void execute(IProgressMonitor monitor) throws CoreException,
			InvocationTargetException, InterruptedException {
		theProject = null;
		SubMonitor progress = SubMonitor.convert(monitor, 6);		
		
		IProject project = workspaceRoot.getProject(projectName);		
		project.create(progress.newChild(1));		
		project.open(progress.newChild(1));
		
		addGraspNature(project, progress.newChild(1));
		createProjectStructure(project, progress.newChild(3));
			
		theProject = project;
	}
	
	/*
	 * Associates the grasp nature with the project
	 */
	private static void addGraspNature(IProject project, IProgressMonitor monitor) throws CoreException {
		IProjectDescription desc = project.getDescription();
		String[] natures = desc.getNatureIds();
		String[] newNatures = new String[natures.length + 1];
		System.arraycopy(natures, 0, newNatures, 0, natures.length);
		newNatures[natures.length] = GraspNature.NATURE_ID;
		desc.setNatureIds(newNatures);
		project.setDescription(desc, monitor);
	}
	
	/*
	 * Create initial project structure - a src folder and a sample architecture
	 */
	private static void createProjectStructure(IProject project, SubMonitor progress) throws CoreException {
		IFolder srcFolder = project.getFolder("src");
		if (!srcFolder.exists()) {
			srcFolder.create(true, true, progress.newChild(1));
		} else {
			progress.setWorkRemaining(2);
		}
		
		IFile sampleFile = srcFolder.getFile("wsn_simulator.grasp");
		InputStream contents = new ByteArrayInputStream(GraspExamples.WSN_SIMULATOR.getText("WsnSimulator").getBytes());
		if (!sampleFile.exists()) {
			sampleFile.create(contents, true, progress.newChild(1));			
		} else {
			sampleFile.setContents(contents, true, false, progress.newChild(1));
		}
		sampleFile.setCharset("utf-8", progress.newChild(1));
	}
		
	/**
	 * Returns the newly created <code>IProject</code>
	 * @return The created project when completed successfully, or <code>null</code> on failure
	 */
	public IProject getProject() {
		return theProject;
	}
}
