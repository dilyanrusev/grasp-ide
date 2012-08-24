package uk.ac.standrews.grasp.ide.commands;

import org.eclipse.core.runtime.Assert;

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
public class AddInstantiableCommand extends AbstractCreateElementCommand {
	
	/**
	 * Construct a new command
	 * @param parent Parent element. Must be system or layer.
	 * @param childType Type of element to create. Must be either component or connector.
	 */
	public AddInstantiableCommand(FirstClassModel parent, ElementType childType) {
		super(parent, childType);
		Assert.isLegal(parent.getType() == ElementType.SYSTEM || parent.getType() == ElementType.LAYER);
		Assert.isNotNull(childType);
		Assert.isLegal(childType == ElementType.COMPONENT || childType == ElementType.CONNECTOR);
	}
	
	@Override
	protected FirstClassModel createElement(String name) {
		InstantiableModel child;
		if (getElementType() == ElementType.COMPONENT) {
			child = new ComponentModel(getParent());			
		} else {
			child = new ConnectorModel(getParent());
		}			
		child.setName(name);
		TemplateModel template = new TemplateModel(getParent().getArchitecture());
		template.setName(name + "Template");
		ModelHelper.ensureUniqueName(template);
		template.setDesignerCreated(true);
		child.setBase(template);
		return child;
	}
}
