package uk.ac.standrews.grasp.ide.model;


/**
 * Provides an interface for elements that export functionality
 * @author Dilyan Rusev
 *
 */
public class ProvidesModel extends InterfaceModel {
	
	/**
	 * Create a copy of another interface
	 * @param other Interface to copy
	 * @param parent Parent element
	 */
	public ProvidesModel(ProvidesModel other, FirstClassModel parent) {
		super(other, parent);
	}
	
	/**
	 * Create a new interface
	 * @param parent Parent element
	 */
	public ProvidesModel(FirstClassModel parent) {
		super(ElementType.PROVIDES, parent);
	}
	
	@Override
	public InterfaceModel createCopy(FirstClassModel parent) {
		return new ProvidesModel(this, parent);
	}

	@Override
	protected InterfaceModel getLinkEndpoint(LinkModel link) {
		return link.getProvider();
	}

	@Override
	protected void setLinkEndpoint(LinkModel link, InterfaceModel endpoint) {
		link.setProvider((ProvidesModel) endpoint);
	}

	@Override
	protected InterfaceModel getOtherLinkEndpoint(LinkModel link) {
		return link.getConsumer();
	}
}
