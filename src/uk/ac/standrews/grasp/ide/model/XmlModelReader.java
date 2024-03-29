package uk.ac.standrews.grasp.ide.model;

import grasp.lang.ISerializable.XmlSchema;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.AssertionFailedException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import uk.ac.standrews.grasp.ide.Log;
import uk.ac.standrews.grasp.ide.editors.TextUtil;
import uk.ac.standrews.grasp.ide.model.expressions.ExpressionParser;

/**
 * Reads Grasp architecture Graph from XML
 * @author Dilyan Rusev
 *
 */
public class XmlModelReader {
	private static final XmlModelReader INSTANCE = new XmlModelReader();
	
	private DocumentBuilderFactory documentFactory;
	private Map<String, ElementType> elementNameToType;
	private List<Runnable> documentLoadedTasks;
	
	/**
	 * Returns the singleton instance of this reader
	 * @return Singleton
	 */
	public static XmlModelReader getDefault() {
		return INSTANCE;
	}
	
	private XmlModelReader() {
		documentFactory = DocumentBuilderFactory.newInstance();
		elementNameToType = new HashMap<String, ElementType>(ElementType.values().length);
		documentLoadedTasks = new ArrayList<Runnable>();
		for (ElementType type: ElementType.values()) {
			elementNameToType.put(type.name().toUpperCase(), type);
		}		
	}
	
	/**
	 * Read an XML file and produce Grasp graph from it
	 * @param input XML file to parse
	 * @return Grasp architecture graph or null on failure
	 */
	public ArchitectureModel readFromFile(IFile input) {
		Document doc;
		try {
			DocumentBuilder builder = documentFactory.newDocumentBuilder();
			doc = builder.parse(input.getContents());
		} catch (ParserConfigurationException e) {
			Log.error(e);
			return null;
		} catch (SAXException e) {
			Log.error(e);
			return null;
		} catch (IOException e) {
			Log.error(e);
			return null;
		} catch (CoreException e) {
			Log.error(e);
			return null;
		}
		
		documentLoadedTasks.clear();
		IFile graspFile = getGraspFile(input);
		ArchitectureModel arch = readArchitecture(doc, graspFile);
		if (arch != null) {
			try {
				for (Runnable task: documentLoadedTasks) {
					task.run();
				}
				fixTemplates(arch);
				
			} catch (AssertionFailedException e) {
				Log.error(e);
				return null;
			}
		}
		
		return arch;
	}
	
	private void fixTemplates(ArchitectureModel arch) {
		for (FirstClassModel fc: arch.getBodyByType(ElementType.TEMPLATE)) {
			TemplateModel template = (TemplateModel) fc;
			fixTemplate(arch, template);
		}
	}
	
	private void fixTemplate(ArchitectureModel arch, TemplateModel template) {
		InstantiableModel instantiation = findTemplateInstantiation(arch, template);
		if (instantiation != null) {
			for (FirstClassModel child: instantiation.getBody()) {
				if (!template.getBody().contains(child)) {
					FirstClassModel copy = FirstClassModel.createCopyOf(child, template);					
					if (copy instanceof InterfaceModel) {
						InterfaceModel iface = (InterfaceModel) copy;
						iface.getConnections().clear();
					}
					template.addChild(copy);
				}
			}
		}
	}
	
	private InstantiableModel findTemplateInstantiation(ArchitectureModel arch, TemplateModel template) {
		Collection<FirstClassModel> res = arch.getBodyByType(ElementType.SYSTEM);
		if (!res.isEmpty()) {
			SystemModel sys = (SystemModel) res.iterator().next();
			for (FirstClassModel child: sys.getBody()) {
				if (child instanceof InstantiableModel
						&& ((InstantiableModel) child).getBase() == template) {
					return (InstantiableModel) child;
				}
			}
			for (FirstClassModel layer: sys.getBodyByType(ElementType.LAYER)) {
				for (FirstClassModel child: layer.getBody()) {
					if (child instanceof InstantiableModel
							&& ((InstantiableModel) child).getBase() == template) {
						return (InstantiableModel) child;
					}
				}
			}
		}
		return null;
	}

	/**
	 * Obtain a handle to the file containig the grasp source code from a handle to the file that contains the XML
	 * @param xmlFile Handle to the XML file
	 * @return Handle to the file containing the source code
	 */
	private IFile getGraspFile(IFile xmlFile) {
		IPath xmlPath = xmlFile.getProjectRelativePath();
		IPath graspPath = xmlPath.removeFileExtension();
		return xmlFile.getProject().getFile(graspPath);
	}

