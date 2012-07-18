package uk.ac.standrews.grasp.ide.model;

import java.util.List;

import grasp.lang.IFirstClass;
import grasp.lang.IQualityAttribute;

public class QualityAttributeModel extends FirstClassModel implements
		IQualityAttribute {
	
	private final List<IFirstClass> supports = new ObservableList<IFirstClass>();
	
	public QualityAttributeModel(IQualityAttribute other, IFirstClass parent) {
		super(other, parent);
		for (IFirstClass child: other.getSupports()) {
			IFirstClass observable = (IFirstClass) GraspModel.INSTANCE.makeObservable(child, this);
			supports.add(observable);
		}
	}
	
	public QualityAttributeModel(IFirstClass parent) {
		super(ElementType.QUALITY_ATTRIBUTE, parent);
	}

	@Override
	public List<IFirstClass> getSupports() {
		return supports;
	}

}
