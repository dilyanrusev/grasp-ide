package uk.ac.standrews.grasp.ide.model;

import org.eclipse.core.runtime.Assert;

import grasp.lang.IElement;
import grasp.lang.IExpression;
import grasp.lang.INamedValue;
import grasp.lang.IValidationContext;

public class NamedValueModel extends ElementModel implements INamedValue {
	public static final String PROPERTY_EXPRESSION = "expression";
	public static final String PROPERTY_IS_INITIALIZED = "isInitialized";
	public static final String PROPERTY_VALUE = "value";
	
	private IExpression expression;

	public NamedValueModel(INamedValue other, IElement parent) {
		super(other, parent);
		this.expression = new ExpressionModel(other.getExpression(), this);
	}
	
	public NamedValueModel(IElement parent) {
		super(ElementType.NAMEDVALUE, parent);
	}

	@Override
	public IExpression getExpression() {
		return expression;
	}

	@Override
	public Object getValue() {
		Assert.isNotNull(expression);
		return expression.getValue();
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
		Assert.isNotNull(expression);
		expression.validate(ctx);
	}

}