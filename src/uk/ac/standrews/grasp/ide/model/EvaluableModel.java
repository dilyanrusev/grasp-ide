package uk.ac.standrews.grasp.ide.model;

public abstract class EvaluableModel extends BecauseModel {

	public static final String PROPERTY_EXPRESSION = "expression";
	public static final String PROPERTY_IS_INITIALIZED = "isInitialized";
	public static final String PROPERTY_VALUE = "value";
	
	private ExpressionModel expression;

	public EvaluableModel(EvaluableModel other, FirstClassModel parent) {
		super(other, parent);
		this.expression = new ExpressionModel(other.getExpression(), this);
	}
	
	public EvaluableModel(ElementType type, FirstClassModel parent) {
		super(type, parent);
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

	public void setExpression(ExpressionModel expression) {		
		this.expression = expression;
		fireElementChanged(PROPERTY_EXPRESSION, PROPERTY_IS_INITIALIZED, PROPERTY_VALUE);
	}
	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj)) return false;
		EvaluableModel other = (EvaluableModel) obj;
		
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
