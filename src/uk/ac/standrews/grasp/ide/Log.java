package uk.ac.standrews.grasp.ide;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

/**
 * Provides a cleaner interface to Eclipse's logging facilities
 * @author Dilyan Rusev
 * 
 * @see org.eclipse.core.runtime.ILog
 */
public final class Log {
	// utility class; no instances should be created
	private Log() {}
	
	/**
	 * Log an informational message with low severity
	 * @param message Text of the log entry
	 */
	public static void info(String message) {
		log(IStatus.INFO, IStatus.OK, message, null);
	}
	
	/**
	 * Log an exception with high severity
	 * @param exception Instance of an exception object
	 */
	public static void error(Throwable exception) {
		error("Unexpected Exception", exception);
	}
	
	/**
	 * Log an exception and provide additional information
	 * @param message Text that gives contextual information about the error (to that of the exception's message)
	 * @param exception Instance of an exception object
	 */
	public static void error(String message, Throwable exception) {
		log(IStatus.ERROR, IStatus.OK, message, exception);
	}
	
	// doc strings for parameters taken from IStatus
	/**
	 * Create and log a custom log entry
	 * @param severity the severity; one of <code>IStatus.OK</code>, <code>IStatus.ERROR</code>, 
	 * <code>IStatus.INFO</code>, <code>IStatus.WARNING</code>,  or <code>IStatus.CANCEL</code>
	 * @param code the plug-in-specific status code, or <code>IStatus.OK</code>
	 * @param message a human-readable message, localized to the
	 *    current locale
	 * @param exception a low-level exception, or <code>null</code> if not
	 *    applicable 
	 * 
	 * @see org.eclipse.core.runtime.IStatus
	 */
	public static void log(int severity, int code, String message, Throwable exception) {
		IStatus status = createStatus(severity, code, message, exception);
		logStatus(status);
	}
	
	/**
	 * Log a status object directly
	 * @param status Status object to log
	 * @see org.eclipse.core.runtime.IStatus
	 */
	public static void logStatus(IStatus status) {
		GraspPlugin.getDefault().getLog().log(status);
	}
	
	// look up org.eclipse.core.runtime.Status#Constructor(int,int,String,Throwable)
	private static IStatus createStatus(int severity, int code, String message, Throwable exception) {
		return new Status(severity, GraspPlugin.PLUGIN_ID, code, message, exception);
	}	
}
