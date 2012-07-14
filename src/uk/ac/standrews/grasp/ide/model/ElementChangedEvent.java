package uk.ac.standrews.grasp.ide.model;

public class ElementChangedEvent {
	private final IObservableElement source;
	private final String propertyName;
	
	public ElementChangedEvent(IObservableElement source, String propertyName) {
		this.source = source;
		this.propertyName = propertyName;
	}
	
	public final IObservableElement getSource() {
		return source;
	}
	
	public final String getPropertyName() {
		return propertyName;
	}
}
