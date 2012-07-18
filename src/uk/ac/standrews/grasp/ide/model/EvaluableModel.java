package uk.ac.standrews.grasp.ide.model;

import grasp.lang.IBecause;
import grasp.lang.IEvaluable;
import grasp.lang.IExpression;
import grasp.lang.IFirstClass;
import grasp.lang.IValidationContext;

import org.eclipse.core.runtime.Assert;

public abstract class EvaluableModel extends BecauseModel implements IEvaluable {

	public static final String PROPERTY_EXPRESSION = "expression";
	public static final String PROPERTY_IS_INITIALIZED = "isInitialized";
	public static final String PROPERTY_VALUE = "value";
	
	private IExpression expression;

	public EvaluableModel(IEvaluable other, IFirstClass parent) {
		super((IBecause)other, parent);
		this.expression = new ExpressionModel(other.getExpression(), this);
	}
	
	public EvaluableModel(ElementType type, IFirstClass parent) {
		super(type, parent);
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
		Assert.isTrue(expression instanceof ExpressionModel);
		this.expression = expression;
		fireElementChanged(PROPERTY_EXPRESSION, PROPERTY_IS_INITIALIZED, PROPERTY_VALUE);
	}
	
	@Override
	public void validate(IValidationContext ctx) {
		super.validate(ctx);
		if (isInitialized())
			expression.validate(ctx);
	}

}
