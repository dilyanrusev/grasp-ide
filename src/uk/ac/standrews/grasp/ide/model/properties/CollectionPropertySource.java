package uk.ac.standrews.grasp.ide.model.properties;

import java.util.Collection;
import java.util.Iterator;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.util.Util;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;

import uk.ac.standrews.grasp.ide.editors.TextUtil;

/**
 * Provides property source for collections of elements
 * @author Dilyan Rusev
 *
 * @param <E> Type of the collection element
 */
public class CollectionPropertySource<E> 
		implements IPropertySource {
	private final Collection<E> collection;
	private final IPropertyDescriptor[] descriptors;
	

	/**
	 * Construct a new property source
	 * @param elementDisplayName Display name for property elements
	 * @param collection Collection to wrap
	 */
	public CollectionPropertySource(String elementDisplayName,
			Collection<E> collection) {
		Assert.isNotNull(collection);
		Assert.isLegal(!TextUtil.isNullOrWhitespace(elementDisplayName));		
		this.collection = collection;	
		descriptors = new IPropertyDescriptor[collection.size()];
		Iterator<E> iter = collection.iterator();
		for (int i = 0; i < collection.size(); i++) {
			E elem = iter.next();
			PropertyDescriptor pd = new PropertyDescriptor(elem, elementDisplayName);
			pd.setLabelProvider(new EmptyStringLabelProvider());
			descriptors[i] = pd;
		}
	}
	
	@Override
	public Object getEditableValue() {
		return this;
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {		
		return descriptors;
	}
	
	@Override
	public Object getPropertyValue(Object id) {
		for (E val: collection) {
			if (val == id) {
				return val;
			}
		}
		return null;
	}

	@Override
	public boolean isPropertySet(Object id) {			
		return false;
	}

	@Override
	public void resetPropertyValue(Object id) {			
		
	}

	@Override
	public void setPropertyValue(Object id, Object value) {			
	}
	
	@Override
	public String toString() {
		return Util.ZERO_LENGTH_STRING;
	}
	
	/**
	 * Label provider that always returns an empty string
	 * @author Dilyan Rusev
	 *
	 */
	private static class EmptyStringLabelProvider extends LabelProvider {
		@Override
		public String getText(Object element) {
			return Util.ZERO_LENGTH_STRING;
		}
	}
}