package uk.ac.standrews.grasp.ide.editParts;

import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ToolbarLayout;

import uk.ac.standrews.grasp.ide.model.ArchitectureModel;

public class ArchitectureEditPart extends AbstractElementEditPart<ArchitectureModel> {
	public ArchitectureEditPart(ArchitectureModel model) {
		super(model);
	}

	@Override
	protected IFigure createFigure() {
		// NB! Root figure must be FreeformFigure or ScalableFreeformRootEditPart won't size it properly
		IFigure f = new FreeformLayer();
		ToolbarLayout layout = new ToolbarLayout(false);
		layout.setSpacing(20);		
		f.setLayoutManager(layout);	
		return f;
	}

	@Override
	protected void createEditPolicies() {
		
		
	}
}
