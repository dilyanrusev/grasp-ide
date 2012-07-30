package uk.ac.standrews.grasp.ide.editParts;

import grasp.lang.IAnnotation;
import grasp.lang.IFirstClass;

import java.util.List;

import org.eclipse.draw2d.IFigure;
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
	
	

	private TModel model;
	private ICollectionChangedListener<IAnnotation> annotationChangedListener;
	private ICollectionChangedListener<IFirstClass> childElementsChangedListener;
	
	@Override
	public void activate() {
		super.activate();
		model.addElementChangedListener(this);
		if (annotationChangedListener == null) {
			annotationChangedListener = new AnnotationsChangedListener();
		}
		getObservableAnnotations().addCollectionChangeListener(annotationChangedListener);
		if (childElementsChangedListener == null) {
			childElementsChangedListener = new ChildElementsChangedListener();
		}
		getObservableModelChildren().addCollectionChangeListener(childElementsChangedListener);
	}
	
	@Override
	public void deactivate() {
		super.deactivate();
		model.removeElementChangedListener(this);
		getObservableAnnotations().removeCollectionChangeListener(annotationChangedListener);
		getObservableModelChildren().removeCollectionChangeListener(childElementsChangedListener);
	}
	
	@Override
	public void elementChanged(ElementChangedEvent event) {
		// TODO Auto-generated method stub
		
	}
	
	protected void annotationsChanged(CollectionChangedEvent<IAnnotation> event) {
		
	}
	
	protected void childElementsChanged(CollectionChangedEvent<IFirstClass> event) {
		
	}

	@Override
	protected IFigure createFigure() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void createEditPolicies() {
		// TODO Auto-generated method stub

	}
	
	@Override
	public Object getModel() {
		return model;
	}
	
	public TModel getGraspModel() {
		return model;
	}
	
	@Override
	protected List<? extends IFirstClass> getModelChildren() {
		return model.getChildElements();
	}

	private IObservableCollection<IAnnotation> getObservableAnnotations() {
		return (IObservableCollection<IAnnotation>) model.getAnnotations();
	}
	
	@SuppressWarnings("unchecked")
	private IObservableCollection<IFirstClass> getObservableModelChildren() {
		return (IObservableCollection<IFirstClass>) model.getChildElements();
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
