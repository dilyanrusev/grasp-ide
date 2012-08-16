package uk.ac.standrews.grasp.ide.model;

/**
 * Base class for Grasp elements that can be instantiated with parameters - most notably, templates
 * @author Dilyan Rusev
 *
 */
public abstract class ParameterizedModel extends ExtensibleModel {

	private final ObservableSet<String> params = new ObservableSet<String>();
	
	/**
	 * Create a copy of another element
	 * @param other Element to copy
	 * @param parent Parent element
	 */
	public ParameterizedModel(ParameterizedModel other, FirstClassModel parent) {
		super(other, parent);
		params.addAll(other.getParameters());
	}

	/**
	 * Create a new elmenet	
	 * @param type Type of the element
	 * @param parent Parent element
	 */
	public ParameterizedModel(ElementType type, FirstClassModel parent) {
		super(type, parent);
	}
	
	/**
	 * Add parameter
	 * @param parameter Parameter to be replaced during instantiation
	 */
	public void addParameter(String parameter) {
		symPut(parameter, null);
		params.add(parameter);
	}

	/**
	 * Return a list of parameters to be replaced during instantiation
	 * @return
	 */
	public ObservableSet<String> getParameters() {
		return params;
	}
	
	@Override
	public boolean equals(Object obj) {
		return super.equals(obj) && 
				collectionsEqual(getParameters(), ((ParameterizedModel) obj).getParameters());
	}
	
	@Override
	public int hashCode() {
		int result = super.hashCode();
		
		for (String param: getParameters()) {
			result = 31 * result + param.hashCode();
		}
		
		return result;
	}

}
