package uk.ac.standrews.grasp.ide.model;

import java.util.List;

import grasp.lang.IAnnotation;
import grasp.lang.INamedValue;
import grasp.lang.IValidationContext;

public class GraspAnnotation extends GraspElement<IAnnotation> implements
		IAnnotation {
	public static final String PROP_HANDLER = "handler";

	public GraspAnnotation(IAnnotation wrapped) {
		super(wrapped);
	}

	@Override
	public void validate(IValidationContext ctx) {
		wrapped.validate(ctx);
	}

	@Override
	public String getHandler() {
		return wrapped.getHandler();
	}

	@Override
	public List<INamedValue> getNamedValues() {
		// TODO: investigate if we need to replace
		return wrapped.getNamedValues();
	}

	@Override
	public void setHandler(String s) {
		wrapped.setHandler(s);
		fireElementChanged(PROP_HANDLER);
	}

}
