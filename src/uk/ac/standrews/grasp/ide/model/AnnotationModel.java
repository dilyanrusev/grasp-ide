package uk.ac.standrews.grasp.ide.model;

public class AnnotationModel extends ElementModel {	
	private ObservableSet<NamedValueModel> namedValues = new ObservableSet<NamedValueModel>();
	
	public AnnotationModel(AnnotationModel other, ElementModel parent) {
		super(other, parent);		
		for (NamedValueModel namedValue: other.getNamedValues()) {
			NamedValueModel observable = new NamedValueModel(namedValue, this);
			namedValues.add(observable);
		}
	}
	
	public AnnotationModel(ElementModel parent) {
		super(ElementType.ANNOTATION, parent);
	}
	
	public ObservableSet<NamedValueModel> getNamedValues() {
		return namedValues;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj)) return false;
		AnnotationModel other = (AnnotationModel) obj;
		
		if (!collectionsEqual(getNamedValues(), other.getNamedValues())) return false;
		
		return true;
	}
	
	@Override
	public int hashCode() {
		int result = super.hashCode();		
		for (NamedValueModel nv: getNamedValues()) {
			result = 31 * result + nv.hashCode();
		}
		return result;
	}

	@Override
	public ElementModel removeFromParent() {
		if (getParent() instanceof FirstClassModel) {
			FirstClassModel theParent = (FirstClassModel) getParent();
			if (theParent.getAnnotations().remove(this)) {
				return theParent;
			}
		}
		return null;
	}

	@Override
	public boolean addChildElement(ElementModel child) {
		if (child instanceof NamedValueModel) {
			return getNamedValues().add((NamedValueModel) child);
		}
		return false;
	}
}
