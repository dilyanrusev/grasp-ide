package uk.ac.standrews.grasp.ide.editParts;

import org.eclipse.draw2d.IFigure;

import uk.ac.standrews.grasp.ide.editors.TextUtil;
import uk.ac.standrews.grasp.ide.figures.LinkFigure;
import uk.ac.standrews.grasp.ide.model.ElementModel;
import uk.ac.standrews.grasp.ide.model.ElementType;
import uk.ac.standrews.grasp.ide.model.FirstClassModel;
import uk.ac.standrews.grasp.ide.model.InterfaceModel;
import uk.ac.standrews.grasp.ide.model.LinkModel;

/**
 * Edit part for Grasp link statements
 * @author Dilyan Rusev
 *
 */
public class LinkEditPart extends AbstractElementNodeEditPart<LinkModel> {

	/**
	 * Construct a new link edit part
	 * @param model Model to bind to
	 */
	public LinkEditPart(LinkModel model) {
		super(model);		
	}

	@Override
	protected boolean isModelChildSupported(FirstClassModel child) {
		return false;
	}

	@Override
	protected IFigure createFigure() {
		return new LinkFigure();
	}
	
	@Override
	protected void refreshVisuals() {
		super.refreshVisuals();
		
		LinkFigure figure = (LinkFigure) getFigure();
		figure.setProvider(buildInterfaceName(getElement().getConsumer()));
		figure.setConsumer(buildInterfaceName(getElement().getProvider()));
	} 
	
	@Override
	protected String getHeaderText() {
		StringBuilder sb = new StringBuilder();
		if (getElement().getName() != null) {
			sb.append(getElement().getName());
			sb.append(' ');
		}
		sb.append(getElement().getConsumer().getName());
		return sb.toString();
	}

	@Override
	protected String getTooltipText() {
		return new StringBuilder()
			.append("Link ")
			.append(buildInterfaceName(getElement().getConsumer()))
			.append(" to ")
			.append(buildInterfaceName(getElement().getProvider()))
			.toString();
	}
	
	/**
	 * Produce a shorter name than qualified names
	 * @param iface
	 * @return
	 */
	protected String buildInterfaceName(InterfaceModel iface) {
		StringBuilder sb = new StringBuilder();
		if (getElement().getParent().getType() == ElementType.SYSTEM) {
			sb.append(iface.getParent().getParent().getName());
			sb.append('.');
		}
		sb.append(iface.getParent().getName());
		if (!TextUtil.isNullOrWhitespace(iface.getAlias())) {
			sb.append(' ');
			sb.append(iface.getAlias());
		}
		return sb.toString();
	}
	
}
