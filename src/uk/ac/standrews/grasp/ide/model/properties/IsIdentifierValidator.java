package uk.ac.standrews.grasp.ide.model.properties;

import org.eclipse.jface.viewers.ICellEditorValidator;

import uk.ac.standrews.grasp.ide.editors.TextUtil;

/**
 * Cell validator that makes certain the input value is a valid Grasp identifier
 * @author Dilyan Rusev
 *
 */
public class IsIdentifierValidator implements ICellEditorValidator {
	/** Singleton instance - for convenience */
	public static IsIdentifierValidator INSTANCE =
			new IsIdentifierValidator();
	
	@Override
	public String isValid(Object value) {
		if (!TextUtil.isIdentifier((String) value)) {
			return "Not a valid Grasp identifier";
		} else {
			return null;
		}
	}		
}
