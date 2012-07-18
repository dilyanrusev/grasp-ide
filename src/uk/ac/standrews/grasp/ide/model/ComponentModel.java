package uk.ac.standrews.grasp.ide.model;

import grasp.lang.IComponent;
import grasp.lang.IFirstClass;

public class ComponentModel extends InstantiableModel implements IComponent {
	
	public ComponentModel(IComponent other, IFirstClass parent) {
		super(other, parent);
	}
	
	public ComponentModel(IFirstClass parent) {
		super(ElementType.COMPONENT, parent);
	}
}
