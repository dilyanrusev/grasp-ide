package uk.ac.standrews.grasp.ide.model;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.core.runtime.AssertionFailedException;

/**
 * Base class for elements that can appear as top-level elements and can have children on their own
 * @author Dilyan Rusev
 *
 */
public abstract class FirstClassModel extends ElementModel {
	/**
	 * ID for the annotations property
	 */
	public static final String PROPERTY_ANNOTATIONS = "annotations";
	/**
	 * ID of the body property
	 */
	public static final String PROPERTY_BODY = "body";
	
	private final ObservableSet<AnnotationModel> annotations = 
			new ObservableSet<AnnotationModel>();
	private final ObservableSet<FirstClassModel> body = 
			new ObservableSet<FirstClassModel>();
	
	/**
	 * Construct a copy of another element
	 * @param other Source
	 * @param parent Parent
	 */
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
	
	/**
	 * Create a new element
	 * @param type Type of the element
	 * @param parent Parent
	 */
	public FirstClassModel(ElementType type, ElementModel parent) {
		super(type, parent);
	}

	/**
	 * Adds an element as a child of this, and also puts the child to this element's symbol table
	 * Use this instead of manually adding children to {@link #getBody()}. 
	 * @param child Element to add as a child
	 */
	public void addChild(FirstClassModel child) {		
		symPut(child.getReferencingName(), child);
		body.add(child);
	}	

	/**
	 * Get this element's annotations
	 * @return 
	 */
	public ObservableSet<AnnotationModel> getAnnotations() {
		return annotations;
	}
	
	/**
	 * Get this element's children
	 * @return
	 */
	public ObservableSet<FirstClassModel> getBody() {
		return body;
	}
	
	/**
	 * Get direct children of this element by type
	 * @param elementtype Type of children to look for
	 * @return
	 */
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
	public ElementModel removeFromParent() {
		if (getParent() instanceof FirstClassModel) {
			FirstClassModel theParent = (FirstClassModel) getParent();
			if (theParent.getBody().remove(this)) {
				return theParent;
			}
		}
		return null;
	}
	
	@Override
	public boolean addChildElement(ElementModel child) {
		if (child instanceof FirstClassModel && this != child) {
			return getBody().add((FirstClassModel) child);
		}
		return false;
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
	
	/**
	 * Helper method that constructs a copy via the copy constructor of another element
	 * @param source Source to be copied
	 * @param parent Parent of the requested copy
	 * @return
	 */
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
