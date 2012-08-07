package uk.ac.standrews.grasp.ide.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import uk.ac.standrews.grasp.ide.GraspPlugin;

/**
 * Exposes UI that edits settings related to the source code editor
 * @author Dilyan Rusev
 *
 */
public class TextEditorPreferencesPage extends FieldEditorPreferencePage
		implements IWorkbenchPreferencePage {

	/**
	 * Creates text editor preferences page
	 */
	public TextEditorPreferencesPage() {
		super(GRID);
		setPreferenceStore(GraspPlugin.getDefault().getPreferenceStore());
		setDescription("Set up the behaviour of the Grasp source code editor");
	}

	@Override
	public void init(IWorkbench workbench) {
	}

	@Override
	protected void createFieldEditors() {	
		addField(new BooleanFieldEditor(
				PreferenceKeys.ENABLE_SHOW_LINE_NUMBERS.getSettingName(), 
				"Show line numbers", 
				getFieldEditorParent()));
		addField(new BooleanFieldEditor(
				PreferenceKeys.ENABLE_SYNTAX_HIGHLIGHTING.getSettingName(), 
				"Enable syntax highlighting", 
				getFieldEditorParent()));
		addField(new ColorFieldEditor(
				PreferenceKeys.COLOUR_KEYWORD.getSettingName(), 
				"Keywords", 
				getFieldEditorParent()));
		addField(new ColorFieldEditor(
				PreferenceKeys.COLOUR_INLINE_COMMENT.getSettingName(), 
				"Inline comments", 
				getFieldEditorParent()));
		addField(new ColorFieldEditor(
				PreferenceKeys.COLOUR_BLOCK_COMMENT.getSettingName(), 
				"Block comments", 
				getFieldEditorParent()));
		addField(new ColorFieldEditor(
				PreferenceKeys.COLOUR_STRING_LITERAL.getSettingName(), 
				"String literals", 
				getFieldEditorParent()));
		addField(new ColorFieldEditor(
				PreferenceKeys.COLOUR_DECLARATIVE_LITERAL.getSettingName(), 
				"Declarative literals", 
				getFieldEditorParent()));
		addField(new BooleanFieldEditor(
				PreferenceKeys.ENABLE_CODE_COMPLETION.getSettingName(), 
				"Enable code completion", 
				getFieldEditorParent()));
		addField(new BooleanFieldEditor(
				PreferenceKeys.ENABLE_KEYWORD_COMPLETION.getSettingName(), 
				"Enable keyword code completion", 
				getFieldEditorParent()));
	}

}
