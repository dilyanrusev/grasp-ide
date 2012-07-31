package uk.ac.standrews.grasp.ide.editParts;

import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.RoundedRectangle;
import org.eclipse.swt.graphics.Image;

import uk.ac.standrews.grasp.ide.model.FirstClassModel;

public abstract class AbstractElementNodeEditPart<TModel extends FirstClassModel> 
		extends AbstractElementEditPart<TModel> {
	
	private Label headerLabel;
	
	public AbstractElementNodeEditPart(TModel model) {
		super(model);
	}
	
	@Override
	protected void elementPropertyChanged(String propertyName) {
		super.elementPropertyChanged(propertyName);
		if (FirstClassModel.PROPERTY_REFERENCING_NAME.equals(propertyName)) {
			getHeaderLabel().setText(getElement().getReferencingName());
		}
	}
	
	protected Label getHeaderLabel() {
		if (headerLabel == null) {
			headerLabel = createLabelHeader();
		}
		return headerLabel;
	}
	
	protected Label createLabelHeader() {
		Label result = new Label();
		result.setText(getElement().getReferencingName());
		result.setIcon(getIcon());
		return result;
	}
	
	@Override
	protected IFigure createFigure() {
		IFigure outline = createOutlineFigure();
		outline.setLayoutManager(new FlowLayout());
		outline.add(getHeaderLabel());
		outline.setToolTip(createTooltip());
		return outline;
	}
	
	protected IFigure createOutlineFigure() {
		return new RoundedRectangle();
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
