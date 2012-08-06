package uk.ac.standrews.grasp.ide.model;

public class SystemModel extends BecauseModel {
	
	public SystemModel(SystemModel other, ArchitectureModel parent) {
		super(other, parent);
	}
	
	public SystemModel(ArchitectureModel parent) {
		super(ElementType.SYSTEM, parent);
	}
	
}
