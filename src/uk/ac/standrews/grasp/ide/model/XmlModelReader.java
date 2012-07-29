package uk.ac.standrews.grasp.ide.model;

import grasp.lang.IElement;
import grasp.lang.IElement.ElementType;
import grasp.lang.IElement.XmlSchema;
import grasp.lang.IFirstClass;
import grasp.lang.ILayer;
import grasp.lang.ILink;
import grasp.lang.IProvides;
import grasp.lang.IRationale;
import grasp.lang.IRequires;
import grasp.lang.ITemplate;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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
import org.eclipse.core.runtime.CoreException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import uk.ac.standrews.grasp.ide.Log;
import uk.ac.standrews.grasp.ide.editors.TextUtil;

public class XmlModelReader {
	private DocumentBuilderFactory documentFactory;
	private Map<String, ElementType> elementNameToType;
	private List<Runnable> documentLoadedTasks;
	
	public XmlModelReader() {
		documentFactory = DocumentBuilderFactory.newInstance();
		elementNameToType = new HashMap<String, ElementType>(ElementType.values().length);
		documentLoadedTasks = new ArrayList<Runnable>();
		for (ElementType type: ElementType.values()) {
			elementNameToType.put(type.name().toUpperCase(), type);
		}
	}
	
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
		ArchitectureModel arch = readArchitecture(doc, input);
		if (arch != null) {
			for (Runnable task: documentLoadedTasks) {
				task.run();
			}
		}
		
		return arch;
	}

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
			if (IElement.XmlSchema.ANNOTATIONS.tag().equalsIgnoreCase(childNode.getNodeName())) {
				readAnnotations((Element) childNode, model);
			} else if (IElement.XmlSchema.BODY.tag().equalsIgnoreCase(childNode.getNodeName())) {
				readBody((Element) childNode, model);
			}
		}
		
		return true;
	}
	
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
				parent.getBody().add(childModel);
			}
		}
	}
	
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
			break;
		case RATIONALE:
			break;
		case REASON:
			break;
		case REQUIREMENT:
			break;
		case REQUIRES:
		{
			RequiresModel requires = new RequiresModel(parent);
			return readInterface(node, parent, requires) ? requires : null;
		}
		case SYSTEM:
			break;
		case TEMPLATE:
			break;
		default:
			Assert.isTrue(false, "Unknown ElmentType: " + type);
			return null;
		}
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
		Element argumentsTag = findChildByName(elem, XmlSchema.ARGUMENTS.tag());
		if (argumentsTag == null) {
			return false;
		}
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
	
	private ExpressionModel readExpression(Element elem, FirstClassModel parent) {
		String val = elem.getAttribute(XmlSchema.AT_VALUE.tag());
		return ExpressionModel.createLiteral(parent, val);		
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
		String handler = annotationElem.getAttribute(IElement.XmlSchema.AT_HANDLER.tag());
		annotation.setHandler(handler);
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
		String name = elem.getAttribute(IElement.XmlSchema.AT_NAME.tag());
		if (!TextUtil.isIdentifier(name)) {
			return null;
		}
		String value = elem.getAttribute(IElement.XmlSchema.AT_VALUE.tag());
		NamedValueModel nv = new NamedValueModel(parent);
		nv.setName(name);
		nv.setExpression(ExpressionModel.createLiteral(nv, value));
		return nv;
	}

	private boolean readElement(Element elem, ElementModel model) {
		if (elem == null || model == null || !elem.getNodeName().equalsIgnoreCase(model.getType().name())) {
			return false;
		}
		String name = elem.getAttribute(IElement.XmlSchema.AT_NAME.tag());
		String refName = elem.getAttribute(IElement.XmlSchema.AT_RNAME.tag());
		
		if (!TextUtil.isIdentifier(name) || !TextUtil.isIdentifier(refName)) {
			return false;
		}
		
		model.setReferencingName(refName);
		model.setName(name);
		
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
				Log.error(e);
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
		protected <T extends IElement> T getElementByQualifiedName(Class<T> resType, String qualifiedName) {
			Assert.isNotNull(resType);
			Assert.isNotNull(qualifiedName);
			
			ArchitectureModel arch = parent.getArchitecture();
			Assert.isNotNull(arch, "Cannot get architecture from parent elment");
			
			IElement found = arch.findByQualifiedName(qualifiedName);
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
			IRationale reference = getElementByQualifiedName(IRationale.class, rationaleQualifiedName);
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
			ITemplate base = getElementByQualifiedName(ITemplate.class, templateQualifiedName);
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
				IFirstClass argument = getElementByQualifiedName(IFirstClass.class, param.getQualifiedName());
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
				ILayer overRef = getElementByQualifiedName(ILayer.class, qualifiedName);
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
			IProvides providerRef = getElementByQualifiedName(IProvides.class, providerQualifiedName);
			IRequires consumerRef = getElementByQualifiedName(IRequires.class, consumerQualifiedName);
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
				ILink link = getElementByQualifiedName(ILink.class, qualifiedName);
				parent.getConnections().add(link);
			}
		}
	}
}
