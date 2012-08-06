package uk.ac.standrews.grasp.ide.model;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.AssertionFailedException;

public abstract class FirstClassModel extends ElementModel {
	private final ObservableSet<AnnotationModel> annotations = 
			new ObservableSet<AnnotationModel>();
	private final ObservableSet<FirstClassModel> body = 
			new ObservableSet<FirstClassModel>();
	
	public FirstClassModel(FirstClassModel other, FirstClassModel parent) {
		super(other, parent);
		for (AnnotationModel annotation: other.getAnnotations()) {
			AnnotationModel copy = new AnnotationModel(annotation, this);
			annotations.add(copy);
		}
		for (FirstClassModel child: other.getBody()) {
			FirstClassModel copy = createCopyOf(child, this);
			addChild(copy);
		}
	}
	
	public FirstClassModel(ElementType type, ElementModel parent) {
		super(type, parent);
	}

	public void addChild(FirstClassModel child) {
		Assert.isTrue(child.getParent() == this
				&& child.getReferencingName() != null);
		symPut(child.getReferencingName(), child);
		body.add(child);
	}	

	public ObservableSet<AnnotationModel> getAnnotations() {
		return annotations;
	}
	
	public ObservableSet<FirstClassModel> getBody() {
		return body;
	}
	
	public Collection<FirstClassModel> getBodyByType(ElementType elementtype) {
		// do not need to observe; should be fast since body is sorted
		Collection<FirstClassModel> ofType = new ArrayList<FirstClassModel>();
		for (FirstClassModel child: body) {
			if (child.getType() == elementtype) {
				ofType.add(child);
			}
		}
		return ofType;
	}	
	
	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj)) return false;
		FirstClassModel other = (FirstClassModel) obj;
		
		// omit body for speed
		// annotations might contain important designer data
		if (!collectionsEqual(getAnnotations(), other.getAnnotations()));
		
		return true;
	}
	
	@Override
	public int hashCode() {
		int result = super.hashCode();
		
		// omit body for speed
		// annotations might contain important designer data
		for (AnnotationModel annotation: getAnnotations()) {
			result = 31 * result + annotation.hashCode();
		}
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	protected static <T extends FirstClassModel> T createCopyOf(T source, FirstClassModel parent) {
		switch (source.getType()) {		
		case CHECK:
			return (T) new CheckModel((CheckModel) source, parent);
		case COMPONENT:
			return (T) new ComponentModel((ComponentModel) source, parent);
		case CONNECTOR:
			return (T) new ConnectorModel((ConnectorModel) source, parent);		
		case LAYER:
			return (T) new LayerModel((LayerModel) source, parent);
		case LINK:
			return (T) new LinkModel((LinkModel) source, parent);		
		case PROPERTY:
			return (T) new PropertyModel((PropertyModel) source, parent);
		case PROVIDES:
			return (T) new ProvidesModel((ProvidesModel) source, parent);
		case QUALITY_ATTRIBUTE:
			return (T) new QualityAttributeModel((QualityAttributeModel) source, parent);
		case RATIONALE:
			return (T) new RationaleModel((RationaleModel) source, parent);
		case REASON:
			return (T) new ReasonModel((ReasonModel) source, parent);
		case REQUIREMENT:
			return (T) new RequirementModel((RequirementModel) source, parent);
		case REQUIRES:
			return (T) new RequiresModel((RequiresModel) source, parent);
		case SYSTEM:
			return (T) new SystemModel((SystemModel) source, (ArchitectureModel) parent);
		case TEMPLATE:
			return (T) new TemplateModel((TemplateModel) source, parent);
		default:
			throw new AssertionFailedException("Cannot create copy of " + source.getType());		
		}		
	}
}
