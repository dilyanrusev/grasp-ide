package uk.ac.standrews.grasp.ide.figures;

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

}
