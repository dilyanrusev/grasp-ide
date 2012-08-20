package uk.ac.standrews.grasp.ide.model.properties;

import java.util.List;

import org.eclipse.ui.views.properties.IPropertyDescriptor;

import uk.ac.standrews.grasp.ide.model.FirstClassModel;

/**
 * Property source for models inheriting FirstClassModel
 * @author Dilyan Rusev
 *
 */
public class FirstClassPropertySource extends ElementPropertySource<FirstClassModel> {

	/**
	 * Construct a new property source
	 * @param model Model to bind to
	 */
	public FirstClassPropertySource(FirstClassModel model) {
		super(model);		
	}
	
	@Override
	protected List<IPropertyDescriptor> createDescriptors() {
		List<IPropertyDescriptor> desc = super.createDescriptors();
		
//		desc.add(
//				readOnly(FirstClassModel.PROPERTY_ANNOTATIONS, "Annotations")
//				.category(CATEGORY_ELEMENT)
//				.build());				
		
		return desc;
	}
	
	@Override
	public Object getPropertyValue(Object id) {
//		if (FirstClassModel.PROPERTY_ANNOTATIONS == id) {
//			return new CollectionPropertySource<AnnotationModel>("Annotation", getModel().getAnnotations());
//		}
		return super.getPropertyValue(id);
	}	

}
