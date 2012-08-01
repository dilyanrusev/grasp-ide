package uk.ac.standrews.grasp.ide.model;

import org.eclipse.core.runtime.Assert;

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
    	
    	if (other.getProvider() != null) {
    		final String qname = other.getProvider().getQualifiedName();
    		getArchitecture().executeAtTheEndOfCopy(new Runnable() {			
    			@Override
    			public void run() {
    				provider = (IProvides)getArchitecture().findByQualifiedName(qname);	
    				Assert.isNotNull(provider);
    			}
    		});
    	}
    	if (other.getConsumer() != null) {
    		final String qname = other.getConsumer().getQualifiedName();
    		getArchitecture().executeAtTheEndOfCopy(new Runnable() {			
    			@Override
    			public void run() {
    				consumer = (IRequires)getArchitecture().findByQualifiedName(qname);
    				Assert.isNotNull(consumer);
    			}
    		});
    	}    	
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
	
	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj)) return false;
		LinkModel other = (LinkModel) obj;
		
		if (!objectsEqual(getProvider(), other.getProvider())) return false;
		if (!objectsEqual(getConsumer(), other.getConsumer())) return false;
		
		return true;
	}
	
	@Override
	public int hashCode() {
		int result = super.hashCode();
		
		result = 31 * result + (getProvider() != null ? getProvider().hashCode() : 0);
		result = 31 * result + (getConsumer() != null ? getConsumer().hashCode() : 0);
		
		return result;
	}

}
