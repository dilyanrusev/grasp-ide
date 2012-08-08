package uk.ac.standrews.grasp.ide.editors;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;

import uk.ac.standrews.grasp.ide.GraspPlugin;
import uk.ac.standrews.grasp.ide.preferences.Preferences;

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
		if (Preferences.isSyntaxHighlightingEnabled()) {
			PresentationReconciler reconciler = new PresentationReconciler();
			{
				DefaultDamagerRepairer repairer = new DefaultDamagerRepairer(new GraspTokenScanner());
				reconciler.setDamager(repairer, IDocument.DEFAULT_CONTENT_TYPE);
				reconciler.setRepairer(repairer, IDocument.DEFAULT_CONTENT_TYPE);
			}
			{
				DefaultDamagerRepairer repairer = new DefaultDamagerRepairer(
						new SingleTokenScanner(new Token(new TextAttribute(TextUtil.getBlockCommentColour()))));
				reconciler.setDamager(repairer, PartitionScanner.BLOCK_COMMENT);
				reconciler.setRepairer(repairer, PartitionScanner.BLOCK_COMMENT);
			}		
			return reconciler;		
		} else {
			return super.getPresentationReconciler(sourceViewer);
		}
	}
	
	@Override
	public IContentAssistant getContentAssistant(ISourceViewer sourceViewer) {		
		// Add support for code completion
		ContentAssistant assistant = new ContentAssistant();
		IContentAssistProcessor processor = GraspCodeCompletionProcessor.INSTANCE;
		assistant.setContentAssistProcessor(processor, IDocument.DEFAULT_CONTENT_TYPE);
		assistant.setInformationControlCreator(getInformationControlCreator(sourceViewer));
		return assistant;
	}
	
	private static class SingleTokenScanner extends RuleBasedScanner {		
		public SingleTokenScanner(IToken token) {			
			setDefaultReturnToken(token);
		}		
	}
}
