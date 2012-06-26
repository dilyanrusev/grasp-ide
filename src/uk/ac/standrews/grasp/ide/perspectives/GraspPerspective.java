package uk.ac.standrews.grasp.ide.perspectives;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.IConsoleConstants;
import org.eclipse.core.internal.runtime.PlatformActivator;
import org.eclipse.jdt.ui.JavaUI;

import uk.ac.standrews.grasp.ide.views.GefView;
import uk.ac.standrews.grasp.ide.wizards.NewProjectWizard;


/**
 *  This class is meant to serve as an example for how various contributions 
 *  are made to a perspective. Note that some of the extension point id's are
 *  referred to as API constants while others are hardcoded and may be subject 
 *  to change. 
 */
public class GraspPerspective implements IPerspectiveFactory {	
	public static final String ID = "uk.ac.standrews.grasp.ide.perspectives.GraspPerspective";
	
	private static final String ID_ERROR_LOG_VIEW = "org.eclipse.pde.runtime.LogView";

	private IPageLayout factory;

	public GraspPerspective() {
		super();
	}

	public void createInitialLayout(IPageLayout factory) {
		this.factory = factory;
		addViews();
		addActionSets();
		addNewWizardShortcuts();
		addPerspectiveShortcuts();
		addViewShortcuts();
	}

	private void addViews() {
		// Creates the overall folder layout. 
		// Note that each new Folder uses a percentage of the remaining EditorArea.
			
		IFolderLayout bottom =
			factory.createFolder(
				"bottomRight", //NON-NLS-1
				IPageLayout.BOTTOM,
				0.75f,
				factory.getEditorArea());
		
		bottom.addView(ID_ERROR_LOG_VIEW);
		bottom.addView(IPageLayout.ID_PROBLEM_VIEW);
		bottom.addView(IConsoleConstants.ID_CONSOLE_VIEW);

		IFolderLayout topLeft =
			factory.createFolder(
				"topLeft", //NON-NLS-1
				IPageLayout.LEFT,
				0.15f,
				factory.getEditorArea());
		
		// TODO: Use Common Navigator Framework
		topLeft.addView(IPageLayout.ID_RES_NAV);
		
		IFolderLayout topRight = factory.createFolder(
				"topRight", IPageLayout.RIGHT, 0.85f, factory.getEditorArea());
		topRight.addView(GefView.ID);
		topRight.addView(IPageLayout.ID_OUTLINE);
		topRight.addView(IPageLayout.ID_PROP_SHEET);		
		
		factory.addFastView(GefView.ID);
		factory.addFastView(ID_ERROR_LOG_VIEW); 
		factory.addFastView(IPageLayout.ID_OUTLINE);
		factory.addFastView(IPageLayout.ID_PROP_SHEET);
	}

	private void addActionSets() {
		factory.addActionSet(IPageLayout.ID_NAVIGATE_ACTION_SET); //NON-NLS-1
	}

	private void addPerspectiveShortcuts() {
		factory.addPerspectiveShortcut(JavaUI.ID_PERSPECTIVE); //NON-NLS-1
	}

	private void addNewWizardShortcuts() {		
		factory.addNewWizardShortcut(NewProjectWizard.ID);
		// TODO: Add shortcut for new file when it is implemented
		
		factory.addNewWizardShortcut("org.eclipse.ui.wizards.new.folder");//NON-NLS-1
		factory.addNewWizardShortcut("org.eclipse.ui.wizards.new.file");//NON-NLS-1
	}

	private void addViewShortcuts() {
		
		factory.addFastView(GefView.ID);
		factory.addFastView(IPageLayout.ID_PROP_SHEET);
		factory.addFastView(IPageLayout.ID_OUTLINE);
		factory.addShowViewShortcut(IConsoleConstants.ID_CONSOLE_VIEW);
		factory.addShowViewShortcut(IPageLayout.ID_PROBLEM_VIEW);
		factory.addFastView(ID_ERROR_LOG_VIEW); 
		// TODO: Use Common Navigator Framework
		factory.addShowViewShortcut(IPageLayout.ID_RES_NAV);
	}

}
