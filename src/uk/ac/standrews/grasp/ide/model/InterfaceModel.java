package uk.ac.standrews.grasp.ide.model;

/**
 * Base class for elements that provide the component and connector interface
 * @author Dilyan Rusev
 *
 */
public abstract class InterfaceModel extends BecauseModel {
	/** Maximum number of instantiations */
	public static final String PROPERTY_MAXDEG = "maxdeg";
	/** Whether or not it can be further instantiated */
	public static final String PROPERTY_HAS_CAPACITY = "hasCapacity";
	
	private ObservableSet<LinkModel> connections = new ObservableSet<LinkModel>();
	private int maxdeg;
	
	/**
	 * Construct a copy of another element
	 * @param other Source
	 * @param parent Parent element
	 */
	public InterfaceModel(InterfaceModel other, FirstClassModel parent) {
		super(other, parent);
		copyCollectionAtTheEndOfCopy(other.getConnections(), connections);
		maxdeg = other.getMaxdeg();
	}

	/**
	 * Construct a new element with infinite capacity
	 * @param type Type of the element
	 * @param parent Parent element
	 */
	public InterfaceModel(ElementType type, FirstClassModel parent) {
		super(type, parent);
		maxdeg = -1;
	}

	/**
	 * Add this as an endpoint of a connection and add the connection to the list of connections using this interface
	 * @param link Connection that contains the two endpoints
	 * @return True if successfully added this as an endpoint
	 */
	public boolean connect(LinkModel link) {
		if (getLinkEndpoint(link) == null && hasCapacity() && !connections.contains(link)) {
			setLinkEndpoint(link, this);
			connections.add(link);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Remove this as an endpoint of a connection and remove the connection from the list of connections using this interface
	 * @param link Connection that contains the two endpoints
	 * @return True if successfully removed this as an endpoint
	 */
	public boolean disconnect(LinkModel link) {
		if (getLinkEndpoint(link) != null && connections.contains(link)) {
			setLinkEndpoint(link, null);
			connections.remove(link);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Return a list of connections using this interface
	 * @return
	 */
	public ObservableSet<LinkModel> getConnections() {
		return connections;
	}

	/**
	 * Get the maximum number of connections to this interface
	 * @return
	 */
	public int getMaxdeg() {
		return maxdeg;
	}

	/**
	 * Return true if further connections can be made to this interface
	 * @return
	 */
	public boolean hasCapacity() {
		return maxdeg >= 0 ? maxdeg > connections.size() : true;
	}

	/**
	 * Return true if there are any connections attached to this interface
	 * @return
	 */
	public boolean hasConnections() {
		return !connections.isEmpty();
	}

	/**
	 * Update the maximum number of connections to this interface
	 * @param i New number of connections. -1 is infinite
	 */
	public void setMaxdeg(int i) {
		maxdeg = i;
		fireElementChanged(PROPERTY_MAXDEG, PROPERTY_HAS_CAPACITY);
	}

	/**
	 * Return self if attached to a connection, or null if this interface is not part of the connection
	 * @param link Connection to test
	 * @return This or null
	 */
	protected abstract InterfaceModel getLinkEndpoint(LinkModel link);

	/**
	 * Set or unset self as an endpoint to the connection
	 * @param link Connection
	 * @param endpoint This or null to unset
	 */
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
