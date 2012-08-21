package uk.ac.standrews.grasp.ide.model;

import java.util.HashSet;
import java.util.Set;


/**
 * Perform a refactoring over the graph by notifying all elements of the change
 * @author Dilyan Rusev
 *
 */
public class Refactor {
	public static final String OPERATION_RENAME = "rename";
	public static final String OPERATION_ALIAS_RENAME = "alias rename";
	
	
	/**
	 * Rename an element
	 * @param element Element to rename
	 * @param newName New name
	 */
	public static void rename(ElementModel element, String newName) {		
		String oldName = element.getName();
		element.setName(newName);		
		IVisitor visitor = new DefaultVisitor(OPERATION_RENAME, element, oldName, newName);		
		visit(element.getArchitecture(), visitor);		
	}
	
	/**
	 * Rename an element
	 * @param iface Element to rename
	 * @param newAlias New name
	 */
	public static void changeAlias(InterfaceModel iface, String newAlias) {		
		String oldName = iface.getAlias();
		iface.setAlias(newAlias);		
		IVisitor visitor = new DefaultVisitor(OPERATION_ALIAS_RENAME, iface, oldName, newAlias);		
		visit(iface.getArchitecture(), visitor);		
	}
	
	
	private static boolean visit(ElementModel element, IVisitor visitor) {
		if (element == null || visitor.isVisited(element)) return false;
		
		switch (element.getType()) {
		case ANNOTATION:
			return visitAnnotation((AnnotationModel) element, visitor);
		case ARCHITECTURE:
			return visitFirstClass((FirstClassModel) element, visitor);			
		case CHECK:
			return visitEvaluable((EvaluableModel) element, visitor);			
		case COMPONENT:
			return visitInstantiable((InstantiableModel) element, visitor);			
		case CONNECTOR:
			return visitInstantiable((InstantiableModel) element, visitor);			
		case EXPRESSION:
			return visitExpression((ExpressionModel) element, visitor);			
		case LAYER:
			return visitLayer((LayerModel) element, visitor);			
		case LINK:
			return visitLink((LinkModel) element, visitor);			
		case NAMEDVALUE:
			return visitNamedValue((NamedValueModel) element, visitor);			
		case PROPERTY:
			return visitEvaluable((EvaluableModel) element, visitor);			
		case PROVIDES:
			return visitInterface((ProvidesModel) element, visitor);			
		case QUALITY_ATTRIBUTE:
			return visitQualityAttribute((QualityAttributeModel) element, visitor);			
		case RATIONALE:
			return visitParameterized((ParameterizedModel) element, visitor);			
		case REASON:
			return visitReason((ReasonModel) element, visitor);			
		case REQUIREMENT:
			return visitRequirement((RequirementModel) element, visitor);			
		case REQUIRES:
			return visitInterface((RequiresModel) element, visitor);			
		case SYSTEM:
			return visitBecause((BecauseModel) element, visitor);			
		case TEMPLATE:
			return visitParameterized((ParameterizedModel) element, visitor);			
		default:
			throw new IllegalArgumentException("Unrecongnized element type: " + element.getType());
		}
	}
	
	private static boolean visitEvaluable(EvaluableModel element,
			IVisitor visitor) {		
		if (visitBecause(element, visitor)) {
			visitExpression(element.getExpression(), visitor);
			return true;
		}
		return false;
	}
	
	private static boolean visitParameterized(ParameterizedModel element,
			IVisitor visitor) {
		if (visitBecause(element, visitor)) {
			visit(element.getExtendee(), visitor);
			return true;
		} 
		return false;
	}
	
	private static boolean visitInstantiable(InstantiableModel element,
			IVisitor visitor) {		
		if (visitBecause(element, visitor)) {
			visitParameterized(element.getBase(), visitor);
			return true;
		}
		return false;
	}
	
	private static boolean visitLayer(LayerModel element,
			IVisitor visitor) {		 
		if (visitBecause(element, visitor)) {
			for (LayerModel over: element.getOver()) {
				visitLayer(over, visitor);
			}
			return true;
		}
		return false;
	}

