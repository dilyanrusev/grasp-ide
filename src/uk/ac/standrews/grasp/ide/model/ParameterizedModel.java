package uk.ac.standrews.grasp.ide.model;

import java.util.List;

import grasp.lang.IExtensible;
import grasp.lang.IFirstClass;
import grasp.lang.IHasParameters;

public abstract class ParameterizedModel extends ExtensibleModel implements
		IHasParameters {

	private final List<String> params = new ObservableList<String>();
	
	public ParameterizedModel(IHasParameters other, IFirstClass parent) {
		super((IExtensible) other, parent);
		params.addAll(other.getParameters());
	}
	
	public ParameterizedModel(ElementType type, IFirstClass parent) {
		super(type, parent);
	}
	
	@Override
	public void addParameter(String parameter) {
		symPut(parameter, null);
		params.add(parameter);
	}

	@Override
	public List<String> getParameters() {
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
