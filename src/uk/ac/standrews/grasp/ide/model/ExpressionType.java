package uk.ac.standrews.grasp.ide.model;

import uk.ac.standrews.grasp.ide.Log;

/**
 * Defines the type of Grasp expressions
 * @author Dilyan Rusev
 *
 */
public enum ExpressionType {
	/** boolean expression */
	BOOLEAN,
	/** integer expression (hex or decimal) */
	INTEGER,
	/** real (floating point) expression */
	REAL,
	/** a set of values */
	SET,
	/** string expression */
	STRING
	;
	
	/**
	 * Attempt to parse an XML attribute to determine the expressoin type
	 * @param val Attribute value
	 * @return Expression type or {@link #STRING} on failure
	 */
	public static ExpressionType parseXmlAttribute(String val) {
		if ("boolean".equalsIgnoreCase(val)) {
			return BOOLEAN;
		} else if ("integer".equalsIgnoreCase(val)) {
			return INTEGER;
		} else if ("real".equalsIgnoreCase(val)) {
			return REAL;			
		} else if ("set".equalsIgnoreCase(val)) {
			return SET;
		} else if ("string".equalsIgnoreCase(val)) {
			return STRING;
		}
		
		Log.warn("ExpressionType.parseXmlAttribute defaults to STRING");
		// default to the safes choice
		return STRING;
	}
}
