package uk.ac.standrews.grasp.ide.model;

import grasp.lang.IElement;
import grasp.lang.IExpression;
import grasp.lang.IValidationContext;

public class ExpressionModel extends ElementModel implements IExpression {
	private Object value;
	
	public ExpressionModel(IElement parent) {
		super(ElementType.EXPRESSION, parent);
	}
	
	public ExpressionModel(IExpression other, IElement parent) {
		super(other, parent);
		this.value = other.getValue();
	}

	@Override
	public Object evaluate(IValidationContext ctx) {
		if (value == null) {
			value = Boolean.TRUE;
		}
		return value;
	}

	@Override
	public Object getValue() {
		return value;
	}
	
	@Override
	public void validate(IValidationContext ctx) {
		evaluate(ctx);
	}

}
