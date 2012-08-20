package uk.ac.standrews.grasp.ide.model.properties;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.ui.views.properties.IPropertySource;

import uk.ac.standrews.grasp.ide.model.AnnotationModel;
import uk.ac.standrews.grasp.ide.model.ArchitectureModel;
import uk.ac.standrews.grasp.ide.model.ElementModel;
import uk.ac.standrews.grasp.ide.model.FirstClassModel;
import uk.ac.standrews.grasp.ide.model.NamedValueModel;

/**
 * Adapts model objects to property descriptors
 * @author Dilyan Rusev
 *
 */
public class PropertiesAdapterFactory implements IAdapterFactory {
	private static final Class<?>[] ADAPTERS = 
			new Class<?>[] { IPropertySource.class };

	@Override
	public Object getAdapter(Object adaptableObject, 
			@SuppressWarnings("rawtypes") Class adapterType) {
		if (IPropertySource.class.equals(adapterType)) {
			if (AnnotationModel.class.isInstance(adaptableObject)) {
				return new AnnotationPropertySource((AnnotationModel) adaptableObject);
			}
			if (ArchitectureModel.class.isInstance(adaptableObject)) {
				return new ElementPropertySource<ArchitectureModel>((ArchitectureModel) adaptableObject);
			}
			if (NamedValueModel.class.isInstance(adaptableObject)) {
				return new NamedValuePropertySource((NamedValueModel) adaptableObject);
			}
			if (FirstClassModel.class.isInstance(adaptableObject)) {
				return new FirstClassPropertySource((FirstClassModel) adaptableObject);
			}
			return new ElementPropertySource<ElementModel>((ElementModel) adaptableObject);
		}
		
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Class[] getAdapterList() {
		return ADAPTERS;
	}

}
