package uk.ac.standrews.grasp.ide.model;

import java.util.List;

import grasp.lang.IFirstClass;
import grasp.lang.IRationale;
import grasp.lang.IReason;

public class RationaleModel extends ParameterizedModel implements IRationale {
	private final List<IReason> reasons = new ObservableList<IReason>();
	
	public RationaleModel(IRationale other, IFirstClass parent) {
		super(other, parent);
		for (IReason reason: other.getReasons()) {
			IReason observable = new ReasonModel(reason, this);
			reasons.add(observable);
		}
	}
	
	public RationaleModel(IFirstClass parent) {
		super(ElementType.RATIONALE, parent);
	}

	@Override
	public List<IReason> getReasons() {
		return reasons;
	}

}