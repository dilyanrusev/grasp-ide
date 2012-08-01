package uk.ac.standrews.grasp.ide.editParts;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.Panel;
import org.eclipse.draw2d.text.FlowPage;

import uk.ac.standrews.grasp.ide.model.ArchitectureModel;

public class ArchitectureEditPart extends AbstractElementEditPart<ArchitectureModel> {
	public ArchitectureEditPart(ArchitectureModel model) {
		super(model);
	}

	@Override
	protected IFigure createFigure() {
		Figure f = new FreeformLayer();
		f.setLayoutManager(new FreeformLayout());
		f.setBorder(new MarginBorder(5));
		return f;
	}

	@Override
	protected void createEditPolicies() {
		
		
	}
}
