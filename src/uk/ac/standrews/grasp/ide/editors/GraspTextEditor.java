/**
 * 
 */
package uk.ac.standrews.grasp.ide.editors;

import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.editors.text.TextEditor;

/**
 * Text editor for Grasp code
 * @author Dilyan Rusev
 *
 */
public class GraspTextEditor extends TextEditor {
	public static final RGB RGB_KEYWORD = new RGB(127, 0, 85);
	
	public GraspTextEditor() {
		super();
		setSourceViewerConfiguration(new GraspSourceViewerConfiguration());
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
	
	
}
