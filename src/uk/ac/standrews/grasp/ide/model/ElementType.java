package uk.ac.standrews.grasp.ide.model;

/**
 * Types of Grasp elements
 * @author Dilyan Rusev
 *
 */
public enum ElementType {
	/** Grasp annotation element */
	ANNOTATION
	/** Grasp architecture element */
	, ARCHITECTURE
	/** Grasp check statement */
	, CHECK
	/** Grasp component element */
	, COMPONENT
	/** Grasp connector element */
	, CONNECTOR
	/** Grasp expression statement */
	, EXPRESSION
	/** Grasp layer element */
	, LAYER
	/** Grasp link statement */
	, LINK
	/** Grasp named value element */
	, NAMEDVALUE
	/** Grasp property element */
	, PROPERTY
	/** Grasp provides element */
	, PROVIDES
	/** Grasp quality_attribute element */
	, QUALITY_ATTRIBUTE
	/** Grasp rationale element */
	, RATIONALE
	/** Grasp reason element */
	, REASON
	/** Grasp requirement element */
	, REQUIREMENT
	/** Grasp requires element */
	, REQUIRES
	/** Grasp system element */
	, SYSTEM
	/** Grasp template element */
	, TEMPLATE
}
