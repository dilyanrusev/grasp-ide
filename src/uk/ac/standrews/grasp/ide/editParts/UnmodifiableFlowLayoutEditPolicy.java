package uk.ac.standrews.grasp.ide.editParts;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gef.editpolicies.FlowLayoutEditPolicy;
import org.eclipse.gef.requests.CreateRequest;

class UnmodifiableFlowLayoutEditPolicy extends FlowLayoutEditPolicy {

	@Override
	protected Command createAddCommand(EditPart child, EditPart after) {
		return UnexecutableCommand.INSTANCE;
	}

	@Override
	protected Command createMoveChildCommand(EditPart child, EditPart after) {
		return UnexecutableCommand.INSTANCE;
	}

	@Override
	protected Command getCreateCommand(CreateRequest request) {
		return UnexecutableCommand.INSTANCE;
	}
	
}