package uk.ac.standrews.grasp.ide.editParts;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

import uk.ac.standrews.grasp.ide.model.ArchitectureModel;
import uk.ac.standrews.grasp.ide.model.ComponentModel;
import uk.ac.standrews.grasp.ide.model.ConnectionModel;
import uk.ac.standrews.grasp.ide.model.ConnectorModel;
import uk.ac.standrews.grasp.ide.model.ElementType;
import uk.ac.standrews.grasp.ide.model.LayerModel;
import uk.ac.standrews.grasp.ide.model.LinkModel;
import uk.ac.standrews.grasp.ide.model.SystemModel;

public class GraspEditPartFactory implements EditPartFactory {

	@Override
	public EditPart createEditPart(EditPart context, Object model) {
		if (model == null) 
			return null;
		
		if (model instanceof ArchitectureModel) 
			return new ArchitectureEditPart((ArchitectureModel) model);
		if (model instanceof ComponentModel)
			return new ComponentEditPart((ComponentModel) model);
		if (model instanceof ConnectorModel)
			return new ConnectorEditPart((ConnectorModel) model);
		if (model instanceof LayerModel)
			return new LayerEditPart((LayerModel) model);		
		if (model instanceof SystemModel)
			return new SystemEditPart((SystemModel) model);
		if (model instanceof LinkModel) 
			return new LinkEditPart((LinkModel) model);
		if (model instanceof ConnectionModel) {
			ConnectionModel con = (ConnectionModel) model;
			if (con.getEndpointType() == ElementType.LAYER) {
				return new LayerOverLayerConnection(con);
			}
		}
		
		throw new IllegalStateException("Can not create edit part for model of type " + model.getClass());
	}
	
}
