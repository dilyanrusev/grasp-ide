package uk.ac.standrews.grasp.ide.model.properties;

import org.eclipse.jface.viewers.ICellEditorValidator;

import uk.ac.standrews.grasp.ide.editors.TextUtil;
import uk.ac.standrews.grasp.ide.model.ElementModel;

public class ElementNameValidator implements ICellEditorValidator {
	private final ElementModel model;
	
	public ElementNameValidator(ElementModel model) {
		this.model = model;
	}
	
	@Override
	public String isValid(Object value) {
		if (!(value instanceof String)) {
			return "Must be text";
		}
		String text = (String) value;
		if (!TextUtil.isIdentifier(text)) {
			return "Not a valid Grasp identifier";
		}
		if (model.getArchitecture() != null) {
			String myQualifiedName = model.getQualifiedName();
			String nextQName = myQualifiedName.substring(myQualifiedName.lastIndexOf('.') + 1);
			nextQName = nextQName + "." + text;
			if (model.getArchitecture().findByQualifiedName(nextQName) != null) {
				return "There is already an element with name " + nextQName;
			}
		}
		return null;
	}	
}
