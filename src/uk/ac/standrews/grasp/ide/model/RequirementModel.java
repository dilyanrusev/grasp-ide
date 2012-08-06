package uk.ac.standrews.grasp.ide.model;

public class RequirementModel extends FirstClassModel {
	public static final String PROPERTY_EXPRESSION = "expression";
	public static final String PROPERTY_VALUE = "value";
	public static final String PROPERTY_IS_INITIALIZED = "isInitialized";
	
	private ExpressionModel expression;
	
	public RequirementModel(RequirementModel other, FirstClassModel parent) {
		super(other, parent);
		expression = new ExpressionModel(other.getExpression(), this);
	}
	
	public RequirementModel(FirstClassModel parent) {
		super(ElementType.REQUIREMENT, parent);
	}

	public ExpressionModel getExpression() {
		return expression;
	}

	public Object getValue() {
		return isInitialized() ? expression.getValue() : null;
	}

	public boolean isInitialized() {
		return expression != null;
	}

	public void setExpression(ExpressionModel iexpression) {
		this.expression = null;
		fireElementChanged(PROPERTY_EXPRESSION, PROPERTY_IS_INITIALIZED, PROPERTY_VALUE);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj)) return false;
		RequirementModel other = (RequirementModel) obj;
		
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
