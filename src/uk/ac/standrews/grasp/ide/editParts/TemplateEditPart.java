package uk.ac.standrews.grasp.ide.editParts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.swt.graphics.Image;

import uk.ac.standrews.grasp.ide.model.ElementType;
import uk.ac.standrews.grasp.ide.model.FirstClassModel;
import uk.ac.standrews.grasp.ide.model.TemplateModel;

public class TemplateEditPart extends AbstractElementNodeEditPart<TemplateModel> {
	
	private List<TemplateInheritanceConnection> sourceInheritanceConnections;
	private List<TemplateInheritanceConnection> targetInheirtanceConnections;

	public TemplateEditPart(TemplateModel model) {
		super(model);
		sourceInheritanceConnections = new ArrayList<TemplateInheritanceConnection>(1);
		if (model.getExtendee() instanceof TemplateModel) {
			sourceInheritanceConnections.add(new TemplateInheritanceConnection(model, (TemplateModel) model.getExtendee()));
		}
		targetInheirtanceConnections = new ArrayList<TemplateInheritanceConnection>();
		for (FirstClassModel fc: model.getArchitecture().getBodyByType(ElementType.TEMPLATE)) {
			TemplateModel other = (TemplateModel) fc;
			if (!other.getQualifiedName().equalsIgnoreCase(model.getQualifiedName())
					&& other.getExtendee() != null
					&& other.getExtendee().getQualifiedName().equalsIgnoreCase(model.getQualifiedName())) {
				targetInheirtanceConnections.add(new TemplateInheritanceConnection(other, model));
			}
		}
	}
	
	@Override
	public void activate() {
		super.activate();
	}
	
	@Override
	protected List<TemplateInheritanceConnection> getModelSourceConnections() {
		//return sourceInheritanceConnections;
		return Collections.emptyList();
	}
	
	@Override
	protected List<TemplateInheritanceConnection> getModelTargetConnections() {
		//return targetInheirtanceConnections;
		return Collections.emptyList();
	}
	
	@Override
	protected Image getIcon() {
		return IconsCache.getDefault().getTemplateIcon();
	}

	@Override
	protected void createEditPolicies() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected boolean isModelChildSupported(FirstClassModel child) {
		return child.getType() == ElementType.PROVIDES 
				|| child.getType() == ElementType.REQUIRES;
	}

}