	/**
	 * Read an architecture element from an XML document
	 * @param doc XML DOM document
	 * @param file Handle to the file containing the Grasp source code, not the XML
	 * @return Instance of the Grasp architecture graph, or null on failure
	 */
	private ArchitectureModel readArchitecture(Document doc, IFile file) {
		Node archNode = doc.getFirstChild();
		if (!(archNode instanceof Element)) {
			return null;
		}
		ArchitectureModel arch = new ArchitectureModel(file);
		if (!readFirstClass((Element) archNode, arch, null)) {
			return null;
		}
		
		return arch;
	}
	
	/**
	 * Read FirstClassModel from an XML DOM element
	 * @param elem XML DOM element to read from
	 * @param model Instance to fill-in with information
	 * @param parent Parent Grasp element
	 * @return false on any error
	 */
	private boolean readFirstClass(Element elem, FirstClassModel model, FirstClassModel parent) {		
		if (!readElement(elem, model)) {
			return false;
		}
		
		NodeList childNodes = elem.getChildNodes();
		for (int i = 0, len = childNodes.getLength(); i < len; i++) {
			Node childNode = childNodes.item(i);
			if (childNode.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}			
			if (XmlSchema.ANNOTATIONS.tag().equalsIgnoreCase(childNode.getNodeName())) {
				readAnnotations((Element) childNode, model);
			} else if (XmlSchema.BODY.tag().equalsIgnoreCase(childNode.getNodeName())) {
				readBody((Element) childNode, model);
			}
		}
		
		return true;
	}
	
	/**
	 * Read the children of a FirstClassModel
	 * @param body XML DOM element to read from
	 * @param parent FirstClassModel whose body should be read
	 */
	private void readBody(Element body, FirstClassModel parent) {
		NodeList childNodes = body.getChildNodes();
		for (int i = 0, len = childNodes.getLength(); i < len; i++) {
			Node node = childNodes.item(i);
			if (node.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}
			ElementType type = elementNameToType.get(node.getNodeName().toUpperCase());
			if (type == null) {
				continue;
			}
			FirstClassModel childModel = readElementByType((Element) node, type, parent);
			if (childModel != null) {
				parent.addChild(childModel);
			}
		}
	}
	
	/**
	 * Polymorphic read of an element
	 * @param node DOM XML element to read from
	 * @param type Type of the element to be read
	 * @param parent Parent Grasp Element
	 * @return Instance of the Grasp element that was read, or null on error
	 */
	private FirstClassModel readElementByType(Element node, ElementType type, FirstClassModel parent) {
		switch (type) {
		case ANNOTATION:
			return null; // handled by readFirstClass
		case ARCHITECTURE:
			return null; // read only once
		case CHECK:
		{
			CheckModel check = new CheckModel(parent);
			return readEvaluable(node, parent, check) ? check : null;
		}
		case COMPONENT:
		{
			ComponentModel comp = new ComponentModel(parent);
			return readInstantiable(node, parent, comp) ? comp : null;
		}
		case CONNECTOR:
		{
			ConnectorModel conn = new ConnectorModel(parent);
			return readInstantiable(node, parent, conn) ? conn : null;
		}
		case EXPRESSION:
			return null; // handled by evaluable-s
		case LAYER:
			return readLayer(node, parent);
		case LINK:
			return readLink(node, parent);
		case NAMEDVALUE:
			return null; // handled by annotations
		case PROPERTY:
		{
			PropertyModel prop = new PropertyModel(parent);
			return readEvaluable(node, parent, prop) ? prop : null;
		}
		case PROVIDES:
		{
			ProvidesModel provides = new ProvidesModel(parent);
			return readInterface(node, parent, provides) ? provides : null;
		}
		case QUALITY_ATTRIBUTE:
			return readQualityAttribute(node, parent);
		case RATIONALE:
			return readRationale(node, parent);
		case REASON:
			return readReason(node, parent);
		case REQUIREMENT:
			return readRequirement(node, parent);
		case REQUIRES:
		{
			RequiresModel requires = new RequiresModel(parent);
			return readInterface(node, parent, requires) ? requires : null;
		}
		case SYSTEM:
		{
			SystemModel model = new SystemModel((ArchitectureModel) parent);
			return readBecause(node, parent, model) ? model : null;
		}
		case TEMPLATE:
			return readTemplate(node, parent);
		default:
			Assert.isTrue(false, "Unknown ElmentType: " + type);
			return null;
		}
	}
	
	/**
	 * Read a template from an XML DOM element
	 * @param node XML DOM element
	 * @param parent Parent Grasp element
	 * @return Template that was read or null on error
	 */
	private TemplateModel readTemplate(Element node, FirstClassModel parent) {
		TemplateModel model = new TemplateModel(parent);
		if (!readParameterised(node, parent, model)) {
			return null;
		}
		return model;
	}
	
