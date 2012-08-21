package uk.ac.standrews.grasp.ide.model;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Collection;

import uk.ac.standrews.grasp.ide.editors.TextUtil;

/**
 * Serialises Grasp graph to grasp source code
 * @author Dilyan Rusev
 *
 */
public class ModelToSourceSerializer {
	
	/**
	 * Perform serialisation
	 * @param graph Grasp architecture model graph
	 * @param encoding Text encoding. If null, will use the default charset.
	 * @return Stream that contains the encoded text
	 */
	public InputStream serializeToStream(ArchitectureModel graph, Charset encoding) {		
		StatementBuilder builder = new StatementBuilder();
		
		writeArchitecture(builder, graph);
		
		return builder.toStream(encoding);
	}
	
	private void writeAnnotations(StatementBuilder builder, 
			FirstClassModel forModel) {
		for (AnnotationModel annotation: forModel.getAnnotations()) {
			writeAnnotation(builder, annotation);
		}
	}
	
	private void writeArchitecture(StatementBuilder builder, ArchitectureModel model) {
		writeAnnotations(builder, model);
		builder
			.openStatement(model)
			.identifier(model.getName())
		;
		
		builder.startBody();		
		for (FirstClassModel child: model.getBody()) {
			writeArchitectureChild(builder, child);
		}
		builder.endBody();
	}
	
	private void writeArchitectureChild(StatementBuilder builder,
			FirstClassModel child) {
		switch (child.getType()) {
		case REQUIREMENT:
			writeRequirement(builder, (RequirementModel) child);
			break;
		case QUALITY_ATTRIBUTE:
			writeQualityAttribute(builder, (QualityAttributeModel) child);
			break;
		case TEMPLATE:
			writeTemplate(builder, (TemplateModel) child);
			break;
		case RATIONALE:
			writeRationale(builder, (RationaleModel) child);
			break;
		case SYSTEM:
			writeSystem(builder, (SystemModel) child);
			break;
		default:
			throw new IllegalArgumentException("Invalid architecture child");
		}
	}
	
	private void writeChild(StatementBuilder builder,
			FirstClassModel child) {
		switch (child.getType()) {
		case LAYER:
			writeLayer(builder, (LayerModel) child);
			break;
		case COMPONENT:
			writeComponent(builder, (ComponentModel) child);
			break;
		case CONNECTOR:
			writeConnector(builder, (ConnectorModel) child);
			break;
		case REQUIRES:
			writeRequires(builder, (RequiresModel) child);
			break;
		case PROVIDES:
			writeProvides(builder, (ProvidesModel) child);
			break;
		case LINK:
			writeLink(builder, (LinkModel) child);
			break;
		case CHECK:
			writeCheck(builder, (CheckModel) child);
			break;
		case PROPERTY:
			writeProperty(builder, (PropertyModel) child);
			break;
		default:
			throw new IllegalArgumentException("Invalid child type: "
					+ child.getType());
		}
	}
	
	private void writeCheck(StatementBuilder builder, CheckModel model) {
		writeAnnotations(builder, model);
		builder.openStatement(model);
		writeEqualsExpression(builder, model.getExpression());
		writeBecause(builder, model);
		builder.closeStatement(model);		
	}
	
	private void writeLink(StatementBuilder builder, LinkModel model) {
		writeAnnotations(builder, model);
		builder.openStatement(model);
		if (!TextUtil.isNullOrEmpty(model.getName())) {
			builder.identifier(model.getName());
		}
		builder
			.identifier(model.getConsumerLocalName())
			.keyword("to")
			.identifier(model.getProviderLocalName());
		if (model.getBody().size() > 0) {
			builder.startBody();
			for (FirstClassModel child: model.getBody()) {
				writeCheck(builder, (CheckModel) child);
			}
			builder.endBody();
		} else {
			builder.closeStatement(model);
		}
	}

	private void writeProvides(StatementBuilder builder, ProvidesModel model) {
		writeInterface(builder, model);
	}
	
	private void writeRequires(StatementBuilder builder, RequiresModel model) {
		writeInterface(builder, model);
	}

	private void writeInterface(StatementBuilder builder
			, InterfaceModel model) {
		startFirstClass(builder, model);
		if (model.getAlias() != null) {
			builder.identifier(model.getAlias());			
		}
		writeBecause(builder, model);
		if (model.getBody().size() > 0) {
			builder.startBody();
			for (FirstClassModel child: model.getBody()) {
				writeProperty(builder, (PropertyModel) child);
			}
			builder.endBody();
		} else {
			builder.closeStatement(model);
		}
	}

	private void writeInstantiable(StatementBuilder builder,
			InstantiableModel model) {
		startFirstClass(builder, model);
		builder
			.equals()
			.identifier(model.getBase().getReferencingName());
		builder.openBracket();
		writeCommaSeparatedReferencesList(builder, model.getArguments());
		builder.closeBracket();
		builder.closeStatement(model);
	}
	
	private void writeConnector(StatementBuilder builder, ConnectorModel model) {
		writeInstantiable(builder, model);
	}

	private void writeComponent(StatementBuilder builder, ComponentModel model) {
		writeInstantiable(builder, model);
	}

