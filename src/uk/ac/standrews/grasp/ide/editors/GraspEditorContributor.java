package uk.ac.standrews.grasp.ide.editors;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.DeleteRetargetAction;
import org.eclipse.gef.ui.actions.RedoRetargetAction;
import org.eclipse.gef.ui.actions.UndoRetargetAction;
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.RetargetAction;
import org.eclipse.ui.ide.IDEActionFactory;
import org.eclipse.ui.part.MultiPageEditorActionBarContributor;
import org.eclipse.ui.texteditor.ITextEditorActionConstants;

import uk.ac.standrews.grasp.ide.Log;

/**
 * Manages the installation/deinstallation of global actions for multi-page editors.
 * Responsible for the redirection of global actions to the active editor.
 * Multi-page contributor replaces the contributors for the individual editors in the multi-page editor.
 */
public class GraspEditorContributor extends MultiPageEditorActionBarContributor {	
	private ActionRegistry registry = new ActionRegistry();
	private List<RetargetAction> retargetActions = new ArrayList<RetargetAction>();
	private List<String> globalActionKeys = new ArrayList<String>();
	
	protected ActionRegistry getActionRegistry() {
		return registry;
	}
	
	protected void addAction(IAction action) {
		getActionRegistry().registerAction(action);
	}
	
	protected void addRetargetAction(RetargetAction action) {
		addAction(action);
		retargetActions.add(action);
		getPage().addPartListener(action);
		addGlobalActionKey(action.getId());
	}
	
	protected void addGlobalActionKey(String key) {
		globalActionKeys.add(key);
	}
	
	protected void buildActions() {
		addRetargetAction(new UndoRetargetAction());
		addRetargetAction(new RedoRetargetAction());
		addRetargetAction(new DeleteRetargetAction());
	}
	
	protected void declareGlobalActionKeys() {
		addGlobalActionKey(ActionFactory.UNDO.getId());
		addGlobalActionKey(ActionFactory.REDO.getId());
		addGlobalActionKey(ActionFactory.DELETE.getId());
	}
	
	@Override
	public void init(IActionBars bars) {
		buildActions();
		declareGlobalActionKeys();
		super.init(bars);
	}
	
	@Override
	public void dispose() {		
		super.dispose();
		for (int i = 0; i < retargetActions.size(); i++) {
			RetargetAction action = retargetActions.get(i);
			getPage().removePartListener(action);
			action.dispose();
		}
		registry.dispose();
		retargetActions = null;
		registry = null;
	}
	
	protected IAction getAction(String id) {
		return getActionRegistry().getAction(id);
	}
	
	/**
	 * Returns the action resisted with the given text editor.
	 * @return IAction or null if editor is null.
	 */
	protected IAction getAction(IEditorPart editor, String actionID) {
		if (editor instanceof GraspTextEditor) {
			((GraspTextEditor) editor).getAction(actionID);
		} else if (editor instanceof GraspDesigner) {
			ActionRegistry registry = (ActionRegistry)
					editor.getAdapter(ActionRegistry.class);
			IAction action = registry.getAction(actionID);
			return action;
		}
		return null;
	}
	
	@Override
	public void setActivePage(IEditorPart part) {
		if (part instanceof GraspTextEditor) {
			setActivePageForTextEditor((GraspTextEditor) part);
		} else if (part instanceof GraspDesigner) {
			setActivePageForDesigner((GraspDesigner) part);
		} else {
			Log.warn("Unrecognized edit part - " + part);
		}		
	}

	private void setActivePageForDesigner(GraspDesigner part) {		
		ActionRegistry registry = (ActionRegistry) part
				.getAdapter(ActionRegistry.class);
		IActionBars bars = getActionBars();
		bars.clearGlobalActionHandlers();
		for (int i = 0; i < globalActionKeys.size(); i++) {
			String id = (String) globalActionKeys.get(i);
			bars.setGlobalActionHandler(id, registry.getAction(id));
		}
		bars.updateActionBars();
	}

	private void setActivePageForTextEditor(GraspTextEditor part) {
		IActionBars actionBars = getActionBars();		
		actionBars.clearGlobalActionHandlers();
		actionBars.setGlobalActionHandler(
			ActionFactory.DELETE.getId(),
			getAction(part, ITextEditorActionConstants.DELETE));
		actionBars.setGlobalActionHandler(
			ActionFactory.UNDO.getId(),
			getAction(part, ITextEditorActionConstants.UNDO));
		actionBars.setGlobalActionHandler(
			ActionFactory.REDO.getId(),
			getAction(part, ITextEditorActionConstants.REDO));
		
		actionBars.setGlobalActionHandler(
			ActionFactory.CUT.getId(),
			getAction(part, ITextEditorActionConstants.CUT));
		actionBars.setGlobalActionHandler(
			ActionFactory.COPY.getId(),
			getAction(part, ITextEditorActionConstants.COPY));
		actionBars.setGlobalActionHandler(
			ActionFactory.PASTE.getId(),
			getAction(part, ITextEditorActionConstants.PASTE));			
		actionBars.setGlobalActionHandler(
			ActionFactory.FIND.getId(),
			getAction(part, ITextEditorActionConstants.FIND));
		actionBars.setGlobalActionHandler(
			IDEActionFactory.BOOKMARK.getId(),
			getAction(part, IDEActionFactory.BOOKMARK.getId()));
		actionBars.updateActionBars();
	}
}
