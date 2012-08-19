package uk.ac.standrews.grasp.ide.model;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

import uk.ac.standrews.grasp.ide.editors.TextUtil;

/**
 * Represents Grasp annotations
 * @author Dilyan Rusev
 *
 */
public class AnnotationModel extends ElementModel {	
	public static final String PROPERTY_NAMED_VALUES = "namedValues";
	public static final String CATEGORY_ANNOTATION = "Annotation";
	
	private ObservableSet<NamedValueModel> namedValues = new ObservableSet<NamedValueModel>();
	
	/**
	 * Create a copy of another annotation
	 * @param other Source
	 * @param parent Parent
	 */
	public AnnotationModel(AnnotationModel other, ElementModel parent) {
		super(other, parent);		
		for (NamedValueModel namedValue: other.getNamedValues()) {
			NamedValueModel observable = new NamedValueModel(namedValue, this);
			namedValues.add(observable);
		}
	}
	
	/**
	 * Create a new annotation
	 * @param parent Parent
	 */
	public AnnotationModel(ElementModel parent) {
		super(ElementType.ANNOTATION, parent);
	}
	
	/**
	 * Return the values stored in this annotation
	 * @return
	 */
	public ObservableSet<NamedValueModel> getNamedValues() {
		return namedValues;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj)) return false;
		AnnotationModel other = (AnnotationModel) obj;
		
		if (!collectionsEqual(getNamedValues(), other.getNamedValues())) return false;
		
		return true;
	}
	
	@Override
	public int hashCode() {
		int result = super.hashCode();		
		for (NamedValueModel nv: getNamedValues()) {
			result = 31 * result + nv.hashCode();
		}
		return result;
	}

	@Override
	public ElementModel removeFromParent() {
		if (getParent() instanceof FirstClassModel) {
			FirstClassModel theParent = (FirstClassModel) getParent();
			if (theParent.getAnnotations().remove(this)) {
				return theParent;
			}
		}
		return null;
	}

	@Override
	public boolean addChildElement(ElementModel child) {
		if (child instanceof NamedValueModel) {
			return getNamedValues().add((NamedValueModel) child);
		}
		return false;
	}
	
	@Override
	protected Collection<IPropertyDescriptor> createPropertyDescriptors() {
		Collection<IPropertyDescriptor> desc = new ArrayList<IPropertyDescriptor>();
		
		{
			TextPropertyDescriptor handler =
					new TextPropertyDescriptor(PROPERTY_NAME, "handler");
			handler.setAlwaysIncompatible(true);
			handler.setCategory(CATEGORY_ANNOTATION);
			handler.setDescription("Name of the annotation");
			handler.setValidator(new ICellEditorValidator() {				
				@Override
				public String isValid(Object value) {
					String text = (String) value;
					if (text != null) {
						if (!TextUtil.isIdentifier(text)) {
							return "Not a valid Grasp identifier";
						}
					}
					return null;
				}
			});
			desc.add(handler);
		}
		
		{
			PropertyDescriptor namedValues = 
					new PropertyDescriptor(PROPERTY_NAMED_VALUES, "Named values");
			namedValues.setAlwaysIncompatible(true);
			namedValues.setCategory(CATEGORY_ANNOTATION);
			desc.add(namedValues);
		}
		
		return desc;
	}
	
	@Override
	public Object getPropertyValue(Object id) {
		if (PROPERTY_NAME.equals(id)) {
			return getName();
		} else if (PROPERTY_NAMED_VALUES.equals(id)) {
			return new CollectionPropertySource<NamedValueModel>("Named value", getNamedValues());
		}
		return null;
	}
}
