package uk.ac.standrews.grasp.ide.model.properties;

import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

/**
 * Holds the implementation of PropertyBuilder
 * @author Dilyan Rusev
 *
 */
class PropertyBuilderImpl {
	/**
	 * Base builder
	 * @author Dilyan Rusev
	 *
	 * @param <T> Type of the property descriptor
	 */
	private static abstract class AbstractBuilder<T extends IPropertyDescriptor> {		
		private final T desc;
		
		/**
		 * Constructs a new descriptor
		 * @param desc Property descriptor to wrap
		 */
		public AbstractBuilder(T desc) {
			this.desc = desc;
		}
		
		/**
		 * Returns the property descriptor
		 */
		protected T getDescriptor() {
			return desc;
		}
		
		/** Get the property descriptor */
		public T build() {			
			return desc;
		}
	}
	
	/**
	 * Base builder for property descriptors inheriting PropertyDescriptor
	 * @author Dilyan Rusev
	 *
	 * @param <E> Type of the property descriptor
	 * @param <B> Type of the builder implementation
	 */
	private static abstract class AbstractBaseBuilder
			<E extends PropertyDescriptor, B extends AbstractBuilder<E>> 
			extends AbstractBuilder<E> {	
		
		/**
		 * Constructs a new descriptor
		 * @param desc Property descriptor to wrap
		 */
		public AbstractBaseBuilder(E desc) {
			super(desc);
		}
		
		/**
	     * Sets a flag indicating whether this property descriptor is to be always 
	     * considered incompatible with any other property descriptor.
	     * Setting this flag prevents a property from displaying during multiple 
	     * selection.
	     *
	     * @param flag <code>true</code> to indicate always incompatible
	     * @return This
	     */
		@SuppressWarnings("unchecked")
		public B alwaysIncompatible(boolean flag) {
			getDescriptor().setAlwaysIncompatible(flag);
			return (B) this;
		}
		
		/**
	     * Sets the category for this property descriptor.
	     * 
	     * @param category the category for the descriptor, or <code>null</code> if none
	     * @return This
	     */
		@SuppressWarnings("unchecked")
		public B category(String category) {
			getDescriptor().setCategory(category);
			return (B) this;
		}
		
		/**
	     * Sets the description for this property descriptor.
	     * The description should be limited to a single line so that it can be
	     * displayed in the status line.
	     * 
	     * @param description the description, or <code>null</code> if none
	     * @return This	 
	     */
		@SuppressWarnings("unchecked")
		public B description(String description) {
			getDescriptor().setDescription(description);
			return (B) this;
		}
		
		/**
	     * Sets the label provider for this property descriptor.
	     * <p>
	     * If no label provider is set an instance of <code>LabelProvider</code>
	     * will be created as the default when needed.
	     * </p>
	     * 
	     * @param provider the label provider for the descriptor, or 
	     *   <code>null</code> if the default label provider should be used	
	     * @return This   
	     */
		@SuppressWarnings("unchecked")
		public B labelProvider(ILabelProvider provider) {
			getDescriptor().setLabelProvider(provider);
			return (B) this;
		}		
		
		 /**
	     * Sets the input validator for the cell editor for this property descriptor.
	     * <p>
	     * [Issue: This method should be unnecessary is the cell editor's own
	     *  validator is used.
	     * ]
	     * </p>
	     *  
	     * @param validator the cell input validator, or <code>null</code> if none
	     * @return This
	     */
		@SuppressWarnings("unchecked")
		public B validator(ICellEditorValidator validator) {
			getDescriptor().setValidator(validator);
			return (B) this;
		}
	}

	/**
	 * Property builder for read only properties
	 * @author Dilyan Rusev
	 *
	 */
	public static class ReadOnlyBuilder 
			extends AbstractBaseBuilder<PropertyDescriptor, ReadOnlyBuilder> {
		
		/**
		 * Constructs a new descriptor
		 * @param desc Property descriptor to wrap
		 */
		public ReadOnlyBuilder(PropertyDescriptor desc) {
			super(desc);
		}
	}
	
	/**
	 * Property builder for text properties
	 * @author Dilyan Rusev
	 *
	 */
	public static class TextBuilder			 
			extends AbstractBaseBuilder<TextPropertyDescriptor, TextBuilder> {
		
		/**
		 * Constructs a new descriptor
		 * @param desc Property descriptor to wrap
		 */
		public TextBuilder(TextPropertyDescriptor desc) {
			super(desc);
		}
	}	
}