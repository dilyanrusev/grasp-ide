package uk.ac.standrews.grasp.ide.model.expressions;

import uk.ac.standrews.grasp.ide.model.ElementModel;
import uk.ac.standrews.grasp.ide.model.ExpressionModel;
import uk.ac.standrews.grasp.ide.model.ExpressionType;

/**
 * Helper class for parsing expressions from strings
 * @author Dilyan Rusev
 *
 */
public class ExpressionParser {
	/**
	 * Construct an expression when the type is already know (such as when loading from XML)
	 * @param text Expression
	 * @param value String value
	 * @param type Type
	 * @param parent Grasp parent
	 * @return ExpressionModel
	 */
	public static ExpressionModel construct(String value, String text, String type, ElementModel parent) {
		ExpressionType expressionType = ExpressionType.parseXmlAttribute(type);
		ExpressionModel result = new ExpressionModel(parent);
		result.setExpressionType(expressionType);
		result.setText(text);
		result.setValue(parseValue(value, expressionType));
		return result;
	}
	
	/**
	 * Parse the value of an expression when the type is already known
	 * @param text Expression
	 * @param type Type
	 * @return Parsed expression
	 */
	public static Object parseValue(String text, ExpressionType type) {
		switch (type) {
		case BOOLEAN:
			return Boolean.valueOf(text);			
		case INTEGER:
			return Integer.valueOf(text);
		case REAL:
			return Double.valueOf(text);
		case SET:
			return null;
		case STRING:
			return text;
		default:
			throw new IllegalArgumentException("Unknow type: " + type);		
		}
	}
}
