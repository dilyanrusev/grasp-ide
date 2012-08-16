package uk.ac.standrews.grasp.ide.model;

/**
 * Represents Grasp rationale elements
 * @author Dilyan Rusev
 *
 */
public class RationaleModel extends ParameterizedModel {
	private final ObservableSet<ReasonModel> reasons = new ObservableSet<ReasonModel>();
	
	/**
	 * Construct a copy of another rationale 
	 * @param other Rationale to copy
	 * @param parent Parent element
	 */
	public RationaleModel(RationaleModel other, FirstClassModel parent) {
		super(other, parent);
		copyCollectionAtTheEndOfCopy(other.getReasons(), reasons);
	}
	
	/**
	 * Construct a new rationale
	 * @param parent Parent element
	 */
	public RationaleModel(FirstClassModel parent) {
		super(ElementType.RATIONALE, parent);
	}

	/**
	 * Return a list of reasons supporting this rationale
	 * @return
	 */
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
