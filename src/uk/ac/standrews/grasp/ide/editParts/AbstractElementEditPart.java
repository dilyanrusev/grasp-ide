package uk.ac.standrews.grasp.ide.editParts;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

import uk.ac.standrews.grasp.ide.model.AnnotationModel;
import uk.ac.standrews.grasp.ide.model.CollectionChangedEvent;
import uk.ac.standrews.grasp.ide.model.ElementChangedEvent;
import uk.ac.standrews.grasp.ide.model.ElementModel;
import uk.ac.standrews.grasp.ide.model.FirstClassModel;
import uk.ac.standrews.grasp.ide.model.ICollectionChangedListener;
import uk.ac.standrews.grasp.ide.model.IElementChangedListener;

/**
 * Base class for parts that have Grasp model
 * @author Dilyan Rusev
 *
 * @param <TModel> Type of the Grasp model this part is bound to
 */
public abstract class AbstractElementEditPart<TModel extends FirstClassModel>
		extends AbstractGraphicalEditPart
		implements IElementChangedListener {

	private ICollectionChangedListener<AnnotationModel> annotationChangedListener;
	private ICollectionChangedListener<FirstClassModel> childElementsChangedListener;
	private IElementChangedListener annotationModifiedListener;
	private List<FirstClassModel> supportedChildren;	

	/**
	 * Construct a new part
	 * @param model Model to bind to
	 */
	public AbstractElementEditPart(TModel model) {
		super();
		supportedChildren = new ArrayList<FirstClassModel>();
		setModel(model);		
		updateSupportedChildren();		
	}

	@Override
	public void activate() {
		super.activate();
		annotationChangedListener = new AnnotationsChangedListener();
		annotationModifiedListener = new AnnotationModifiedListener();
		childElementsChangedListener = new ChildElementsChangedListener();		
		getElement().addElementChangedListener(this);	
		getElement().getAnnotations().addCollectionChangeListener(annotationChangedListener);		
		getElement().getBody().addCollectionChangeListener(childElementsChangedListener);
		for (AnnotationModel annotation: getElement().getAnnotations()) {
			annotation.addElementChangedListener(annotationModifiedListener);
		}
	}

	@Override
	public void deactivate() {		
		getElement().removeElementChangedListener(this);
		getElement().getAnnotations().removeCollectionChangeListener(annotationChangedListener);
		getElement().getBody().removeCollectionChangeListener(childElementsChangedListener);
		for (AnnotationModel annotation: getElement().getAnnotations()) {
			annotation.removeElementChangedListener(annotationModifiedListener);
		}
		annotationChangedListener = new AnnotationsChangedListener();
		annotationModifiedListener = new AnnotationModifiedListener();
		childElementsChangedListener = new ChildElementsChangedListener();
		supportedChildren = new ArrayList<FirstClassModel>();
		super.deactivate();
	}
	
	/**
	 * Override to override changes to the model. Calls {@link #refreshVisuals()} by default.
	 * 
	 * @see #elementPropertyChanged(String)
	 * @see #annotationModified(ElementChangedEvent)
	 */
	@Override
	public void elementChanged(ElementChangedEvent event) {		
		for (String propertyName: event.getPropertyNames()) {
			elementPropertyChanged(propertyName);
		}
		refreshVisuals();
	}
	
	/**
	 * Override to react when an annotation that belongs to this element has changed.
	 * No need to call {@link #refreshVisuals()}
	 * @param event Describes the properties that have changed
	 */
	protected void annotationModified(ElementChangedEvent event) {		
	}
	
	/**
	 * Override to react when a property of the model has changed.
	 * No need to call {@link #refreshVisuals()}
	 * @param propertyName Name of the changed property
	 */
	protected void elementPropertyChanged(String propertyName) {
		System.out.printf("%s changed in %s%n", propertyName, getElement().getQualifiedName());
	}

	/**
	 * Override to react when an annotation has been added or removed to the model. 
	 * By default, calls {@link #refreshVisuals()}
	 * @param event Change details
	 */
	protected void annotationsChanged(CollectionChangedEvent<AnnotationModel> event) {
		for (AnnotationModel added: event.getAdded()) {
			added.addElementChangedListener(annotationModifiedListener);			
		}
		for (AnnotationModel removed: event.getRemoved()) {
			removed.removeElementChangedListener(annotationModifiedListener);
		}
		refreshVisuals();
	}

	/**
	 * Override to read when the elements are added or removed as children to the model. 
	 * By default, 
	 * @param event Change details
	 */
	protected void childElementsChanged(CollectionChangedEvent<FirstClassModel> event) {
		updateSupportedChildren();
		refreshChildren();
	}

	/**
	 * Returns the value of {@link #getModel()} cast to {@link TModel}
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public TModel getElement() {
		return (TModel) getModel();
	}

	@Override
	protected List<? extends FirstClassModel> getModelChildren() {		
		return supportedChildren;
	}
	
	private void updateSupportedChildren() {
		getElement().getBody().resort();		
		supportedChildren.clear();
		for (FirstClassModel childModel: getElement().getBody()) {
			if (isModelChildSupported(childModel)) {
				supportedChildren.add(childModel);
			}
		}
	}
	
	/**
	 * Helper method that extracts element name to display from name, alias and referencing name
	 * @param element Model whose name to extract
	 * @return "Name", "Name Alias" or "ReferencingName", in that order, whichever is available 
	 */
	protected static String getDisplayName(ElementModel element) {
		StringBuilder name = new StringBuilder();
		if (element.getName() != null && element.getAlias() == null) {
			name.append(element.getName());
		} else if (element.getName() != null && element.getAlias() != null) {
			name.append(element.getName());
			name.append(' ');
			name.append(element.getAlias());
		} else {
			name.append(element.getReferencingName());
		}
		
		return name.toString();
	}
	
	/**
	 * Used to filter getElement().getBody(), so that {@link #getModelChildren()} returns only elements that have edit parts
	 * @param child Child to filter
	 * @return True if the model has edit part and can be displayed as child of this, false otherwise
	 */
	protected abstract boolean isModelChildSupported(FirstClassModel child);
	
	private final class AnnotationsChangedListener implements ICollectionChangedListener<AnnotationModel> {
		@Override
		public void collectionChanged(CollectionChangedEvent<AnnotationModel> event) {
			annotationsChanged(event);
		}
	}
	
	private final class ChildElementsChangedListener implements ICollectionChangedListener<FirstClassModel> {
		@Override
		public void collectionChanged(CollectionChangedEvent<FirstClassModel> event) {
			childElementsChanged(event);
		}
	}
	
	private final class AnnotationModifiedListener implements IElementChangedListener {
		@Override
		public void elementChanged(ElementChangedEvent event) {
			annotationModified(event);
		}
	}
}