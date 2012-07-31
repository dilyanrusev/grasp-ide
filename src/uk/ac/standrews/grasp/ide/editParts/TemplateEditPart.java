package uk.ac.standrews.grasp.ide.editParts;

import grasp.lang.IElement.ElementType;
import grasp.lang.IFirstClass;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Image;

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
		for (IFirstClass fc: model.getArchitecture().getBodyByType(ElementType.TEMPLATE)) {
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
		return sourceInheritanceConnections;
	}
	
	@Override
	protected List<TemplateInheritanceConnection> getModelTargetConnections() {
		return targetInheirtanceConnections;
	}
	
	@Override
	protected Image getIcon() {
		return IconsCache.getDefault().getTemplateIcon();
	}

	@Override
	protected void createEditPolicies() {
		// TODO Auto-generated method stub
		
	}

}
