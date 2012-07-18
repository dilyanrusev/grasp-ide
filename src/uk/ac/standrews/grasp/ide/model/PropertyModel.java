package uk.ac.standrews.grasp.ide.model;

import grasp.lang.IFirstClass;
import grasp.lang.IProperty;

public class PropertyModel extends EvaluableModel implements IProperty {
	
	public PropertyModel(IProperty other, IFirstClass parent) {
		super(other, parent);
	}
	
	public PropertyModel(IFirstClass parent) {
		super(ElementType.PROPERTY, parent);
	}
}
