package uk.ac.standrews.grasp.ide.model;

import org.eclipse.core.runtime.Assert;

public class LinkModel extends BecauseModel {	
	public static final String PROPERTY_CONSUMER = "consumer";
	public static final String PROPERTY_PROVIDER = "provider";
	
	private ProvidesModel provider;
    private RequiresModel consumer;

    public LinkModel(LinkModel other, FirstClassModel parent) {
    	super(other, parent);
    	
    	if (other.getProvider() != null) {
    		final String qname = other.getProvider().getQualifiedName();
    		getArchitecture().executeAtTheEndOfCopy(new Runnable() {			
    			@Override
    			public void run() {
    				provider = (ProvidesModel) getArchitecture().findByQualifiedName(qname);	
    				Assert.isNotNull(provider);
    			}
    		});
    	}
    	if (other.getConsumer() != null) {
    		final String qname = other.getConsumer().getQualifiedName();
    		getArchitecture().executeAtTheEndOfCopy(new Runnable() {			
    			@Override
    			public void run() {
    				consumer = (RequiresModel) getArchitecture().findByQualifiedName(qname);
    				Assert.isNotNull(consumer);
    			}
    		});
    	}    	
    }    
        
    public LinkModel(FirstClassModel parent) {
    	super(ElementType.LINK, parent);
    }
    
	public RequiresModel getConsumer() {
		return consumer;
	}

	public ProvidesModel getProvider() {
		return provider;
	}

	public void setConsumer(RequiresModel consumer) {
		this.consumer = consumer;
		fireElementChanged(PROPERTY_CONSUMER);
	}

	public void setProvider(ProvidesModel provider) {
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
