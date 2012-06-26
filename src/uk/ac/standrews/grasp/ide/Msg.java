/**
 * 
 */
package uk.ac.standrews.grasp.ide;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

/**
 * Utility class for opening message boxes. Used when there is no <code>IStatus</code> or <code>Shell</code>
 * @author Dilyan Rusev
 *
 */
public final class Msg {
	private Msg() {
		// should not be instantiated
	}
	
	/**
	 * Show an error message. Use when you have no <code>IStatus</code>
	 * @param shell Parent shell
	 * @param title 
	 * 		the title to use for this dialog, or <code>null</code> to
	 * 		indicate that the default title should be used
	 * @param message 
	 * 		the message to show in this dialog, or <code>null</code> to
	 * 		indicate that the error's message should be shown as the
	 *      primary message 
	 * @param exception 
	 * 		the exception that cause the error. This object will be wrapped in an <code>IStatus</code>
	 * @see org.eclipse.jface.dialogs.ErrorDialog#openError(Shell, String, String, IStatus)
	 */
	public static void showError(Shell shell, String title, String message, Throwable exception) {
		IStatus status = errorStatus(message, exception);
		ErrorDialog.openError(getShell(), "Error", message, status);
	}
	
	/*
	 * Status for error message boxes
	 */
	private static IStatus errorStatus(String message, Throwable exception) {
		Throwable reason = exception;
		while (reason instanceof InvocationTargetException) {
			reason = reason.getCause();
		}
		return new Status(IStatus.ERROR, GraspActivator.PLUGIN_ID, message, reason);
	}
	
	/*
	 * Get a proper parent shell for the dialog
	 */
	private static Shell getShell() {
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		if (shell != null) {
			return shell;
		} else {
			return Display.getDefault().getActiveShell();
		}
	}
}
