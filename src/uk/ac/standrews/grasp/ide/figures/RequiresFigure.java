package uk.ac.standrews.grasp.ide.figures;

import org.eclipse.swt.graphics.Image;

import uk.ac.standrews.grasp.ide.editParts.IconsCache;

/**
 * Figure that depicts requires interface
 * @author Dilyan Rusev
 *
 */
public class RequiresFigure extends AbstractInterfaceFigure {

	@Override
	protected Image createIcon() {
		return IconsCache.getDefault().getRequiresIcon();
	}
}
