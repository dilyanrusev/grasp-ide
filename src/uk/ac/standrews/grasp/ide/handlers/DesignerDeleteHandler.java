package uk.ac.standrews.grasp.ide.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.handlers.HandlerUtil;

import uk.ac.standrews.grasp.ide.editors.GraspDesigner;

/**
 * Deletes the current selection in the active designer
 * @author Dilyan Rusev
 *
 */
public class DesignerDeleteHandler extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		System.out.println("DesignerDeleteHandler");
		IEditorPart editor = HandlerUtil.getActiveEditor(event);
		if (editor instanceof GraspDesigner) {
			GraspDesigner designer = (GraspDesigner) editor;
			designer.deleteSelection();
		}
		return null;
	}

}
