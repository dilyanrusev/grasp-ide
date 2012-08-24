package uk.ac.standrews.grasp.ide.model;

/**
 * Represents Grasp quality_attribute element
 * @author Dilyan Rusev
 *
 */
public class QualityAttributeModel extends FirstClassModel {
	
	private final ObservableSet<FirstClassModel> supports = new ObservableSet<FirstClassModel>();
	
	/**
	 * Construct a copy of another quality attribute element
	 * @param other Element to copy
	 * @param parent Parent element
	 */
	public QualityAttributeModel(QualityAttributeModel other, FirstClassModel parent) {
		super(other, parent);
		copyCollectionAtTheEndOfCopy(other.getSupports(), supports);
	}
	
	/**
	 * Construct a new quality attribute
	 * @param parent Parent element
	 */
	public QualityAttributeModel(FirstClassModel parent) {
		super(ElementType.QUALITY_ATTRIBUTE, parent);
	}

	/**
	 * Return a list of references to elements that support the use of this quality attribute
	 * @return Support statements
	 */
	public ObservableSet<FirstClassModel> getSupports() {
		return supports;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj)) return false;
		QualityAttributeModel other = (QualityAttributeModel) obj;
		
		if (!collectionsEqual(getSupports(), other.getSupports()));
		
		return true;
	}
	
	@Override
	public int hashCode() {
		int result = super.hashCode();
		
		for (FirstClassModel supported: getSupports()) {
			result = 31 * result + supported.hashCode();
		}
		
		return result;
	}

}
