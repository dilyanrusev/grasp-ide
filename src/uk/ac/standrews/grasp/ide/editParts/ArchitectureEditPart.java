package uk.ac.standrews.grasp.ide.editParts;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ToolbarLayout;

import uk.ac.standrews.grasp.ide.model.ArchitectureModel;
import uk.ac.standrews.grasp.ide.model.ElementType;
import uk.ac.standrews.grasp.ide.model.FirstClassModel;

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
		f.setBackgroundColor(ColorConstants.white);
		f.setOpaque(true);
		return f;
	}

	@Override
	protected void createEditPolicies() {
		
		
	}

	@Override
	protected boolean isModelChildSupported(FirstClassModel child) {
		return child.getType() == ElementType.SYSTEM;
	}
}
