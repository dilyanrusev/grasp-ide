package uk.ac.standrews.grasp.ide.model;

/**
 * Types of Grasp elements
 * @author Dilyan Rusev
 *
 */
public enum ElementType {
	/** Grasp annotation element */
	ANNOTATION("@", false)
	/** Grasp architecture element */
	, ARCHITECTURE("architecture", false)
	/** Grasp check statement */
	, CHECK("check", true)
	/** Grasp component element */
	, COMPONENT("component", true)
	/** Grasp connector element */
	, CONNECTOR("connector", true)
	/** Grasp expression statement */
	, EXPRESSION(null, false)
	/** Grasp layer element */
	, LAYER("layer", false)
	/** Grasp link statement */
	, LINK("link", true)
	/** Grasp named value element */
	, NAMEDVALUE(null, false)
	/** Grasp property element */
	, PROPERTY("property", true)
	/** Grasp provides element */
	, PROVIDES("provides", true)
	/** Grasp quality_attribute element */
	, QUALITY_ATTRIBUTE("quality_attribute", true)
	/** Grasp rationale element */
	, RATIONALE("rationale", false)
	/** Grasp reason element */
	, REASON("reason", true)
	/** Grasp requirement element */
	, REQUIREMENT("requirement", true)
	/** Grasp requires element */
	, REQUIRES("requires", true)
	/** Grasp system element */
	, SYSTEM("system", false)
	/** Grasp template element */
	, TEMPLATE("template", false)
	;
	
	private String keyword;
	private boolean endsWithSemicolon;
	
	/** Define new element type 
	 *
	 * @param keyword Grasp language keyword for this particular type
	 */
	ElementType(String keyword, boolean endsWithSemicolon) {
		this.keyword = keyword;
		this.endsWithSemicolon = endsWithSemicolon;
	}
	
	/**
	 * Returns the Grasp language keyword for this element
	 * @return Grasp keyword for this element
	 */
	public String getKeyword() {
		return keyword;
	}

	public boolean endsWithSemicolon() {
		return endsWithSemicolon;
	}
}
