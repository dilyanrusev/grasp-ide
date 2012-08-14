package uk.ac.standrews.grasp.ide.model;

/**
 * Source or target for {@link ConnectionModel}
 * @author Dilyan Rusev
 *
 */
public interface IConnectionEndpoint {
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
	 * @return
	 */
	ObservableList<ConnectionModel> getSourceConnections();
	
	/**
	 * Retrieves connections where this endpoint is target 
	 * @return
	 */
	ObservableList<ConnectionModel> getTargetConnections();
}
