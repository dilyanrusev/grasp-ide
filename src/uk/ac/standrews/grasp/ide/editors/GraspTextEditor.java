/**
 * 
 */
package uk.ac.standrews.grasp.ide.editors;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.editors.text.FileDocumentProvider;
import org.eclipse.ui.editors.text.TextEditor;

/**
 * Text editor for Grasp code
 * @author Dilyan Rusev
 *
 */
public class GraspTextEditor extends TextEditor {
	
	public GraspTextEditor() {
		super();
		setSourceViewerConfiguration(new GraspSourceViewerConfiguration());
		setDocumentProvider(new GraspDocumentProvider());
	}
	
	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {		
		GraspEditor.assertInputIsGraspContent(input);
		super.init(site, input);
	}
	
	@Override
	public void dispose() {
		super.dispose();
	}	
	
	@Override
	protected boolean isLineNumberRulerVisible() {
		return true;
	}
	
	@Override
	protected ISourceViewer createSourceViewer(Composite parent,
			IVerticalRuler ruler, int styles) {
		fAnnotationAccess= getAnnotationAccess();
		fOverviewRuler= createOverviewRuler(getSharedColors());

		ISourceViewer viewer= new GraspSourceViewer(this, parent, ruler, getOverviewRuler(), isOverviewRulerVisible(), styles);
		// ensure decoration support has been created and configured.
		getSourceViewerDecorationSupport(viewer);

		return viewer;
	}
	
	private static class GraspDocumentProvider extends FileDocumentProvider {
		@Override
		protected IDocument createDocument(Object element) throws CoreException {
			IDocument document = super.createDocument(element);
		    if (document != null)
		    {
		        IDocumentPartitioner partitioner = new DocumentPartitioner(
		        		PartitionScanner.INSTANCE, new String[] { PartitionScanner.BLOCK_COMMENT });
		        partitioner.connect(document);
		        document.setDocumentPartitioner(partitioner);
		    }
		    return document;
		}
	}
}
