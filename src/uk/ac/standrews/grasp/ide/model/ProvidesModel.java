package uk.ac.standrews.grasp.ide.model;

public class ProvidesModel extends InterfaceModel {
	
	public ProvidesModel(ProvidesModel other, FirstClassModel parent) {
		super(other, parent);
	}
	
	public ProvidesModel(FirstClassModel parent) {
		super(ElementType.PROVIDES, parent);
	}

	@Override
	protected InterfaceModel getLinkEndpoint(LinkModel link) {
		return link.getProvider();
	}

	@Override
	protected void setLinkEndpoint(LinkModel link, InterfaceModel endpoint) {
		link.setProvider((ProvidesModel) endpoint);
	}
}
