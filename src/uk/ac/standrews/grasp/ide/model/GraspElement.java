package uk.ac.standrews.grasp.ide.model;

import grasp.lang.IElement;
import grasp.lang.elements.AbstractElement;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;

import shared.xml.IXmlWriter;

public abstract class GraspElement<T extends IElement> 
		extends AbstractElement 
		implements IObservable {
	
	public static final String PROP_ALIAS = "alias";
	public static final String PROP_NAME = "name";
	public static final String PROP_PARENT = "parent";
	public static final String PROP_SYM_TABLE = "sym-table";
	
	private List<IElementChangedListener> changeListeners;
	protected T wrapped;
	
	public GraspElement(T wrapped) {
		super(wrapped.getType(), wrapped.getParent());
		this.wrapped = wrapped;
		this.changeListeners = new ArrayList<IElementChangedListener>();
	}


	@Override
	public void addElementChangedListener(IElementChangedListener listener) {
		if (listener != null && !changeListeners.contains(listener)) {
			changeListeners.add(listener);
		}
	}

	@Override
	public void removeElementChangedListener(IElementChangedListener listener) {
		if (listener != null) {
			changeListeners.remove(listener);
		}
	}

	@Override
	public void fireElementChanged(String propertyName) {
		ElementChangedEvent event = new ElementChangedEvent(this, propertyName);
		for (IElementChangedListener listener: changeListeners) {
			listener.elementChanged(event);
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		return wrapped.equals(obj);
	}
	
	@Override
	public int hashCode() {
		return wrapped.hashCode();
	}
	
	@Override
	public String getAlias() {
		return wrapped.getAlias();
	}
	
	@Override
	public int getInstanceId() {
		return wrapped.getInstanceId();
	}

	@Override
	public String getName() {
		return wrapped.getName();
	}
	
	@Override
	public IElement getParent() {
		return wrapped.getParent();
	}
	
	@Override
	public String getQualifiedName() {
		return wrapped.getQualifiedName();
	}
	
	@Override
	public String getReferencingName() {
		return wrapped.getReferencingName();
	}
	
	@Override
	public ElementType getType() {
		return wrapped.getType();
	}
	
	@Override
	public void serialize(IXmlWriter writer, Node parent) {
		wrapped.serialize(writer, parent);
	}
	
	@Override
	public void setAlias(String alias) {
		wrapped.setAlias(alias);
		fireElementChanged(PROP_ALIAS);
	}
	
	@Override
	public void setName(String name) {
		wrapped.setName(name);
		fireElementChanged(PROP_NAME);
	}
	
	@Override
	public void setParent(IElement parent) {
		wrapped.setParent(parent);
		fireElementChanged(PROP_PARENT);
	}
	
	@Override
	public IElement symGet(String symbol) {
		// TODO: investigate if we should wrap result
		return wrapped.symGet(symbol);
	}
	
	@Override
	public boolean symLookup(String symbol) {
		return wrapped.symLookup(symbol);
	}
	
	@Override
	public void symPut(String symbol, IElement referrer) {		
		wrapped.symPut(symbol, referrer);
		fireElementChanged(PROP_SYM_TABLE);
	}
	
	@Override
	public String toString() {
		return wrapped.toString();
	}
	
	@Override
	public void toXml(IXmlWriter writer) {
		wrapped.toXml(writer);
	}
}
