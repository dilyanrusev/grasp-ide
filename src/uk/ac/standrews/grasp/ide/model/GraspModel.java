package uk.ac.standrews.grasp.ide.model;

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

/**
 * Helper class that contains statistics about the Grasp model
 * @author Dilyan Rusev
 *
 */
public final class GraspModel {
	/** Singleton instance */
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
					break;
				case IResourceDelta.REMOVED:
					GraspModel.INSTANCE.removeFileStats(file);
					break;
				}
				return true;
			}
		};
	}
	
	/**
	 * Retrieve file-model mapping for a particular file. Mapping will be created if it didn't exist
	 * @param file File whose model is requested
	 * @return
	 */
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
	
	/**
	 * Called in the plugin's start method to subscribe to global events
	 */
	public void init() {
		ResourcesPlugin.getWorkspace().addResourceChangeListener(resourceChangeListener, IResourceChangeEvent.POST_CHANGE);
	}
	
	/**
	 * Called in the plugin's stop method to remove subscriptions from global events
	 */
	public void dispose() {
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(resourceChangeListener);
	}
	
	private StringBuilder buildDumpIdent(int depth) {
		StringBuilder indent = new StringBuilder(depth);
		int num = depth * 3;
		for (int i = 0; i < num; i++) 
			indent.append(' ');		
		return indent;
	}

	/**
	 * Print an architecture to a string for debugging purposes
	 * @param arch Architecture to print
	 * @return
	 */
	public String dumpArchitecture(ArchitectureModel arch) {
		StringBuilder sb = new StringBuilder();
		if (arch != null) {
			dumpFirstClass(sb, arch, 0);
		} else {
			sb.append("<null>");
		}
		return sb.toString();
	}
	
	/**
	 * Print an architecture to a file for debugging purposes
	 * @param arch Architecture to print
	 * @param filename File to write to
	 */
	public void dumpArchitectureToFile(ArchitectureModel arch, String filename) {
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
	
	private void dumpElement(StringBuilder sb, ElementModel element, StringBuilder indent, int depth) {
		switch (element.getType()) {
		case REASON:
			ReasonModel r = (ReasonModel) element;
			sb.append(indent).append(element.getType().toString());
			sb.append(' ').append("supports: ").append(r.getSupports()).append("; inhibits: ").append(r.getInhibits());
			if (r.getExpression() != null) {
				sb.append("; expression: ");
				sb.append(r.getExpression().getValue());
			}
			break;
		case TEMPLATE:
			TemplateModel t = (TemplateModel) element;
			sb.append(indent).append(element.getType().toString());
			sb.append(' ').append(element.getReferencingName());
			sb.append("; parameters=").append(t.getParameters());			
			break;
		case LINK:
			LinkModel l = (LinkModel) element;
			sb.append(indent).append(element.getType().toString());
			if (l.getName() != null && l.getName().length() > 0) {
				sb.append(' ').append(l.getName());
			}
			sb.append(" provider=").append(l.getProvider() != null ? l.getProvider().getQualifiedName() : null);
			sb.append("; consumer=").append(l.getConsumer() != null ? l.getConsumer().getQualifiedName() : null);
			break;
		case REQUIREMENT:
			RequirementModel req = (RequirementModel) element;
			sb.append(indent).append(element.getType().toString());
			sb.append(' ').append(element.getReferencingName());
			sb.append(' ').append(req.getValue());
			break;
		case QUALITY_ATTRIBUTE:
			QualityAttributeModel qa = (QualityAttributeModel) element;
			sb.append(indent).append(element.getType().toString());
			sb.append(' ').append(element.getReferencingName());
			for (FirstClassModel child: qa.getSupports()) {
				dumpElement(sb, child, indent, depth);
				sb.append(' ');
			}
			break;
		default:
			sb.append(indent).append(element.getType().toString());
			sb.append(' ').append(element.getReferencingName());
		}
	}
	
	private void dumpFirstClass(StringBuilder sb, FirstClassModel fc, int depth) {
		StringBuilder indent = buildDumpIdent(depth);
		
		for (AnnotationModel annotation: fc.getAnnotations()) {			
			dumpAnnotation(sb, annotation, depth);
			sb.append(System.getProperty("line.separator"));
		}
		
		dumpElement(sb, fc, indent, depth);
		
		for (FirstClassModel child: fc.getBody()) {
			sb.append(System.getProperty("line.separator"));
			dumpFirstClass(sb, child, depth + 1);
		}
	}

	private void dumpAnnotation(StringBuilder sb, AnnotationModel anot, int depth) {
		StringBuilder indent = buildDumpIdent(depth);
		sb.append(indent);
		sb.append('@');
		if (anot.getName() != null)
			sb.append(anot.getName());
		sb.append('(');
		boolean first = true;
		for (NamedValueModel nv: anot.getNamedValues()) {
			if (!first) {
				sb.append(", ");
			} else {
				first = false;
			}
			
			if (nv.getName() != null) {
				sb.append(nv.getName());
				sb.append(" = ");
			}
			sb.append(nv.getValue());
		}		
		sb.append(')');
	}	
}