	/**
	 * Read a Grasp requirement element from XML
	 * @param node XML DOM element to read from
	 * @param parent Grasp parent element
	 * @return Instance or null on error
	 */
	private RequirementModel readRequirement(Element node, FirstClassModel parent) {
		RequirementModel model = new RequirementModel(parent);
		if (!readFirstClass(node, model, parent)) {
			return null;
		}
		
		Element expressionTag = findChildByName(node, ElementType.EXPRESSION.name());
		if (expressionTag != null) {
			ExpressionModel expression = readExpression(expressionTag, model);
			if (expression != null) {
				model.setExpression(expression);
			}
		}
		
		return model;
	}
	
	/**
	 * Read Grasp reason element from XML
	 * @param node XML DOM node to read from
	 * @param parent Parent Grasp element
	 * @return Instance or null on error
	 */
	private ReasonModel readReason(Element node, FirstClassModel parent) {
		ReasonModel model = new ReasonModel(parent);
		if (!readFirstClass(node, model, parent)) {
			return null;
		}
		Element expressionTag = findChildByName(node, ElementType.EXPRESSION.name());
		if (expressionTag != null) {
			ExpressionModel expression = readExpression(expressionTag, model);
			if (expression != null) {
				model.setExpression(expression);
			}
		}
		List<String> supportsQualifiedNames = new ArrayList<String>();
		List<String> inhibitsQualifiedNames = new ArrayList<String>();
		
		Element supportsTag = findChildByName(node, XmlSchema2.SUPPORTS.tag());
		if (supportsTag != null) {
			NodeList supportsChildren = supportsTag.getChildNodes();
			for (int i = 0, len = supportsChildren.getLength(); i < len; i++) {
				Node current = supportsChildren.item(i);
				if (current.getNodeType() == Node.ELEMENT_NODE &&
						current.getNodeName().equalsIgnoreCase(XmlSchema2.ELEMENT.tag())) {
					Element elemTag = (Element) current;
					String ref = elemTag.getAttribute(XmlSchema.AT_REFERENCE.tag());
					if (ref != null) {
						supportsQualifiedNames.add(ref);
					}
				}
			}
		}
		Element inhibitsTag = findChildByName(node, XmlSchema2.INHIBITS.tag());
		if (inhibitsTag != null) {
			NodeList inhibitsChildren = inhibitsTag.getChildNodes();
			for (int i = 0, len = inhibitsChildren.getLength(); i < len; i++) {
				Node current = inhibitsChildren.item(i);
				if (current.getNodeType() == Node.ELEMENT_NODE &&
						current.getNodeName().equalsIgnoreCase(XmlSchema2.ELEMENT.tag())) {
					Element elemTag = (Element) current;
					String ref = elemTag.getAttribute(XmlSchema.AT_REFERENCE.tag());
					if (ref != null) {
						inhibitsQualifiedNames.add(ref);
					}
				}
			}
		}
		
		if (inhibitsQualifiedNames.size() > 0 || supportsQualifiedNames.size() > 0) {
			documentLoadedTasks.add(
					new AddReasonSupportsInhibitsReferencesTask(model,
							supportsQualifiedNames, inhibitsQualifiedNames));
		}
		
		return model;
	}
	
	private RationaleModel readRationale(Element node, FirstClassModel parent) {
		RationaleModel model = new RationaleModel(parent);
		if (!readParameterised(node, parent, model)) {
			return null;
		}
		
		Element reasonsTag = findChildByName(node, XmlSchema2.REASONS.tag());
		if (reasonsTag != null) {
			NodeList reasonsTagChildren = reasonsTag.getChildNodes();
			List<String> reasonQualifiedNames = new ArrayList<String>();
			for (int i = 0, len = reasonsTagChildren.getLength(); i < len; i++) {
				Node current = reasonsTagChildren.item(i);
				if (current.getNodeType() == Node.ELEMENT_NODE &&
						current.getNodeName().equalsIgnoreCase(XmlSchema2.REASON.tag())) {
					Element reasonTag = (Element) current;
					String reasonQualifiedName = reasonTag.getAttribute(XmlSchema.AT_REFERENCE.tag());
					if (reasonQualifiedName != null) {
						reasonQualifiedNames.add(reasonQualifiedName);
					}
				}
			}
			documentLoadedTasks.add(new AddRationaleReasonReferencesTask(model, reasonQualifiedNames));
		}
		
		return model;
	}
	
