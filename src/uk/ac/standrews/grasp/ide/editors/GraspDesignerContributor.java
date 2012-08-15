package uk.ac.standrews.grasp.ide.editors;

import org.eclipse.gef.ui.actions.ActionBarContributor;
import org.eclipse.ui.actions.ActionFactory;

public class GraspDesignerContributor extends ActionBarContributor {

	public GraspDesignerContributor() {
		
	}

	@Override
	protected void buildActions() {
	}

	@Override
	protected void declareGlobalActionKeys() {
		addGlobalActionKey(ActionFactory.UNDO.getId());
		addGlobalActionKey(ActionFactory.REDO.getId());		
	}

}

