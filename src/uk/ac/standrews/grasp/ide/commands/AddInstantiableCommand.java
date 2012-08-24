package uk.ac.standrews.grasp.ide.commands;

import org.eclipse.core.runtime.Assert;
import org.eclipse.gef.commands.Command;

import uk.ac.standrews.grasp.ide.model.ComponentModel;
import uk.ac.standrews.grasp.ide.model.ConnectorModel;
import uk.ac.standrews.grasp.ide.model.ElementType;
import uk.ac.standrews.grasp.ide.model.FirstClassModel;
import uk.ac.standrews.grasp.ide.model.InstantiableModel;
import uk.ac.standrews.grasp.ide.model.TemplateModel;

/**
 * Command used to add components and connectors
 * @author Dilyan Rusev
 *
 */
public class AddInstantiableCommand extends Command {
	private final FirstClassModel parent;
	private final ElementType childType;
	private InstantiableModel child;
	private boolean added;
	private boolean fakeUndo;
	
	/**
	 * Construct a new command
	 * @param parent Parent element. Must be system or layer.
	 * @param childType Type of element to create. Must be either component or connector.
	 */
	public AddInstantiableCommand(FirstClassModel parent, ElementType childType) {
		Assert.isNotNull(parent);
		Assert.isLegal(parent.getType() == ElementType.SYSTEM || parent.getType() == ElementType.LAYER);
		Assert.isNotNull(childType);
		Assert.isLegal(childType == ElementType.COMPONENT || childType == ElementType.CONNECTOR);
		
		this.parent = parent;
		this.childType = childType;		
		this.added = false;
		
		setLabel("Add " + childType.getDisplayName());
	}
	
	@Override
	public void execute() {
		if (child == null) {
			ChooseNameDialog dlg = new ChooseNameDialog(parent, childType.getDisplayName());
			if (dlg.open() == ChooseNameDialog.OK) {
				String name = dlg.getElementName();
				if (childType == ElementType.COMPONENT) {
					child = new ComponentModel(parent);			
				} else {
					child = new ConnectorModel(parent);
				}			
				child.setName(name);
				TemplateModel template = new TemplateModel(parent.getArchitecture());
				template.setName(name + "Template");
				ModelHelper.ensureUniqueName(template);
				template.setDesignerCreated(true);
				child.setBase(template);
			}
		}
		if (child != null) {
			added = parent.addChildElement(child);
		} else {
			fakeUndo = true;
		}
	}
	
	@Override
	public boolean canUndo() {
		return fakeUndo || (child != null && added);
	}
	
	@Override
	public void undo() {
		if (child != null) {
			child.removeFromParent();
			added = false;
		}
		fakeUndo = false;
	}
}
