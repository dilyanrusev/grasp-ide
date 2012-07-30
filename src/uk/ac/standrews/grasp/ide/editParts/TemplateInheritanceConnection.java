package uk.ac.standrews.grasp.ide.editParts;

import uk.ac.standrews.grasp.ide.model.TemplateModel;

public class TemplateInheritanceConnection {
	private final TemplateModel source;
	private final TemplateModel target;
	
	public TemplateInheritanceConnection(TemplateModel source, TemplateModel target) {
		this.source = source;
		this.target = target;
	}
	
	public TemplateModel getSource() {
		return source;
	}
	
	public TemplateModel getTarget() {
		return target;
	}
}
