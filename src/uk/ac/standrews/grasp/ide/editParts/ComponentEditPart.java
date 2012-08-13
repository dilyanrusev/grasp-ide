package uk.ac.standrews.grasp.ide.editParts;

import org.eclipse.draw2d.IFigure;

import uk.ac.standrews.grasp.ide.figures.ComponentFigure;
import uk.ac.standrews.grasp.ide.model.ComponentModel;

public class ComponentEditPart extends AbstractInstantiableEditPart<ComponentModel> {

	public ComponentEditPart(ComponentModel model) {
		super(model);		
	}
	
	@Override
	protected IFigure createFigure() {
		ComponentFigure figure = new ComponentFigure();
		return figure;
	}
		
}
