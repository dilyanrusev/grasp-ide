package uk.ac.standrews.grasp.ide.editors;

import java.util.Collection;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;

import uk.ac.standrews.grasp.ide.GraspPlugin;
import uk.ac.standrews.grasp.ide.editors.completion.Context;
import uk.ac.standrews.grasp.ide.editors.completion.ICodeCompletionContext;
import uk.ac.standrews.grasp.ide.editors.completion.ICodeCompletionProcessor;
import uk.ac.standrews.grasp.ide.editors.completion.KeywordCodeCompletion;
import uk.ac.standrews.grasp.ide.preferences.PreferenceKeys;
import uk.ac.standrews.grasp.ide.preferences.Preferences;

/**
 * Code completion for Grasp
 * @author Dilyan Rusev
 *
 */
class GraspCodeCompletionProcessor implements IContentAssistProcessor {
	public static final GraspCodeCompletionProcessor INSTANCE 
		= new GraspCodeCompletionProcessor();
	
	private static final IContextInformation[] EMPTY_CONTEXT_LIST = { };
	private static final ICodeCompletionProcessor[] EMPTY_PROCESSOR_LIST = { };
	private static final ICodeCompletionProcessor[] DEFAULT_PROCESSOR_LIST = {
		new KeywordCodeCompletion()
	};
	private static final char[] PROPOSAL_ACTIVATION_CHARS = {};
	private static final char[] INFORMATION_ACTIVATION_CHARS = {};
	
	
	private ICodeCompletionContext context = new Context();
	private ICodeCompletionProcessor[] rules;
	private final Runnable updateRulesTask;
	private final IPropertyChangeListener preferencesChangedListener;

	
	private GraspCodeCompletionProcessor() {
		updateRulesTask = new Runnable() {			
			@Override
			public void run() {
				if (Preferences.isKeywordCompletionEnabled()) {
					rules = DEFAULT_PROCESSOR_LIST;
				} else {
					rules = EMPTY_PROCESSOR_LIST;
				}				
			}
		};
		preferencesChangedListener = new IPropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				if (PreferenceKeys.ENABLE_KEYWORD_COMPLETION.equals(event)) {
					updateRules();
				}
			}
		};
		updateRules();	
		Preferences.getStore().addPropertyChangeListener(preferencesChangedListener);
		GraspPlugin.getDefault().executeAtPluginStop(new Runnable() {			
			@Override
			public void run() {
				Preferences.getStore().removePropertyChangeListener(preferencesChangedListener);
			}
		});
	}
	
	private void updateRules() {
		Display.getDefault().asyncExec(updateRulesTask);
	}

	@Override
	public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer,	int offset) {
		Assert.isTrue(viewer instanceof GraspSourceViewer, "TextViewer is not GraspSourceViewer");
		
		IDocument doc = viewer.getDocument();
		IEditorInput input = ((GraspSourceViewer)viewer).getEditor().getEditorInput();
		IFile file = ((IFileEditorInput)input).getFile();
		context.computeFor(file, doc, offset);
		Collection<ICompletionProposal> completions = ICodeCompletionProcessor.NO_PROPOSALS;
		for (ICodeCompletionProcessor rule: rules) {
			Collection<ICompletionProposal> props = rule.evaluateContext(context);
			if (props.size() > 0) {
				completions = props;
				break;
			}
		}
		
		return completions.toArray(new ICompletionProposal[completions.size()]);
	}

	@Override
	public IContextInformation[] computeContextInformation(ITextViewer viewer, int offset) {
		return EMPTY_CONTEXT_LIST;
	}

	@Override
	public char[] getCompletionProposalAutoActivationCharacters() {
		return PROPOSAL_ACTIVATION_CHARS;
	}

	@Override
	public char[] getContextInformationAutoActivationCharacters() {
		return INFORMATION_ACTIVATION_CHARS;
	}

	@Override
	public String getErrorMessage() {
		return "Something went terribly wrong :D";
	}

	@Override
	public IContextInformationValidator getContextInformationValidator() {
		return null;
	}
}
