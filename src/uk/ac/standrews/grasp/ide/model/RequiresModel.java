package uk.ac.standrews.grasp.ide.model;

public class RequiresModel extends InterfaceModel {
	
	public RequiresModel(RequiresModel other, FirstClassModel parent) {
		super(other, parent);
	}
	
	public RequiresModel(FirstClassModel parent) {
		super(ElementType.REQUIRES, parent);
	}
	
	@Override
	public void setMaxdeg(int i) {
		// ignore -> no limit on requirements (i.e. always -1)
	}

	@Override
	protected InterfaceModel getLinkEndpoint(LinkModel link) {
		return link.getConsumer();
	}

	@Override
	protected void setLinkEndpoint(LinkModel link, InterfaceModel endpoint) {
		link.setConsumer((RequiresModel) endpoint);
	}
}
