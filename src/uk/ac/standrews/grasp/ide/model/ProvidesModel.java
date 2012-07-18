package uk.ac.standrews.grasp.ide.model;

import grasp.lang.IFirstClass;
import grasp.lang.IInterface;
import grasp.lang.ILink;
import grasp.lang.IProvides;

public class ProvidesModel extends InterfaceModel implements IProvides {
	
	public ProvidesModel(IProvides other, IFirstClass parent) {
		super(other, parent);
	}
	
	public ProvidesModel(IFirstClass parent) {
		super(ElementType.PROVIDES, parent);
	}

	@Override
	protected IInterface getLinkEndpoint(ILink link) {
		return link.getProvider();
	}

	@Override
	protected void setLinkEndpoint(ILink link, IInterface endpoint) {
		link.setProvider((IProvides)endpoint);
	}
}
