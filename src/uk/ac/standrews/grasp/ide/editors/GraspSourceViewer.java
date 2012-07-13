package uk.ac.standrews.grasp.ide.editors;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.text.source.IOverviewRuler;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IFileEditorInput;

/**
 * Extends <code>SourceViewer</code> to enable access to the editor component
 * @author Dilyan Rusev
 *
 */
public class GraspSourceViewer extends SourceViewer {
	private GraspTextEditor editor;
	
	/**
	 * Create a new link between this source viewer and its editor
	 * @param editor Text editor that creates this source viewer
	 * @param parent the parent of the viewer's control
	 * @param verticalRuler the vertical ruler used by this source viewer
	 * @param overviewRuler the overview ruler
	 * @param showAnnotationsOverview <code>true</code> if the overview ruler should be visible, <code>false</code> otherwise
	 * @param styles the SWT style bits for the viewer's control,
	 * 			<em>if <code>SWT.WRAP</code> is set then a custom document adapter needs to be provided, see {@link #createDocumentAdapter()}
	 */
	public GraspSourceViewer(GraspTextEditor editor, Composite parent, IVerticalRuler verticalRuler, 
			IOverviewRuler overviewRuler, boolean showAnnotationsOverview, int styles)	{
		super(parent, verticalRuler, overviewRuler, showAnnotationsOverview, styles);
		Assert.isNotNull(editor);
		this.editor = editor;
	}
	
	/**
	 * Returns the text editor that created the source viewer
	 * @return Editor that created the source viewer
	 */
	public GraspTextEditor getEditor() {
		return editor;
	}	
	
	/**
	 * Shortcut for extracting the <code>IFile</code> from the editor input
	 * @return File opened by the editor
	 */
	public IFile getFile() {
		return ((IFileEditorInput)editor.getEditorInput()).getFile();
	}
}
