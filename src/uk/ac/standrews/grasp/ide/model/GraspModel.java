package uk.ac.standrews.grasp.ide.model;

import grasp.lang.IAnnotation;
import grasp.lang.IArchitecture;
import grasp.lang.ICheck;
import grasp.lang.IComponent;
import grasp.lang.IConnector;
import grasp.lang.IElement;
import grasp.lang.IElement.ElementType;
import grasp.lang.IExpression;
import grasp.lang.IFirstClass;
import grasp.lang.ILayer;
import grasp.lang.ILink;
import grasp.lang.INamedValue;
import grasp.lang.IProperty;
import grasp.lang.IProvides;
import grasp.lang.IQualityAttribute;
import grasp.lang.IRationale;
import grasp.lang.IReason;
import grasp.lang.IRequirement;
import grasp.lang.IRequires;
import grasp.lang.ISyntaxNode;
import grasp.lang.ISystem;
import grasp.lang.ITemplate;
import grasp.lang.IValidationContext;
import grasp.lang.Parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.content.IContentDescription;

import uk.ac.standrews.grasp.ide.GraspPlugin;
import uk.ac.standrews.grasp.ide.Log;

public final class GraspModel {
	public static final GraspModel INSTANCE = new GraspModel();
	
	private IResourceChangeListener resourceChangeListener;
	private Map<IFile, GraspFile> fileStats;
	private IResourceDeltaVisitor visitor;
	
	private GraspModel() { 
		fileStats = new HashMap<IFile, GraspFile>();
		resourceChangeListener = new IResourceChangeListener() {			
			@Override
			public void resourceChanged(IResourceChangeEvent event) {
				Assert.isTrue(event.getType() == IResourceChangeEvent.POST_CHANGE);
				try {
					event.getDelta().accept(visitor);
				} catch (CoreException e) {
					Log.error(e);
				}				
			}
		};
		visitor = new IResourceDeltaVisitor() {			
			@Override
			public boolean visit(IResourceDelta delta) throws CoreException {
				IResource res = delta.getResource();
				if (!(res instanceof IFile)) return true;				
				IFile file = (IFile)res;								
				
				switch (delta.getKind()) {
				case IResourceDelta.ADDED:
				case IResourceDelta.CHANGED:
					IContentDescription desc = file.getContentDescription();
					if (desc == null 
							|| !GraspPlugin.ID_GRASP_CONTENT_TYPE.equals(desc.getContentType().getId())) {
						return true;
					}
					
					GraspModel.INSTANCE.ensureFileStats(file).reparse();
					break;
				case IResourceDelta.REMOVED:
					GraspModel.INSTANCE.removeFileStats(file);
					break;
				}
				return true;
			}
		};
	}
	
	public GraspFile ensureFileStats(IFile file) {
		GraspFile stats = fileStats.get(file);
		if (stats == null) {
			stats = new GraspFile(file);
			fileStats.put(file, stats);
		}
		return stats;
	}
	
	private void removeFileStats(IFile file) {
		fileStats.remove(file);
	}	
	
	public void init() {
		ResourcesPlugin.getWorkspace().addResourceChangeListener(resourceChangeListener, IResourceChangeEvent.POST_CHANGE);
	}
	
	public void dispose() {
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(resourceChangeListener);
	}	
	
