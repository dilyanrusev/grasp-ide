package uk.ac.standrews.grasp.ide.preferences;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import uk.ac.standrews.grasp.ide.GraspPlugin;

/**
 * Exposes displays information about the plugin
 * @author Dilyan Rusev
 *
 */
public class GraspPreferencePage
	extends PreferencePage
	implements IWorkbenchPreferencePage {

	/**
	 * Create
	 */
	public GraspPreferencePage() {		
		setPreferenceStore(GraspPlugin.getDefault().getPreferenceStore());
		setDescription("Expand child pages to configure the plugin");
	}	

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	@Override
	public void init(IWorkbench workbench) {
	}

	@Override
	protected Control createContents(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new FillLayout());
		Link info = new Link(container, SWT.NONE);
		info.setText("Visit the project <a>website</a> for more information");
		info.addListener(SWT.Selection, new Listener() {			
			@Override
			public void handleEvent(Event event) {
				Program.launch("http://code.google.com/p/grasp-ide");
			}
		});
		return container;
	}
	
}