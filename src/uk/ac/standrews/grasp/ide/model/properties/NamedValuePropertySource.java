package uk.ac.standrews.grasp.ide.model.properties;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.ui.views.properties.IPropertyDescriptor;

import uk.ac.standrews.grasp.ide.model.NamedValueModel;

/**
 * Property source for named values
 * @author Dilyan Rusev
 *
 */
public class NamedValuePropertySource 
		extends AbstractElementPropertySource<NamedValueModel> {

	/**
	 * Create a new property source
	 * @param model Model to bind to
	 */
	public NamedValuePropertySource(NamedValueModel model) {
		super(model);		
	}

	@Override
	protected List<IPropertyDescriptor> createDescriptors() {
		List<IPropertyDescriptor> allProps = new ArrayList<IPropertyDescriptor>(2);
		
		allProps.add(
				text(NamedValueModel.PROPERTY_NAME, "Handler")
				.category(getModel().getType().getDisplayName())
				.build());
		
		allProps.add(
				text(NamedValueModel.PROPERTY_EXPRESSION, "Expression")
				.category(getModel().getType().getDisplayName())
				.build());
		
		allProps.add(
				readOnly(NamedValueModel.PROPERTY_VALUE, "Value")
				.category(getModel().getType().getDisplayName())
				.build());
		
		return allProps;
	}
	
	@Override
	public Object getPropertyValue(Object id) {
		if (NamedValueModel.PROPERTY_NAME == id) {
			return getModel().getName();
		}
		if (NamedValueModel.PROPERTY_EXPRESSION == id) {
			return getModel().getExpression().getText();
		}
		if (NamedValueModel.PROPERTY_VALUE == id) {
			return getModel().getExpression().getValue();
		}
		return super.getPropertyValue(id);
	}
	
	@Override
	public void setPropertyValue(Object id, Object value) {		
		super.setPropertyValue(id, value);
		if (NamedValueModel.PROPERTY_NAME == id) {
			getModel().setName((String) value);
		}
//		if (NamedValueModel.PROPERTY_EXPRESSION == id) {
//			getModel().setExpression()
//		}		
	}
}
