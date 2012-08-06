package uk.ac.standrews.grasp.ide.model;

public class CheckModel extends EvaluableModel {
	public CheckModel(CheckModel other, FirstClassModel parent) {
		super(other, parent);
	}
	
	public CheckModel(FirstClassModel parent) {
		super(ElementType.CHECK, parent);
	}	
}
