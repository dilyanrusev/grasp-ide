package uk.ac.standrews.grasp.ide.editParts;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gef.editpolicies.FlowLayoutEditPolicy;

public abstract class GraspLayoutPolicy extends FlowLayoutEditPolicy  {

	@Override
	protected Command getMoveChildrenCommand(Request request) {
		return UnexecutableCommand.INSTANCE;
	}
	
	@Override
	protected Command createAddCommand(EditPart child, EditPart after) {
		return UnexecutableCommand.INSTANCE;
	}

	@Override
	protected Command createMoveChildCommand(EditPart child, EditPart after) {
		return UnexecutableCommand.INSTANCE;
	}

}
