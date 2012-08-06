package uk.ac.standrews.grasp.ide.model;

public abstract class InterfaceModel extends BecauseModel {
	public static final String PROPERTY_MAXDEG = "maxdeg";
	public static final String PROPERTY_HAS_CAPACITY = "hasCapacity";
	
	private ObservableSet<LinkModel> connections = new ObservableSet<LinkModel>();
	private int maxdeg;
	
	public InterfaceModel(InterfaceModel other, FirstClassModel parent) {
		super(other, parent);
		copyCollectionAtTheEndOfCopy(other.getConnections(), connections);
		maxdeg = other.getMaxdeg();
	}

	public InterfaceModel(ElementType type, FirstClassModel parent) {
		super(type, parent);
		maxdeg = -1;
	}

	public boolean connect(LinkModel link) {
		if (getLinkEndpoint(link) == null && hasCapacity() && !connections.contains(link)) {
			setLinkEndpoint(link, this);
			connections.add(link);
			return true;
		} else {
			return false;
		}
	}

	public boolean disconnect(LinkModel link) {
		if (getLinkEndpoint(link) != null && connections.contains(link)) {
			setLinkEndpoint(link, null);
			connections.remove(link);
			return true;
		} else {
			return false;
		}
	}

	public ObservableSet<LinkModel> getConnections() {
		return connections;
	}

	public int getMaxdeg() {
		return maxdeg;
	}

	public boolean hasCapacity() {
		return maxdeg >= 0 ? maxdeg > connections.size() : true;
	}

	public boolean hasConnections() {
		return !connections.isEmpty();
	}

	public void setMaxdeg(int i) {
		maxdeg = i;
		fireElementChanged(PROPERTY_MAXDEG, PROPERTY_HAS_CAPACITY);
	}

	protected abstract InterfaceModel getLinkEndpoint(LinkModel link);

    protected abstract void setLinkEndpoint(LinkModel link, InterfaceModel endpoint);
    
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
