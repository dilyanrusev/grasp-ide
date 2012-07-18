package uk.ac.standrews.grasp.ide.model;

import grasp.lang.IArchitecture;

public class ArchitectureModel extends FirstClassModel implements IArchitecture {
	public ArchitectureModel() {
		super(ElementType.ARCHITECTURE, null);
	}
	
	public ArchitectureModel(IArchitecture other) {
		super(other, null);
	}
}
