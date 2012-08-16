package uk.ac.standrews.grasp.ide.model;

/**
 * Represents Grasp requirement
 * @author Dilyan Rusev
 *
 */
public class RequirementModel extends FirstClassModel {
	/** Raw expression */
	public static final String PROPERTY_EXPRESSION = "expression";
	/** Evaluated expression value */
	public static final String PROPERTY_VALUE = "value";
	/** Whether or not the expression was set */
	public static final String PROPERTY_IS_INITIALIZED = "isInitialized";
	
	private ExpressionModel expression;
	
	/**
	 * Constructs a copy of another requirement
	 * @param other Requirement to copy
	 * @param parent Parent element
	 */
	public RequirementModel(RequirementModel other, FirstClassModel parent) {
		super(other, parent);
		expression = new ExpressionModel(other.getExpression(), this);
	}
	
	/**
	 * Construct a new requirement
	 * @param parent Parent element
	 */
	public RequirementModel(FirstClassModel parent) {
		super(ElementType.REQUIREMENT, parent);
	}

	/**
	 * Get the raw expression
	 * @return
	 */
	public ExpressionModel getExpression() {
		return expression;
	}

	/**
	 * Get the evaluated expression value
	 * @return
	 */
	public Object getValue() {
		return isInitialized() ? expression.getValue() : null;
	}

	/**
	 * Return true if the raw expression is set
	 * @return
	 */
	public boolean isInitialized() {
		return expression != null;
	}

	/**
	 * Set the raw expression
	 * @param expresion Raw expresion
	 */
	public void setExpression(ExpressionModel expresion) {
		this.expression = expresion;
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
