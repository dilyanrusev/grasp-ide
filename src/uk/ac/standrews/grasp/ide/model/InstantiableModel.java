package uk.ac.standrews.grasp.ide.model;

public abstract class InstantiableModel extends BecauseModel {
	public static final String PROPERTY_BASE = "base";
	
	private final ObservableSet<FirstClassModel> arguments = new ObservableSet<FirstClassModel>();
	private TemplateModel base;
	
	public InstantiableModel(InstantiableModel other, FirstClassModel parent) {
		super(other, parent);
		if (other.getBase() != null) {
			final String qualifiedName = other.getBase().getQualifiedName();			
			getArchitecture().executeAtTheEndOfCopy(new Runnable() {				
				@Override
				public void run() {
					base = (TemplateModel) getArchitecture().findByQualifiedName(qualifiedName);
				}
			});
		}
		copyCollectionAtTheEndOfCopy(other.getArguments(), arguments);
	}
	
	public InstantiableModel(ElementType type, FirstClassModel parent) {
		super(type, parent);
	}

	public ObservableSet<FirstClassModel> getArguments() {
		return arguments;
	}

	public TemplateModel getBase() {
		return base;
	}

	public void setBase(TemplateModel base) {
		this.base = base;
		fireElementChanged(PROPERTY_BASE);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj)) return false;
		InstantiableModel other = (InstantiableModel) obj;
		
		if (!objectsEqual(getBase(), other.getBase())) return false;
		if (!collectionsEqual(getArguments(), other.getArguments())) return false;
		
		return true;
	}
	
	@Override
	public int hashCode() {
		int result = super.hashCode();
		
		result = 31 * result + (getBase() != null ? getBase().hashCode() : 0);
		for (FirstClassModel arg: getArguments()) {
			result = 31 * result + arg.hashCode();
		}
		
		return result;
	}

}
