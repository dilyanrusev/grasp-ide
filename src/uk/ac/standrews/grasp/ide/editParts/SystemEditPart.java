package uk.ac.standrews.grasp.ide.editParts;

import org.eclipse.swt.graphics.Image;

import uk.ac.standrews.grasp.ide.model.ElementType;
import uk.ac.standrews.grasp.ide.model.FirstClassModel;
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

	@Override
	protected boolean isModelChildSupported(FirstClassModel child) {
		return child.getType() == ElementType.LAYER
				|| child.getType() == ElementType.COMPONENT
				|| child.getType() == ElementType.CONNECTOR
				|| child.getType() == ElementType.LINK;
	}

}
