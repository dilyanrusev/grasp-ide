package uk.ac.standrews.grasp.ide.figures;

import org.eclipse.draw2d.Label;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

import uk.ac.standrews.grasp.ide.GraspPlugin;
import uk.ac.standrews.grasp.ide.editParts.IconsCache;

/**
 * Figure that draws Grasp link statements
 * @author Dilyan Rusev
 *
 */
public class LinkFigure extends AbstractNodeFigure {
	private Label provider;
	private Label consumer;
	
	/**
	 * Construct a new figure
	 */
	public LinkFigure() {
		provider = new Label(IconsCache.getDefault().getProvidesIcon());
		consumer = new Label(IconsCache.getDefault().getRequiresIcon());		
		add(consumer);		
		add(provider);
	}

	@Override
	protected Image createIcon() {
		return IconsCache.getDefault().getLinkIcon();
	}
	
	@Override
	protected Color createBackgroundColour() {
		return GraspPlugin.getDefault().getColour(255, 255, 206);
	}
	
	/**
	 * Set provider interface name
	 * @param name Interface name
	 */
	public void setProvider(String name) {
		provider.setText(name);
	}
	
	/**
	 * Set consumer interface name
	 * @param name Interface name
	 */
	public void setConsumer(String name) {
		consumer.setText(name);
	}
	
	
}
