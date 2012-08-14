package uk.ac.standrews.grasp.ide.model;

public class LayerModel extends BecauseModel {
	
	private ObservableSet<LayerModel> over = new ObservableSet<LayerModel>();
	
	public LayerModel(LayerModel other, FirstClassModel parent) {
		super(other, parent);
		copyCollectionAtTheEndOfCopy(other.getOver(), over);
	}
	
	public LayerModel(FirstClassModel parent) {
		super(ElementType.LAYER, parent);
	}

	public ObservableSet<LayerModel> getOver() {
		return over;
	}
	
	@Override
	protected int doCompareTo(ElementModel elem) {
		LayerModel other = (LayerModel) elem;
		if (this.getOver().contains(other)) {
			return 1;
		}
		if (other.getOver().contains(this)) {
			return -1;
		}
		return super.doCompareTo(other);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj)) return false;
		return collectionsEqual(getOver(), ((LayerModel) obj).getOver());
	}
	
	@Override
	public int hashCode() {
		int result = super.hashCode();
		
		for (LayerModel layer: getOver()) {
			result = 31 * result + layer.hashCode();
		}
		
		return result;
	}

}
