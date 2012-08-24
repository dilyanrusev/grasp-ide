package uk.ac.standrews.grasp.ide.commands;

import uk.ac.standrews.grasp.ide.model.ElementType;
import uk.ac.standrews.grasp.ide.model.FirstClassModel;
import uk.ac.standrews.grasp.ide.model.LayerModel;
import uk.ac.standrews.grasp.ide.model.SystemModel;

/**
 * Command to add a new layer
 * @author Dilyan Rusev
 *
 */
public class AddLayerCommand extends AbstractCreateElementCommand {
	/**
	 * Create a new layer command
	 * @param parent Parent element
	 */
	public AddLayerCommand(SystemModel parent) {
		super(parent, ElementType.LAYER);
	}
	
	@Override
	protected FirstClassModel createElement(String name) {
		LayerModel layer = new LayerModel(getParent());
		layer.setName(name);
		return layer;
	}
}
