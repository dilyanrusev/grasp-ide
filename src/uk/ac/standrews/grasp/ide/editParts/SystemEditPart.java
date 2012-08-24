package uk.ac.standrews.grasp.ide.editParts;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gef.requests.CreateRequest;

import uk.ac.standrews.grasp.ide.commands.AddInstantiableCommand;
import uk.ac.standrews.grasp.ide.commands.AddLayerCommand;
import uk.ac.standrews.grasp.ide.commands.AddLinkCommand;
import uk.ac.standrews.grasp.ide.figures.SystemFigure;
import uk.ac.standrews.grasp.ide.model.ElementType;
import uk.ac.standrews.grasp.ide.model.FirstClassModel;
import uk.ac.standrews.grasp.ide.model.SystemModel;

public class SystemEditPart extends AbstractElementNodeEditPart<SystemModel> {
		
	public SystemEditPart(SystemModel model) {
		super(model);		
	}
	
	@Override
	protected IFigure createFigure() {
		SystemFigure figure = new SystemFigure();		
		return figure;
	}
	
	@Override
	protected void createEditPolicies() {
		// super.createEditPolicies(); // -> no delete		
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new SystemLayoutPolicy());
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

class SystemLayoutPolicy extends GraspLayoutPolicy {

	@Override
	protected Command getCreateCommand(CreateRequest request) {
		if (request.getNewObjectType() instanceof ElementType) {
			ElementType type = (ElementType) request.getNewObjectType();
			SystemModel system = (SystemModel) getHost().getModel();
			if (type == ElementType.COMPONENT || type == ElementType.CONNECTOR) {
				return new AddInstantiableCommand(system, type);
			} else if (type == ElementType.LINK) {
				return new AddLinkCommand(system);
			} else if (type == ElementType.LAYER) {
				return new AddLayerCommand(system);
			}
		}
		return UnexecutableCommand.INSTANCE;
	}
	
}