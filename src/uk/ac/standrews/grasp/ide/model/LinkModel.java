package uk.ac.standrews.grasp.ide.model;

import grasp.lang.IFirstClass;
import grasp.lang.ILink;
import grasp.lang.IProvides;
import grasp.lang.IRequires;

public class LinkModel extends BecauseModel implements ILink {	
	public static final String PROPERTY_CONSUMER = "consumer";
	public static final String PROPERTY_PROVIDER = "provider";
	
	private IProvides provider;
    private IRequires consumer;

    public LinkModel(ILink other, IFirstClass parent) {
    	super(other, parent);
    	// TODO: copy consumer and provider
    }
    
    public LinkModel(IFirstClass parent) {
    	super(ElementType.LINK, parent);
    }
    
	@Override
	public IRequires getConsumer() {
		return consumer;
	}

	@Override
	public IProvides getProvider() {
		return provider;
	}

	@Override
	public void setConsumer(IRequires consumer) {
		this.consumer = consumer;
		fireElementChanged(PROPERTY_CONSUMER);
	}

	@Override
	public void setProvider(IProvides provider) {
		this.provider = provider;
		fireElementChanged(PROPERTY_PROVIDER);
	}

}
