package uk.ac.standrews.grasp.ide.model.properties;

import java.util.List;

import org.eclipse.jface.util.Util;
import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import uk.ac.standrews.grasp.ide.editors.TextUtil;
import uk.ac.standrews.grasp.ide.model.InterfaceModel;
import uk.ac.standrews.grasp.ide.model.Refactor;

/**
 * Common property source for interfaces (provides and requires)
 * @author Dilyan Rusev
 *
 */
public class InterfacePropertySource extends ElementPropertySource<InterfaceModel> {

	/**
	 * Construct a new property source
	 * @param model Model to bind to
	 */
	public InterfacePropertySource(InterfaceModel model) {
		super(model);		
	}
	
	@Override
	protected List<IPropertyDescriptor> createDescriptors() {
		List<IPropertyDescriptor> allProps = super.createDescriptors();
		
		allProps.add(
				text(InterfaceModel.PROPERTY_ALIAS, "Alias")
				.category(getModel().getType().getDisplayName())
				.validator(new ICellEditorValidator() {					
					@Override
					public String isValid(Object value) {
						if (!(value instanceof String)) {
							return "Must be text";
						}
						String text = (String) value;
						if (!TextUtil.isIdentifier(text)) {
							return "Not a valid Grasp identifier";
						}
						if (getModel().getArchitecture() != null) {
							String myQualifiedName = getModel().getQualifiedName();
							String nextQName = myQualifiedName.substring(myQualifiedName.lastIndexOf('.') + 1);
							nextQName = nextQName + "." + text;
							if (getModel().getArchitecture().findByQualifiedName(nextQName) != null) {
								return "There is already an element with name " + nextQName;
							}
						}
						return null;
					}
				})
				.build());
		
		return allProps;
	}
	
	@Override
	public Object getPropertyValue(Object id) {
		if (InterfaceModel.PROPERTY_ALIAS == id) {
			return getModel().getAlias() != null ? getModel().getAlias() : Util.ZERO_LENGTH_STRING;
		}
		return super.getPropertyValue(id);
	}
	
	@Override
	public void setPropertyValue(Object id, Object value) {
		if (InterfaceModel.PROPERTY_ALIAS == id) {			
			Refactor.changeAlias(getModel(), (String) value);
		}
		super.setPropertyValue(id, value);
	}
	
}
