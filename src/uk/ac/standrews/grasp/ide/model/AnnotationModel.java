package uk.ac.standrews.grasp.ide.model;



/**
 * Represents Grasp annotations
 * @author Dilyan Rusev
 *
 */
public class AnnotationModel extends ElementModel {	
	public static final String PROPERTY_NAMED_VALUES = "namedValues";
	public static final String CATEGORY_ANNOTATION = "Annotation";
	
	private ObservableSet<NamedValueModel> namedValues = new ObservableSet<NamedValueModel>();
	
	/**
	 * Create a copy of another annotation
	 * @param other Source
	 * @param parent Parent
	 */
	public AnnotationModel(AnnotationModel other, ElementModel parent) {
		super(other, parent);		
		for (NamedValueModel namedValue: other.getNamedValues()) {
			NamedValueModel observable = new NamedValueModel(namedValue, this);
			namedValues.add(observable);
		}
	}
	
	/**
	 * Create a new annotation
	 * @param parent Parent
	 */
	public AnnotationModel(ElementModel parent) {
		super(ElementType.ANNOTATION, parent);
	}
	
	/**
	 * Return the values stored in this annotation
	 * @return Named values
	 */
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

	@Override
	public void elementRefactored(ElementModel element, String operation, 
			Object oldValue, Object newName) {		
		if (element != this && getNamedValues().contains(element)) {
			fireElementChanged(PROPERTY_NAMED_VALUES);
		}		
	}
}