	private boolean readParameterised(Element node, FirstClassModel parent, ParameterizedModel model) {
		if (!readExtensible(node, parent, model)) {
			return false;
		}
		Element parametersTag = findChildByName(node, XmlSchema.PARAMETERS.tag());
		List<ParameterReferenceInfo> parameters = new ArrayList<XmlModelReader.ParameterReferenceInfo>();
		if (parametersTag != null) {
			NodeList paramTags = parametersTag.getChildNodes();
			for (int i = 0, len = paramTags.getLength(); i < len; i++) {
				Node current = paramTags.item(i);
				if (current.getNodeType() == Node.ELEMENT_NODE
						&& current.getNodeName().equalsIgnoreCase(XmlSchema.PARAM.tag())) {
					Element paramTag = (Element) current;
					Integer ordinal = parseIntAttribute(paramTag, XmlSchema.AT_ORDINAL.tag());
					String paramName = paramTag.getAttribute(XmlSchema.AT_NAME.tag());
					if (ordinal != null && paramName != null) {
						parameters.add(new ParameterReferenceInfo(ordinal, paramName));
					}
				}
			}
		}
		if (parameters.size() > 0) {
			Collections.sort(parameters, ParameterReferenceInfo.ORDINAL_COMPARATOR);
			for (ParameterReferenceInfo paramInfo: parameters) {
				model.getParameters().add(paramInfo.getQualifiedName());
			}
		}
		return true;
	}
	
	private boolean readExtensible(Element node, FirstClassModel parent, ExtensibleModel model) {
		if (!readBecause(node, parent, model)) {
			return false;
		}
		
		Element extendsTag = findChildByName(node, XmlSchema.EXTENDS.tag());
		if (extendsTag != null) {
			String extendeeQualifiedName = extendsTag.getAttribute(XmlSchema.AT_REFERENCE.tag());
			if (extendeeQualifiedName != null) {
				documentLoadedTasks.add(new SetExtendeeReferenceTask(model, extendeeQualifiedName));
			}
		}
		
		return true;
	}
	
	private QualityAttributeModel readQualityAttribute(Element node,
			FirstClassModel parent) {
		QualityAttributeModel qualityAttribute = new QualityAttributeModel(parent);
		if (!readFirstClass(node, qualityAttribute, parent)) {
			return null;
		}
		
		Element supportsTag = findChildByName(node, XmlSchema2.SUPPORTS.tag());
		List<String> supporteeQualifiedNames = new ArrayList<String>();
		if (supportsTag != null) {
			NodeList supporteeNodes = supportsTag.getChildNodes();
			for (int i = 0, len = supporteeNodes.getLength(); i < len; i++) {
				Node current = supporteeNodes.item(i);
				if (current.getNodeType() == Node.ELEMENT_NODE 
						&& current.getNodeName().equalsIgnoreCase(XmlSchema2.ELEMENT.tag())) {
					Element supporteeTag = (Element) current;
					String elemRef = supporteeTag.getAttribute(XmlSchema.AT_REFERENCE.tag());
					if (elemRef != null) {
						supporteeQualifiedNames.add(elemRef);
					}
				}
			}
		}
		if (supporteeQualifiedNames.size() > 0) {
			documentLoadedTasks.add(new AddQualityAttributeSupportsReferencesTask(qualityAttribute, supporteeQualifiedNames));
		}
		
		return qualityAttribute;
	}

	private boolean readInterface(Element node, FirstClassModel parent, InterfaceModel model) {
		if (!readBecause(node, parent, model)) {
			return false;
		}
		Integer maxDeg = parseIntAttribute(node, XmlSchema.AT_MAXDEG.tag());
		if (maxDeg == null) {
			return false;
		}
		model.setMaxdeg(maxDeg);
		Element connectionsTag = findChildByName(node, XmlSchema.CONNECTIONS.tag());
		if (connectionsTag == null) {
			return false;
		}
		NodeList linkRefTags = connectionsTag.getChildNodes();
		List<String> linkQualifiedNames = new ArrayList<String>();
		for (int i = 0, len = linkRefTags.getLength(); i < len; i++) {
			Node current = linkRefTags.item(i);
			if (current.getNodeType() == Node.ELEMENT_NODE 
					&& current.getNodeName().equalsIgnoreCase(XmlSchema.LINK_REF.tag())) {
				Element linkRef = (Element) current;
				String linkQualifiedName = linkRef.getAttribute(XmlSchema.AT_REFERENCE.tag());
				linkQualifiedNames.add(linkQualifiedName);
			}
		}
		if (linkQualifiedNames.size() > 0) {
			documentLoadedTasks.add(new AddInterfaceConnectionsTask(model, linkQualifiedNames));
		}
		
		return true;
	}
	
