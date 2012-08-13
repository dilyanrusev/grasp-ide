package uk.ac.standrews.grasp.ide.editParts;

import org.eclipse.draw2d.IFigure;
import org.eclipse.swt.graphics.Image;

import uk.ac.standrews.grasp.ide.figures.ComponentFigure;
import uk.ac.standrews.grasp.ide.model.ComponentModel;
import uk.ac.standrews.grasp.ide.model.ElementType;
import uk.ac.standrews.grasp.ide.model.FirstClassModel;
import uk.ac.standrews.grasp.ide.model.ProvidesModel;
import uk.ac.standrews.grasp.ide.model.RequiresModel;

public class ComponentEditPart extends AbstractInstantiableEditPart<ComponentModel> {

	public ComponentEditPart(ComponentModel model) {
		super(model);		
	}
	
	@Override
	protected IFigure createFigure() {
		ComponentFigure figure = new ComponentFigure();
		return figure;
	}
	
	@Override
	protected void refreshVisuals() {		
		ComponentFigure figure = (ComponentFigure) getFigure();
		figure.setHeaderText(getElement().getReferencingName());
		figure.clearBody();
		for (FirstClassModel child: getElement().getBodyByType(ElementType.PROVIDES)) {
			ProvidesModel provides = (ProvidesModel) child;
			figure.addProvides(provides.getReferencingName());
		}
		for (FirstClassModel child: getElement().getBodyByType(ElementType.REQUIRES)) {
			RequiresModel requires = (RequiresModel) child;
			figure.addRequires(requires.getReferencingName());
		}
	}
	
	@Override
	protected Image getIcon() {
		return IconsCache.getDefault().getComponentIcon();
	}	
	
	@Override
	protected boolean isModelChildSupported(FirstClassModel child) {
		return false;
	}
}
