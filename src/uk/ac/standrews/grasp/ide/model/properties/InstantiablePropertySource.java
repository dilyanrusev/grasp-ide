package uk.ac.standrews.grasp.ide.model.properties;

import java.util.List;

import org.eclipse.ui.views.properties.IPropertyDescriptor;

import uk.ac.standrews.grasp.ide.model.InstantiableModel;
import uk.ac.standrews.grasp.ide.model.Refactor;

/**
 * Property source for instantiable model objects (components and connectors)
 * @author Dilyan Rusev
 *
 */
public class InstantiablePropertySource extends ElementPropertySource<InstantiableModel> {
	private static final String PROPERTY_TEMPLATE_NAME = "templateName";

	/**
	 * Create a new property source
	 * @param model Model to bind to
	 */
	public InstantiablePropertySource(InstantiableModel model) {
		super(model);		
	}
	
	@Override
	protected List<IPropertyDescriptor> createDescriptors() {
		List<IPropertyDescriptor> allProps = super.createDescriptors();
		
		allProps.add(
				identifier(PROPERTY_TEMPLATE_NAME, "Template name")
				.category(getModel().getType().getDisplayName())				
				.build());
		
		return allProps;
	}
	
	@Override
	public Object getPropertyValue(Object id) {
		if (PROPERTY_TEMPLATE_NAME == id) {
			return getModel().getBase().getName();
		}
		return super.getPropertyValue(id);
	}
	
	@Override
	public void setPropertyValue(Object id, Object value) {
		super.setPropertyValue(id, value);
		if (PROPERTY_TEMPLATE_NAME == id) {
			Refactor.rename(getModel().getBase(), (String) value);
		}
	}

}
