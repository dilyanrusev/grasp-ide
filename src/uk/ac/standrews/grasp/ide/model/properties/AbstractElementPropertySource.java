package uk.ac.standrews.grasp.ide.model.properties;

import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

import uk.ac.standrews.grasp.ide.model.ElementModel;
import uk.ac.standrews.grasp.ide.model.properties.PropertyBuilderImpl.ReadOnlyBuilder;
import uk.ac.standrews.grasp.ide.model.properties.PropertyBuilderImpl.TextBuilder;

public abstract class AbstractElementPropertySource<T extends ElementModel> 
		implements IPropertySource {

	protected final T model;
	protected final IPropertyDescriptor[] properties;
	
	/**
	 * Construct a new property source
	 * @param model Model to bind to
	 */
	public AbstractElementPropertySource(T model) {
		Assert.isNotNull(model);
		this.model = model;
		List<IPropertyDescriptor> desc = createDescriptors();
		this.properties = desc.toArray(new IPropertyDescriptor[desc.size()]);
	}	

	/**
	 * Return the model passed to the constructor
	 * @return
	 */
	protected T getModel() {
		return model;
	}

	/**
	 * Construct all property descriptors. Called in constructor.
	 * @return Default implementation returns writable name and read-only type
	 */
	protected abstract List<IPropertyDescriptor> createDescriptors();

	@Override
	public Object getEditableValue() {
		return this;
	}

	@Override
	public Object getPropertyValue(Object id) {		
		return null;
	}
	
	@Override
	public boolean isPropertySet(Object id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void resetPropertyValue(Object id) {
		// TODO Auto-generated method stub
	
	}

	@Override
	public void setPropertyValue(Object id, Object value) {		
	}
	
	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		return this.properties;
	}

	/**
	 * Construct a writable text property
	 * @param id ID of the property
	 * @param displayName Name to be displayed in the property editor		 
	 * @return Writable text property builder
	 */
	public static TextBuilder text(Object id, String displayName) {
		return new TextBuilder(new TextPropertyDescriptor(id, displayName));
	}

	/**
	 * Construct a read-only property
	 * @param id ID of the property
	 * @param displayName Name to be displayed in the property editor		 
	 * @return Read-only property builder
	 */
	public static ReadOnlyBuilder readOnly(Object id, String displayName) {
		return new ReadOnlyBuilder(new PropertyDescriptor(id, displayName));
	}
	
}