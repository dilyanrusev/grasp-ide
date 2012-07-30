package uk.ac.standrews.grasp.ide.editParts;

import grasp.lang.IAnnotation;
import grasp.lang.IFirstClass;

import java.util.List;

import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

import uk.ac.standrews.grasp.ide.model.CollectionChangedEvent;
import uk.ac.standrews.grasp.ide.model.ElementChangedEvent;
import uk.ac.standrews.grasp.ide.model.FirstClassModel;
import uk.ac.standrews.grasp.ide.model.ICollectionChangedListener;
import uk.ac.standrews.grasp.ide.model.IElementChangedListener;
import uk.ac.standrews.grasp.ide.model.IObservableCollection;

public abstract class AbstractElementEditPart<TModel extends FirstClassModel> 
		extends AbstractGraphicalEditPart
		implements IElementChangedListener {
	
	private ICollectionChangedListener<IAnnotation> annotationChangedListener;
	private ICollectionChangedListener<IFirstClass> childElementsChangedListener;
	
	public AbstractElementEditPart(TModel model) {
		setModel(model);
		annotationChangedListener = new AnnotationsChangedListener();
		childElementsChangedListener = new ChildElementsChangedListener();
	}
	
	@Override
	public void activate() {
		super.activate();
		getElement().addElementChangedListener(this);	
		getObservableAnnotations().addCollectionChangeListener(annotationChangedListener);		
		getObservableModelChildren().addCollectionChangeListener(childElementsChangedListener);
	}
	
	@Override
	public void deactivate() {
		super.deactivate();
		getElement().removeElementChangedListener(this);
		getObservableAnnotations().removeCollectionChangeListener(annotationChangedListener);
		getObservableModelChildren().removeCollectionChangeListener(childElementsChangedListener);
	}
	
	@Override
	public void elementChanged(ElementChangedEvent event) {		
	}
	
	protected void annotationsChanged(CollectionChangedEvent<IAnnotation> event) {		
	}
	
	protected void childElementsChanged(CollectionChangedEvent<IFirstClass> event) {		
	}
	
	@SuppressWarnings("unchecked")
	public TModel getElement() {
		return (TModel) getModel();
	}
	
	@Override
	protected List<? extends IFirstClass> getModelChildren() {
		return getElement().getChildElements();
	}

	private IObservableCollection<IAnnotation> getObservableAnnotations() {
		return (IObservableCollection<IAnnotation>) getElement().getAnnotations();
	}
	
	@SuppressWarnings("unchecked")
	private IObservableCollection<IFirstClass> getObservableModelChildren() {
		return (IObservableCollection<IFirstClass>) getElement().getChildElements();
	}
	
	private final class AnnotationsChangedListener implements ICollectionChangedListener<IAnnotation> {
		@Override
		public void collectionChanged(CollectionChangedEvent<IAnnotation> event) {
			annotationsChanged(event);
		}
	}
	
	private final class ChildElementsChangedListener implements ICollectionChangedListener<IFirstClass> {
		@Override
		public void collectionChanged(CollectionChangedEvent<IFirstClass> event) {
			childElementsChanged(event);
		}
	}
}
