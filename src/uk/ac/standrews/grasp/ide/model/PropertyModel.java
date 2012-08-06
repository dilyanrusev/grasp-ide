package uk.ac.standrews.grasp.ide.model;

public class PropertyModel extends EvaluableModel {
	
	public PropertyModel(PropertyModel other, FirstClassModel parent) {
		super(other, parent);
	}
	
	public PropertyModel(FirstClassModel parent) {
		super(ElementType.PROPERTY, parent);
	}
}
