package uk.ac.standrews.grasp.ide.model.properties;

import java.util.List;

import org.eclipse.ui.views.properties.IPropertyDescriptor;

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
				readOnly(SystemModel.PROPERTY_ARCHITECTURE, "Architecture")
				.category(getModel().getType().getDisplayName())
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

}
