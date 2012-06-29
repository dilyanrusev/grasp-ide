package uk.ac.standrews.grasp.ide.editors;

import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.reconciler.IReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;

import uk.ac.standrews.grasp.ide.GraspPlugin;

/**
 * Configuration for <code>GraspTextEditor</code>
 * @author Dilyan Rusev
 * @see GraspTextEditor
 */
class GraspSourceViewerConfiguration extends SourceViewerConfiguration {
	private static final String[] CONTENT_TYPES;
	
	static {
		CONTENT_TYPES = new String[] { GraspPlugin.ID_GRASP_CONTENT_TYPE };
	}
	
	@Override
	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
		return CONTENT_TYPES.clone();
	}
	
	@Override
	public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
		// Add support for syntax colouring
		PresentationReconciler reconciler = new PresentationReconciler();
		DefaultDamagerRepairer repairer = new DefaultDamagerRepairer(new GraspTokenScanner());
		reconciler.setDamager(repairer, GraspPlugin.ID_GRASP_CONTENT_TYPE);
		reconciler.setRepairer(repairer, GraspPlugin.ID_GRASP_CONTENT_TYPE);		
		return reconciler;
	}
}
