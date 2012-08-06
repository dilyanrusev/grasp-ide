package uk.ac.standrews.grasp.ide.model;

public class RationaleModel extends ParameterizedModel {
	private final ObservableSet<ReasonModel> reasons = new ObservableSet<ReasonModel>();
	
	public RationaleModel(RationaleModel other, FirstClassModel parent) {
		super(other, parent);
		copyCollectionAtTheEndOfCopy(other.getReasons(), reasons);
	}
	
	public RationaleModel(FirstClassModel parent) {
		super(ElementType.RATIONALE, parent);
	}

	public ObservableSet<ReasonModel> getReasons() {
		return reasons;
	}
	
	@Override
	public boolean equals(Object obj) {
		return super.equals(obj) &&
				collectionsEqual(getReasons(), ((RationaleModel) obj).getReasons());
	}
	
	@Override
	public int hashCode() {
		int result = super.hashCode();
		
		for (ReasonModel reason: getReasons()) {
			result = 31 * result + reason.hashCode();
		}
		
		return result;
	}

}
