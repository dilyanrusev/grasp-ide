package uk.ac.standrews.grasp.ide.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.util.Util;
import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

import uk.ac.standrews.grasp.ide.Log;
import uk.ac.standrews.grasp.ide.editors.TextUtil;

/**
 * Base class for all Grasp elements
 * @author Dilyan Rusev
 *
 */
public abstract class ElementModel implements IObservable, 
		Comparable<ElementModel>, IPropertySource {
	/** Element Type */
	public static final String PROPERTY_TYPE = "type";
	/** Parent of the element */
	public static final String PROPERTY_PARENT = "parent";
	/** Alias */
	public static final String PROPERTY_ALIAS = "alias";
	/** Name */
	public static final String PROPERTY_NAME = "name";
	/** Referencing name */
	public static final String PROPERTY_REFERENCING_NAME = "referencingName";
	/** Qualified name */
	public static final String PROPERTY_QUALIFIED_NAME = "qualifiedName";
	/** Name of the generic property category */
	public static final String CATEGORY_ELEMENT = "Element";
	
	private static Map<ElementType, Integer> elementWeights;
	
	private List<IElementChangedListener> changeListeners = new ArrayList<IElementChangedListener>();
	private Map<String, ElementModel> symbolTable = new HashMap<String, ElementModel>();	
	private ElementType type;
	private ElementModel parent;
	private String name;
	private String alias;
	private String referencingName;
	private String qualifiedName;
	private ArchitectureModel architecture;
	private IPropertyDescriptor[] propertyDescriptors;
	
	/**
	 * Create a new element model
	 * @param type Type of the element
	 * @param parent Parent of the element, or null
	 */
	public ElementModel(ElementType type, ElementModel parent) {
		this.type = type;
		this.parent = parent;
	}
	
	/**
	 * Construct an element that is copy of other
	 * @param other Element to copy
	 * @param parent Parent of the element, or null
	 */
	public ElementModel(ElementModel other, ElementModel parent) {
		this.type = other.getType();
		this.parent = parent;
		this.name = other.getName();
		this.alias = other.getAlias();	
		this.referencingName = other.getReferencingName();
		this.qualifiedName = other.getQualifiedName();
	}
	
	private void rebuildNames() {
		// alias
		this.referencingName = alias != null ? alias : name != null ? name : buildName(this);
		this.qualifiedName = buildQualifiedName(this);
	}
	
	private static String buildName(ElementModel model) {
		StringBuilder sb = new StringBuilder();
		sb.append(model.type.name());
		sb.append('@');
		sb.append(model.getInstanceId());
		return sb.toString();
	}
	
	private static String buildQualifiedName(ElementModel model) {
		return buildQualifiedName(model, new StringBuilder()).toString();
	}
	
	private static StringBuilder buildQualifiedName(ElementModel model, StringBuilder sb) {
		if (model.parent instanceof ElementModel) {
			buildQualifiedName((ElementModel)model.parent, sb);
			sb.append('.');
		}
		sb.append(model.referencingName);
		return sb;		
	}

	@Override
	public void addElementChangedListener(IElementChangedListener listener) {
		if (!changeListeners.contains(listener)) {
			changeListeners.add(listener);
		}
	}

	@Override
	public void removeElementChangedListener(IElementChangedListener listener) {
		changeListeners.remove(listener);
	}

	@Override
	public void fireElementChanged(String... propertyNames) {
		ElementChangedEvent event = new ElementChangedEvent(this, propertyNames);
		for (IElementChangedListener listener: changeListeners) {
			listener.elementChanged(event);
		}
	}
	
	/**
	 * Return the Java reference of this object
	 * @return
	 */
	public int getInstanceId() {
		return super.hashCode();
	}

	/**
	 * Return the element type
	 * @return
	 */
	public ElementType getType() {
		return type;
	}
	
	/**
	 * Return the parent. Architectures have no parent
	 * @return
	 */
	public ElementModel getParent() {
		return parent;
	}
	
	/**
	 * Return the name of the element
	 * @return
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Return the name of the element
	 * @return
	 */
	public String getAlias() {
		return alias;
	}
	
	/**
	 * Return the referencing name. Unlike name and alias, this cannot be null
	 * @return
	 */
	public String getReferencingName() {
		return referencingName;
	}
	
	/**
	 * Return the qualified name - in the form of List.Of.Parents.MyReferenceingName
	 * @return
	 */
	public String getQualifiedName() {
		return qualifiedName;
	}
	
	/**
	 * Set the element parent
	 * @param parent
	 */
	public void setParent(ElementModel parent) {		
		this.parent = parent;
		rebuildNames();
		fireElementChanged(PROPERTY_PARENT, PROPERTY_QUALIFIED_NAME, PROPERTY_REFERENCING_NAME);
	}
	
	/**
	 * Set the element name
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
		rebuildNames();
		fireElementChanged(PROPERTY_NAME, PROPERTY_REFERENCING_NAME);
	}
	
	/**
	 * Set the referencing name
	 * @param name
	 */
	public void setReferencingName(String name) {
		this.referencingName = name;
		rebuildNames();
		fireElementChanged(PROPERTY_REFERENCING_NAME);
	}
	
	/**
	 * Set the element alias
	 * @param alias
	 */
	public void setAlias(String alias) {
		this.alias = alias;
		rebuildNames();
		fireElementChanged(PROPERTY_ALIAS, PROPERTY_REFERENCING_NAME);
	}
	
	public String getDisplayName() {
		rebuildNames();
		StringBuilder sb = new StringBuilder();
		if (name != null) {
			sb.append(name);
			if (alias != null) {
				sb.append(' ');
				sb.append(alias);
			}			
		} else {
			sb.append(referencingName);
		}		
		return sb.toString();
	}
	
	/**
	 * Put an element into the symbol table. Does not raise events.
	 * @param s Name of the element
	 * @param element Child element
	 */
	protected void symPut(String s, ElementModel element) {
		// higher-level method will notify for changes
		if (!symLookup(s)) {			
			symbolTable.put(s, element);
		}
	}
	
	/**
	 * Retrieve a child by name
	 * @param s Name of the child
	 * @return Instance of the child element, if contained within the symbol table
	 */
	protected ElementModel symGet(String s) {
		return symbolTable.get(s);
	}
	
	/**
	 * Check if a child is contained by name
	 * @param s Name of the child
	 * @return True if contained within the symbol table
	 */
	protected boolean symLookup(String s) {
		return symbolTable.containsKey(s);
	}
	
	/**
	 * Attempt to remove from parent
	 * @return Element that used to be parent, or null
	 */
	public abstract ElementModel removeFromParent();
	
	/**
	 * Add element as a child
	 * @param child Element to add. Query type to determine how to add this
	 * @return true if successful
	 */
	public abstract boolean addChildElement(ElementModel child);
	
	/**
	 * Return a cached copy of the architecture containing this element, or this if architecture
	 * @return Architecture containing this element, or null
	 */
	public ArchitectureModel getArchitecture() {
		if (type == ElementType.ARCHITECTURE) {
			return (ArchitectureModel) this;
		}
		if (architecture == null) {
			ElementModel arch = parent;
			while (arch.getType() != ElementType.ARCHITECTURE) {
				arch = arch.getParent();
			}
			architecture = (ArchitectureModel) arch;
		}
		return architecture;
	}
	
	@Override
	public String toString() {
		return new StringBuilder().append('[').append(type).append("] ").append(qualifiedName).toString();
	}

	@Override
	public int compareTo(ElementModel other) {
		if (other == this) {
			return 0;
		}
		if (this.getType() != other.getType()) {
			int myWeight = getElementWeight(getType());
			int otherWeight = getElementWeight(other.getType());
			return myWeight - otherWeight;
		} else {
			return doCompareTo(other);
		}
	}
	
	/**
	 * Compare to another element of the same type. By default compares referencing name.
	 * @param other Element guaranteed to be of the same type
	 * @return -1 if this is less, 0 if equal, and 1 if this is more than the other element
	 */
	protected int doCompareTo(ElementModel other) {
		return this.getReferencingName().compareTo(other.getReferencingName());
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!getClass().isInstance(obj)) {
			return false;
		}
		ElementModel other = (ElementModel) obj;
		if (!getType().equals(other.getType())) return false;
		if (!objectsEqual(getReferencingName(), other.getReferencingName())) return false;
		if (!objectsEqual(getQualifiedName(), other.getQualifiedName())) return false;		
		
		return true;		
	}
	
	@Override
	public int hashCode() {
		int result = 17;
		result = 31 * result + getType().hashCode();
		result = 31 * result + getReferencingName().hashCode();
		result = 31 * result + getQualifiedName().hashCode();
		
		return result;
	}
	
	/**
	 * Test if two objects are equal, allowing for null
	 * @param o1 First object
	 * @param o2 Second object
	 * @return True if equal or if both parameters are null
	 */
	protected static boolean objectsEqual(Object o1, Object o2) {
		if (o1 == o2) return true;
		return o1 != null ? o1.equals(o2) : o2 == null;
	}
	
	/**
	 * Compare two collections, element by element, to see if they are equal.
	 * @param c1 First collection
	 * @param c2 Second collection
	 * @return True if all elements are equal
	 */
	protected static <E> boolean collectionsEqual(Collection<E> c1, Collection<E> c2) {
		if (c1 == c2) return true;
		if (c1.size() != c2.size()) return false;
		
		Iterator<E> i1 = c1.iterator();
		Iterator<E> i2 = c2.iterator();
		E o1;
		E o2;
		
		while (i1.hasNext()) {
			if (!i2.hasNext()) return false;
			o1 = i1.next();
			o2 = i2.next();
			if (!objectsEqual(o1, o2)) return false;
		}
		
		return true;
	}
	
	private static int getElementWeight(ElementType type) {
		// we want elements to be in that order:
		/*
		 * annotation
		 * architecture
		 * 	* rationale
		 * 		* reason
		 * 	* requirement
		 * 	* quality_attribute
		 * 	template
		 * 		provides
		 * 		requires
		 * 		* property
		 * 		* check
		 * 	system
		 * 		layer
		 * 			component
		 * 			connector
		 * 			link
		 */
		if (elementWeights == null) {
			elementWeights = new HashMap<ElementType, Integer>(ElementType.values().length);
			elementWeights.put(ElementType.ANNOTATION, 0);
			elementWeights.put(ElementType.ARCHITECTURE, 1);
			elementWeights.put(ElementType.RATIONALE, 2);
			elementWeights.put(ElementType.REASON, 3);
			elementWeights.put(ElementType.REQUIREMENT, 4);
			elementWeights.put(ElementType.QUALITY_ATTRIBUTE, 5);
			elementWeights.put(ElementType.TEMPLATE, 6);
			elementWeights.put(ElementType.PROVIDES, 7);
			elementWeights.put(ElementType.REQUIRES, 8);
			elementWeights.put(ElementType.PROPERTY, 9);
			elementWeights.put(ElementType.CHECK, 10);
			elementWeights.put(ElementType.SYSTEM, 11);
			elementWeights.put(ElementType.LAYER, 12);
			elementWeights.put(ElementType.COMPONENT, 13);
			elementWeights.put(ElementType.CONNECTOR, 14);
			elementWeights.put(ElementType.LINK, 15);
		}
		Integer weight = elementWeights.get(type);
		return weight != null ? weight : 99;
	}
	
	/**
	 * Append a task that will copy one collection into the other when the architecture
	 * containing this element has completed copying the whole tree
	 * @param source Collection that will be copied into target
	 * @param target Collection that will be the same as source
	 */
	protected <E extends ElementModel> void copyCollectionAtTheEndOfCopy(
			Collection<E> source, final Collection<E> target) {
		final List<String> qualifiedNames = extractQualifiedNames(source);
		final ArchitectureModel arch = getArchitecture();
		if (qualifiedNames.size() > 0) {
			arch.executeAtTheEndOfCopy(new Runnable() {				
				@SuppressWarnings("unchecked")
				@Override
				public void run() {
					for (String qname: qualifiedNames) {
						E found = (E) arch.findByQualifiedName(qname);
						Assert.isNotNull(found, "Cannot find " + qname + " in " + arch);
						target.add(found);
					}
				}
			});
		}
	}
	
	private static <E extends ElementModel> List<String> extractQualifiedNames(Collection<E> collection) {
		List<String> qualifiedNames = new ArrayList<String>(collection.size());
		for (E elem: collection) {
			qualifiedNames.add(elem.getQualifiedName());
		}
		return qualifiedNames;
	}
	
	/**
	 * Helper method to create an element based on type (to prevent reflection)
	 * @param type Type of the element to create. Cannot be {@link ElementType#ARCHITECTURE}
	 * @param parent Parent element, if any. 
	 * @return Instance of the requested type
	 */
	public static ElementModel createElementByType(ElementType type, FirstClassModel parent) {
		switch (type) {
		case ANNOTATION:
			return new AnnotationModel(parent);
		case ARCHITECTURE:
			Log.info("GraspModel.createElementByType: cannot create architecture");
			return null;
		case CHECK:
			return new CheckModel(parent);
		case COMPONENT:
			return new ComponentModel(parent);
		case CONNECTOR:
			return new ConnectorModel(parent);
		case EXPRESSION:
			return new ExpressionModel(parent);
		case LAYER:
			return new LayerModel(parent);
		case LINK:
			return new LinkModel(parent);		
		case PROPERTY:
			return new PropertyModel(parent);
		case PROVIDES:
			return new ProvidesModel(parent);
		case QUALITY_ATTRIBUTE:
			return new QualityAttributeModel(parent);
		case RATIONALE:
			return new RationaleModel(parent);
		case REASON:
			return new ReasonModel(parent);
		case REQUIREMENT:
			return new RequirementModel(parent);
		case REQUIRES:
			return new RequiresModel(parent);
		case SYSTEM:
			return new SystemModel((ArchitectureModel) parent);
		case TEMPLATE:
			return new TemplateModel(parent);
		default:
			Assert.isTrue(false, "Unknown ElmentType: " + type);
			return null;
		}
	}

	@Override
	public Object getEditableValue() {
		return this;
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		if (propertyDescriptors == null) {
			Collection<IPropertyDescriptor> desc = createPropertyDescriptors();
			propertyDescriptors = desc.toArray(new IPropertyDescriptor[desc.size()]);
		}
		return propertyDescriptors;
	}
	
	/**
	 * Override to add type-specific property descriptors
	 * @return
	 */
	protected Collection<IPropertyDescriptor> createPropertyDescriptors() {
		Collection<IPropertyDescriptor> props = new ArrayList<IPropertyDescriptor>(10);
		
		{
			PropertyDescriptor refNameProp = 
					new PropertyDescriptor(PROPERTY_TYPE, "Type");
			refNameProp.setCategory(CATEGORY_ELEMENT);
			refNameProp.setAlwaysIncompatible(true);
			refNameProp.setDescription(
					"Type of this element");
			props.add(refNameProp);
		}
		
		{
			TextPropertyDescriptor nameProp = 
					new TextPropertyDescriptor(PROPERTY_NAME, "Name");
			nameProp.setCategory(CATEGORY_ELEMENT);
			nameProp.setAlwaysIncompatible(true);
			nameProp.setDescription("Name for this element");
			nameProp.setValidator(new ElementNameValidator());
			props.add(nameProp);
		}
		
		{
			TextPropertyDescriptor aliasProp = 
					new TextPropertyDescriptor(PROPERTY_ALIAS, "Alias");
			aliasProp.setCategory(CATEGORY_ELEMENT);
			aliasProp.setAlwaysIncompatible(true);
			aliasProp.setDescription("Alternative name for this element");
			aliasProp.setValidator(new ElementNameValidator());		
			props.add(aliasProp);
		}
		
		{
			PropertyDescriptor refNameProp = 
					new PropertyDescriptor(PROPERTY_REFERENCING_NAME, "Referencing name");
			refNameProp.setCategory(CATEGORY_ELEMENT);
			refNameProp.setAlwaysIncompatible(true);
			refNameProp.setDescription("Alias, name, or Type@Address - in that order. Forms qualified name");
			props.add(refNameProp);
		}
		
		{
			PropertyDescriptor refNameProp = 
					new PropertyDescriptor(PROPERTY_QUALIFIED_NAME, "Fully qualified name");
			refNameProp.setCategory(CATEGORY_ELEMENT);
			refNameProp.setAlwaysIncompatible(true);
			refNameProp.setDescription(
					"Name formed by the referencing names of this element's parents and its own referencing name");
			props.add(refNameProp);
		}
		
				
		return props;
	}

	@Override
	public Object getPropertyValue(Object id) {
		if (PROPERTY_ALIAS.equals(id)) {
			return getAlias();
		} else if (PROPERTY_NAME.equals(id)) {
			return getName();
		} else if (PROPERTY_QUALIFIED_NAME.equals(id)) {
			return getQualifiedName();			
		} else if (PROPERTY_REFERENCING_NAME.equals(id)) {
			return getReferencingName();
		} else if (PROPERTY_TYPE.equals(id)) {
			return getType().name();
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
		if (PROPERTY_ALIAS.equals(id)) {
			setAlias((String) value);
		} else if (PROPERTY_NAME.equals(id)) {
			setName((String) value);
		} 	
	}
	
	/**
	 * Cell validator that makes certain the input value is a valid Grasp identifier
	 * @author Dilyan Rusev
	 *
	 */
	protected static class IsIdentifierValidator implements ICellEditorValidator {
		/** Singleton instance - for convenience */
		public static IsIdentifierValidator INSTANCE =
				new IsIdentifierValidator();
		
		@Override
		public String isValid(Object value) {
			if (!TextUtil.isIdentifier((String) value)) {
				return "Not a valid Grasp identifier";
			} else {
				return null;
			}
		}		
	}
	
	protected class ElementNameValidator implements ICellEditorValidator {

		@Override
		public String isValid(Object value) {
			if (!(value instanceof String)) {
				return "Must be text";
			}
			String text = (String) value;
			if (!TextUtil.isIdentifier(text)) {
				return "Not a valid Grasp identifier";
			}
			if (getArchitecture() != null) {
				String myQualifiedName = getQualifiedName();
				String nextQName = myQualifiedName.substring(myQualifiedName.lastIndexOf('.') + 1);
				nextQName = nextQName + "." + text;
				if (getArchitecture().findByQualifiedName(nextQName) != null) {
					return "There is already an element with name " + nextQName;
				}
			}
			return null;
		}
		
	}
	
	/**
	 * Provides property source for collections of elements
	 * @author Dilyan Rusev
	 *
	 * @param <E> Type of the collection element
	 */
	protected static class CollectionPropertySource<E> 
			implements IPropertySource {
		private IObservableCollection<E> collection;
		private IPropertyDescriptor[] descriptors;
		private String elementDisplayName;

		/**
		 * Construct a new property source
		 * @param elementDisplayName Display name for property elements
		 * @param collection Collection to wrap
		 */
		public CollectionPropertySource(String elementDisplayName,
				IObservableCollection<E> collection) {
			Assert.isNotNull(collection);
			Assert.isLegal(!TextUtil.isNullOrWhitespace(elementDisplayName));
			// TODO: figure out a way to observe without leaking memory
			this.collection = collection;
			this.elementDisplayName = elementDisplayName;
		}
		
		@Override
		public Object getEditableValue() {
			return this;
		}

		@Override
		public IPropertyDescriptor[] getPropertyDescriptors() {
			if (descriptors == null) {
				descriptors = createDescriptors();
			}
			return descriptors;
		}
		
		private IPropertyDescriptor[] createDescriptors() {
			Collection<IPropertyDescriptor> desc = new ArrayList<IPropertyDescriptor>();
			ILabelProvider labelProvider = new LabelProvider() {
				@Override
				public String getText(Object element) {
					return Util.ZERO_LENGTH_STRING;
				}
			};
			for (E source: collection) {
				PropertyDescriptor pd = new PropertyDescriptor(source, elementDisplayName);
				pd.setLabelProvider(labelProvider);
				desc.add(pd);				
			}
			return desc.toArray(new IPropertyDescriptor[desc.size()]);
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
	}
}
