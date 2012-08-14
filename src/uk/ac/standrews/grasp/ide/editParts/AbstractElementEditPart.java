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

public abstract class AbstractElementEditPart<TModel extends FirstClassModel>
		extends AbstractGraphicalEditPart
		implements IElementChangedListener {

	private ICollectionChangedListener<AnnotationModel> annotationChangedListener;
	private ICollectionChangedListener<FirstClassModel> childElementsChangedListener;
	private List<FirstClassModel> supportedChildren;	

	public AbstractElementEditPart(TModel model) {
		super();
		setModel(model);
		annotationChangedListener = new AnnotationsChangedListener();
		childElementsChangedListener = new ChildElementsChangedListener();
		supportedChildren = new ArrayList<FirstClassModel>();
		updateSupportedChildren();
	}

	@Override
	public void activate() {
		super.activate();
		getElement().addElementChangedListener(this);	
		getElement().getAnnotations().addCollectionChangeListener(annotationChangedListener);		
		getElement().getBody().addCollectionChangeListener(childElementsChangedListener);
	}

	@Override
	public void deactivate() {
		super.deactivate();
		getElement().removeElementChangedListener(this);
		getElement().getAnnotations().removeCollectionChangeListener(annotationChangedListener);
		getElement().getBody().removeCollectionChangeListener(childElementsChangedListener);
	}

	@Override
	public void elementChanged(ElementChangedEvent event) {		
		for (String propertyName: event.getPropertyNames()) {
			elementPropertyChanged(propertyName);
		}
	}
	
	protected void elementPropertyChanged(String propertyName) {
		refresh();
	}

	protected void annotationsChanged(CollectionChangedEvent<AnnotationModel> event) {
		refresh();
	}

	protected void childElementsChanged(CollectionChangedEvent<FirstClassModel> event) {
		updateSupportedChildren();
		refreshChildren();
	}

	@SuppressWarnings("unchecked")
	public TModel getElement() {
		return (TModel) getModel();
	}

	@Override
	protected List<? extends FirstClassModel> getModelChildren() {		
		return supportedChildren;
	}
	
	private void updateSupportedChildren() {
		supportedChildren.clear();
		for (FirstClassModel childModel: getElement().getBody()) {
			if (isModelChildSupported(childModel)) {
				supportedChildren.add(childModel);
			}
		}
	}
	
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
}