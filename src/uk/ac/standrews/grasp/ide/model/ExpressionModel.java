package uk.ac.standrews.grasp.ide.model;

import uk.ac.standrews.grasp.ide.Log;

/**
 * Common class for Grasp expressions
 * @author Dilyan Rusev
 *
 */
public class ExpressionModel extends ElementModel {
	/**
	 * Value of the evaluated expression
	 */
	public static final String PROPERTY_VALUE = "value";
	/**
	 * Type of the expression
	 */
	public static final String PROPERTY_EXPRESSION_TYPE = "type";
	/**
	 * Text from which the expression was evaluated
	 */
	public static final String PROPERTY_TEXT = "text";	
	
	private Object value;
	private String text;
	private ExpressionType expressionType;
	
	/**
	 * Construct a new expression
	 * @param parent Object that contains this expression
	 */
	public ExpressionModel(ElementModel parent) {
		super(ElementType.EXPRESSION, parent);
	}
	
	/**
	 * Construct a copy of another expression
	 * @param other Source
	 * @param parent Parent of the copy
	 */
	public ExpressionModel(ExpressionModel other, ElementModel parent) {
		super(other, parent);
		this.value = other.getValue();
		this.text = other.getText();
		this.expressionType = other.getExpressionType();
	}

	/**
	 * Return the evaluated expression
	 * @return evaluated expression
	 */
	public Object getValue() {
		return value;
	}
	
	/**
	 * Return the expression type
	 * @return Type of the expression
	 */
	public ExpressionType getExpressionType() {
		return expressionType;
	}
	
	/**
	 * Retrieve the text which produced this expression
	 * @return text which produced this expression
	 */
	public String getText() {
		return text;
	}

	/**
	 * Set the expression's evaluated value
	 * @param value Evaluated value
	 */
	public void setValue(Object value) {
		this.value = value;
		this.fireElementChanged(PROPERTY_VALUE);
	}
	
	/**
	 * Set the expression type
	 * @param type Type of the expression
	 */
	public void setExpressionType(ExpressionType type) {
		this.expressionType = type;
		this.fireElementChanged(PROPERTY_EXPRESSION_TYPE);
	}
	
	/**
	 * Set the text from which this expression originates
	 * @param text text from which this expression originates
	 */
	public void setText(String text) {
		this.text = text;
		this.fireElementChanged(PROPERTY_TEXT);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!(obj instanceof ExpressionModel)) return false;
		ExpressionModel other = (ExpressionModel) obj;
		
		if (!objectsEqual(getValue(), other.getValue())) return false;
		
		return true;
	}
	
	@Override
	public int hashCode() {
		int result = 17;
		
		result = 31 * result + (getValue() != null ? getValue().hashCode() : 0);
		
		return result;
	}

	@Override
	public ElementModel removeFromParent() {
		Log.warn("Expreession.removeFromParent called");
		return null;
	}

	@Override
	public boolean addChildElement(ElementModel child) {
		Log.warn("Expression.addChildElement called");
		return false;
	}
}
