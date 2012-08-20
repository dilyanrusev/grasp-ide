package uk.ac.standrews.grasp.ide.model.properties;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.ui.views.properties.IPropertyDescriptor;

import uk.ac.standrews.grasp.ide.model.ElementModel;

/**
 * Property source for {@link uk.ac.standrews.grasp.ide.model.ElementModel}
 * @author Dilyan Rusev
 *
 * @param <T> Model class for which this source returns properties
 */
public class ElementPropertySource<T extends ElementModel> 
		extends AbstractElementPropertySource<T> {
	/**
	 * Construct a new property source
	 * @param model Model to bind to
	 */
	public ElementPropertySource(T model) {
		super(model);
	}
	
	@Override
	protected List<IPropertyDescriptor> createDescriptors() {
		List<IPropertyDescriptor> allProps = new ArrayList<IPropertyDescriptor>(1);
		
		allProps.add(
				text(ElementModel.PROPERTY_NAME, "Name")
				.description("Type of this Grasp element")
				.category(getModel().getType().getDisplayName())
				.build());
		
		return allProps;
	}
	
	@Override
	public Object getPropertyValue(Object id) {
		if (ElementModel.PROPERTY_NAME == id) {
			return getModel().getName();
		}
		return super.getPropertyValue(id);
	}
	
	@Override
	public void setPropertyValue(Object id, Object value) {
		super.setPropertyValue(id, value);
		if (ElementModel.PROPERTY_NAME == id) {
			getModel().setName((String) value);
		}
	}
}
