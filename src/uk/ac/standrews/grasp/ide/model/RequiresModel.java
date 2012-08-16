package uk.ac.standrews.grasp.ide.model;

/**
 * Represents the Grasp requires inteface
 * @author Dilyan Rusev
 *
 */
public class RequiresModel extends InterfaceModel {
	
	/**
	 * Create a copy of another requires
	 * @param other Requires element to copy
	 * @param parent Parent element
	 */
	public RequiresModel(RequiresModel other, FirstClassModel parent) {
		super(other, parent);
	}
	
	/**
	 * Creates a new requires interface
	 * @param parent Parent element
	 */
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
