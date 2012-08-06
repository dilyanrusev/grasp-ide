package uk.ac.standrews.grasp.ide.model;

public abstract class ExtensibleModel extends BecauseModel {
	public static final String PROPERTY_EXTENDEE = "extendee";
	
	private FirstClassModel extendee;
	
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
	
	public ExtensibleModel(ElementType type, FirstClassModel parent) {
		super(type, parent);
	}

	public FirstClassModel getExtendee() {
		return extendee;
	}

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
