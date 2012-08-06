package uk.ac.standrews.grasp.ide.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.Assert;

import uk.ac.standrews.grasp.ide.Log;

public abstract class ElementModel implements IObservable, Comparable<ElementModel> {
	public static final String PROPERTY_TYPE = "type";
	public static final String PROPERTY_PARENT = "parent";
	public static final String PROPERTY_ALIAS = "alias";
	public static final String PROPERTY_NAME = "name";
	public static final String PROPERTY_REFERENCING_NAME = "referencingName";
	public static final String PROPERTY_QUALIFIED_NAME = "qualifiedName";
	
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
	
	public ElementModel(ElementType type, ElementModel parent) {
		this.type = type;
		this.parent = parent;
	}
	
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

	public int getInstanceId() {
		return super.hashCode();
	}

	public ElementType getType() {
		return type;
	}
	
	public ElementModel getParent() {
		return parent;
	}
	
	public String getName() {
		return name;
	}
	
	public String getAlias() {
		return alias;
	}
	
	public String getReferencingName() {
		return referencingName;
	}
	
	public String getQualifiedName() {
		return qualifiedName;
	}
	
	public void setParent(ElementModel parent) {		
		this.parent = parent;
		rebuildNames();
		fireElementChanged(PROPERTY_PARENT, PROPERTY_QUALIFIED_NAME, PROPERTY_REFERENCING_NAME);
	}
	
	public void setName(String name) {
		this.name = name;
		rebuildNames();
		fireElementChanged(PROPERTY_NAME, PROPERTY_REFERENCING_NAME);
	}
	
	void setReferencingName(String name) {
		this.referencingName = name;
		rebuildNames();
		fireElementChanged(PROPERTY_REFERENCING_NAME);
	}
	
	public void setAlias(String alias) {
		this.alias = alias;
		rebuildNames();
		fireElementChanged(PROPERTY_ALIAS, PROPERTY_REFERENCING_NAME);
	}
	
	protected void symPut(String s, ElementModel element) {
		// higher-level method will notify for changes
		if (!symLookup(s)) {			
			symbolTable.put(s, element);
		}
	}
	
	protected ElementModel symGet(String s) {
		return symbolTable.get(s);
	}
	
	protected boolean symLookup(String s) {
		return symbolTable.containsKey(s);
	}
	
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
			return this.getReferencingName().compareTo(other.getReferencingName());
		}
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
	
	protected static boolean objectsEqual(Object o1, Object o2) {
		if (o1 == o2) return true;
		return o1 != null ? o1.equals(o2) : o2 == null;
	}
	
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
}
