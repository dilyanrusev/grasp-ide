package uk.ac.standrews.grasp.ide.editParts;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gef.editpolicies.LayoutEditPolicy;

public abstract class GraspLayoutPolicy extends LayoutEditPolicy {

	@Override
	protected EditPolicy createChildEditPolicy(EditPart child) {
		return new UnmodifiableFlowLayoutEditPolicy();
	}	

	@Override
	protected Command getMoveChildrenCommand(Request request) {
		return UnexecutableCommand.INSTANCE;
	}

}
