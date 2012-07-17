package uk.ac.standrews.grasp.ide.model;

import grasp.lang.IExpression;
import grasp.lang.IValidationContext;

public class GraspExpression extends GraspElement<IExpression> implements IExpression {

	public GraspExpression(IExpression wrapped) {
		super(wrapped);
	}

	@Override
	public void validate(IValidationContext ctx) {
		wrapped.validate(ctx);		
	}

	@Override
	public Object getValue() {
		return wrapped.getValue();
	}

	@Override
	public Object evaluate(IValidationContext ctx) {
		return wrapped.evaluate(ctx);
	}	

}