	private void writeLayer(StatementBuilder builder, LayerModel model) {
		startFirstClass(builder, model);
		if (model.getOver().size() > 0) {
			builder.keyword("over");
			writeCommaSeparatedReferencesList(builder, model.getOver());
		}
		writeBecause(builder, model);
		writeBody(builder, model);
	}

	private void writeSystem(StatementBuilder builder
			, SystemModel model) {
		startFirstClass(builder, model);
		writeBecause(builder, model);
		writeBody(builder, model);
	}
	
	private void writeCommaSeparatedReferencesList(
			StatementBuilder builder
			, Collection<? extends ElementModel> references) {
		boolean first = true;
		for (ElementModel ref: references) {
			if (!first) builder.comma(); else first = false;
			builder.identifier(ref.getReferencingName());
		}
	}
	
	private void writeSupports(StatementBuilder builder
			, Collection<? extends ElementModel> references) {
		if (references.size() > 0) {
			builder.keyword("supports");
			writeCommaSeparatedReferencesList(builder, references);
		}
	}
	
	private void writeInhibits(StatementBuilder builder
			, Collection<? extends ElementModel> references) {
		if (references.size() > 0) {
			builder.keyword("inhibits");
			writeCommaSeparatedReferencesList(builder, references);
		}
	}

	private void writeReason(StatementBuilder builder
			, ReasonModel model) {
		writeAnnotations(builder, model);
		builder.openStatement(model);
		if (model.getExpression() != null) {
			builder.expression(model.getExpression());
		} else {
			writeSupports(builder, model.getSupports());
		}
		writeInhibits(builder, model.getInhibits());
		builder.closeStatement(model);
	}
	
	private void writeRationale(StatementBuilder builder
			, RationaleModel model) {
		startFirstClass(builder, model);
		writeParameters(builder, model);
		writeExtendee(builder, model);
		writeBecause(builder, model);
		builder.startBody();
		for (FirstClassModel child: model.getBody()) {
			writeReason(builder, (ReasonModel) child);
		}
		builder.endBody();
	}
	
	private void writeParameters(StatementBuilder builder,
			ParameterizedModel model) {
		builder.openBracket();
		boolean firstParam = true;
		for (String param: model.getParameters()) {
			if (!firstParam) builder.comma();
			firstParam = false;
			builder.identifier(param);
		}
		builder.closeBracket();
	}
	
	private void writeExtendee(StatementBuilder builder,
			ExtensibleModel model) {
		if (model.getExtendee() != null) {
			builder.identifier(model.getExtendee().getReferencingName());			
		}
	}
	
	private void writeBecause(StatementBuilder builder,
			BecauseModel model) {
		if (model.getRationales().size() > 0) {
			builder.keyword("because");
			writeCommaSeparatedReferencesList(builder, model.getRationales());
		}		
	}
	
	private void startElement(StatementBuilder builder,
			ElementModel model) {
		builder.openStatement(model).identifier(model.getName());
	}
	
	private void startFirstClass(StatementBuilder builder,
			FirstClassModel model) {
		writeAnnotations(builder, model);
		startElement(builder, model);
	}
	
	private void writeBody(StatementBuilder builder,
			FirstClassModel model) {
		builder.startBody();
		for (FirstClassModel child: model.getBody()) {
			writeChild(builder, child);
		}
		builder.endBody();
	}
	
	private void writeEqualsExpression(StatementBuilder builder,
			ExpressionModel expression) {
		if (expression != null 
				&& expression.getValue() != null) {
			builder.equals().expression(expression);			
		}
	}
	
	private void writeTemplate(StatementBuilder builder
			, TemplateModel model) {
		startFirstClass(builder, model);
		writeParameters(builder, model);
		writeExtendee(builder, model);
		writeBecause(builder, model);
		writeBody(builder, model);
	}

	private void writeQualityAttribute(StatementBuilder builder,
			QualityAttributeModel model) {
		startFirstClass(builder, model);		
		if (model.getBody().size() > 0) {
			builder.startBody();
			for (ElementModel child: model.getBody()) {
				writeProperty(builder, (PropertyModel) child);
			}
			builder.endBody();
		} else {
			builder.closeStatement(model);
		}
	}
	
	private void writeProperty(StatementBuilder builder,
			PropertyModel model) {
		startFirstClass(builder, model);
		if (model.getExpression() != null) {
			builder.equals().expression(model.getExpression());
		}		
		builder.closeStatement(model);
	}

	private void writeRequirement(StatementBuilder builder, 
			RequirementModel model) {
		startFirstClass(builder, model);
		writeEqualsExpression(builder, model.getExpression());
		builder.closeStatement(model);
	}	
	
	private void writeAnnotation(StatementBuilder builder, AnnotationModel annotation) {
		builder
			.openStatement(annotation);
		if (annotation.getName() != null) {
			builder.identifier(annotation.getName());
		}
		builder.openBracket();
		
		boolean firstChild = true;
		for (NamedValueModel nv: annotation.getNamedValues()) {
			if (!firstChild) {
				builder.comma();
			} else {
				firstChild = false;
			}
			
			builder
				.identifier(nv.getName())
				.equals()
				.string(nv.getValue().toString());
		}
		
		
		builder.closeBracket();
		builder.closeStatement(annotation);
	}
}