	private LinkModel readLink(Element node, FirstClassModel parent) {
		LinkModel model = new LinkModel(parent);
		if (!readBecause(node, parent, model)) {
			return null;
		}
		Element providerTag = findChildByName(node, XmlSchema.PROVIDER.tag());
		Element consumerTag = findChildByName(node, XmlSchema.CONSUMER.tag());
		if (providerTag == null || consumerTag == null) {
			return null;
		}
		
		String providerQualifiedName = providerTag.getAttribute(XmlSchema.AT_REFERENCE.tag());
		String consumerQualifiedName = consumerTag.getAttribute(XmlSchema.AT_REFERENCE.tag());
		if (providerQualifiedName == null || consumerQualifiedName == null) {
			return null;
		}
		
		documentLoadedTasks.add(new SetLinkProviderAndConsumerReferencesTask(model, providerQualifiedName, consumerQualifiedName));
		
		return model;
	}

	private LayerModel readLayer(Element elem, FirstClassModel parent) {
		LayerModel model = new LayerModel(parent);
		if (!readBecause(elem, parent, model)) {
			return null;
		}
		Element overTag = findChildByName(elem, XmlSchema.OVER.tag());
		List<String> layerQualifiedNames = new ArrayList<String>();
		if (overTag != null) {
			NodeList layerRefTags = overTag.getChildNodes();
			for (int i = 0, len = layerRefTags.getLength(); i < len; i++) {
				Node current = layerRefTags.item(i);
				if (current.getNodeType() != Node.ELEMENT_NODE 
						|| !current.getNodeName().equalsIgnoreCase(XmlSchema.LAYER_REF.tag())) {
					continue;
				}
				String qualifiedName = ((Element) current).getAttribute(XmlSchema.AT_REFERENCE.tag());
				if (qualifiedName != null) {
					layerQualifiedNames.add(qualifiedName);
				}
			}
		}
		if (layerQualifiedNames.size() > 0) {
			documentLoadedTasks.add(new AddLayerOverReferencesTask(model, layerQualifiedNames));
		}
		return model;
	}
	
	private boolean readInstantiable(Element elem, FirstClassModel parent, InstantiableModel model) {
		if (!readBecause(elem, parent, model)) {
			return false;
		}
		Element baseTag = findChildByName(elem, XmlSchema.BASE.tag());
		if (baseTag == null) {
			return false;
		}
		String templateQualifiedName = baseTag.getAttribute(XmlSchema.AT_REFERENCE.tag());
		if (templateQualifiedName != null) {
			documentLoadedTasks.add(new SetBaseTemplateReferenceTask(model, templateQualifiedName));
		} else {
			return false;
		}
		Element argumentsTag = findChildByName(baseTag, XmlSchema.ARGUMENTS.tag());
		if (argumentsTag != null) {
			NodeList argumentTags = argumentsTag.getChildNodes();
			List<ParameterReferenceInfo> parameters = new ArrayList<XmlModelReader.ParameterReferenceInfo>();
			for (int i = 0, argumentTagsLen = argumentTags.getLength(); i < argumentTagsLen; i++) {
				Node argumentTag = argumentTags.item(i);
				if (argumentTag.getNodeType() != Node.ELEMENT_NODE 
						|| !argumentTag.getNodeName().equalsIgnoreCase(XmlSchema.ARUGMENT.tag())) {
					continue;
				}
				Element argumentElem = (Element) argumentTag;
				String argumentQualifiedName = argumentElem.getAttribute(XmlSchema.AT_REFERENCE.tag());
				if (argumentQualifiedName == null) {
					continue;
				}
				Integer ordinalTest = parseIntAttribute(argumentElem, XmlSchema.AT_ORDINAL.tag());
				if (ordinalTest == null) {
					continue;
				}
				int ordinal = ordinalTest;
				ordinal--; // saved ordinals start from 1
				if (ordinal >= 0) {
					parameters.add(new ParameterReferenceInfo(ordinal, argumentQualifiedName));
				}
			}
			documentLoadedTasks.add(new SetInstantiableArgumentReferencesTask(model, parameters));
		}	
		
		return true;
	}
	
	private boolean readEvaluable(Element elem, FirstClassModel parent, EvaluableModel model) {
		if (!readBecause(elem, parent, model)) {
			return false;
		}
		Element expressionNode = findChildByName(elem, ElementType.EXPRESSION.name());
		if (expressionNode != null) {
			ExpressionModel expr = readExpression(expressionNode, model);
			model.setExpression(expr);
		} 
		
		return true;
	}
	
