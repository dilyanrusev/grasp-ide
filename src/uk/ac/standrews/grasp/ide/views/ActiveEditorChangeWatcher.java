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

public class ActiveEditorChangeWatcher {
	private IWorkbench workbench;
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
	
	
	public ActiveEditorChangeWatcher(IWorkbench workbench) {
		workbench.addWindowListener(windowListener);
		this.workbench = workbench;
	}
	
	public void dispose() {
		this.workbench.removeWindowListener(windowListener);
	}
	
	public void addEditorVisibilityListener(IActiveEditorChangedListener listener) {
		listeners.add(listener);
	}
	
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