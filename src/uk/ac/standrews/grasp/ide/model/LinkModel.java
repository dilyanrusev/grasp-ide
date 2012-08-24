package uk.ac.standrews.grasp.ide.model;

import org.eclipse.core.runtime.Assert;

/**
 * Model class for the Grasp link statement
 * @author Dilyan Rusev
 *
 */
public class LinkModel extends BecauseModel {	
	/**
	 * ID of the consumer property
	 */
	public static final String PROPERTY_CONSUMER = "consumer";
	/**
	 * ID of the provider property
	 */
	public static final String PROPERTY_PROVIDER = "provider";
	
	private ProvidesModel provider;
    private RequiresModel consumer;

    /**
     * Construct a copy of another link
     * @param other Link to copy
     * @param parent Parent of the copy
     */
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
        
    /**
     * Construct a new link
     * @param parent Parent of the link
     */
    public LinkModel(FirstClassModel parent) {
    	super(ElementType.LINK, parent);
    }
    
    /**
     * Get a reference to the consumer
     * @return Consumer
     */
	public RequiresModel getConsumer() {
		return consumer;
	}

	/**
	 * Get a references to the provider
	 * @return Provider
	 */
	public ProvidesModel getProvider() {
		return provider;
	}

	/**
	 * Set the consumer of this link
	 * @param consumer New consumer
	 */
	public void setConsumer(RequiresModel consumer) {
		this.consumer = consumer;
		fireElementChanged(PROPERTY_CONSUMER);
	}

	/**
	 * Set the provider of this link
	 * @param provider New provider
	 */
	public void setProvider(ProvidesModel provider) {
		this.provider = provider;
		fireElementChanged(PROPERTY_PROVIDER);
	}
	
	/**
	 * Get a local name for the link provider
	 * @return Qualified name relative to the parent of the component/connector containing the provider
	 */
	public String getProviderLocalName() {
		return getLocalName(getProvider());
	}
	
	/**
	 * Get a local name for the link consumer
	 * @return Qualified name relative to the parent of the component/connector containing the provider
	 */
	public String getConsumerLocalName() {
		return getLocalName(getConsumer());
	}
	
	private String getLocalName(InterfaceModel iface) {
		if (getParent().getType() == ElementType.SYSTEM) {
			String systemQName = getParent().getQualifiedName();
			return iface.getQualifiedName().substring(systemQName.length() + 1);			
		} else {
			String layerQName = iface.getParent().getParent().getQualifiedName();
			return iface.getQualifiedName().substring(layerQName.length() + 1);
		}
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
	
	@Override
	public void elementRefactored(ElementModel element, String operation,
			Object oldName,	Object newName) {
		super.elementRefactored(element, operation, oldName, newName);
		
		if (element == getProvider()) {
			fireElementChanged(PROPERTY_PROVIDER);
		} else if (element == getConsumer()) {
			fireElementChanged(PROPERTY_CONSUMER);
		} else if (element instanceof InstantiableModel) {
			InstantiableModel in = (InstantiableModel) element;
			if (in.getBody().contains(getConsumer()) || in.getBody().contains(getProvider())) {
				fireElementChanged(PROPERTY_PROVIDER, PROPERTY_CONSUMER);
			}
		}		
	}
	
	@Override
	public ElementModel removeFromParent() {
		getProvider().getConnections().remove(this);
		getProvider().getDeletedConnections().add(this);
		getConsumer().getConnections().remove(this);
		getConsumer().getDeletedConnections().add(this);
		return super.removeFromParent();
	}

}
