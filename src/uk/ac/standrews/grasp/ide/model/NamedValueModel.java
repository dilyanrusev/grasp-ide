package uk.ac.standrews.grasp.ide.model;

import org.eclipse.core.runtime.Assert;

public class NamedValueModel extends ElementModel {
	public static final String PROPERTY_EXPRESSION = "expression";
	public static final String PROPERTY_IS_INITIALIZED = "isInitialized";
	public static final String PROPERTY_VALUE = "value";
	
	private ExpressionModel expression;

	public NamedValueModel(NamedValueModel other, AnnotationModel parent) {
		super(other, parent);
		this.expression = new ExpressionModel(other.getExpression(), this);
	}
	
	public NamedValueModel(AnnotationModel parent) {
		super(ElementType.NAMEDVALUE, parent);
	}

	public ExpressionModel getExpression() {
		return expression;
	}

	public Object getValue() {
		Assert.isNotNull(expression);
		return expression.getValue();
	}

	public boolean isInitialized() {
		return expression != null;
	}

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

}