	private ExpressionModel readExpression(Element elem, ElementModel parent) {
		String val = elem.getAttribute(XmlSchema.AT_VALUE.tag());
		String text = elem.getAttribute(XmlSchema.AT_TEXT.tag());
		String type = elem.getAttribute(XmlSchema.AT_TYPE.tag());
		
		return ExpressionParser.construct(val, text, type, parent);		
	}
	
	private boolean readBecause(Element elem, FirstClassModel parent, BecauseModel model) {
		if (!readFirstClass(elem, model, parent)) {
			return false;
		}
		Element becauseNode = findChildByName(elem, XmlSchema.BECAUSE.tag());
		if (becauseNode != null) {
			NodeList rationaleNodes = becauseNode.getChildNodes();
			for (int i = 0, len = rationaleNodes.getLength(); i < len; i++) {
				readRationaleReference(rationaleNodes.item(i), model);
			}
		}
		return true;
	}
	
	private void readRationaleReference(Node node, BecauseModel parent) {
		if (node.getNodeType() != Node.ELEMENT_NODE
				|| !node.getNodeName().equalsIgnoreCase(XmlSchema.RATIONALE_REF.tag())) {
			return;
		}
		String qualifiedName = ((Element) node).getAttribute(XmlSchema.AT_REFERENCE.tag());
		documentLoadedTasks.add(new AddRationaleReferenceTask(parent, qualifiedName));
	}

