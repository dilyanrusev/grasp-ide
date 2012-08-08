package uk.ac.standrews.grasp.ide.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.StringConverter;
import org.eclipse.swt.graphics.RGB;

import uk.ac.standrews.grasp.ide.GraspPlugin;

/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
	 */
	public void initializeDefaultPreferences() {
		IPreferenceStore store = GraspPlugin.getDefault().getPreferenceStore();		
		
		store.setDefault(PreferenceKeys.ENABLE_SYNTAX_HIGHLIGHTING.getSettingName(), true);		
		store.setDefault(PreferenceKeys.ENABLE_KEYWORD_COMPLETION.getSettingName(), true);
		store.setDefault(PreferenceKeys.ENABLE_SHOW_LINE_NUMBERS.getSettingName(), true);
		store.setDefault(PreferenceKeys.ENABLE_SHOW_LINE_NUMBERS.getSettingName(), true);
		store.setDefault(PreferenceKeys.ENABLE_INTEGRATED_COMPILER.getSettingName(), true);		
		
		store.setDefault(PreferenceKeys.COLOUR_KEYWORD.getSettingName(), 
				StringConverter.asString(new RGB(127, 0, 85)));		
		store.setDefault(PreferenceKeys.COLOUR_INLINE_COMMENT.getSettingName(), 
				StringConverter.asString(new RGB(63, 127, 95)));		
		store.setDefault(PreferenceKeys.COLOUR_BLOCK_COMMENT.getSettingName(), 
				StringConverter.asString(new RGB(63, 127, 95)));		
		store.setDefault(PreferenceKeys.COLOUR_STRING_LITERAL.getSettingName(), 
				StringConverter.asString(new RGB(42, 0, 255)));		
		store.setDefault(PreferenceKeys.COLOUR_DECLARATIVE_LITERAL.getSettingName(), 
				StringConverter.asString(new RGB(63, 95, 191)));
	}

}
