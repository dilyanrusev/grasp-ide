package uk.ac.standrews.grasp.ide.model;

import grasp.lang.IBecause;
import grasp.lang.IFirstClass;
import grasp.lang.IInterface;
import grasp.lang.ILink;

import java.util.List;

public abstract class InterfaceModel extends BecauseModel implements IInterface {
	public static final String PROPERTY_MAXDEG = "maxdeg";
	public static final String PROPERTY_HAS_CAPACITY = "hasCapacity";
	
	private List<ILink> connections = new ObservableList<ILink>();
	private int maxdeg;
	
	public InterfaceModel(IInterface other, IFirstClass parent) {
		super((IBecause) other, parent);
		for (ILink connection: other.getConnections()) {
			ILink observable = new LinkModel(connection, this);
			connections.add(observable);
		}
		maxdeg = other.getMaxdeg();
	}

	public InterfaceModel(ElementType type, IFirstClass parent) {
		super(type, parent);
		maxdeg = -1;
	}

	@Override
	public boolean connect(ILink link) {
		if (getLinkEndpoint(link) == null && hasCapacity() && !connections.contains(link)) {
			setLinkEndpoint(link, this);
			connections.add(link);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean disconnect(ILink link) {
		if (getLinkEndpoint(link) != null && connections.contains(link)) {
			setLinkEndpoint(link, null);
			connections.remove(link);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public List<ILink> getConnections() {
		return connections;
	}

	@Override
	public int getMaxdeg() {
		return maxdeg;
	}

	@Override
	public boolean hasCapacity() {
		return maxdeg >= 0 ? maxdeg > connections.size() : true;
	}

	@Override
	public boolean hasConnections() {
		return !connections.isEmpty();
	}

	@Override
	public void setMaxdeg(int i) {
		maxdeg = i;
		fireElementChanged(PROPERTY_MAXDEG, PROPERTY_HAS_CAPACITY);
	}

	protected abstract IInterface getLinkEndpoint(ILink link);

    protected abstract void setLinkEndpoint(ILink link, IInterface endpoint);
    
    @Override
    public boolean equals(Object obj) {
    	if (!super.equals(obj)) return false;
    	InterfaceModel other = (InterfaceModel) obj;
    	
    	if (getMaxdeg() != other.getMaxdeg()) return false;
    	// don't compare connections to avoid stack overflow 
    	// (connections contain links, links check their provides/consumers)
    	
    	return true;
    }
    
    @Override
    public int hashCode() {
    	int result = super.hashCode();
    	result = 31 * result + getMaxdeg();
    	// don't compare connections to avoid stack overflow 
    	return result;
    }
}
