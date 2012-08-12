/**
 * 
 */
package uk.ac.standrews.grasp.ide.wizards;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveRegistry;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.wizards.newresource.BasicNewResourceWizard;

import uk.ac.standrews.grasp.ide.Log;
import uk.ac.standrews.grasp.ide.Msg;
import uk.ac.standrews.grasp.ide.perspectives.GraspPerspective;

/**
 * Creates a new project, associates it with the Grasp nature, and opens the Grasp perspective
 * @author Dilyan Rusev
 *
 */
public class NewProjectWizard extends Wizard implements INewWizard {
	/**
	 * ID of the wizard in plugin.xml
	 */
	public static final String ID = "uk.ac.standrews.grasp.ide.wizards.newProject";
	
	private IWorkbench workbench;
	private NewProjectWizardPage page;	

	/**
	 * Default constructor, required by Eclipse
	 */
	public NewProjectWizard() {
		setWindowTitle("New Grasp Project");
		setNeedsProgressMonitor(false);
	}

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench, org.eclipse.jface.viewers.IStructuredSelection)
	 */
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.workbench = workbench;
	}
	
	@Override
	public void addPages() {
		super.addPages();
		page = new NewProjectWizardPage();
		addPage(page);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		String projectName = page.getProjectName();
		
		CreateProjectOperation createProjectOp = new CreateProjectOperation(projectName);
		
		try {
			getContainer().run(false, true, createProjectOp);
		} catch (InvocationTargetException e) {
			String message = "Cannot create project " + projectName;
			Msg.showError(getShell(), null, message, e);
			Log.error(message, e);
			return false;
		} catch (InterruptedException e) {
			// it was cancelled - no need to log
			return false;
		}
		
		IProject project = createProjectOp.getProject();
		if (project == null) {
			return false;
		}
		
		openGraspPerspective();
		
		BasicNewResourceWizard.selectAndReveal(project, workbench.getActiveWorkbenchWindow());
		
		try {
			IDE.openEditor(workbench.getActiveWorkbenchWindow().getActivePage(), 
					project.getFile("src/wsn_simulator.grasp"));
		} catch (PartInitException e) {
			Log.error(e);
		}
			
		return true;
	}
	
	private void openGraspPerspective() {
		IPerspectiveRegistry registry = workbench.getPerspectiveRegistry();
		IPerspectiveDescriptor desc = registry.findPerspectiveWithId(GraspPerspective.ID);

		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if (window != null) {
			IWorkbenchPage page = window.getActivePage();
			page.setPerspective(desc);
		}
	}

}
