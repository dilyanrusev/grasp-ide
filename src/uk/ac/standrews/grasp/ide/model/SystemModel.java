package uk.ac.standrews.grasp.ide.model;

import grasp.lang.IFirstClass;
import grasp.lang.ISystem;

public class SystemModel extends BecauseModel implements ISystem {
	
	public SystemModel(ISystem other, IFirstClass parent) {
		super(other, parent);
	}
	
	public SystemModel(IFirstClass parent) {
		super(ElementType.SYSTEM, parent);
	}
	
}
