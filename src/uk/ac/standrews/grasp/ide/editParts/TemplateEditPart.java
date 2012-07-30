package uk.ac.standrews.grasp.ide.editParts;

import grasp.lang.IElement.ElementType;
import grasp.lang.IFirstClass;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.RoundedRectangle;

import uk.ac.standrews.grasp.ide.model.ElementChangedEvent;
import uk.ac.standrews.grasp.ide.model.TemplateModel;

public class TemplateEditPart extends AbstractElementEditPart<TemplateModel> {
	
	private List<TemplateInheritanceConnection> sourceInheritanceConnections;
	private List<TemplateInheritanceConnection> targetInheirtanceConnections; 
	private Label labelHeader;

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
	public void elementChanged(ElementChangedEvent event) {
		super.elementChanged(event);
		labelHeader.setText(getElement().getReferencingName());
	}
	
	@Override
	protected IFigure createFigure() {
		RoundedRectangle outline = new RoundedRectangle();
		outline.setLayoutManager(new FlowLayout());
		labelHeader = new Label();
		labelHeader.setText(getElement().getReferencingName());
		labelHeader.setIcon(IconsCache.getDefault().getTemplateIcon());
		outline.add(labelHeader);
		return outline;
	}

	@Override
	protected void createEditPolicies() {
		// TODO Auto-generated method stub
		
	}

}
