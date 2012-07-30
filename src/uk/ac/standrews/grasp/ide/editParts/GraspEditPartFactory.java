package uk.ac.standrews.grasp.ide.editParts;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

import uk.ac.standrews.grasp.ide.model.ComponentModel;
import uk.ac.standrews.grasp.ide.model.ConnectorModel;
import uk.ac.standrews.grasp.ide.model.LayerModel;
import uk.ac.standrews.grasp.ide.model.SystemModel;
import uk.ac.standrews.grasp.ide.model.TemplateModel;

public class GraspEditPartFactory implements EditPartFactory {

	@Override
	public EditPart createEditPart(EditPart context, Object model) {
		if (model instanceof ComponentModel)
			return new ComponentEditPart((ComponentModel) model);
		if (model instanceof ConnectorModel)
			return new ConnectorEditPart((ConnectorModel) model);
		if (model instanceof LayerModel)
			return new LayerEditPart((LayerModel) model);
		if (model instanceof LayerOverLayerConnection)
			return new LayerOverLayerConnectionEditPart((LayerOverLayerConnection) model);
//		if (model instanceof LinkModel)
//			return new LinkEditPart((LinkModel) model);
		if (model instanceof SystemModel)
			return new SystemEditPart((SystemModel) model);
		if (model instanceof TemplateModel)
			return new TemplateEditPart((TemplateModel) model);
		if (model instanceof TemplateInheritanceConnection)
			return new TemplateInheritanceConnectionEditPart((TemplateInheritanceConnection) model);
		
		throw new IllegalStateException("Can not create edit part for model of type " + model.getClass());
	}
	
}
