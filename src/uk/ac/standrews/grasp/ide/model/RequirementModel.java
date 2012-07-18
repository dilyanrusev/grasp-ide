package uk.ac.standrews.grasp.ide.model;

import grasp.lang.IFirstClass;
import grasp.lang.IExpression;
import grasp.lang.IRequirement;
import grasp.lang.IValidationContext;

public class RequirementModel extends FirstClassModel implements IRequirement {
	public static final String PROPERTY_EXPRESSION = "expression";
	public static final String PROPERTY_VALUE = "value";
	public static final String PROPERTY_IS_INITIALIZED = "isInitialized";
	
	private IExpression expression;
	
	public RequirementModel(IRequirement other, IFirstClass parent) {
		super(other, parent);
		expression = new ExpressionModel(other.getExpression(), this);
	}
	
	public RequirementModel(IFirstClass parent) {
		super(ElementType.REQUIREMENT, parent);
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
	public void setExpression(IExpression iexpression) {
		this.expression = null;
		fireElementChanged(PROPERTY_EXPRESSION, PROPERTY_IS_INITIALIZED, PROPERTY_VALUE);
	}
	
	@Override
	public void validate(IValidationContext ctx) {
		super.validate(ctx);
		if(isInitialized())
            expression.validate(ctx);
	}

}
