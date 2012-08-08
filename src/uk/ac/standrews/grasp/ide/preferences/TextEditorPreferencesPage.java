package uk.ac.standrews.grasp.ide.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import static org.eclipse.ui.texteditor.AbstractDecoratedTextEditorPreferenceConstants.EDITOR_CURRENT_LINE;
import static org.eclipse.ui.texteditor.AbstractDecoratedTextEditorPreferenceConstants.EDITOR_SPACES_FOR_TABS;
import static org.eclipse.ui.texteditor.AbstractDecoratedTextEditorPreferenceConstants.EDITOR_TAB_WIDTH;
import static org.eclipse.ui.texteditor.AbstractDecoratedTextEditorPreferenceConstants.EDITOR_LINE_NUMBER_RULER;
import static org.eclipse.ui.texteditor.AbstractDecoratedTextEditorPreferenceConstants.EDITOR_SMART_HOME_END;


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
		setDescription("NOTE: Changes will take effect only"
				+ " for newly started editors. Opened editors will have to be closed"
				+ " and reopened");
	}

	@Override
	public void init(IWorkbench workbench) {
	}

	@Override
	protected void createFieldEditors() {	
		addField(new BooleanFieldEditor(
				EDITOR_LINE_NUMBER_RULER, 
				"Show line numbers", 
				getFieldEditorParent()));
		addField(new BooleanFieldEditor(
				EDITOR_CURRENT_LINE, 
				"Highlight current line", 
				getFieldEditorParent()));
		addField(new BooleanFieldEditor(
				EDITOR_SMART_HOME_END, 
				"Smart Home/End navigation", 
				getFieldEditorParent()));
		addField(new BooleanFieldEditor(
				EDITOR_SPACES_FOR_TABS, 
				"Use spaces instead of tabs", 
				getFieldEditorParent()));
		addField(new IntegerFieldEditor(
				EDITOR_TAB_WIDTH, 
				"Tab width", 
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
				PreferenceKeys.ENABLE_KEYWORD_COMPLETION.getSettingName(), 
				"Enable keyword code completion", 
				getFieldEditorParent()));
	}

}
