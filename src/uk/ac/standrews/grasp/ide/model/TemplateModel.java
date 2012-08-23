package uk.ac.standrews.grasp.ide.model;

/**
 * Represents a Grasp template element
 * @author Dilyan Rusev
 *
 */
public class TemplateModel extends ParameterizedModel {
	private static final String KEY_CREATED_BY_DESIGNER = "createdByDesigner";
	
	/**
	 * Construct a copy of another template
	 * @param other Template to copy
	 * @param parent Parent element
	 */
	public TemplateModel(TemplateModel other, FirstClassModel parent) {
		super(other, parent);		
		this.setDesignerCreated(other.isCreatedByDesigner());
	}
	
	/**
	 * Construct a new template
	 * @param parent Parent element
	 */
	public TemplateModel(FirstClassModel parent) {
		super(ElementType.TEMPLATE, parent);
	}
	
	/**
	 * Returns true if created by the designer, and therefore should only be u
	 * @return
	 */
	public boolean isCreatedByDesigner() {
		return Boolean.valueOf(getDesignerValue(KEY_CREATED_BY_DESIGNER)).booleanValue();
	}
	
	/**
	 * Set to true if created by the designer => only one component is using it
	 * @param isCreatedByDesigner
	 */
	public void setDesignerCreated(boolean isCreatedByDesigner) {
		setDesignerValue(KEY_CREATED_BY_DESIGNER, Boolean.toString(isCreatedByDesigner));
	}
	
	@Override
	public void elementRefactored(ElementModel element, String operation,
			Object oldValue, Object newName) {		
		super.elementRefactored(element, operation, oldValue, newName);
		if (operation != Refactor.OPERATION_RENAME && operation != Refactor.OPERATION_ALIAS_RENAME) {
			return;
		}
		String lookupName = operation == Refactor.OPERATION_RENAME
				? (String) oldValue : element.getReferencingName();
		if (element instanceof InterfaceModel && symLookup(lookupName)) {
			ElementModel myCopy = symGet(lookupName);
			if (operation == Refactor.OPERATION_RENAME) {
				myCopy.setName((String) newName);
			} else {
				myCopy.setAlias((String) newName);
			}
		}
		
	}
}
