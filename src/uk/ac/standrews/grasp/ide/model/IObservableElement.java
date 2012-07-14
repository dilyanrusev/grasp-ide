package uk.ac.standrews.grasp.ide.model;

import grasp.lang.IElement;

public interface IObservableElement extends IElement {
	void addElementChangedListener(IElementChangedListener listener);
	void removeElementChangedListener(IElementChangedListener listener);	
}
