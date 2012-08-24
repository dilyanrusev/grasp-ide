package uk.ac.standrews.grasp.ide.model;

/**
 * Represents Grasp reason elements which support rationales
 * @author Dilyan Rusev
 *
 */
public class ReasonModel extends FirstClassModel {
	/** Raw expression */
	public static final String PROPERTY_EXPRESSION = "expression";
	/** Expression can be accessed */
	public static final String PROPERTY_IS_INITIALIZED = "isInitialized";
	/** Evaluated value of the expression */
	public static final String PROPERTY_VALUE = "value";
	
	private final ObservableSet<FirstClassModel> inhibits = new ObservableSet<FirstClassModel>();
	private final ObservableSet<FirstClassModel> supports = new ObservableSet<FirstClassModel>();
	private ExpressionModel expression;
	
	/**
	 * Creates a copy of another reason
	 * @param other Reason to copy
	 * @param parent Parent element
	 */
	public ReasonModel(ReasonModel other, FirstClassModel parent) {
		super(other, parent);
		copyCollectionAtTheEndOfCopy(other.getInhibits(), inhibits);
		copyCollectionAtTheEndOfCopy(other.getSupports(), supports);
		if (other.getExpression() != null) {
			expression = new ExpressionModel(other.getExpression(), this);
		}
	}
	
	/**
	 * Construct a new reason
	 * @param parent Parent element
	 */
	public ReasonModel(FirstClassModel parent) {
		super(ElementType.REASON, parent);
	}

	/**
	 * Get the raw expression that explains this reason. 
	 * @return Raw expression
	 */
	public ExpressionModel getExpression() {
		return expression;
	}

	/**
	 * Result of the evaluated expression
	 * @return Evaluated expression
	 */
	public Object getValue() {
		return isInitialized() ? expression.getValue() : null;
	}

	/**
	 * Returns whether or not the expressions has been set
	 * @return Raw expression != null
	 */
	public boolean isInitialized() {
		return expression != null;
	}

	/**
	 * Set the raw expression supporting this reason
	 * @param expression Raw expression
	 */
	public void setExpression(ExpressionModel expression) {
		this.expression = expression;
		this.fireElementChanged(PROPERTY_EXPRESSION, PROPERTY_IS_INITIALIZED, PROPERTY_VALUE);
	}

	/**
	 * Get a list of references to elements supporting this reason
	 * @return Supporters
	 */
	public ObservableSet<FirstClassModel> getSupports() {
		return supports;
	}

	/**
	 * Get a list of references to elements inhibiting this reason
	 * @return Inhibitors
	 */
	public ObservableSet<FirstClassModel> getInhibits() {
		return inhibits;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj)) return false;
		ReasonModel other = (ReasonModel) obj;
		
		if (isInitialized() != other.isInitialized()) return false;
		if (!objectsEqual(getExpression(), other.getExpression())) return false;
		if (!collectionsEqual(getInhibits(), other.getInhibits())) return false;
		if (!collectionsEqual(getSupports(), other.getSupports())) return false;
		
		return true;
	}
	
	@Override
	public int hashCode() {
		int result = super.hashCode();
		
		result = 31 * result + (isInitialized() ? 1 : 0);
		result = 31 * result + (getExpression() != null ? getExpression().hashCode() : 0);
		for (FirstClassModel inhibited: getInhibits()) {
			result = 31 * result + inhibited.hashCode();
		}
		for (FirstClassModel supported: getSupports()) {
			result = 31 * result + supported.hashCode();
		}
		
		return result;
	}

}
