package uk.ac.standrews.grasp.ide.model;

import grasp.lang.IExpression;
import grasp.lang.INamedValue;
import grasp.lang.IValidationContext;

public class GraspNamedValue extends GraspElement<INamedValue> implements INamedValue {
	public static final String PROP_EXPRESSION = "expression";

	public GraspNamedValue(INamedValue wrapped) {
		super(wrapped);
	}

	@Override
	public void validate(IValidationContext context) {
		wrapped.validate(context);		
	}

	@Override
	public boolean isInitialized() {
		return wrapped.isInitialized();
	}

	@Override
	public Object getValue() {
		return wrapped.getValue();
	}

	@Override
	public IExpression getExpression() {
		return wrapped.getExpression();
	}

	@Override
	public void setExpression(IExpression expr) {
		wrapped.setExpression(expr);
		fireElementChanged(PROP_EXPRESSION);		
	}
	
}
