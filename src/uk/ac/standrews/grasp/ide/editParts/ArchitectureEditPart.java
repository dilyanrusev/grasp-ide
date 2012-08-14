package uk.ac.standrews.grasp.ide.editParts;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.ConnectionLayer;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ShortestPathConnectionRouter;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.gef.LayerConstants;
import org.eclipse.swt.SWT;

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
		FreeformLayer f = new FreeformLayer();
		ToolbarLayout layout = new ToolbarLayout(false);
		layout.setSpacing(20);		
		f.setLayoutManager(layout);
		f.setBackgroundColor(ColorConstants.white);
		f.setOpaque(true);
		ConnectionLayer connLayer = (ConnectionLayer) getLayer(LayerConstants.CONNECTION_LAYER);
        connLayer.setAntialias(SWT.ON);
        connLayer.setConnectionRouter(new ShortestPathConnectionRouter(f));
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
