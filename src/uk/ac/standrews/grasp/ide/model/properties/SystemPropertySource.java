package uk.ac.standrews.grasp.ide.model.properties;

import java.util.List;

import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import uk.ac.standrews.grasp.ide.editors.TextUtil;
import uk.ac.standrews.grasp.ide.model.Refactor;
import uk.ac.standrews.grasp.ide.model.SystemModel;

/**
 * Property source for the System element
 * @author Dilyan Rusev
 *
 */
public class SystemPropertySource extends ElementPropertySource<SystemModel> {

	/**
	 * Create a new property source
	 * @param model Model to bind to
	 */
	public SystemPropertySource(SystemModel model) {
		super(model);		
	}
	
	@Override
	protected List<IPropertyDescriptor> createDescriptors() {
		List<IPropertyDescriptor> allProps = super.createDescriptors();
		
		allProps.add(
				text(SystemModel.PROPERTY_ARCHITECTURE, "Architecture")
				.category(getModel().getType().getDisplayName())
				.validator(new ICellEditorValidator() {					
					@Override
					public String isValid(Object value) {
						if (!(value instanceof String)) return "Must be text";
						String txt = (String) value;
						if (TextUtil.isNullOrWhitespace(txt)) return "Must not be empty or whitespace";
						if (!TextUtil.isIdentifier(txt)) return "Must be valid Grasp identifier";
						return null;
					}
				})
				.build());
		
		return allProps;
	}
	
	@Override
	public Object getPropertyValue(Object id) {
		if (SystemModel.PROPERTY_ARCHITECTURE == id) {
			return getModel().getArchitecture().getName();
		}
		return super.getPropertyValue(id);
	}
	
	@Override
	public void setPropertyValue(Object id, Object value) {		
		super.setPropertyValue(id, value);
		if (SystemModel.PROPERTY_ARCHITECTURE == id) {
			Refactor.rename(getModel().getArchitecture(), (String) value);
		}
	}

}
