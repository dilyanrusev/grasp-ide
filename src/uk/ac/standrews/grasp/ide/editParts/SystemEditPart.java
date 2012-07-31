package uk.ac.standrews.grasp.ide.editParts;

import org.eclipse.swt.graphics.Image;

import uk.ac.standrews.grasp.ide.model.SystemModel;

public class SystemEditPart extends AbstractElementNodeEditPart<SystemModel> {
		
	public SystemEditPart(SystemModel model) {
		super(model);		
	}
	
	@Override
	protected Image getIcon() {
		return IconsCache.getDefault().getSystemIcon();
	}

	@Override
	protected void createEditPolicies() {
				
	}

}
