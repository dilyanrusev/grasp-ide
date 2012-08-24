package uk.ac.standrews.grasp.ide.model.properties;

import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

import uk.ac.standrews.grasp.ide.editors.TextUtil;
import uk.ac.standrews.grasp.ide.model.ElementModel;
import uk.ac.standrews.grasp.ide.model.properties.PropertyBuilderImpl.ReadOnlyBuilder;
import uk.ac.standrews.grasp.ide.model.properties.PropertyBuilderImpl.TextBuilder;

/**
 * Base class for property sources that map to Grasp model elements
 * @author Dilyan Rusev
 *
 * @param <T> Type of the element model the implementation is bound to
 */
public abstract class AbstractElementPropertySource<T extends ElementModel> 
		implements IPropertySource {

	/**
	 * Model this source is bound to
	 */
	protected final T model;
	/**
	 * Properties of this source
	 */
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
	 * @return Element this source is bound to
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
		return false;
	}

	@Override
	public void resetPropertyValue(Object id) {
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
	 * Same as {@link #text(Object, String)}, but also adds a validator to make sure
	 * that the name is unique within the architecture graph
	 * @param id ID of the property
	 * @param displayName Name to be displayed in the property editor
	 * @return Writeable text property builder that validates its value to be Grasp identifier
	 */
	public TextBuilder identifier(Object id, String displayName) {
		return text(id, displayName)
				.validator(new ICellEditorValidator() {					
					@Override
					public String isValid(Object value) {
						if (!(value instanceof String)) {
							return "Must be text";
						}
						String text = (String) value;
						if (!TextUtil.isIdentifier(text)) {
							return "Not a valid Grasp identifier";
						}
						if (getModel().getArchitecture() != null) {
							String myQualifiedName = getModel().getQualifiedName();
							String nextQName = myQualifiedName.substring(myQualifiedName.lastIndexOf('.') + 1);
							nextQName = nextQName + "." + text;
							if (getModel().getArchitecture().findByQualifiedName(nextQName) != null) {
								return "There is already an element with name " + nextQName;
							}
						}
						return null;
					}
				});
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