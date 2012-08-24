package uk.ac.standrews.grasp.ide.wizards;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.IDE;

import uk.ac.standrews.grasp.ide.Log;
import uk.ac.standrews.grasp.ide.Msg;

/**
 * Wizards that creates Grasp Architecture files and adds them to the project
 * @author Dilyan Rusev
 *
 */
public class NewArchitectureWizard extends Wizard implements INewWizard {
	/**
	 * ID of the wizard, as specified in plugin.xml
	 */
	public static final String ID = "uk.ac.standrews.grasp.ide.wizards.newArchitecture";
	
	private NewArchitectureWizardPage page;
	private IWorkbench workbench;
	private IContainer fileContainer;
	private IProject project;

	/**
	 * Construct the wizard
	 */
	public NewArchitectureWizard() {
		setWindowTitle("New Grasp architecture");
		setNeedsProgressMonitor(false);
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		Assert.isTrue(selection != null && !selection.isEmpty());
		this.workbench = workbench;
		Object sel = selection.getFirstElement();
		resolveSelection(sel);
	}
	
	private void resolveSelection(Object selection) {
		if (selection instanceof IContainer) {
			fileContainer = (IContainer) selection;
			project = fileContainer.getProject();
		} else if (selection instanceof IFile) {
			resolveSelection(((IFile) selection).getParent());
		} else if (selection instanceof IProject) {
			project = (IProject) selection;
			fileContainer = project;		
		}		
	}
	
	@Override
	public void addPages() {		
		super.addPages();
		page = new NewArchitectureWizardPage(fileContainer);
		addPage(page);
	}

	@Override
	public boolean performFinish() {
		fileContainer = page.getFileContainer();
		String name = page.getArchitectureName();
		IFile file = page.getArchitectureFile();
		IGraspExample template = page.getSelectedExample();
		
		try {
			getContainer().run(false, true, 
					new CreateArchitectureOperation(template, file, name));
		} catch (InvocationTargetException e) {
			String message = "Cannot create architecture " + name;
			Msg.showError(getShell(), message, null, e);
			Log.error(message, e);
			return false;
		} catch (InterruptedException e) {
			// it was cancelled - no need to log
			return false;
		}
		
		try {
			IDE.openEditor(workbench.getActiveWorkbenchWindow().getActivePage(), 
					file);
		} catch (PartInitException e) {
			Log.error(e);
		}
		
		return true;
	}

}
