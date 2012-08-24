package uk.ac.standrews.grasp.ide.model;

/**
 * Base class for Grasp elements that support the extends keyword; most notably - templates
 * @author Dilyan Rusev
 *
 */
public abstract class ExtensibleModel extends BecauseModel {
	/** Reference to the extended element */
	public static final String PROPERTY_EXTENDEE = "extendee";
	
	private FirstClassModel extendee;
	
	/**
	 * Construct a copy of another element
	 * @param other Source
	 * @param parent Parent element
	 */
	public ExtensibleModel(ExtensibleModel other, FirstClassModel parent) {
		super(other, parent);
		if (other.getExtendee() != null) {
			final String qname = other.getExtendee().getQualifiedName();
			getArchitecture().executeAtTheEndOfCopy(new Runnable() {				
				@Override
				public void run() {
					extendee = (FirstClassModel) getArchitecture().findByQualifiedName(qname);
				}
			});
		}
	}
	
	/**
	 * Construct a new element
	 * @param type Type of the element
	 * @param parent Parent of the element
	 */
	public ExtensibleModel(ElementType type, FirstClassModel parent) {
		super(type, parent);
	}

	/**
	 * Get a reference to the extended element
	 * @return extended element
	 */
	public FirstClassModel getExtendee() {
		return extendee;
	}

	/**
	 * Set a reference to the extended element
	 * @param extendee New extended element
	 */
	public void setExtendee(FirstClassModel extendee) {
		this.extendee = extendee;
		fireElementChanged(PROPERTY_EXTENDEE);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj))	 return false;
		return objectsEqual(getExtendee(), ((ExtensibleModel) obj).getExtendee());
	}
	
	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (getExtendee() != null ? getExtendee().hashCode() : 0);
		return result;
	}

}
