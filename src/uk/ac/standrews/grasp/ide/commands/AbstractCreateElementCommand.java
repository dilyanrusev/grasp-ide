package uk.ac.standrews.grasp.ide.commands;

import org.eclipse.gef.commands.Command;

import uk.ac.standrews.grasp.ide.model.FirstClassModel;

public abstract class AbstractCreateElementCommand extends Command {
	private boolean fakeUndo;
	private boolean needsNameDialog;
	private final FirstClassModel parent;
	private FirstClassModel element;
	
	public AbstractCreateElementCommand(FirstClassModel parent) {
		fakeUndo = false;
		this.parent = parent;
		needsNameDialog = true;
	}
	
	protected FirstClassModel getParent() {
		return parent;
	}
	
	@Override
	public void execute() {		
		if (element != null) {			
			if (needsNameDialog) {
				String name = getPreferredElementName();
				ChooseNameDialog dlg = new ChooseNameDialog(parent, name);
				if (dlg.open() == ChooseNameDialog.OK) {
					name = dlg.getElementName();
				} else {
					fakeUndo = true;
				}
			} else {
				
			}
		} else {
			doExecute(element);
		}
	}
	
	protected abstract void doExecute(FirstClassModel element);
	
	protected boolean getNeedsNameDialog() {
		return needsNameDialog;
	}
	
	protected void setNeedsNameDialog(boolean flag) {
		needsNameDialog = flag;
	}
	
	protected abstract String getPreferredElementName();
	
	protected abstract FirstClassModel createElement(String name);
	
	protected boolean verify() {
		return element != null;
	}
	
	@Override
	public boolean canUndo() {		
		return fakeUndo || verify();
	}
	
	@Override
	public void undo() {
		if (element != null) {
			element.removeFromParent();
		}
		fakeUndo = false;
	}
}
