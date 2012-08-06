package uk.ac.standrews.grasp.ide.model;

public abstract class BecauseModel extends FirstClassModel {
	
	private final ObservableSet<RationaleModel> rationales = new ObservableSet<RationaleModel>();
	
	public BecauseModel(BecauseModel other, FirstClassModel parent) {
		super(other, parent);		
		copyCollectionAtTheEndOfCopy(other.getRationales(), rationales);
	}
	
	public BecauseModel(ElementType type, FirstClassModel parent) {
		super(type, parent);
	}

	public ObservableSet<RationaleModel> getRationales() {
		return rationales;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj)) return false;
		BecauseModel other = (BecauseModel) obj;
		
		if (!collectionsEqual(getRationales(), other.getRationales()));
		
		return true;
	}
	
	@Override
	public int hashCode() {
		int result = super.hashCode();
		
		for (RationaleModel rationale: getRationales()) {
			result = 31 * result + rationale.hashCode();
		}
		
		return result;
	}

}
