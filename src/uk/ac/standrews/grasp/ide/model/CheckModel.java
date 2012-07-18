package uk.ac.standrews.grasp.ide.model;

import grasp.lang.ICheck;
import grasp.lang.IEvaluable;
import grasp.lang.IFirstClass;
import grasp.lang.IValidationContext;

import org.eclipse.core.runtime.Assert;

import uk.ac.standrews.grasp.ide.Log;

public class CheckModel extends EvaluableModel implements ICheck {
	public CheckModel(ICheck other, IFirstClass parent) {
		super((IEvaluable) other, parent);
	}
	
	public CheckModel(IFirstClass parent) {
		super(ElementType.CHECK, parent);
	}
	
	@Override
	public void validate(IValidationContext ctx) {
		super.validate(ctx);
		Assert.isTrue(isInitialized() && getValue() != null);
		if(!((Boolean)getValue()).booleanValue())
            Log.info("CheckModel.validate -> check failed");
	}
}
