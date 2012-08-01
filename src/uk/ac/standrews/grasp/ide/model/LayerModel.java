package uk.ac.standrews.grasp.ide.model;

import grasp.lang.IFirstClass;
import grasp.lang.ILayer;

import java.util.List;

public class LayerModel extends BecauseModel implements ILayer {
	
	private List<ILayer> over = new ObservableList<ILayer>();
	
	public LayerModel(ILayer other, IFirstClass parent) {
		super(other, parent);
		for (ILayer child: over) {
			over.add(new LayerModel(child, this));
		}
	}
	
	public LayerModel(IFirstClass parent) {
		super(ElementType.LAYER, parent);
	}

	@Override
	public List<ILayer> getOver() {
		return over;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj)) return false;
		return collectionsEqual(getOver(), ((LayerModel) obj).getOver());
	}
	
	@Override
	public int hashCode() {
		int result = super.hashCode();
		
		for (ILayer layer: getOver()) {
			result = 31 * result + layer.hashCode();
		}
		
		return result;
	}

}
