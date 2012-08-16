package uk.ac.standrews.grasp.ide.model;

/**
 * Represents a Grasp template element
 * @author Dilyan Rusev
 *
 */
public class TemplateModel extends ParameterizedModel {
	
	/**
	 * Construct a copy of another template
	 * @param other Template to copy
	 * @param parent Parent element
	 */
	public TemplateModel(TemplateModel other, FirstClassModel parent) {
		super(other, parent);		
	}
	
	/**
	 * Construct a new template
	 * @param parent Parent element
	 */
	public TemplateModel(FirstClassModel parent) {
		super(ElementType.TEMPLATE, parent);
	}
}
