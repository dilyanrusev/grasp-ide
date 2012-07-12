package uk.ac.standrews.grasp.ide.editors;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.text.IAutoIndentStrategy;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IEventConsumer;
import org.eclipse.jface.text.IFindReplaceTarget;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextDoubleClickStrategy;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.ITextInputListener;
import org.eclipse.jface.text.ITextListener;
import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.text.IUndoManager;
import org.eclipse.jface.text.IViewportListener;
import org.eclipse.jface.text.TextPresentation;
import org.eclipse.jface.text.quickassist.IQuickAssistAssistant;
import org.eclipse.jface.text.quickassist.IQuickAssistInvocationContext;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.ContentAssistantFacade;
import org.eclipse.jface.text.source.IAnnotationHover;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.ISourceViewerExtension;
import org.eclipse.jface.text.source.ISourceViewerExtension2;
import org.eclipse.jface.text.source.ISourceViewerExtension3;
import org.eclipse.jface.text.source.ISourceViewerExtension4;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;

public class GraspSourceViewer implements ISourceViewer,
		ISourceViewerExtension2, ISourceViewerExtension3,
		ISourceViewerExtension4, ISourceViewerExtension {
	
	private ISourceViewer adapted;
	private ISourceViewerExtension adaptedExtension;
	private ISourceViewerExtension2 adaptedExtension2;
	private ISourceViewerExtension3 adaptedExtension3;
	private ISourceViewerExtension4 adaptedExtension4;
	private GraspTextEditor editor;
	
	public GraspSourceViewer(GraspTextEditor editor, ISourceViewer adapted) {
		Assert.isNotNull(editor);
		Assert.isTrue(adapted instanceof ISourceViewerExtension
				&& adapted instanceof ISourceViewerExtension2
				&& adapted instanceof ISourceViewerExtension3
				&& adapted instanceof ISourceViewerExtension4);
				
		this.editor = editor;
		this.adapted = adapted;	
		this.adaptedExtension = (ISourceViewerExtension)adapted;	
		this.adaptedExtension2 = (ISourceViewerExtension2)adapted;	
		this.adaptedExtension3 = (ISourceViewerExtension3)adapted;	
		this.adaptedExtension4 = (ISourceViewerExtension4)adapted;		
	}

	public GraspTextEditor getEditor() {
		return editor;
	}
	
	@Override
	public StyledText getTextWidget() {
		return adapted.getTextWidget();
	}

	@Override
	public void setUndoManager(IUndoManager undoManager) {
		adapted.setUndoManager(undoManager);
	}

	@Override
	public void setTextDoubleClickStrategy(ITextDoubleClickStrategy strategy,
			String contentType) {
		adapted.setTextDoubleClickStrategy(strategy, contentType);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void setAutoIndentStrategy(IAutoIndentStrategy strategy,
			String contentType) {
		adapted.setAutoIndentStrategy(strategy, contentType);
	}

	@Override
	public void setTextHover(ITextHover textViewerHover, String contentType) {
		adapted.setTextHover(textViewerHover, contentType);
	}

	@Override
	public void activatePlugins() {
		adapted.activatePlugins();
	}

	@Override
	public void resetPlugins() {
		adapted.resetPlugins();
	}

	@Override
	public void addViewportListener(IViewportListener listener) {
		adapted.addViewportListener(listener);
	}

	@Override
	public void removeViewportListener(IViewportListener listener) {
		adapted.removeViewportListener(listener);
	}

	@Override
	public void addTextListener(ITextListener listener) {
		adapted.addTextListener(listener);
	}

	@Override
	public void removeTextListener(ITextListener listener) {
		adapted.removeTextListener(listener);
	}

	@Override
	public void addTextInputListener(ITextInputListener listener) {
		adapted.addTextInputListener(listener);
	}

	@Override
	public void removeTextInputListener(ITextInputListener listener) {
		adapted.removeTextInputListener(listener);
	}

	@Override
	public void setDocument(IDocument document) {
		adapted.setDocument(document);
	}

	@Override
	public IDocument getDocument() {
		return adapted.getDocument();
	}

	@Override
	public void setEventConsumer(IEventConsumer consumer) {
		adapted.setEventConsumer(consumer);
	}

	@Override
	public void setEditable(boolean editable) {
		adapted.setEditable(editable);
	}

	@Override
	public boolean isEditable() {
		return adapted.isEditable();
	}

	@Override
	public void setDocument(IDocument document, int modelRangeOffset,
			int modelRangeLength) {
		adapted.setDocument(document, modelRangeOffset, modelRangeLength);
	}

	@Override
	public void setVisibleRegion(int offset, int length) {
		adapted.setVisibleRegion(offset, length);
	}

	@Override
	public void resetVisibleRegion() {
		adapted.resetVisibleRegion();
	}

	@Override
	public IRegion getVisibleRegion() {
		return adapted.getVisibleRegion();
	}

	@Override
	public boolean overlapsWithVisibleRegion(int offset, int length) {
		return adapted.overlapsWithVisibleRegion(offset, length);
	}

	@Override
	public void changeTextPresentation(TextPresentation presentation,
			boolean controlRedraw) {
		adapted.changeTextPresentation(presentation, controlRedraw);
	}

	@Override
	public void invalidateTextPresentation() {
		adapted.invalidateTextPresentation();
	}

	@Override
	public void setTextColor(Color color) {
		adapted.setTextColor(color);
	}

	@Override
	public void setTextColor(Color color, int offset, int length,
			boolean controlRedraw) {
		adapted.setTextColor(color, offset, length, controlRedraw);
	}

	@Override
	public ITextOperationTarget getTextOperationTarget() {
		return adapted.getTextOperationTarget();
	}

	@Override
	public IFindReplaceTarget getFindReplaceTarget() {
		return adapted.getFindReplaceTarget();
	}

	@Override
	public void setDefaultPrefixes(String[] defaultPrefixes, String contentType) {
		adapted.setDefaultPrefixes(defaultPrefixes, contentType);
	}

	@Override
	public void setIndentPrefixes(String[] indentPrefixes, String contentType) {
		adapted.setIndentPrefixes(indentPrefixes, contentType);
	}

	@Override
	public void setSelectedRange(int offset, int length) {
		adapted.setSelectedRange(offset, length);
	}

	@Override
	public Point getSelectedRange() {
		return adapted.getSelectedRange();
	}

	@Override
	public ISelectionProvider getSelectionProvider() {
		return adapted.getSelectionProvider();
	}

	@Override
	public void revealRange(int offset, int length) {
		adapted.revealRange(offset, length);
	}

	@Override
	public void setTopIndex(int index) {
		adapted.setTopIndex(index);
	}

	@Override
	public int getTopIndex() {
		return adapted.getTopIndex();
	}

	@Override
	public int getTopIndexStartOffset() {
		return adapted.getTopIndexStartOffset();
	}

	@Override
	public int getBottomIndex() {
		return adapted.getBottomIndex();
	}

	@Override
	public int getBottomIndexEndOffset() {
		return adapted.getBottomIndexEndOffset();
	}

	@Override
	public int getTopInset() {
		return adapted.getTopInset();
	}

	@Override
	public void showAnnotationsOverview(boolean show) {
		adaptedExtension.showAnnotationsOverview(show);
	}

	@Override
	public ContentAssistantFacade getContentAssistantFacade() {
		return adaptedExtension4.getContentAssistantFacade();
	}

	@Override
	public IQuickAssistAssistant getQuickAssistAssistant() {
		return adaptedExtension3.getQuickAssistAssistant();
	}

	@Override
	public IQuickAssistInvocationContext getQuickAssistInvocationContext() {
		return adaptedExtension3.getQuickAssistInvocationContext();
	}

	@Override
	public IAnnotationHover getCurrentAnnotationHover() {
		return adaptedExtension3.getCurrentAnnotationHover();
	}

	@Override
	public void unconfigure() {
		adaptedExtension2.unconfigure();
	}

	@Override
	public IAnnotationModel getVisualAnnotationModel() {
		return adaptedExtension2.getVisualAnnotationModel();
	}

	@Override
	public void configure(SourceViewerConfiguration configuration) {
		adapted.configure(configuration);
	}

	@Override
	public void setAnnotationHover(IAnnotationHover annotationHover) {
		adapted.setAnnotationHover(annotationHover);
	}

	@Override
	public void setDocument(IDocument document, IAnnotationModel annotationModel) {
		adapted.setDocument(document, annotationModel);
	}

	@Override
	public void setDocument(IDocument document,
			IAnnotationModel annotationModel, int modelRangeOffset,
			int modelRangeLength) {
		adapted.setDocument(document, annotationModel, modelRangeOffset, modelRangeLength);
	}

	@Override
	public IAnnotationModel getAnnotationModel() {
		return adapted.getAnnotationModel();
	}

	@Override
	public void setRangeIndicator(Annotation rangeIndicator) {
		adapted.setRangeIndicator(rangeIndicator);
	}

	@Override
	public void setRangeIndication(int offset, int length, boolean moveCursor) {
		adapted.setRangeIndication(offset, length, moveCursor);	}

	@Override
	public IRegion getRangeIndication() {
		return adapted.getRangeIndication();
	}

	@Override
	public void removeRangeIndication() {
		adapted.removeRangeIndication();
	}

	@Override
	public void showAnnotations(boolean show) {
		adapted.showAnnotations(show);
	}
}
