package uk.ac.standrews.grasp.ide.editParts;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

import uk.ac.standrews.grasp.ide.commands.DeleteModelCommand;
import uk.ac.standrews.grasp.ide.model.FirstClassModel;

/**
 * Handles deletion of elements
 * @author Dilyan Rusev
 *
 */
public class GraspComponentPolicy extends ComponentEditPolicy {
	
	@Override
	protected Command createDeleteCommand(GroupRequest deleteRequest) {		
		CompoundCommand compound = new CompoundCommand();
		for (Object part: deleteRequest.getEditParts()) {
			if (part instanceof AbstractElementEditPart<?>) {
				FirstClassModel model = ((AbstractElementEditPart<?>) part).getElement();
				compound.add(new DeleteModelCommand(model));
			}
		}
		return compound;		
	}
}
