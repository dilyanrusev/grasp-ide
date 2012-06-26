/**
 * 
 */
package uk.ac.standrews.grasp.ide.wizards;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * Hosts the UI that gathers data used to create a new Grasp project
 * @author Dilyan Rusev
 *
 */
public class NewProjectWizardPage extends WizardPage {
	private String projectName;

	/**
	 * Initialize the new Grasp project page
	 */
	public NewProjectWizardPage() {
		super("newProjectPage");
		setTitle("New Grasp Project");
		setDescription("Create a Grasp Project");
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createControl(Composite parent) {
		final Composite container = new Composite(parent, SWT.NONE);
		setControl(container);
		{
			GridLayout rootLayout = new GridLayout();
			rootLayout.numColumns = 2;
			container.setLayout(rootLayout);
		}
		
		final Label projectLabel = new Label(container, SWT.NONE);
		projectLabel.setText("Project name: ");
		projectLabel.setLayoutData(new GridData());	

		final Text projectText = new Text(container, SWT.BORDER);
		projectText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		projectText.addModifyListener(new ModifyListener() {			
			@Override
			public void modifyText(ModifyEvent e) {
				projectName = projectText.getText();
				validate();
			}
		});		
	}
	
	private void validate() {				
		if (projectName == null || projectName.length() == 0) {
			setErrorMessage("You must enter a project name");
			setPageComplete(false);
			return;
		}
		
		if (projectName.trim().length() == 0) {
			setErrorMessage("Your project name contains only whitespace");
			setPageComplete(false);
			return;
		}
		
		setErrorMessage(null);
		setPageComplete(true);
	}
	
	/**
	 * Project name selected by wizard
	 * @return Project name. May be null if not validated.
	 */
	public String getProjectName() {
		return projectName;
	}
}
