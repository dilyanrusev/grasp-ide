package uk.ac.standrews.grasp.ide.model;

import grasp.lang.IFirstClass;
import grasp.lang.IInterface;
import grasp.lang.ILink;
import grasp.lang.IRequires;

public class RequiresModel extends InterfaceModel implements IRequires {
	
	public RequiresModel(IRequires other, IFirstClass parent) {
		super(other, parent);
	}
	
	public RequiresModel(IFirstClass parent) {
		super(ElementType.REQUIRES, parent);
	}
	
	@Override
	public void setMaxdeg(int i) {
		// ignore -> no limit on requirements (i.e. always -1)
	}

	@Override
	protected IInterface getLinkEndpoint(ILink link) {
		return link.getConsumer();
	}

	@Override
	protected void setLinkEndpoint(ILink link, IInterface endpoint) {
		link.setConsumer((IRequires) endpoint);
	}
}
