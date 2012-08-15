package uk.ac.standrews.grasp.ide.model;

/**
 * Base class for Grasp elements to which the because keyword can be applied
 * @author Dilyan Rusev
 *
 */
public abstract class BecauseModel extends FirstClassModel {
	
	private final ObservableSet<RationaleModel> rationales = new ObservableSet<RationaleModel>();
	
	/**
	 * Create a copy of another element
	 * @param other Source
	 * @param parent Parent
	 */
	public BecauseModel(BecauseModel other, FirstClassModel parent) {
		super(other, parent);		
		copyCollectionAtTheEndOfCopy(other.getRationales(), rationales);
	}
	
	/**
	 * Construct a new element
	 * @param type Type of the element
	 * @param parent Parent
	 */
	public BecauseModel(ElementType type, FirstClassModel parent) {
		super(type, parent);
	}

	/**
	 * Return a list of rationale supporting the use of this element
	 * @return
	 */
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
