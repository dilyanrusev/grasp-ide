package uk.ac.standrews.grasp.ide.model;

public class ComponentModel extends InstantiableModel {
	
	public ComponentModel(ComponentModel other, FirstClassModel parent) {
		super(other, parent);
	}
	
	public ComponentModel(FirstClassModel parent) {
		super(ElementType.COMPONENT, parent);
	}
}
