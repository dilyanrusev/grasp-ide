package uk.ac.standrews.grasp.ide.model;

/**
 * Base class for Grasp elements that can be instantiated from a template
 * @author Dilyan Rusev
 *
 */
public abstract class InstantiableModel extends BecauseModel {
	/**
	 * Reference to the template from which the element was created
	 */
	public static final String PROPERTY_BASE = "base";
	
	private final ObservableSet<FirstClassModel> arguments = new ObservableSet<FirstClassModel>();
	private TemplateModel base;
	
	/**
	 * Creates a copy of another element
	 * @param other Source
	 * @param parent Parent element
	 */
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
	
	/**
	 * Create a new element
	 * @param type Type of the element
	 * @param parent Parent element
	 */
	public InstantiableModel(ElementType type, FirstClassModel parent) {
		super(type, parent);
	}

	/**
	 * Get list of rererences ot the arguments that were used to instantiate the template
	 * @return
	 */
	public ObservableSet<FirstClassModel> getArguments() {
		return arguments;
	}

	/**
	 * Get the template which was instantiated into this element
	 * @return
	 */
	public TemplateModel getBase() {
		return base;
	}

	/**
	 * Set the template that was used for instantiation
	 * @param base Template
	 */
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
