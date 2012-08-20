package uk.ac.standrews.grasp.ide.figures;

import org.eclipse.draw2d.Label;

import uk.ac.standrews.grasp.ide.editParts.IconsCache;

/**
 * Figure that draws Grasp link statements
 * @author Dilyan Rusev
 *
 */
public class LinkFigure extends AbstractFirstClassFigure {
	private Label provider;
	private Label consumer;
	
	/**
	 * Construct a new figure
	 */
	public LinkFigure() {
		provider = new Label(IconsCache.getDefault().getProvidesIcon());
		consumer = new Label(IconsCache.getDefault().getRequiresIcon());		
		getBody().add(consumer);		
		getBody().add(provider);
	}

	@Override
	protected Label createHeadLabel() {
		return new Label(IconsCache.getDefault().getLinkIcon());
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
