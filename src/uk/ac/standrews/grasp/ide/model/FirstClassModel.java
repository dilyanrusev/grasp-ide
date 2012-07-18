package uk.ac.standrews.grasp.ide.model;

import grasp.lang.IAnnotation;
import grasp.lang.IElement;
import grasp.lang.IFirstClass;
import grasp.lang.IValidationContext;
import grasp.util.misc.IMultiMap;

import java.util.Collection;

import org.eclipse.core.runtime.Assert;

public abstract class FirstClassModel extends ElementModel implements IFirstClass {
	public static final String PROPERTY_CHILD = "addChild";
	
	private final Collection<IAnnotation> annotations = 
			new ObservableList<IAnnotation>();
	private final IMultiMap<ElementType, IFirstClass> body = 
			new ObservableMultiHaspMap<IElement.ElementType, IFirstClass>();
	
	public FirstClassModel(IFirstClass other, IFirstClass parent) {
		super(other, parent);
		for (IAnnotation annotation: other.getAnnotations()) {
			IAnnotation observable = new AnnotationModel(annotation, this);
			annotations.add(observable);
		}
		for (IFirstClass child: other.getBody()) {
			IFirstClass observable = (IFirstClass)GraspModel.INSTANCE.makeObservable(child, this);
			addChild(observable, false);
		}
	}
	
	public FirstClassModel(ElementType type, IElement parent) {
		super(type, parent);
	}

	@Override
	public void addChild(IFirstClass child) {
		addChild(child, true);
	}
	
	private void addChild(IFirstClass child, boolean fireEvent) {
		Assert.isTrue(child instanceof FirstClassModel
				&& child.getParent() == this
				&& child.getReferencingName() != null);
		symPut(child.getReferencingName(), child);
		body.put(child.getType(), child);
		if (fireEvent) {
			fireElementChanged(PROPERTY_CHILD);
		}
	}

	@Override
	public Collection<IAnnotation> getAnnotations() {
		return annotations;
	}

	@Override
	public Collection<IFirstClass> getBody() {
		return body.values();
	}

	@Override
	public Collection<IFirstClass> getBodyByType(ElementType elementtype) {
		return body.getAll(elementtype);
	}
	
	@Override
	public void validate(IValidationContext ctx) {
		for (IAnnotation annotation: annotations) {
			annotation.validate(ctx);
		}
		for (IFirstClass child: body.values()) {
			child.validate(ctx);
		}
	}
}
