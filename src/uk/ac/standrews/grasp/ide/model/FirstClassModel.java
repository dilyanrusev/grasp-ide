package uk.ac.standrews.grasp.ide.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

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
	
	protected static final String DESIGNER_ANNOTATION_NAME = "GraspIDE";
	
	private final ObservableSet<AnnotationModel> annotations = 
			new ObservableSet<AnnotationModel>();
	private final ObservableSet<FirstClassModel> body = 
			new ObservableSet<FirstClassModel>();
	private final Set<FirstClassModel> removedChildren =
			new HashSet<FirstClassModel>();
	
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
	 * Removes a child both as a child and from the symbol table
	 * @param child Child to remove
	 */
	public void removeChild(FirstClassModel child) {
		symRemove(child.getReferencingName());
		body.remove(child);
	}

	/**
	 * Get this element's annotations
	 * @return 
	 */
	public ObservableSet<AnnotationModel> getAnnotations() {
		return annotations;
	}
	
	protected String getDesignerValue(String key) {
		NamedValueModel nv = getDesignerNamedValue(key, false);
		if (nv != null && nv.isInitialized() 
				&& nv.getExpression().getExpressionType() == ExpressionType.STRING) {
			return (String) nv.getExpression().getValue();
		}
		return null;
	}
	
	protected void setDesignerValue(String key, String value) {
		NamedValueModel nv = getDesignerNamedValue(key, true);
		ExpressionModel expr = new ExpressionModel(nv);
		expr.setExpressionType(ExpressionType.STRING);
		expr.setValue(value);
		nv.setExpression(expr);		
	}
	
	private NamedValueModel getDesignerNamedValue(String key, boolean create) {
		AnnotationModel annot = getDesignerAnnotation(create);		
		if (annot != null) {
			NamedValueModel namedValue = null;
			for (NamedValueModel nv: annot.getNamedValues()) {
				if (key.equals(nv.getName())) {
					namedValue = nv;
				}
			}
			if (namedValue == null && create) {
				namedValue = new NamedValueModel(annot);
				namedValue.setName(key);
				annot.getNamedValues().add(namedValue);
			}
			return namedValue;
		}
		return null;
	}
	
	private AnnotationModel getDesignerAnnotation(boolean create) {
		for (AnnotationModel annot: getAnnotations()) {
			if (DESIGNER_ANNOTATION_NAME.equals(annot.getName())) {
				return annot;
			}
		}
		if (create) {
			AnnotationModel annot = new AnnotationModel(this);
			annot.setName(DESIGNER_ANNOTATION_NAME);
			getAnnotations().add(annot);
			return annot;
		} else {
			return null;
		}
		
	}
	
	/**
	 * Get this element's children
	 * @return
	 */
	public ObservableSet<FirstClassModel> getBody() {
		return body;
	}
	
	/**
	 * Return a list of children removed by the designer, to be restored when re-added. Provides support for undo and redo
	 * @return
	 */
	Set<FirstClassModel> getRemovedChildren() {
		return removedChildren;
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
			if (theParent.symLookup(this.getReferencingName())) {
				for (FirstClassModel child: getBody()) {
					removedChildren.add(child);
					child.removeFromParent();
				}
				theParent.removeChild(this);
				return theParent;
			}
			
		}
		return null;
	}
	
	@Override
	public boolean addChildElement(ElementModel child) {
		if (child instanceof InstantiableModel) {
			InstantiableModel inst = (InstantiableModel) child;
			FirstClassModel templateParent = (FirstClassModel) inst.getBase().getParent();
			if (!templateParent.symLookup(inst.getBase().getReferencingName())) {
				templateParent.addChild(inst.getBase());
			}
		}
		if (child instanceof LinkModel) {
			LinkModel link = (LinkModel) child;
			link.getProvider().getConnections().add(link);
			link.getConsumer().getConnections().add(link);
		}
		if (child instanceof FirstClassModel && this != child && !symLookup(child.getReferencingName())) {
			FirstClassModel fcChild = (FirstClassModel) child;
			addChild(fcChild);
			for (FirstClassModel removed: fcChild.getRemovedChildren()) {
				fcChild.addChildElement(removed);
			}
			fcChild.getRemovedChildren().clear();
			return true;
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
