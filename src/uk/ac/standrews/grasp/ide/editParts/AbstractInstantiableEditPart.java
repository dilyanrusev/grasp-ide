package uk.ac.standrews.grasp.ide.editParts;

import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gef.requests.CreateRequest;

import uk.ac.standrews.grasp.ide.commands.AddInterfaceCommand;
import uk.ac.standrews.grasp.ide.model.ElementType;
import uk.ac.standrews.grasp.ide.model.FirstClassModel;
import uk.ac.standrews.grasp.ide.model.InstantiableModel;

public abstract class AbstractInstantiableEditPart<TModel extends InstantiableModel> extends
		AbstractElementNodeEditPart<TModel> {

	public AbstractInstantiableEditPart(TModel model) {
		super(model);		
	}	
	
	@Override
	protected boolean isModelChildSupported(FirstClassModel child) {
		return child.getType() == ElementType.PROVIDES || child.getType() == ElementType.REQUIRES;
	}
	
	@Override
	protected void createEditPolicies() {		
		super.createEditPolicies();
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new InstantiableLayoutPolicy());
	}
}


class InstantiableLayoutPolicy extends GraspLayoutPolicy {
	@Override
	protected Command getCreateCommand(CreateRequest request) {
		ElementType type = (ElementType) request.getNewObjectType();
		if (type == ElementType.PROVIDES || type == ElementType.REQUIRES) {
			InstantiableModel parent = (InstantiableModel) getHost().getModel();			
			return new AddInterfaceCommand(parent, type);
		}
		return UnexecutableCommand.INSTANCE;
	}
}