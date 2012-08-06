package uk.ac.standrews.grasp.ide.model;

public abstract class ParameterizedModel extends ExtensibleModel {

	private final ObservableSet<String> params = new ObservableSet<String>();
	
	public ParameterizedModel(ParameterizedModel other, FirstClassModel parent) {
		super(other, parent);
		params.addAll(other.getParameters());
	}
	
	public ParameterizedModel(ElementType type, FirstClassModel parent) {
		super(type, parent);
	}
	
	public void addParameter(String parameter) {
		symPut(parameter, null);
		params.add(parameter);
	}

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
