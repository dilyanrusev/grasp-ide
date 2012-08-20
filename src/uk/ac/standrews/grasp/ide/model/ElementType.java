package uk.ac.standrews.grasp.ide.model;

/**
 * Types of Grasp elements
 * @author Dilyan Rusev
 *
 */
public enum ElementType {
	/** Grasp annotation element */
	ANNOTATION("@", false, "Annotation")
	/** Grasp architecture element */
	, ARCHITECTURE("architecture", false, "Architecture")
	/** Grasp check statement */
	, CHECK("check", true, "Check")
	/** Grasp component element */
	, COMPONENT("component", true, "Component")
	/** Grasp connector element */
	, CONNECTOR("connector", true, "Connector")
	/** Grasp expression statement */
	, EXPRESSION(null, false, "Expression")
	/** Grasp layer element */
	, LAYER("layer", false, "Layer")
	/** Grasp link statement */
	, LINK("link", true, "Link")
	/** Grasp named value element */
	, NAMEDVALUE(null, false, "Named value")
	/** Grasp property element */
	, PROPERTY("property", true, "Property")
	/** Grasp provides element */
	, PROVIDES("provides", true, "Provides")
	/** Grasp quality_attribute element */
	, QUALITY_ATTRIBUTE("quality_attribute", true, "Quality attribute")
	/** Grasp rationale element */
	, RATIONALE("rationale", false, "Rationale")
	/** Grasp reason element */
	, REASON("reason", true, "Reason")
	/** Grasp requirement element */
	, REQUIREMENT("requirement", true, "Requirement")
	/** Grasp requires element */
	, REQUIRES("requires", true, "Requires")
	/** Grasp system element */
	, SYSTEM("system", false, "System")
	/** Grasp template element */
	, TEMPLATE("template", false, "Template")
	;
	
	private final String keyword;
	private final boolean endsWithSemicolon;
	private final String displayName;
	
	/** Define new element type 
	 *
	 * @param keyword Grasp language keyword for this particular type
	 * @param endsWithSemicolon True if the element is allowed to end with semicolon
	 * @param displayName Display name
	 */
	ElementType(String keyword, boolean endsWithSemicolon, String displayName) {
		this.keyword = keyword;
		this.endsWithSemicolon = endsWithSemicolon;
		this.displayName = displayName;
	}
	
	/**
	 * Returns the Grasp language keyword for this element
	 * @return Grasp keyword for this element
	 */
	public String getKeyword() {
		return keyword;
	}

	/**
	 * Return true if the element is allowed to end in semicolon
	 * @return
	 */
	public boolean endsWithSemicolon() {
		return endsWithSemicolon;
	}
	
	/**
	 * Return user-friendly string for this type
	 * @return
	 */
	public String getDisplayName() {
		return displayName;
	}
	
}
