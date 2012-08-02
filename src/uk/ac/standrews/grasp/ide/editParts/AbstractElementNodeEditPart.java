package uk.ac.standrews.grasp.ide.editParts;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.RoundedRectangle;
import org.eclipse.swt.graphics.Image;

import uk.ac.standrews.grasp.ide.model.FirstClassModel;

public abstract class AbstractElementNodeEditPart<TModel extends FirstClassModel> 
		extends AbstractElementEditPart<TModel> {
	
	private Label headerLabel;
	
	public AbstractElementNodeEditPart(TModel model) {
		super(model);
	}
		
	protected Label getHeaderLabel() {
		return headerLabel;
	}
	
	@Override
	protected void refreshVisuals() {
		super.refreshVisuals();
		headerLabel.setBackgroundColor(ColorConstants.red);
		headerLabel.setText(getElement().getReferencingName());
		
	}
	
	@Override
	protected IFigure createFigure() {
		RoundedRectangle figure = new RoundedRectangle();
		
		//figure.setForegroundColor(ColorConstants.black);
		//figure.setBackgroundColor(ColorConstants.white);
		FlowLayout layout = new FlowLayout(false);		
		layout.setMajorSpacing(20);
		layout.setMinorSpacing(10);
		layout.setMajorAlignment(FlowLayout.ALIGN_CENTER);
		layout.setMinorAlignment(FlowLayout.ALIGN_CENTER);
		figure.setLayoutManager(layout);
		headerLabel = new Label(getElement().getReferencingName(), getIcon());
		headerLabel.setTextAlignment(PositionConstants.CENTER);
		figure.add(headerLabel);
		
		return figure;
	}
	
	
	
	protected abstract Image getIcon();
	
	protected IFigure createTooltip() {
		Label tooltip = new Label();
		tooltip.setIcon(getIcon());
		tooltip.setText(String.format("%s %s", getTypeString(), getElement().getQualifiedName()));
		return tooltip;
	}
	
	protected String getTypeString() {
		String initial = getElement().getType().toString();
		String lower = initial.toLowerCase();
		String result = Character.toString(initial.charAt(0)) + lower.substring(1);
		return result;
	}
	
	
}
