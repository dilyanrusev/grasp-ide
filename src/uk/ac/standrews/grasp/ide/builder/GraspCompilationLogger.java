/**
 * 
 */
package uk.ac.standrews.grasp.ide.builder;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;

import shared.logging.ILogger;
import uk.ac.standrews.grasp.ide.Log;

/**
 * Forwards compilation messages to the problem, error and console views. Creates markers for errors/warnings
 * @author Dilyan Rusev
 */
final class GraspCompilationLogger implements ILogger {
	private final IFile file;
	private final String problemMarkerTypeId;
	
	public GraspCompilationLogger(IFile file, String problemMarkerTypeId) {
		this.file = file;
		this.problemMarkerTypeId = problemMarkerTypeId;
	}
	
	@Override
	public ILogger initialize(String name, Level level, boolean debug) {
		// ignore... all messages are going to be forwarded
		return this;
	}

	@Override
	public void shutdown() {
		// noting to do
	}
	
	@Override
	public void compiler_error(String format, Object... args) {
		String message = String.format(format, args);
		int lineNumber = extractLineNumber(message);
		createErrorMarker(message, lineNumber);
		printToConsole("COMPILER_ERROR", message);
	}

	@Override
	public void compiler_warn(String format, Object... args) {
		String message = String.format(format, args);
		int lineNumber = extractLineNumber(message);
		createWarningMarker(message, lineNumber);
		printToConsole("COMPILER_WARNING", message);
	}

	@Override
	public void error(String format, Object... args) {
		String message = String.format(format, args);
		error(message, (String)null);
	}

	@Override
	public void error(String message, Exception exception) {
		final String NEW_LINE = System.getProperty("line.separator");
		StringBuilder sb = new StringBuilder();
		sb.append(message);
		
		Throwable cause = exception;
		while (cause != null) {
			sb.append(NEW_LINE);
			sb.append('\t');
			String name = cause.getClass().getSimpleName();
			sb.append(name != null ? name : "<anonymous exception>");
			String causeMessage = cause.getMessage();
			if (causeMessage != null) {
				sb.append(": ");
				sb.append(causeMessage);
			}
			cause = cause.getCause();
		}
		printToConsole("ERROR", sb.toString());
	}

	@Override
	public void info(String format, Object... args) {
		printToConsoleFormatted("INFO", format, args);
	}	
	
	@Override
	public void print() {
		printToConsole("PRINT", "");
	}

	@Override
	public void print(String format, Object... args) {
		printToConsoleFormatted("PRINT", format, args);
	}

	@Override
	public void trace(String format, Object... args) {
		printToConsoleFormatted("TRANCE", format, args);
	}

	@Override
	public void warn(String format, Object... args) {
		printToConsoleFormatted("TRANCE", format, args);
	}
	
	private void printToConsoleFormatted(String source, String format, Object... args) {
		String message = String.format(format, args);
		printToConsole(source, message);
	}
	
	private void printToConsole(String source, String message) {
		String txt = "!" + source + ": " + message;
		System.out.println(txt);
	}
	
	private int extractLineNumber(String message) {
		// TODO: Implement
		return -1;		
	}
	
	private void createErrorMarker(String message, int lineNumber) {
		createMarker(message, lineNumber, IMarker.SEVERITY_ERROR);
	}
	
	private void createWarningMarker(String message, int lineNumber) {		
		createMarker(message, lineNumber, IMarker.SEVERITY_WARNING);
	}
	
	private void createMarker(String message, int lineNumber, int severity) {
		try {
			IMarker marker = file.createMarker(problemMarkerTypeId);
			marker.setAttribute(IMarker.MESSAGE, message);
			marker.setAttribute(IMarker.SEVERITY, severity);
			if (lineNumber == -1) {
				lineNumber = 1;
			}
			marker.setAttribute(IMarker.LINE_NUMBER, lineNumber);
		} catch (CoreException e) {
			Log.error(e);
		}
	}

}