	private static boolean visitLink(LinkModel element,
			IVisitor visitor) {
		if (visitBecause(element, visitor)) {
			visitInterface(element.getProvider(), visitor);
			visitInterface(element.getConsumer(), visitor);
			return true;
		}
		return false;
	}	
	
	private static boolean visitInterface(InterfaceModel element,
			IVisitor visitor) {
		if (visitBecause(element, visitor)) {
			for (LinkModel connection: element.getConnections()) {
				visitLink(connection, visitor);
			}
			return true;
		}
		return false;
	}

	private static boolean visitBecause(BecauseModel element,
			IVisitor visitor) {
		if (visitFirstClass(element, visitor)) {
			for (RationaleModel rationale: element.getRationales()) {
				visitParameterized(rationale, visitor);
			}
			return true;
		}
		return false;
	}	

	private static boolean visitQualityAttribute(QualityAttributeModel element,
			IVisitor visitor) {
		if (visitFirstClass(element, visitor)) {
			for (FirstClassModel supports: element.getSupports()) {
				visit(supports, visitor);
			}
			return true;
		}
		return false;
	}

	private static boolean visitReason(ReasonModel element,
			IVisitor visitor) {
		if (visitFirstClass(element, visitor)) {
			visitExpression(element.getExpression(), visitor);
			for (FirstClassModel supports: element.getSupports()) {
				visit(supports, visitor);
			}
			for (FirstClassModel inhibits: element.getInhibits()) {
				visit(inhibits, visitor);
			}
			return true;
		} 
		return false;
	}

	private static boolean visitRequirement(RequirementModel element,
			IVisitor visitor) {
		if (visitFirstClass(element, visitor)) {		
			visitExpression(element.getExpression(), visitor);
			return true;
		} else {
			return false;
		}
	}

	private static boolean visitAnnotation(AnnotationModel element,
			IVisitor visitor) {
		if (!visitor.isVisited(element)) {
			visitor.visit(element);
			for (NamedValueModel nv: element.getNamedValues()) {
				visitNamedValue(nv, visitor);
			}
			return true;
		}
		return false;
	}
	
	private static boolean visitNamedValue(NamedValueModel element,
			IVisitor visitor) {
		if (!visitor.isVisited(element)) {
			visitor.visit(element);
			visitExpression(element.getExpression(), visitor);
			return true;
		}
		return false;
	}

	private static boolean visitExpression(ExpressionModel element,
			IVisitor visitor) {
		if (!visitor.isVisited(element)) {
			visitor.visit(element);
			return true;
		}
		return false;
	}

	private static boolean visitFirstClass(FirstClassModel element,
			IVisitor visitor) {
		if (visitor.isVisited(element)) return false;
		for (AnnotationModel annotation: element.getAnnotations()) {
			visitAnnotation(annotation, visitor);
		}
		visitor.visit(element);
		for (FirstClassModel child: element.getBody()) {
			visit(child, visitor);
		}
		return true;
	}
	
	
}

/**
 * Notifies individual elements that a change will be performed
 * @author Dilyan Rusev
 *
 */
interface IVisitor {
	/**
	 * Notify an element that a refactoring operation will be performed
	 * @param element Element to be notified of the change	 
	 */
	void visit(IRefactorable element);
	
	/**
	 * Returns true of the element was visited
	 * @param element Element to check
	 * @return 
	 */
	boolean isVisited(IRefactorable element);
}

/**
 * Default implementation of IVisitor
 * @author Dilyan Rusev
 *
 */
class DefaultVisitor implements IVisitor {
	private final Set<IRefactorable> visited;
	private final Object oldValue;
	private final Object newValue;
	private final String operation;
	private final ElementModel source;
	
	/**
	 * Create a new notifier
	 */
	public DefaultVisitor(String operation, ElementModel source, Object oldValue, Object newValue) {
		visited = new HashSet<IRefactorable>();	
		this.oldValue = oldValue;
		this.newValue = newValue;
		this.operation = operation;
		this.source = source;
	}
	
	
	@Override
	public void visit(IRefactorable element) {
		visited.add(element);
		element.elementRefactored(source, operation, oldValue, newValue);
	}
	
	@Override
	public boolean isVisited(IRefactorable element) {
		return visited.contains(element);
	}
}