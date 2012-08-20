package uk.ac.standrews.grasp.ide.model.properties;

import java.util.List;

import org.eclipse.ui.views.properties.IPropertyDescriptor;

import uk.ac.standrews.grasp.ide.model.AnnotationModel;
import uk.ac.standrews.grasp.ide.model.NamedValueModel;

/**
 * Property source for annotations
 * @author Dilyan Rusev
 *
 */
public class AnnotationPropertySource extends ElementPropertySource<AnnotationModel> {
	
	/**
	 * Construct a new property source
	 * @param model Model to bind to
	 */
	public AnnotationPropertySource(AnnotationModel model) {
		super(model);
	}
	
	@Override
	protected List<IPropertyDescriptor> createDescriptors() {
		List<IPropertyDescriptor> allProps = super.createDescriptors();
		
		allProps.add(
				readOnly(AnnotationModel.PROPERTY_NAMED_VALUES, "Named values")
				.category(getModel().getType().getDisplayName())
				.build());
		
		return allProps;
	}
	
	@Override
	public Object getPropertyValue(Object id) {
		if (AnnotationModel.PROPERTY_NAMED_VALUES == id) {
			return new CollectionPropertySource<NamedValueModel>("Value", getModel().getNamedValues());
		}
		return super.getPropertyValue(id);
	}
}
