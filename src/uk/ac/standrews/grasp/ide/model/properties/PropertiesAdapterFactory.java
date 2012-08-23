package uk.ac.standrews.grasp.ide.model.properties;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.ui.views.properties.IPropertySource;

import uk.ac.standrews.grasp.ide.model.AnnotationModel;
import uk.ac.standrews.grasp.ide.model.ElementModel;
import uk.ac.standrews.grasp.ide.model.InstantiableModel;
import uk.ac.standrews.grasp.ide.model.InterfaceModel;
import uk.ac.standrews.grasp.ide.model.LinkModel;
import uk.ac.standrews.grasp.ide.model.NamedValueModel;
import uk.ac.standrews.grasp.ide.model.SystemModel;

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
			if (adaptableObject instanceof LinkModel) {
				return new LinkPropertySource((LinkModel) adaptableObject);
			}
			if (adaptableObject instanceof SystemModel) {
				return new SystemPropertySource((SystemModel) adaptableObject);
			}
			if (adaptableObject instanceof InstantiableModel) {
				return new InstantiablePropertySource((InstantiableModel) adaptableObject);
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
