package uk.ac.standrews.grasp.ide.editParts;

import org.eclipse.draw2d.IFigure;

import uk.ac.standrews.grasp.ide.figures.SystemFigure;
import uk.ac.standrews.grasp.ide.model.ElementType;
import uk.ac.standrews.grasp.ide.model.FirstClassModel;
import uk.ac.standrews.grasp.ide.model.SystemModel;

public class SystemEditPart extends AbstractElementNodeEditPart<SystemModel> {
		
	public SystemEditPart(SystemModel model) {
		super(model);		
	}
	
	@Override
	protected void createEditPolicies() {
				
	}
	
	@Override
	protected IFigure createFigure() {
		SystemFigure figure = new SystemFigure();		
		return figure;
	}
	
	@Override
	protected String getHeaderText() {
		return getElement().getArchitecture().getName() + " :: " + getElement().getName();
	}

	@Override
	protected boolean isModelChildSupported(FirstClassModel child) {
		return child.getType() == ElementType.LAYER
				|| child.getType() == ElementType.COMPONENT
				|| child.getType() == ElementType.CONNECTOR
				|| child.getType() == ElementType.LINK
				;
	}

}
