package uk.ac.standrews.grasp.ide.model;

public class ReasonModel extends FirstClassModel {
	public static final String PROPERTY_EXPRESSION = "expression";
	public static final String PROPERTY_IS_INITIALIZED = "isInitialized";
	public static final String PROPERTY_VALUE = "value";
	
	private final ObservableSet<FirstClassModel> inhibits = new ObservableSet<FirstClassModel>();
	private final ObservableSet<FirstClassModel> supports = new ObservableSet<FirstClassModel>();
	private ExpressionModel expression;
	
	public ReasonModel(ReasonModel other, FirstClassModel parent) {
		super(other, parent);
		copyCollectionAtTheEndOfCopy(other.getInhibits(), inhibits);
		copyCollectionAtTheEndOfCopy(other.getSupports(), supports);
		if (other.getExpression() != null) {
			expression = new ExpressionModel(other.getExpression(), this);
		}
	}
	
	public ReasonModel(FirstClassModel parent) {
		super(ElementType.REASON, parent);
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
		this.fireElementChanged(PROPERTY_EXPRESSION, PROPERTY_IS_INITIALIZED, PROPERTY_VALUE);
	}

	public ObservableSet<FirstClassModel> getSupports() {
		return supports;
	}

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