	public IElement makeObservable(IElement element, IElement parent) {
		Assert.isNotNull(element, "element cannot be null");
		if (ElementModel.class.isInstance(element)) {
			if (element.getParent() != null && !element.getParent().equals(parent)) {
				element.setParent(parent);
			}
			return element;
		}
		
		if (INamedValue.class.isInstance(element)) 
			return new NamedValueModel((INamedValue) element, parent);
		
		if (IAnnotation.class.isInstance(element)) 
			return new AnnotationModel((IAnnotation) element, parent);
		
		if (IExpression.class.isInstance(element)) 
			return new ExpressionModel((IExpression) element, parent);
		
		if (IRequirement.class.isInstance(element)) 
			return new RequirementModel((IRequirement) element, (IFirstClass) parent);
		
//		if (IArchitecture.class.isInstance(element)) 
//			return new ArchitectureModel((IArchitecture) element);
		
		if (IQualityAttribute.class.isInstance(element)) 
			return new QualityAttributeModel((IQualityAttribute) element, (IFirstClass) parent);
		
		if (IReason.class.isInstance(element)) 
			return new ReasonModel((IReason) element, (IFirstClass) parent);
		
		if (ILayer.class.isInstance(element))
			return new LayerModel((ILayer) element, (IFirstClass) parent);
		
		if (ILink.class.isInstance(element))
			return new LinkModel((ILink) element, (IFirstClass) parent);
		
		if (ISystem.class.isInstance(element))
			return new SystemModel((ISystem) element, (IFirstClass) parent);
		
		if (ICheck.class.isInstance(element)) 
			return new CheckModel((ICheck) element, (IFirstClass) parent);
		
		if (IProperty.class.isInstance(element))
			return new PropertyModel((IProperty) element, (IFirstClass) parent);
		
		if (IRationale.class.isInstance(element)) 
			return new RationaleModel((IRationale) element, (IFirstClass) parent);
		
		if (ITemplate.class.isInstance(element))
			return new TemplateModel((ITemplate) element, (IFirstClass) parent);
		
		if (IComponent.class.isInstance(element)) 
			return new ComponentModel((IComponent) element, (IFirstClass) parent);
		
		if (IConnector.class.isInstance(element)) 
			return new ConnectorModel((IConnector) element, (IFirstClass) parent);
		
		if (IProvides.class.isInstance(element)) 
			return new ProvidesModel((IProvides) element, (IFirstClass) parent);
		
		if (IRequires.class.isInstance(element))
			return new RequiresModel((IRequires) element, (IFirstClass) parent);
		
		throw new AssertionError("Unrecognized type: " + element.getClass());
	}
	
	public ElementModel createElementByType(ElementType type, FirstClassModel parent) {
		switch (type) {
		case ANNOTATION:
			return new AnnotationModel(parent);
		case ARCHITECTURE:
			Log.info("GraspModel.createElementByType: cannot create architecture");
			return null;
		case CHECK:
			return new CheckModel(parent);
		case COMPONENT:
			return new ComponentModel(parent);
		case CONNECTOR:
			return new ConnectorModel(parent);
		case EXPRESSION:
			return new ExpressionModel(parent);
		case LAYER:
			return new LayerModel(parent);
		case LINK:
			return new LinkModel(parent);
		case NAMEDVALUE:
			return new NamedValueModel(parent);
		case PROPERTY:
			return new PropertyModel(parent);
		case PROVIDES:
			return new ProvidesModel(parent);
		case QUALITY_ATTRIBUTE:
			return new QualityAttributeModel(parent);
		case RATIONALE:
			return new RationaleModel(parent);
		case REASON:
			return new ReasonModel(parent);
		case REQUIREMENT:
			return new RequirementModel(parent);
		case REQUIRES:
			return new RequiresModel(parent);
		case SYSTEM:
			return new SystemModel(parent);
		case TEMPLATE:
			return new TemplateModel(parent);
		default:
			Assert.isTrue(false, "Unknown ElmentType: " + type);
			return null;
		}
	}
	
	private StringBuilder buildDumpIdent(int depth) {
		StringBuilder indent = new StringBuilder(depth);
		int num = depth * 3;
		for (int i = 0; i < num; i++) 
			indent.append(' ');		
		return indent;
	}

	public String dumpArchitecture(IArchitecture arch) {
		StringBuilder sb = new StringBuilder();
		if (arch != null) {
			dumpFirstClass(sb, arch, 0);
		} else {
			sb.append("<null>");
		}
		return sb.toString();
	}
	
