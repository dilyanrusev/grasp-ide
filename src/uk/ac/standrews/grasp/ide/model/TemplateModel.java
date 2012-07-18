package uk.ac.standrews.grasp.ide.model;

import grasp.lang.IFirstClass;
import grasp.lang.ISyntaxNode;
import grasp.lang.ITemplate;

public class TemplateModel extends ParameterizedModel implements ITemplate {
	public static final String PROPERTY_PAYLOAD = "payload";
	
	private ISyntaxNode payload;
	
	public TemplateModel(ITemplate other, IFirstClass parent) {
		super(other, parent);
		if (other.getPayload() != null) {
			payload = new SyntaxNodeModel(other.getPayload(), other.getPayload().getOwner());
		}
	}
	
	public TemplateModel(IFirstClass parent) {
		super(ElementType.TEMPLATE, parent);
	}

	@Override
	public ISyntaxNode getPayload() {
		return payload;
	}

	@Override
	public void setPayload(ISyntaxNode payload) {
		this.payload = payload;
		this.fireElementChanged(PROPERTY_PAYLOAD);
	}

}
