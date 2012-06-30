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
	public static final RGB RGB_INLINE_COMMENT = new RGB(63, 127, 95);
	public static final RGB RGB_BLOCK_COMMENT = new RGB(63, 127, 95);
	public static final RGB RGB_STRING_LITERAL = new RGB(42, 0, 255);
	public static final RGB RGB_DECLARATIVE_LITERAL = new RGB(63, 95, 191);
	
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
