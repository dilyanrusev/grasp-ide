package uk.ac.standrews.grasp.ide.model;

public class LayerModel extends BecauseModel 
	implements IConnectionEndpoint {
	
	private ObservableSet<LayerModel> over = new ObservableSet<LayerModel>();
	private ObservableList<ConnectionModel> sourceConnections = new ObservableList<ConnectionModel>();
	private ObservableList<ConnectionModel> targetConnections = new ObservableList<ConnectionModel>();
	
	public LayerModel(LayerModel other, FirstClassModel parent) {
		super(other, parent);
		copyCollectionAtTheEndOfCopy(other.getOver(), over);
	}
	
	public LayerModel(FirstClassModel parent) {
		super(ElementType.LAYER, parent);
	}

	public ObservableSet<LayerModel> getOver() {
		return over;
	}
	
	@Override
	protected int doCompareTo(ElementModel elem) {
		LayerModel other = (LayerModel) elem;
		if (this.getOver().contains(other)) {
			return 1;
		}
		if (other.getOver().contains(this)) {
			return -1;
		}
		return super.doCompareTo(other);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj)) return false;
		return collectionsEqual(getOver(), ((LayerModel) obj).getOver());
	}
	
	@Override
	public int hashCode() {
		int result = super.hashCode();
		
		for (LayerModel layer: getOver()) {
			result = 31 * result + layer.hashCode();
		}
		
		return result;
	}

	@Override
	public boolean addConnection(ConnectionModel connection) {
		if (this == connection.getSource()) {
			return sourceConnections.add(connection);
		} else if (this == connection.getTarget()) {
			return targetConnections.add(connection);
		}
		return false;
	}

	@Override
	public boolean removeConnection(ConnectionModel connection) {
		if (this == connection.getSource()) {
			return sourceConnections.remove(connection);
		} else if (this == connection.getTarget()) {
			return targetConnections.remove(connection);
		}
		return false;
	}

	@Override
	public ObservableList<ConnectionModel> getSourceConnections() {
		return sourceConnections;
	}

	@Override
	public ObservableList<ConnectionModel> getTargetConnections() {
		return targetConnections;
	}

	@Override
	public ConnectionModel getConnectionWith(IConnectionEndpoint other,
			EndpointKind kind) {
		if (kind == EndpointKind.Source) {
			for (ConnectionModel connection: targetConnections) {
				if (connection.getSource() == other) {
					return connection;
				}
			}
		} else {
			for (ConnectionModel connection: sourceConnections) {
				if (connection.getTarget() == other) {
					return connection;
				}
			}
		}
		return null;
	}

	
}
