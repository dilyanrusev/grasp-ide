package uk.ac.standrews.grasp.ide.model;

import grasp.lang.IAnnotation;
import grasp.lang.IArchitecture;
import grasp.lang.ICheck;
import grasp.lang.IElement;
import grasp.lang.IExpression;
import grasp.lang.IFirstClass;
import grasp.lang.ILayer;
import grasp.lang.ILink;
import grasp.lang.INamedValue;
import grasp.lang.IProperty;
import grasp.lang.IQualityAttribute;
import grasp.lang.IRationale;
import grasp.lang.IReason;
import grasp.lang.IRequirement;
import grasp.lang.ISyntaxTree;
import grasp.lang.ISystem;
import grasp.lang.ITemplate;
import grasp.lang.Parser;

import java.io.InputStreamReader;
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

import shared.io.ISource;
import uk.ac.standrews.grasp.ide.GraspPlugin;
import uk.ac.standrews.grasp.ide.Log;
import uk.ac.standrews.grasp.ide.builder.GraspSourceFile;
import uk.ac.standrews.grasp.ide.builder.NullLogger;
import uk.ac.standrews.grasp.ide.editors.completion.GraspScanner;

public final class GraspModel {
	public static final GraspModel INSTANCE = new GraspModel();
	
	private IResourceChangeListener resourceChangeListener;
	private Map<IFile, GraspScanner> scannedFiles;
	private Map<IFile, ISyntaxTree> parsedFiles;
	private IResourceDeltaVisitor visitor;
	
	private GraspModel() { 
		scannedFiles = new HashMap<IFile, GraspScanner>();
		parsedFiles = new HashMap<IFile, ISyntaxTree>();
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
					
					GraspScanner scanner = new GraspScanner();
					scanner.parse(new InputStreamReader(file.getContents()));
					GraspModel.INSTANCE.scannedFiles.put(file, scanner);
					Parser parser = new Parser();
					ISource source = new GraspSourceFile(file);
					ISyntaxTree tree = parser.parse(source, NullLogger.INSTANCE);
					GraspModel.INSTANCE.parsedFiles.put(file, tree);
					break;
				case IResourceDelta.REMOVED:
					GraspModel.INSTANCE.scannedFiles.remove(file);	
					GraspModel.INSTANCE.parsedFiles.remove(file);
					break;
				}
				return true;
			}
		};
	}
	
	public GraspScanner getScannerForFile(IFile file) {
		return scannedFiles.get(file);
	}
	
	public void setScannerForFile(IFile file, GraspScanner scanner) {
		scannedFiles.put(file, scanner);		
	}
	
	public ISyntaxTree getSyntaxTreeForFile(IFile file) {
		return parsedFiles.get(file);
	}
	
	public void setFileSyntaxTree(IFile file, ISyntaxTree tree) {
		parsedFiles.put(file, tree);
	}
	
	public void init() {
		ResourcesPlugin.getWorkspace().addResourceChangeListener(resourceChangeListener, IResourceChangeEvent.POST_CHANGE);
	}
	
	public void dispose() {
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(resourceChangeListener);
	}
	
	
	public static IElement makeObservable(IElement element, IElement parent) {
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
		
		if (IArchitecture.class.isInstance(element)) 
			return new ArchitectureModel((IArchitecture) element);
		
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
		
		throw new AssertionError("Unrecognized type: " + element.getClass());
	}

	
}
