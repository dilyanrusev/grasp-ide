package uk.ac.standrews.grasp.ide.model;

public class ExpressionModel extends ElementModel {
	private Object value;
	
	public ExpressionModel(ElementModel parent) {
		super(ElementType.EXPRESSION, parent);
	}
	
	public static ExpressionModel createLiteral(ElementModel parent, Object value) {
		ExpressionModel literal = new ExpressionModel(parent);
		literal.value = value;
		return literal;
	}
	
	public ExpressionModel(ExpressionModel other, ElementModel parent) {
		super(other, parent);
		this.value = other.getValue();
	}

	public Object getValue() {
		return value;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!(obj instanceof ExpressionModel)) return false;
		ExpressionModel other = (ExpressionModel) obj;
		
		if (!objectsEqual(getValue(), other.getValue())) return false;
		
		return true;
	}
	
	@Override
	public int hashCode() {
		int result = 17;
		
		result = 31 * result + (getValue() != null ? getValue().hashCode() : 0);
		
		return result;
	}

}
