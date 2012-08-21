package uk.ac.standrews.grasp.ide.model.properties;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.ui.views.properties.IPropertySource;

import uk.ac.standrews.grasp.ide.model.AnnotationModel;
import uk.ac.standrews.grasp.ide.model.ElementModel;
import uk.ac.standrews.grasp.ide.model.InterfaceModel;
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
			if (adaptableObject instanceof AnnotationModel) {
				return new AnnotationPropertySource((AnnotationModel) adaptableObject);
			}			
			if (adaptableObject instanceof NamedValueModel) {
				return new NamedValuePropertySource((NamedValueModel) adaptableObject);
			}
			if (adaptableObject instanceof InterfaceModel) {
				return new InterfacePropertySource((InterfaceModel) adaptableObject);
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
