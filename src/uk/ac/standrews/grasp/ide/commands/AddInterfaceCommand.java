package uk.ac.standrews.grasp.ide.commands;

import org.eclipse.core.runtime.Assert;
import org.eclipse.gef.commands.Command;

import uk.ac.standrews.grasp.ide.model.ElementType;
import uk.ac.standrews.grasp.ide.model.InstantiableModel;
import uk.ac.standrews.grasp.ide.model.InterfaceModel;
import uk.ac.standrews.grasp.ide.model.ProvidesModel;
import uk.ac.standrews.grasp.ide.model.RequiresModel;
import uk.ac.standrews.grasp.ide.model.TemplateModel;

public class AddInterfaceCommand extends Command {
	private final TemplateModel originalTemplate;
	private final TemplateModel newTemplate;
	private final InstantiableModel parent;
	private final InterfaceModel iface;
	private final InterfaceModel templateInterface;	
	private final String label;
	
	public AddInterfaceCommand(InstantiableModel parent, ElementType type) {
		Assert.isNotNull(parent);
		Assert.isLegal(type == ElementType.PROVIDES || type == ElementType.REQUIRES);
		
		this.parent = parent;		
		this.originalTemplate = parent.getBase();
		this.newTemplate = getDesignedTemplate(parent);
		
		if (type == ElementType.PROVIDES) {
			this.iface = new ProvidesModel(parent);
			this.iface.setName(ModelHelper.getUniqueName(type, parent));
			this.templateInterface = new ProvidesModel((ProvidesModel) this.iface, this.newTemplate);			
		} else {
			this.iface = new RequiresModel(parent);
			this.iface.setName(ModelHelper.getUniqueName(type, parent));
			this.templateInterface = new RequiresModel((RequiresModel) this.iface, this.newTemplate);
		}
		
		this.label = new StringBuilder()
			.append("Create ").append(iface.getType().getDisplayName())
				.append(' ').append(iface.getName()).toString();
	}
	
	private static TemplateModel getDesignedTemplate(InstantiableModel forParent) {
		TemplateModel original = forParent.getBase();
		TemplateModel designed = original;
		if (!original.isCreatedByDesigner()) {
			designed = ModelHelper.createDesignedTemplate(original, forParent);
		}
		return designed;
	}
	
	@Override
	public void execute() {
		parent.addChild(iface);
		parent.setBase(newTemplate);
		newTemplate.addChild(templateInterface);		
		parent.getArchitecture().addChild(newTemplate);		
	}
	
	@Override
	public void undo() {
		parent.removeChild(iface);
		parent.setBase(originalTemplate);
		newTemplate.removeChild(iface);
		parent.getArchitecture().removeChild(newTemplate);
	}
	
	@Override
	public String getLabel() {
		return this.label; 
	}
}
