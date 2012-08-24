package uk.ac.standrews.grasp.ide.editors;

import org.eclipse.gef.ui.actions.ActionBarContributor;
import org.eclipse.gef.ui.actions.DeleteRetargetAction;
import org.eclipse.gef.ui.actions.RedoRetargetAction;
import org.eclipse.gef.ui.actions.UndoRetargetAction;
import org.eclipse.ui.actions.ActionFactory;

/**
 * Contributor for GraspDesginer if used on its own.
 * @author Dilyan Rusev
 *
 */
public class GraspDesignerContributor extends ActionBarContributor {

	/**
	 * Construct a new contributor
	 */
	public GraspDesignerContributor() {
		
	}

	@Override
	protected void buildActions() {
		addRetargetAction(new UndoRetargetAction());
		addRetargetAction(new RedoRetargetAction());
		addRetargetAction(new DeleteRetargetAction());
	}

	@Override
	protected void declareGlobalActionKeys() {
		addGlobalActionKey(ActionFactory.UNDO.getId());
		addGlobalActionKey(ActionFactory.REDO.getId());
		addGlobalActionKey(ActionFactory.DELETE.getId());
	}

}

