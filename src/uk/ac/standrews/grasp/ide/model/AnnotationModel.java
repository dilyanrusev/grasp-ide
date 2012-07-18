package uk.ac.standrews.grasp.ide.model;

import java.util.List;

import grasp.lang.IAnnotation;
import grasp.lang.IElement;
import grasp.lang.INamedValue;
import grasp.lang.IValidationContext;

public class AnnotationModel extends ElementModel implements IAnnotation {
	public static final String PROPERTY_HANDLER = "handler";
	
	private String handler;
	private List<INamedValue> namedValues = new ObservableList<INamedValue>();
	
	public AnnotationModel(IAnnotation other, IElement parent) {
		super(other, parent);
		handler = other.getHandler();
		for (INamedValue namedValue: other.getNamedValues()) {
			INamedValue observable = new NamedValueModel(namedValue, this);
			namedValues.add(observable);
		}
	}
	
	public AnnotationModel(IElement parent) {
		super(ElementType.ANNOTATION, parent);
	}

	@Override
	public String getHandler() {
		return handler;
	}

	@Override
	public List<INamedValue> getNamedValues() {
		return namedValues;
	}

	@Override
	public void setHandler(String handler) {
		this.handler = handler;
		fireElementChanged(PROPERTY_HANDLER);
	}
	
	@Override
	public void validate(IValidationContext ctx) {
		for (INamedValue child: namedValues) {
			child.validate(ctx);
		}
	}

}
