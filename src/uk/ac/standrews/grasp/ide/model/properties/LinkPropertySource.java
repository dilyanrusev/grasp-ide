package uk.ac.standrews.grasp.ide.model.properties;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.ui.views.properties.IPropertyDescriptor;

import uk.ac.standrews.grasp.ide.model.LinkModel;

/**
 * Property source for links
 * @author Dilyan Rusev
 *
 */
public class LinkPropertySource extends AbstractElementPropertySource<LinkModel> {
	
	/**
	 * ID for the interface property
	 */
	public static final String PROPERTY_INTERFACE = "interface";

	/**
	 * Create a new property source
	 * @param model Model to bind to
	 */
	public LinkPropertySource(LinkModel model) {
		super(model);		
	}

	@Override
	protected List<IPropertyDescriptor> createDescriptors() {
		List<IPropertyDescriptor> allProps = new ArrayList<IPropertyDescriptor>(3);
		
		allProps.add(
				readOnly(PROPERTY_INTERFACE, "Interface")
				.category(getModel().getType().getDisplayName())
				.build());
		
		allProps.add(
				readOnly(LinkModel.PROPERTY_CONSUMER, "Consumer")
				.category(getModel().getType().getDisplayName())
				.build());
		
		allProps.add(
				readOnly(LinkModel.PROPERTY_PROVIDER, "Provider")
				.category(getModel().getType().getDisplayName())
				.build());
		
		return allProps;
	}
	
	@Override
	public Object getPropertyValue(Object id) {
		if (LinkModel.PROPERTY_CONSUMER == id) {
			return getModel().getConsumer().getParent().getDisplayName();
		} else if (LinkModel.PROPERTY_PROVIDER == id) {
			return getModel().getProvider().getParent().getDisplayName();
		} else if (PROPERTY_INTERFACE == id) {
			return getModel().getConsumer().getName();
		}
		return super.getPropertyValue(id);
	}

}
