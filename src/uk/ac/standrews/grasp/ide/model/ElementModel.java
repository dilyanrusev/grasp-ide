package uk.ac.standrews.grasp.ide.model;

import grasp.lang.IElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.Assert;
import org.w3c.dom.Node;

import shared.xml.IXmlWriter;

public abstract class ElementModel implements IElement, IObservable {
	public static final String PROPERTY_TYPE = "type";
	public static final String PROPERTY_PARENT = "parent";
	public static final String PROPERTY_ALIAS = "alias";
	public static final String PROPERTY_NAME = "name";
	public static final String PROPERTY_REFERENCING_NAME = "referencingName";
	public static final String PROPERTY_QUALIFIED_NAME = "qualifiedName";
	
	private List<IElementChangedListener> changeListeners = new ArrayList<IElementChangedListener>();
	private Map<String, IElement> symbolTable = new HashMap<String, IElement>();
	private ElementType type;
	private IElement parent;
	private String name;
	private String alias;
	private String referencingName;
	private String qualifiedName;
	
	public ElementModel(ElementType type, IElement parent) {
		this.type = type;
		this.parent = parent;
	}
	
	public ElementModel(IElement other, IElement parent) {
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
		sb.append('_');
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

	@Override
	public int getInstanceId() {
		return super.hashCode();
	}

	@Override
	public ElementType getType() {
		return type;
	}

	@Override
	public IElement getParent() {
		return parent;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getAlias() {
		return alias;
	}

	@Override
	public String getReferencingName() {
		return referencingName;
	}

	@Override
	public String getQualifiedName() {
		return qualifiedName;
	}

	@Override
	public void setParent(IElement parent) {
		Assert.isTrue(parent == null || parent instanceof ElementModel, 
				"Parent must be ElementModel or null");
		this.parent = parent;
		rebuildNames();
		fireElementChanged(PROPERTY_PARENT, PROPERTY_QUALIFIED_NAME, PROPERTY_REFERENCING_NAME);
	}

	@Override
	public void setName(String name) {
		this.name = name;
		rebuildNames();
		fireElementChanged(PROPERTY_NAME, PROPERTY_REFERENCING_NAME);
	}

	@Override
	public void setAlias(String alias) {
		this.alias = alias;
		rebuildNames();
		fireElementChanged(PROPERTY_ALIAS, PROPERTY_REFERENCING_NAME);
	}

	@Override
	public void symPut(String s, IElement element) {
		// higher-level method will notify for changes
		if (!symLookup(s)) {
			Assert.isTrue(element instanceof ElementModel);
			symbolTable.put(s, element);
		}
	}

	@Override
	public IElement symGet(String s) {
		return symbolTable.get(s);
	}

	@Override
	public boolean symLookup(String s) {
		return symbolTable.containsKey(s);
	}

	@Override
	public void toXml(IXmlWriter ixmlwriter) {
		// no serialisation
	}

	@Override
	public void serialize(IXmlWriter ixmlwriter, Node node) {
		// no serialisation
	}

}
