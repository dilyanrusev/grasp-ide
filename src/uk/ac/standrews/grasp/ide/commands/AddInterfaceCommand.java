package uk.ac.standrews.grasp.ide.commands;

import org.eclipse.core.runtime.Assert;

import uk.ac.standrews.grasp.ide.model.ElementType;
import uk.ac.standrews.grasp.ide.model.FirstClassModel;
import uk.ac.standrews.grasp.ide.model.InstantiableModel;
import uk.ac.standrews.grasp.ide.model.InterfaceModel;
import uk.ac.standrews.grasp.ide.model.ProvidesModel;
import uk.ac.standrews.grasp.ide.model.RequiresModel;

/**
 * Command that adds interfaces to components/connectors
 * @author Dilyan Rusev
 *
 */
public class AddInterfaceCommand extends AbstractCreateElementCommand {
	
	/**
	 * Create a new command
	 * @param parent Component or connector
	 * @param type Interface type - either provides or requires
	 */
	public AddInterfaceCommand(InstantiableModel parent, ElementType type) {
		super(parent, type);
		Assert.isLegal(type == ElementType.PROVIDES || type == ElementType.REQUIRES);
	}
	
	@Override
	protected InstantiableModel getParent() {		
		return (InstantiableModel) super.getParent();
	}
	
	@Override
	protected String getPreferredElementName() {		
		return "I" + super.getPreferredElementName();
	}
	
	@Override
	protected FirstClassModel createElement(String name) {
		InterfaceModel iface;
		
		if (getElementType() == ElementType.PROVIDES) {
			iface = new ProvidesModel(getParent());
			iface.setName(name);					
		} else {
			iface = new RequiresModel(getParent());
			iface.setName(name);			
		}
		return iface;
	}
}
