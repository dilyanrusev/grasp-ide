package uk.ac.standrews.grasp.ide.model;

public class TemplateModel extends ParameterizedModel {
	
	public TemplateModel(TemplateModel other, FirstClassModel parent) {
		super(other, parent);		
	}
	
	public TemplateModel(FirstClassModel parent) {
		super(ElementType.TEMPLATE, parent);
	}
}
