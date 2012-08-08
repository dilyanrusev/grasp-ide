package uk.ac.standrews.grasp.ide.preferences;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.swt.graphics.RGB;

import uk.ac.standrews.grasp.ide.GraspPlugin;

/**
 * Easy access to the plugin's settings
 * @author Dilyan Rusev
 *
 */
public final class Preferences {
	private Preferences() {}
	
	/**
	 * Queries preference store if syntax highlighting is enabled
	 * @return
	 */
	public static boolean isSyntaxHighlightingEnabled() {
		return getStore().getBoolean(
				PreferenceKeys.ENABLE_SYNTAX_HIGHLIGHTING.getSettingName());
	}
	
	/**
	 * Queries preference store if text editor should show line numbers
	 * @return
	 */
	public static boolean isShowLineNumbersEnabled() {
		return getStore().getBoolean(
				PreferenceKeys.ENABLE_SHOW_LINE_NUMBERS.getSettingName());
	}
	
	/**
	 * Queries preference store if text editor should enable keyword code completion processor
	 * @return
	 */
	public static boolean isKeywordCompletionEnabled() {
		return getStore().getBoolean(
				PreferenceKeys.ENABLE_KEYWORD_COMPLETION.getSettingName());
	}
	
	/**
	 * Queries preference store for the keyword colour
	 * @return
	 */
	public static RGB getKeywordRgb() {		
		return PreferenceConverter.getColor(getStore(), 
				PreferenceKeys.COLOUR_KEYWORD.getSettingName());
	}
	
	/**
	 * Queries preference store for the inline comments colour
	 * @return
	 */
	public static RGB getInlineCommentRgb() {		
		return PreferenceConverter.getColor(getStore(), 
				PreferenceKeys.COLOUR_INLINE_COMMENT.getSettingName());
	}
	
	/**
	 * Queries preference store for the block comments colour 
	 * @return
	 */
	public static RGB getBlockCommentRgb() {		
		return PreferenceConverter.getColor(getStore(), 
				PreferenceKeys.COLOUR_BLOCK_COMMENT.getSettingName());
	}
	
	/**
	 * Queries preference store for the string literal colour
	 * @return
	 */
	public static RGB getStringLiteralRgb() {		
		return PreferenceConverter.getColor(getStore(), 
				PreferenceKeys.COLOUR_STRING_LITERAL.getSettingName());
	}
	
	/**
	 * Queries preference store for the declarative literal colour 
	 * @return
	 */
	public static RGB getDeclarativeLiteralRgb() {		
		return PreferenceConverter.getColor(getStore(), 
				PreferenceKeys.COLOUR_DECLARATIVE_LITERAL.getSettingName());
	}
	
	/**
	 * Queries preference store if the integrated compiler should be used over the external compiler
	 * @return
	 */
	public static boolean isIntegratedCompilerEnabled() {
		return getStore().getBoolean(
				PreferenceKeys.ENABLE_INTEGRATED_COMPILER.getSettingName());
	}
	
	/**
	 * Queries preference store for the absolute path to the external compiler's jar file
	 * @return
	 */
	public static String getExternalCompilerPath() {
		return getStore().getString(
				PreferenceKeys.FILE_EXTERNAL_COMPILER.getSettingName());
	}	
	
	/**
	 * Shortcut for GraspPlugin.getDefault().getPreferenceStore()
	 * @return
	 */
	public static IPreferenceStore getStore() {
		return GraspPlugin.getDefault().getPreferenceStore();
	}
}
