package uk.ac.standrews.grasp.ide.model;

import org.eclipse.core.runtime.Assert;

/**
 * Element that contains the values of annotations
 * @author Dilyan Rusev
 *
 */
public class NamedValueModel extends ElementModel {
	/** Expression containing the value */
	public static final String PROPERTY_EXPRESSION = "expression";
	/** Whether the expression is evaluated or not */
	public static final String PROPERTY_IS_INITIALIZED = "isInitialized";
	/** Value of the property */
	public static final String PROPERTY_VALUE = "value";
	
	private ExpressionModel expression;

	/**
	 * Construct a copy of another named value pair
	 * @param other Named value pair to copy
	 * @param parent Annotation to which this belongs
	 */
	public NamedValueModel(NamedValueModel other, AnnotationModel parent) {
		super(other, parent);
		this.expression = new ExpressionModel(other.getExpression(), this);
	}
	
	/**
	 * Create a new named value pair
	 * @param parent Annotation containing this named value pair
	 */
	public NamedValueModel(AnnotationModel parent) {
		super(ElementType.NAMEDVALUE, parent);
	}

	/**
	 * Retrieve the expression which defines the value of this named value pair
	 * @return
	 */
	public ExpressionModel getExpression() {
		return expression;
	}

	/**
	 * Retrieve the value of this named value pair
	 * @return
	 */
	public Object getValue() {
		Assert.isNotNull(expression);
		return expression.getValue();
	}

	/**
	 * Determine whether the expression was evaluated. Do not call {@link #getValue()} unless this is true
	 * @return
	 */
	public boolean isInitialized() {
		return expression != null;
	}

	/**
	 * Set the value of this named value pair
	 * @param expression Expression from which the value will be extracted
	 */
	public void setExpression(ExpressionModel expression) {
		this.expression = expression;
		fireElementChanged(PROPERTY_EXPRESSION, PROPERTY_IS_INITIALIZED, PROPERTY_VALUE);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj)) return false;
		NamedValueModel other = (NamedValueModel) obj;
		
		if (isInitialized() != other.isInitialized()) return false;
		if (!objectsEqual(getExpression(), other.getExpression())) return false;
		
		return true;
	}
	
	@Override
	public int hashCode() {
		int result = super.hashCode();
		
		result = 31 * result + (isInitialized() ? 1 : 0);
		result = 31 * result + (getExpression() != null ? getExpression().hashCode() : 0);
		
		return result;
	}

	@Override
	public ElementModel removeFromParent() {
		if (getParent() instanceof AnnotationModel) {
			AnnotationModel theParent = (AnnotationModel) getParent();
			if (theParent.getNamedValues().remove(this)) {
				return theParent;
			}
		}
		return null;
	}

	@Override
	public boolean addChildElement(ElementModel child) {
		if (child instanceof ExpressionModel) {
			setExpression((ExpressionModel) child);
			return true;
		}
		return false;
	}
}
