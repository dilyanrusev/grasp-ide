package uk.ac.standrews.grasp.ide.commands;

import org.eclipse.core.runtime.Assert;
import org.eclipse.gef.commands.Command;

import uk.ac.standrews.grasp.ide.model.ElementType;
import uk.ac.standrews.grasp.ide.model.LayerModel;
import uk.ac.standrews.grasp.ide.model.SystemModel;

/**
 * Command to add a new layer
 * @author Dilyan Rusev
 *
 */
public class AddLayerCommand extends Command {
	private final SystemModel parent;
	private final LayerModel layer;
	
	/**
	 * Create a new layer command
	 * @param parent Parent element
	 */
	public AddLayerCommand(SystemModel parent) {
		Assert.isNotNull(parent);
		
		this.parent = parent;
		layer = new LayerModel(parent);
		layer.setName(ModelHelper.getUniqueName(ElementType.LAYER, parent));
		
		setLabel("Add layer");
	}
	
	@Override
	public void execute() {
		parent.addChildElement(layer);
	}
	
	@Override
	public void undo() {
		layer.removeFromParent();
	}
}
