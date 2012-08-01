package uk.ac.standrews.grasp.ide.model;

import java.util.List;

import grasp.lang.IExpression;
import grasp.lang.IFirstClass;
import grasp.lang.IReason;
import grasp.lang.IValidationContext;

public class ReasonModel extends FirstClassModel implements IReason {
	public static final String PROPERTY_EXPRESSION = "expression";
	public static final String PROPERTY_IS_INITIALIZED = "isInitialized";
	public static final String PROPERTY_VALUE = "value";
	
	private final List<IFirstClass> inhibits = new ObservableList<IFirstClass>();
	private final List<IFirstClass> supports = new ObservableList<IFirstClass>();
	private IExpression expression;
	
	public ReasonModel(IReason other, IFirstClass parent) {
		super(other, parent);
		for (IFirstClass rule: other.getInhibits()) {
			IFirstClass observable = (IFirstClass) GraspModel.INSTANCE.makeObservable(rule, this);
			inhibits.add(observable);
		}
		for (IFirstClass rule: other.getSupports()) {
			IFirstClass observable = (IFirstClass) GraspModel.INSTANCE.makeObservable(rule, this);
			supports.add(observable);
		}
		if (other.getExpression() != null) {
			expression = new ExpressionModel(other.getExpression(), this);
		}
	}
	
	public ReasonModel(IFirstClass parent) {
		super(ElementType.REASON, parent);
	}

	@Override
	public IExpression getExpression() {
		return expression;
	}

	@Override
	public Object getValue() {
		return isInitialized() ? expression.getValue() : null;
	}

	@Override
	public boolean isInitialized() {
		return expression != null;
	}

	@Override
	public void setExpression(IExpression expression) {
		this.expression = expression;
		this.fireElementChanged(PROPERTY_EXPRESSION, PROPERTY_IS_INITIALIZED, PROPERTY_VALUE);
	}

	@Override
	public List<IFirstClass> getSupports() {
		return supports;
	}

	@Override
	public List<IFirstClass> getInhibits() {
		return inhibits;
	}
	
	@Override
	public void validate(IValidationContext ctx) {
		super.validate(ctx);
		if (isInitialized()) {
			expression.validate(ctx);
		}
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
		for (IFirstClass inhibited: getInhibits()) {
			result = 31 * result + inhibited.hashCode();
		}
		for (IFirstClass supported: getSupports()) {
			result = 31 * result + supported.hashCode();
		}
		
		return result;
	}

}
