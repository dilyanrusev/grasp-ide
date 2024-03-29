package uk.ac.standrews.grasp.ide.model;

/**
 * Source or target for {@link ConnectionModel}
 * @author Dilyan Rusev
 *
 */
public interface IConnectionEndpoint {
	/**
	 * Defines the types of connection endpoints
	 * @author Dilyan Rusev
	 *
	 */
	public enum EndpointKind {
		/**
		 * Endpoint that is the connection source
		 */
		Source,
		/**
		 * Endpoint that is the connection target
		 */
		Target
	}
	
	/**
	 * Adds to source connections if source, to target connections if target, or false if not added
	 * @param connection Connection to which this is source or target
	 * @return True if the connection was successfully added
	 */
	boolean addConnection(ConnectionModel connection);
	
	/**
	 * Removes from source connections if source, from target connections if target, or false if not added
	 * @param connection Connection to which this is source or target
	 * @return True if connection was successfully removed
	 */
	boolean removeConnection(ConnectionModel connection);	
	
	
	/**
	 * Retrieves connections where this endpoint is source
	 * @return Source connections
	 */
	ObservableList<ConnectionModel> getSourceConnections();
	
	/**
	 * Retrieves connections where this endpoint is target 
	 * @return Target connections
	 */
	ObservableList<ConnectionModel> getTargetConnections();
	
	/**
	 * Find connection with another node
	 * @param other Other endpoint of the connection
	 * @param kind What role does other play in the connection
	 * @return Connection if connected to other, or null
	 */
	ConnectionModel getConnectionWith(IConnectionEndpoint other, EndpointKind kind);
}
