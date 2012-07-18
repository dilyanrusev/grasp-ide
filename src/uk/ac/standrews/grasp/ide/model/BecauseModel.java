package uk.ac.standrews.grasp.ide.model;

import grasp.lang.IBecause;
import grasp.lang.IFirstClass;
import grasp.lang.IRationale;

import java.util.List;

public abstract class BecauseModel extends FirstClassModel implements IBecause {
	
	private final List<IRationale> rationales = new ObservableList<IRationale>();
	
	public BecauseModel(IBecause other, IFirstClass parent) {
		super((IFirstClass)other, parent);		
		for (IRationale child: other.getRationales()) {
			IRationale observable = new RationaleModel(child);
			rationales.add(observable);
		}
	}
	
	public BecauseModel(ElementType type, IFirstClass parent) {
		super(type, parent);
	}

	@Override
	public List<IRationale> getRationales() {
		return rationales;
	}

}
