package uk.ac.standrews.grasp.ide.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import uk.ac.standrews.grasp.ide.GraspPlugin;

/**
 * Exposes UI that edits settings related to the compiler that should be used (integrated or external)
 * @author Dilyan Rusev
 *
 */
public class CompilerPreferencesPage extends FieldEditorPreferencePage
		implements IWorkbenchPreferencePage {

	/**
	 * Creates a new compiler preferences page
	 */
	public CompilerPreferencesPage() {
		super(GRID);
		setPreferenceStore(GraspPlugin.getDefault().getPreferenceStore());
		setDescription("Set up which compiler to use");
	}
	
	@Override
	public void init(IWorkbench workbench) {
	}

	@Override
	protected void createFieldEditors() {
		addField(new BooleanFieldEditor(
				PreferenceKeys.ENABLE_INTEGRATED_COMPILER.getSettingName(), 
				"Use integrated compiler over the external compiler", 
				getFieldEditorParent()));
		addField(new FileFieldEditor(
				PreferenceKeys.FILE_EXTERNAL_COMPILER.getSettingName(), 
				"Path to the external compiler", 
				true, // enforce absolute path 
				getFieldEditorParent()));
	}

}
