package uk.ac.standrews.grasp.ide.editors;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.swt.graphics.Color;

import uk.ac.standrews.grasp.ide.GraspPlugin;

/**
 * Configuration for <code>GraspTextEditor</code>
 * @author Dilyan Rusev
 * @see GraspTextEditor
 */
class GraspSourceViewerConfiguration extends SourceViewerConfiguration {
	private static final String[] CONTENT_TYPES;
	
	static {
		CONTENT_TYPES = new String[] { IDocument.DEFAULT_CONTENT_TYPE, 
				GraspPlugin.ID_GRASP_CONTENT_TYPE };
	}
	
	@Override
	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
		return CONTENT_TYPES.clone();
	}
	
	@Override
	public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
		// Add support for syntax colouring
		PresentationReconciler reconciler = new PresentationReconciler();
		{
			DefaultDamagerRepairer repairer = new DefaultDamagerRepairer(GraspTokenScanner.INSTANCE);
			reconciler.setDamager(repairer, IDocument.DEFAULT_CONTENT_TYPE);
			reconciler.setRepairer(repairer, IDocument.DEFAULT_CONTENT_TYPE);
		}
		{
			DefaultDamagerRepairer repairer = new DefaultDamagerRepairer(
					new SingleTokenScanner(TextUtil.getBlockCommentColour()));
			reconciler.setDamager(repairer, PartitionScanner.BLOCK_COMMENT);
			reconciler.setRepairer(repairer, PartitionScanner.BLOCK_COMMENT);
		}
		
		return reconciler;
	}
	
	private static class SingleTokenScanner extends RuleBasedScanner {
		public SingleTokenScanner(Color foregroundColour) {
			TextAttribute attrib = new TextAttribute(foregroundColour);
			setDefaultReturnToken(new Token(attrib));
		}
	}
}
