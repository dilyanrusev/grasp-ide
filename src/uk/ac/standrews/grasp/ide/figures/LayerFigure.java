package uk.ac.standrews.grasp.ide.figures;

import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.LayoutManager;
import org.eclipse.swt.graphics.Image;

import uk.ac.standrews.grasp.ide.editParts.IconsCache;

public class LayerFigure extends AbstractElementFigure {

	@Override
	protected Image createIcon() {
		return IconsCache.getDefault().getLayerIcon();
	}
	
	@Override
	protected IHeaderBorder createBorder() {
		return new LayerBorder(getIcon());
	}
	
	@Override
	protected LayoutManager createLayout() {
		FlowLayout layout = (FlowLayout) super.createLayout();
		//layout.setHorizontal(true);
		return layout;
	}

}
