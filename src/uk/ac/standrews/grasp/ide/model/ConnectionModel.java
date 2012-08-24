package uk.ac.standrews.grasp.ide.model;

import org.eclipse.core.runtime.Assert;

/**
 * Model class for visual connections between two model elements
 * @author Dilyan Rusev
 *
 */
public class ConnectionModel {
	private IConnectionEndpoint source;
	private IConnectionEndpoint target;
	private final ElementType endpointType;
	
	/**
	 * Construct a new connection
	 * @param source Source connection. Cannot be null or the same as target.
	 * @param target Target connection. Cannot be null or the same as source.
	 * @param endpointType Type of the endpoint connections. Cannot be null.
	 */
	public ConnectionModel(IConnectionEndpoint source, IConnectionEndpoint target, ElementType endpointType) {
		Assert.isNotNull(source);
		Assert.isNotNull(target);
		Assert.isNotNull(endpointType);
		Assert.isLegal(source != target);		
		
		this.source = source;
		this.target = target;
		this.endpointType = endpointType;
	}
	
	/**
	 * Get the source model
	 * @return Source model
	 */
	public IConnectionEndpoint getSource() {
		return source;
	}
	
	/**
	 * Get the target model
	 * @return Target model
	 */
	public IConnectionEndpoint getTarget() {
		return target;
	}
	
	/**
	 * Get the type of the endpoints
	 * @return Type of contained endpoints
	 */
	public ElementType getEndpointType() {
		return endpointType;
	}
	
	/**
	 * Connect this connection to the endpoints
	 */
	public void connect() {
		source.addConnection(this);
		target.addConnection(this);
	}
	
	/**
	 * Disconnect this connection from the endpoints
	 */
	public void disconnect() {
		source.removeConnection(this);
		target.removeConnection(this);
	}
	
	/**
	 * Reconnect connection to different endpoints
	 * @param source Source connection. Cannot be null or the same as target.
	 * @param target Target connection. Cannot be null or the same as source. 
	 */
	public void reconnect(IConnectionEndpoint source, IConnectionEndpoint target) {
		Assert.isNotNull(source);
		Assert.isNotNull(target);
		Assert.isLegal(source != target);
		
		disconnect();
		this.source = source;
		this.target = target;
		connect();
	}
	
	@Override
	public String toString() {
		return new StringBuilder("s=").append(source).append(" t=").append(target).toString();
	}
}
