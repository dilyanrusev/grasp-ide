package uk.ac.standrews.grasp.ide.views;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPageListener;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IWindowListener;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.IWorkbenchWindow;

import uk.ac.standrews.grasp.ide.GraspPlugin;

/**
 * Watches for changes in the workbench and notifies when the active editor has changed
 * @author Dilyan Rusev
 *
 */
public class ActiveEditorChangeWatcher {
	private IWorkbench workbench;
	private IWorkbenchWindow initialActiveWindow;
	private IWorkbenchPage initialPage;
	private Set<IActiveEditorChangedListener> listeners = new HashSet<IActiveEditorChangedListener>();
	
	private IWindowListener windowListener = new WindowListenerStub() {
		@Override
		public void windowOpened(IWorkbenchWindow window) {
			window.addPageListener(pageListener);
		}
		
		@Override
		public void windowClosed(IWorkbenchWindow window) { 
			window.removePageListener(pageListener);
		}
	};	
	private IPageListener pageListener = new PageListenerStub() {
		@Override
		public void pageOpened(IWorkbenchPage page) {
			page.addPartListener(partListener);
		}
		
		@Override
		public void pageClosed(IWorkbenchPage page) {
			page.removePartListener(partListener);
		}
	};
	private IPartListener2 partListener = new PartListenerStub() {
		@Override
		public void partVisible(IWorkbenchPartReference partRef) {
			if (partRef.getId().equals(GraspPlugin.ID_GRASP_EDITOR) ||
					partRef.getId().equals(GraspPlugin.ID_GRASP_TEXT_EDITOR)) {
				fireEditorVisibilityChanged((IEditorPart) partRef.getPart(true));
			}
		}		
	};
	
	
	/**
	 * Construct a new watcher
	 */
	public ActiveEditorChangeWatcher() {		
		
	}
	
	/**
	 * Start watching the workbench. Will fire notification immediately for the currently selected editor.
	 * @param workbench Eclipse Workbench
	 */
	public void start(IWorkbench workbench) {
		this.initialActiveWindow = workbench.getActiveWorkbenchWindow();
		this.workbench = workbench;
		this.workbench.addWindowListener(windowListener);
		if (this.initialActiveWindow != null) {
			this.initialPage = this.initialActiveWindow.getActivePage();
			if (this.initialPage != null) {
				IEditorPart activeEditor = this.initialPage.getActiveEditor();
				if (activeEditor != null) {
					fireEditorVisibilityChanged(activeEditor);
				}
				this.initialPage.addPartListener(this.partListener);
			}
			this.initialActiveWindow.addPageListener(this.pageListener);
		}
	}
	
	/**
	 * Remove listeners. Must be called after {@link #start(IWorkbench)}
	 */
	public void dispose() {
		if (initialActiveWindow != null) {
			initialActiveWindow.removePageListener(pageListener);
		}
		if (initialPage != null) {
			initialPage.removePartListener(partListener);
		}
		if (workbench != null) {
			workbench.removeWindowListener(windowListener);
		}
	}
	
	/**
	 * Add listener
	 * @param listener Listener to receive notifications
	 */
	public void addEditorVisibilityListener(IActiveEditorChangedListener listener) {
		listeners.add(listener);
	}
	
	/**
	 * Remove listener
	 * @param listener Listener that shall no longer receive notifications
	 */
	public void removeEditorVisibilityListener(IActiveEditorChangedListener listener) {
		listeners.remove(listener);
	}
	
	private void fireEditorVisibilityChanged(IEditorPart editor) {
		fireEditorVisibilityChanged(new ActiveEditorChangedEvent(editor));
	}
	
	private void fireEditorVisibilityChanged(ActiveEditorChangedEvent event) {
		for (IActiveEditorChangedListener listener: listeners) {
			listener.activeEditorChanged(event);
		}
	}
}

class WindowListenerStub implements IWindowListener {
	@Override
	public void windowActivated(IWorkbenchWindow window) {}

	@Override
	public void windowDeactivated(IWorkbenchWindow window) {}

	@Override
	public void windowClosed(IWorkbenchWindow window) {}

	@Override
	public void windowOpened(IWorkbenchWindow window) {}	
}

class PartListenerStub implements IPartListener2 {
	@Override
	public void partActivated(IWorkbenchPartReference partRef) {}

	@Override
	public void partBroughtToTop(IWorkbenchPartReference partRef) {}

	@Override
	public void partClosed(IWorkbenchPartReference partRef) {}

	@Override
	public void partDeactivated(IWorkbenchPartReference partRef) {}

	@Override
	public void partOpened(IWorkbenchPartReference partRef) {}

	@Override
	public void partHidden(IWorkbenchPartReference partRef) {}

	@Override
	public void partVisible(IWorkbenchPartReference partRef) {}

	@Override
	public void partInputChanged(IWorkbenchPartReference partRef) {}	
}

class PageListenerStub implements IPageListener {
	@Override
	public void pageActivated(IWorkbenchPage page) {}

	@Override
	public void pageClosed(IWorkbenchPage page) {}

	@Override
	public void pageOpened(IWorkbenchPage page) {}	
}