package uk.ac.standrews.grasp.ide.model;

import grasp.lang.IBecause;
import grasp.lang.IExtensible;
import grasp.lang.IFirstClass;

public abstract class ExtensibleModel extends BecauseModel implements
		IExtensible {
	public static final String PROPERTY_EXTENDEE = "extendee";
	
	private IFirstClass extendee;
	
	public ExtensibleModel(IExtensible other, IFirstClass parent) {
		super((IBecause) other, parent);
		extendee = (IFirstClass) GraspModel.makeObservable(other.getExtendee(), this);
	}
	
	public ExtensibleModel(ElementType type, IFirstClass parent) {
		super(type, parent);
	}

	@Override
	public IFirstClass getExtendee() {
		return extendee;
	}

	@Override
	public void setExtendee(IFirstClass extendee) {
		this.extendee = extendee;
		fireElementChanged(PROPERTY_EXTENDEE);
	}

}
