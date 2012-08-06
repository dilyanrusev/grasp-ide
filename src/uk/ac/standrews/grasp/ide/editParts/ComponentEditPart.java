package uk.ac.standrews.grasp.ide.editParts;

import org.eclipse.swt.graphics.Image;

import uk.ac.standrews.grasp.ide.model.ComponentModel;

public class ComponentEditPart extends AbstractInstantiableEditPart<ComponentModel> {

	public ComponentEditPart(ComponentModel model) {
		super(model);		
	}
	
	@Override
	protected Image getIcon() {
		return IconsCache.getDefault().getComponentIcon();
	}	
}
