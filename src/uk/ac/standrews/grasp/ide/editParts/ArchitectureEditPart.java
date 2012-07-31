package uk.ac.standrews.grasp.ide.editParts;

import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Panel;

import uk.ac.standrews.grasp.ide.model.ArchitectureModel;

public class ArchitectureEditPart extends AbstractElementEditPart<ArchitectureModel> {
	public ArchitectureEditPart(ArchitectureModel model) {
		super(model);
	}

	@Override
	protected IFigure createFigure() {
		IFigure figure = new Panel();
		figure.setLayoutManager(new FlowLayout());
		return figure;
	}

	@Override
	protected void createEditPolicies() {
		
		
	}
}
