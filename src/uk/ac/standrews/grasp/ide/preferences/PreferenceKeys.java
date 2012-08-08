package uk.ac.standrews.grasp.ide.preferences;

import org.eclipse.jface.util.PropertyChangeEvent;

/**
 * Constant definitions for plug-in preferences
 */
public enum PreferenceKeys {
	
	/** Enable or disable syntax highlighting */
	ENABLE_SYNTAX_HIGHLIGHTING ("enable-syntax-highlighting"),	
	/** Enable or disable code completion for keywords */
	ENABLE_KEYWORD_COMPLETION ("enable-keyword-completion"),	
	/** Colour used in syntax highlighting for keywords */ 
	COLOUR_KEYWORD ("colour-keyword"),
	/** Colour used in syntax highlighting for inline comments */
	COLOUR_INLINE_COMMENT ("colour-inline-comment"),
	/** Colour used in syntax highlighting for block comments */
	COLOUR_BLOCK_COMMENT ("colour-block-comment"),
	/** Colour used in syntax highlighting for string literals */
	COLOUR_STRING_LITERAL ("colour-string-literal"),
	/** Colour used in syntax highlighting for declaration literals */
	COLOUR_DECLARATIVE_LITERAL ("colour-declaration-literal"),
	/** Enable or disable the use of the integraded compiler */
	ENABLE_INTEGRATED_COMPILER ("enable-integrated-compiler"),
	/** Path to the external compiler */
	FILE_EXTERNAL_COMPILER ("file-external-compiler"),
	;
		
	private final String settingName;
	
	PreferenceKeys(String settingName) {
		this.settingName = settingName;
	}
	
	/**
	 * Retrieve the setting name used in preferences store
	 * @return
	 */
	public String getSettingName() {
		return settingName;
	}
	
	public boolean equals(PropertyChangeEvent propChangeEvent) {
		return settingName.equals(propChangeEvent.getProperty());
	}
}
