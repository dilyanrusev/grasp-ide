package uk.ac.standrews.grasp.ide.model;

public class QualityAttributeModel extends FirstClassModel {
	
	private final ObservableSet<FirstClassModel> supports = new ObservableSet<FirstClassModel>();
	
	public QualityAttributeModel(QualityAttributeModel other, FirstClassModel parent) {
		super(other, parent);
		copyCollectionAtTheEndOfCopy(other.getSupports(), supports);
	}
	
	public QualityAttributeModel(FirstClassModel parent) {
		super(ElementType.QUALITY_ATTRIBUTE, parent);
	}

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
