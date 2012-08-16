package uk.ac.standrews.grasp.ide.model;

/**
 * Base class for Grasp elements that contain expressions that can be evaluated
 * @author Dilyan Rusev
 *
 */
public abstract class EvaluableModel extends BecauseModel {

	/** Expression */
	public static final String PROPERTY_EXPRESSION = "expression";
	/** Whether Expression can be accessed */
	public static final String PROPERTY_IS_INITIALIZED = "isInitialized";
	/** Evaluated value of the expression */
	public static final String PROPERTY_VALUE = "value";
	
	private ExpressionModel expression;

	/**
	 * Construct a copy of another element
	 * @param other Source
	 * @param parent Parent element
	 */
	public EvaluableModel(EvaluableModel other, FirstClassModel parent) {
		super(other, parent);
		this.expression = new ExpressionModel(other.getExpression(), this);
	}
	
	/**
	 * Construct a new element
	 * @param type Type of the element
	 * @param parent Parent element
	 */
	public EvaluableModel(ElementType type, FirstClassModel parent) {
		super(type, parent);
	}

	/**
	 * Get the raw expression
	 * @return
	 */
	public ExpressionModel getExpression() {
		return expression;
	}

	/**
	 * Get the evaluated value of the expression
	 * @return
	 */
	public Object getValue() {
		return isInitialized() ? expression.getValue() : null;
	}

	/**
	 * Return true if the element contains an expression
	 * @return
	 */
	public boolean isInitialized() {
		return expression != null;
	}

	/**
	 * Set the raw expression
	 * @param expression
	 */
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
