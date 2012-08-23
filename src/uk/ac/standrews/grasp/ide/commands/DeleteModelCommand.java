package uk.ac.standrews.grasp.ide.commands;

import org.eclipse.gef.commands.Command;

import uk.ac.standrews.grasp.ide.model.ElementModel;

/**
 * Command to remove a Grasp element from its parent
 * @author Dilyan Rusev
 *
 */
public class DeleteModelCommand extends Command {
	private ElementModel model;
	private ElementModel parent;
	
	public DeleteModelCommand(ElementModel model) {
		this.model = model;		
	}
	
	@Override
	public void execute() {
		parent = model.removeFromParent();
	}
	
	@Override
	public void undo() {
		if (parent != null) {
			parent.addChildElement(model);
		}
	}
	
	@Override
	public boolean canUndo() {
		return parent != null;
	}
	
	@Override
	public boolean canExecute() {
		return model != null;
	}
	
	@Override
	public String getLabel() {
		return "Delete " + model.getQualifiedName();
	}
}