	public void dumpArchitectureToFile(IArchitecture arch, String filename) {
		File f = new File(filename);
		System.out.println(f.getAbsolutePath());
		if (!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e) {
				// dumping - ignore
			}
		}		
		try {
			PrintWriter pw = new PrintWriter(f);
			pw.append(dumpArchitecture(arch));
			pw.close();
		} catch (FileNotFoundException e) {
			// dumping - ignore
		}		
	}
	
	private void dumpElement(StringBuilder sb, IElement element, StringBuilder indent, int depth) {
		switch (element.getType()) {
		case REASON:
			IReason r = (IReason) element;
			sb.append(indent).append(element.getType().toString());
			sb.append(' ').append("supports: ").append(r.getSupports()).append("; inhibits: ").append(r.getInhibits());
			if (r.getExpression() != null) {
				sb.append("; expression: ");
				sb.append(r.getExpression().evaluate(new IValidationContext() {}));
			}
			break;
		case TEMPLATE:
			ITemplate t = (ITemplate) element;
			sb.append(indent).append(element.getType().toString());
			sb.append(' ').append(element.getReferencingName());
			sb.append("; parameters=").append(t.getParameters());
			sb.append("; payload={");
			dumpSyntaxNode(sb, t.getPayload(), depth + 1);
			sb.append(System.getProperty("line.separator")).append(indent).append('}');
			break;
		case LINK:
			ILink l = (ILink) element;
			sb.append(indent).append(element.getType().toString());
			if (l.getName() != null && l.getName().length() > 0) {
				sb.append(' ').append(l.getName());
			}
			sb.append(" provider=").append(l.getProvider().getQualifiedName());
			sb.append("; consumer=").append(l.getConsumer().getQualifiedName());
			break;
		case REQUIREMENT:
			IRequirement req = (IRequirement) element;
			sb.append(indent).append(element.getType().toString());
			sb.append(' ').append(element.getReferencingName());
			sb.append(' ').append(req.getValue());
			break;
		case QUALITY_ATTRIBUTE:
			IQualityAttribute qa = (IQualityAttribute) element;
			sb.append(indent).append(element.getType().toString());
			sb.append(' ').append(element.getReferencingName());
			for (IFirstClass child: qa.getSupports()) {
				dumpElement(sb, child, indent, depth);
				sb.append(' ');
			}
			break;
		default:
			sb.append(indent).append(element.getType().toString());
			sb.append(' ').append(element.getReferencingName());
		}
	}
	
	private void dumpSyntaxNode(StringBuilder sb, ISyntaxNode current, int depth) {
		sb.append(System.getProperty("line.separator"));
		sb.append(buildDumpIdent(depth));
		//sb.append(graspTokenIdToString(current.getTokenId()));
		sb.append(' ').append(current.getTokenText());
		for (ISyntaxNode child: current.getChildren()) {			
			dumpSyntaxNode(sb, child, depth + 1);
		}
	}
	
	private void dumpFirstClass(StringBuilder sb, IFirstClass fc, int depth) {
		StringBuilder indent = buildDumpIdent(depth);
		
		for (IAnnotation annotation: fc.getAnnotations()) {			
			dumpAnnotation(sb, annotation, depth);
			sb.append(System.getProperty("line.separator"));
		}
		
		dumpElement(sb, fc, indent, depth);
		
		for (IFirstClass child: fc.getBody()) {
			sb.append(System.getProperty("line.separator"));
			dumpFirstClass(sb, child, depth + 1);
		}
	}

	private void dumpAnnotation(StringBuilder sb, IAnnotation anot, int depth) {
		StringBuilder indent = buildDumpIdent(depth);
		sb.append(indent);
		sb.append('@');
		if (anot.getHandler() != null)
			sb.append(anot.getHandler());
		sb.append('(');
		for (int i = 0; i < anot.getNamedValues().size(); i++) {
			if (i != 0) {
				sb.append(", ");
			}
			INamedValue nv = anot.getNamedValues().get(i);
			if (nv.getName() != null) {
				sb.append(nv.getName());
				sb.append(" = ");
			}
			sb.append(nv.getValue());
		}
		sb.append(')');
	}	
	
	public String graspTokenIdToString(int id) {
		switch (id) {
		   case Parser.TOKEN_ARCHITECTURE: return "TOKEN_ARCHITECTURE";
		   case Parser.TOKEN_REQUIREMENT: return "TOKEN_REQUIREMENT";
		   case Parser.TOKEN_QATTRIBUTE: return "TOKEN_QATTRIBUTE";
		   case Parser.TOKEN_RATIONALE : return "TOKEN_RATIONALE";
		   case Parser.TOKEN_REASON : return "TOKEN_REASON";
		   case Parser.TOKEN_TEMPLATE : return "TOKEN_TEMPLATE";
		   case Parser.TOKEN_SYSTEM : return "TOKEN_SYSTEM";
		   case Parser.TOKEN_LAYER : return "TOKEN_LAYER";
		   case Parser.TOKEN_OVER : return "TOKEN_OVER";
		   case Parser.TOKEN_COMPONENT : return "TOKEN_COMPONENT";
		   case Parser.TOKEN_CONNECTOR : return "TOKEN_CONNECTOR";
		   case Parser.TOKEN_PROVIDES : return "TOKEN_PROVIDES";
		   case Parser.TOKEN_REQUIRES : return "TOKEN_REQUIRES";
		   case Parser.TOKEN_CHECK : return "TOKEN_CHECK";
		   case Parser.TOKEN_LINK : return "TOKEN_LINK";
		   case Parser.TOKEN_EXPR : return "TOKEN_EXPR";
		   case Parser.TOKEN_ANNOTATION : return "TOKEN_ANNOTATION";
		   case Parser.TOKEN_NAMEDVALUE : return "TOKEN_NAMEDVALUE";
		   case Parser.TOKEN_PROPERTY : return "TOKEN_PROPERTY";
		   case Parser.TOKEN_SUPPORTS : return "TOKEN_SUPPORTS";
		   case Parser.TOKEN_INHIBITS : return "TOKEN_INHIBITS";
		   case Parser.TOKEN_BECAUSE : return "TOKEN_BECAUSE";
		   case Parser.TOKEN_EXTENDS : return "TOKEN_EXTENDS";
		   case Parser.TOKEN_TRUE : return "TOKEN_TRUE";
		   case Parser.TOKEN_FALSE : return "TOKEN_FALSE";
		   case Parser.TOKEN_HANDLER : return "TOKEN_HANDLER";
		   case Parser.TOKEN_PROVIDER : return "TOKEN_PROVIDER";
		   case Parser.TOKEN_CONSUMER : return "TOKEN_CONSUMER";
		   case Parser.TOKEN_NAME : return "TOKEN_NAME";
		   case Parser.TOKEN_ALIAS : return "TOKEN_ALIAS";
		   case Parser.TOKEN_BASE : return "TOKEN_BASE";
		   case Parser.TOKEN_ARGS : return "TOKEN_ARGS";
		   case Parser.TOKEN_BODY : return "TOKEN_BODY";
		   case Parser.TOKEN_PAYLOAD : return "TOKEN_PAYLOAD";
		   case Parser.TOKEN_CALL : return "TOKEN_CALL";
		   case Parser.TOKEN_PARMS : return "TOKEN_PARMS";
		   case Parser.TOKEN_MEMB : return "TOKEN_MEMB";
		   case Parser.TOKEN_SUBSETOF : return "TOKEN_SUBSETOF";
		   case Parser.TOKEN_ACCEPTS : return "TOKEN_ACCEPTS";
		   case Parser.TOKEN_MAXDEG : return "TOKEN_MAXDEG";
		   case Parser.TOKEN_DIS : return "TOKEN_DIS";
		   case Parser.TOKEN_CON : return "TOKEN_CON";
		   case Parser.TOKEN_IOR : return "TOKEN_IOR";
		   case Parser.TOKEN_XOR : return "TOKEN_XOR";
		   case Parser.TOKEN_AND : return "TOKEN_AND";
		   case Parser.TOKEN_EQL : return "TOKEN_EQL";
		   case Parser.TOKEN_NEQ : return "TOKEN_NEQ";
		   case Parser.TOKEN_GTN : return "TOKEN_GTN";
		   case Parser.TOKEN_GTE : return "TOKEN_GTE";
		   case Parser.TOKEN_LTN : return "TOKEN_LTN";
		   case Parser.TOKEN_LTE : return "TOKEN_LTE";
		   case Parser.TOKEN_AUG : return "TOKEN_AUG";
		   case Parser.TOKEN_NAG : return "TOKEN_NAG";
		   case Parser.TOKEN_ADD : return "TOKEN_ADD";
		   case Parser.TOKEN_SUB: return "TOKEN_SUB";
		   case Parser.TOKEN_MUL : return "TOKEN_MUL";
		   case Parser.TOKEN_DIV : return "TOKEN_DIV";
		   case Parser.TOKEN_MOD : return "TOKEN_MOD";
		   case Parser.TOKEN_CMP : return "TOKEN_CMP";
		   case Parser.TOKEN_NOT: return "TOKEN_NOT";
		   case Parser.TOKEN_POS : return "TOKEN_POS";
		   case Parser.TOKEN_NEG : return "TOKEN_NEG";
		   case Parser.TOKEN_SET : return "TOKEN_SET";
		   case Parser.TOKEN_PAIR : return "TOKEN_PAIR";
		   case Parser.TOKEN_INTEGER : return "TOKEN_INTEGER";
		   case Parser.TOKEN_REAL : return "TOKEN_REAL";
		   case Parser.TOKEN_BOOLEAN : return "TOKEN_BOOLEAN";
		   case Parser.TOKEN_STRING : return "TOKEN_STRING";
		   case Parser.TOKEN_DECL : return "TOKEN_DECL";
		   // deduced through debugging
		   case 42: return "TOKEN_IDENTIFIER";
		   default:
			   return "unknown token id ("+id+")";
		}
	}
}