	private void readAnnotations(Element annotationsElem, FirstClassModel model) {
		NodeList annotationNodes = annotationsElem.getChildNodes();
		for (int i = 0, len = annotationNodes.getLength(); i < len; i++) {
			Node node = annotationNodes.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element annotationElem = (Element) node;
				AnnotationModel annotation = readAnnotation(annotationElem, model);
				if (annotation != null) {
					model.getAnnotations().add(annotation);
				}
			}
		}
	}
	
	private AnnotationModel readAnnotation(Element annotationElem, FirstClassModel parent) {
		if (!annotationElem.getTagName().equalsIgnoreCase(ElementType.ANNOTATION.name())) {
			return null;
		}
		AnnotationModel annotation = new AnnotationModel(parent);
		String handler = annotationElem.getAttribute(XmlSchema.AT_HANDLER.tag());		
		annotation.setName(handler);
		NodeList namedValueNodes = annotationElem.getChildNodes();
		for (int i = 0, len = namedValueNodes.getLength(); i < len; i++) {
			Node child = namedValueNodes.item(i);
			if (child.getNodeType() == Node.ELEMENT_NODE 
					&& child.getNodeName().equalsIgnoreCase(ElementType.NAMEDVALUE.name())) {
				NamedValueModel nv = readNamedValue((Element) child, annotation);
				if (nv != null) {
					annotation.getNamedValues().add(nv);
				}
			}
		}
		return annotation;
	}
	
	private NamedValueModel readNamedValue(Element elem, AnnotationModel parent) {
		String name = elem.getAttribute(XmlSchema.AT_NAME.tag());
		if (!TextUtil.isIdentifier(name)) {
			return null;
		}
		
		NamedValueModel nv = new NamedValueModel(parent);
		nv.setName(name);
		ExpressionModel expression = readExpression(elem, nv);
		nv.setExpression(expression);
		return nv;
	}

	private boolean readElement(Element elem, ElementModel model) {
		if (elem == null || model == null || !elem.getNodeName().equalsIgnoreCase(model.getType().name())) {
			return false;
		}
		String name = elem.getAttribute(XmlSchema.AT_NAME.tag());
		String refName = elem.getAttribute(XmlSchema.AT_RNAME.tag());
		
		// don't check for identifier validity - the XML is supposed to be written by the compiler
		// after successful compilation, so we just assume name/refname are valid
		
		if (name == null) {
			model.setName(refName);
		} else {
			model.setName(name);
			if (!refName.equals(name)) {
				model.setAlias(refName);
			}
		}	
		
		return true;
	}
	
	private Element findChildByName(Element parent, String name) {
		NodeList children = parent.getChildNodes();
		for (int i = 0, len = children.getLength(); i < len; i++) {
			Node current = children.item(i);
			if (current.getNodeType() == Node.ELEMENT_NODE 
					&& current.getNodeName().equalsIgnoreCase(name)) {
				return (Element) current;
			}
		}
		return null;
	}
	
	private Integer parseIntAttribute(Element node, String name) {
		String txt = node.getAttribute(name);
		if (txt != null) {
			txt = txt.trim();
			try {
				if (txt.startsWith("0x") && txt.length() > 2) {
					String hex = txt.substring(2);
					return Integer.parseInt(hex, 16);
				} else {
					return Integer.parseInt(txt);
				}
			} catch (NumberFormatException e) {
				//Log.error(e);
			}
		}
		return null;
	}
	
	
	private static abstract class DocumentLoadedTask<TParent extends FirstClassModel> implements Runnable {
		protected TParent parent;
		
		public DocumentLoadedTask(TParent parent) {
			Assert.isNotNull(parent, "Parent element in document laoded tasks must not be null");
			this.parent = parent;
		}
		
		@SuppressWarnings("unchecked")
		protected <T extends ElementModel> T getElementByQualifiedName(Class<T> resType, String qualifiedName) {
			Assert.isNotNull(resType);
			Assert.isNotNull(qualifiedName);
			
			ArchitectureModel arch = parent.getArchitecture();
			Assert.isNotNull(arch, "Cannot get architecture from parent elment");
			
			ElementModel found = arch.findByQualifiedName(qualifiedName);
			if (resType.isInstance(found)) {
				return (T) found;
			} else {
				Assert.isTrue(false, "Cannot find " + qualifiedName + " in " + arch);
				return null;
			}
		}
	}
	
	private static class AddRationaleReferenceTask extends DocumentLoadedTask<BecauseModel> {
		private String rationaleQualifiedName;
		
		public AddRationaleReferenceTask(BecauseModel parent, String rationaleQualifiedName) {
			super(parent);
			this.rationaleQualifiedName = rationaleQualifiedName;
		}

		@Override
		public void run() {
			RationaleModel reference = getElementByQualifiedName(RationaleModel.class, rationaleQualifiedName);
			parent.getRationales().add(reference);
		}		
	}
	
	private static class SetBaseTemplateReferenceTask extends DocumentLoadedTask<InstantiableModel> {
		private String templateQualifiedName;
		
		public SetBaseTemplateReferenceTask(InstantiableModel parent, String templateQualifiedName) {
			super(parent);
			this.templateQualifiedName = templateQualifiedName;
		}
		
		@Override
		public void run() {
			TemplateModel base = getElementByQualifiedName(TemplateModel.class, templateQualifiedName);
			parent.setBase(base);
		}
	}
	
	private static class ParameterReferenceInfo  {
		public static final Comparator<ParameterReferenceInfo> ORDINAL_COMPARATOR = 
				new OrdinalComparator();
		private int ordinal;
		private String qualifiedName;
		
		public ParameterReferenceInfo(int ordinal, String qualifiedName) {
			this.ordinal = ordinal;
			this.qualifiedName = qualifiedName;
		}
		
		public int getOrdinal() {
			return ordinal;
		}
		
		public String getQualifiedName() {
			return qualifiedName;
		}

		private static class OrdinalComparator implements Comparator<ParameterReferenceInfo> {
			@Override
			public int compare(ParameterReferenceInfo o1,
					ParameterReferenceInfo o2) {
				return o1.getOrdinal() - o2.getOrdinal();
			}			
		}
	}
	
	private static class SetInstantiableArgumentReferencesTask extends DocumentLoadedTask<InstantiableModel> {
		private List<ParameterReferenceInfo> parameters;
		
		public SetInstantiableArgumentReferencesTask(InstantiableModel parent, 
				List<ParameterReferenceInfo> parameters) {
			super(parent);
			this.parameters = parameters;
		}
		
		@Override
		public void run() {			
			Collections.sort(parameters, ParameterReferenceInfo.ORDINAL_COMPARATOR);
			for (ParameterReferenceInfo param: parameters) {
				FirstClassModel argument = getElementByQualifiedName(FirstClassModel.class, param.getQualifiedName());
				parent.getArguments().add(argument);					
			}			
		}		
	}
	
	private static class AddLayerOverReferencesTask extends DocumentLoadedTask<LayerModel> {
		private List<String> layerQualifiedNames;
		
		public AddLayerOverReferencesTask(LayerModel parent, List<String> layerQualifiedNames) {
			super(parent);
			this.layerQualifiedNames = layerQualifiedNames;
		}
		
		@Override
		public void run() {			
			for (String qualifiedName: layerQualifiedNames) {
				LayerModel overRef = getElementByQualifiedName(LayerModel.class, qualifiedName);
				parent.getOver().add(overRef);
			}
		}
	}
	
	private static class SetLinkProviderAndConsumerReferencesTask extends DocumentLoadedTask<LinkModel> {
		private String providerQualifiedName;
		private String consumerQualifiedName;
		
		public SetLinkProviderAndConsumerReferencesTask(LinkModel parent,
				String providerQualifiedName, String consumerQualifiedName) {
			super(parent);
			this.providerQualifiedName = providerQualifiedName;
			this.consumerQualifiedName = consumerQualifiedName;
		}		
		
		@Override
		public void run() {
			ProvidesModel providerRef = getElementByQualifiedName(ProvidesModel.class, providerQualifiedName);
			RequiresModel consumerRef = getElementByQualifiedName(RequiresModel.class, consumerQualifiedName);
			parent.setProvider(providerRef);
			parent.setConsumer(consumerRef);			
		}
	}
	
	private static class AddInterfaceConnectionsTask extends DocumentLoadedTask<InterfaceModel> {
		private List<String> linkQualifiedNames;
		
		public AddInterfaceConnectionsTask(InterfaceModel parent, List<String> linkQualifiedNames) {
			super(parent);
			this.linkQualifiedNames = linkQualifiedNames;
		}
		
		@Override
		public void run() {
			for (String qualifiedName: linkQualifiedNames) {
				LinkModel link = getElementByQualifiedName(LinkModel.class, qualifiedName);
				parent.getConnections().add(link);
			}
		}
	}
	
	private static class AddQualityAttributeSupportsReferencesTask extends DocumentLoadedTask<QualityAttributeModel> {
		private List<String> supporteeQualifiedNames;
		
		public AddQualityAttributeSupportsReferencesTask(QualityAttributeModel parent, List<String> linkQualifiedNames) {
			super(parent);
			this.supporteeQualifiedNames = linkQualifiedNames;
		}
		
		@Override
		public void run() {
			for (String qualifiedName: supporteeQualifiedNames) {
				FirstClassModel supportee = getElementByQualifiedName(FirstClassModel.class, qualifiedName);
				parent.getSupports().add(supportee);
			}
		}
	}
	
	private static class SetExtendeeReferenceTask extends DocumentLoadedTask<ExtensibleModel> {
		private String extendeeQualifiedName;
		
		public SetExtendeeReferenceTask(ExtensibleModel parent, String extendeeeQualifiedName) {
			super(parent);
			this.extendeeQualifiedName = extendeeeQualifiedName;
		}
		
		@Override
		public void run() {
			FirstClassModel extendee = getElementByQualifiedName(FirstClassModel.class, extendeeQualifiedName);
			parent.setExtendee(extendee);
		}
	}
	
	private static class AddRationaleReasonReferencesTask extends DocumentLoadedTask<RationaleModel> {
		private List<String> reasonQualifiedNames;
		
		public AddRationaleReasonReferencesTask(RationaleModel parent, List<String> reasonQualifiedNames) {
			super(parent);
			this.reasonQualifiedNames = reasonQualifiedNames;
		}
		
		@Override
		public void run() {
			for (String qualifiedName: reasonQualifiedNames) {
				ReasonModel reason = getElementByQualifiedName(ReasonModel.class, qualifiedName);
				parent.getReasons().add(reason);
			}
		}
	}
	
	private static class AddReasonSupportsInhibitsReferencesTask extends DocumentLoadedTask<ReasonModel> {
		List<String> supportsQualifiedNames;
		List<String> inhibitsQualifiedNames;
		
		public AddReasonSupportsInhibitsReferencesTask(ReasonModel parent, 
				List<String> supportsQualifiedNames, List<String> inhibitsQualifiedNames) {
			super(parent);
			this.supportsQualifiedNames = supportsQualifiedNames;
			this.inhibitsQualifiedNames = inhibitsQualifiedNames;
		}
		
		@Override
		public void run() {
			for (String qualifiedName: supportsQualifiedNames) {
				ReasonModel supportee = getElementByQualifiedName(ReasonModel.class, qualifiedName);
				parent.getSupports().add(supportee);
			}
			for (String qualifiedName: inhibitsQualifiedNames) {
				ReasonModel inhibitee = getElementByQualifiedName(ReasonModel.class, qualifiedName);
				parent.getInhibits().add(inhibitee);
			}
		}
	}
}

enum XmlSchema2  {
	SUPPORTS("SUPPORTS"),
	ELEMENT("ELEMENT"), 
	REASONS("REASONS"), 
	REASON("REASON"), 
	INHIBITS("INHIBITS"),
	AT_COLUMN("col"),
	AT_LINE("line"),
	AT_START_POSITION("start"),
	AT_TOKEN_ID("id"),
	AT_TOKEN_TEXT("text"),
	CHILD_NODES("CHILDREN"), SYNTAX_NODE("NODE"),
	;
	
	private final String tagName;
	
	XmlSchema2(String tagName) {
		this.tagName = tagName;
	}
	
	public String tag() {
		return tagName;
	}
}
