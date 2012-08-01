package uk.ac.standrews.grasp.ide.model;

import grasp.lang.IElement;
import grasp.lang.IExpression;
import grasp.lang.IValidationContext;

public class ExpressionModel extends ElementModel implements IExpression {
	private Object value;
	
	public ExpressionModel(IElement parent) {
		super(ElementType.EXPRESSION, parent);
	}
	
	public static ExpressionModel createLiteral(IElement parent, Object value) {
		ExpressionModel literal = new ExpressionModel(parent);
		literal.value = value;
		return literal;
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
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!(obj instanceof ExpressionModel)) return false;
		ExpressionModel other = (ExpressionModel) obj;
		
		if (!objectsEqual(getValue(), other.getValue())) return false;
		
		return true;
	}
	
	@Override
	public int hashCode() {
		int result = 17;
		
		result = 31 * result + (getValue() != null ? getValue().hashCode() : 0);
		
		return result;
	}

}
