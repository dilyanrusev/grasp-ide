/**
 * 
 */
package uk.ac.standrews.grasp.ide.builder;

import java.io.IOException;

import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;

import shared.logging.ILogger;
import uk.ac.standrews.grasp.ide.GraspPlugin;
import uk.ac.standrews.grasp.ide.Log;

/**
 * Forwards logging messages to the Eclipse Console View. Ignores severity level and prints all messages.
 * @author Dilyan Rusev
 */
final class GraspCompilationLogger implements ILogger {		
	private boolean debug;
	private MessageConsoleStream messageStream;
	
	/**
	 * Creates a new instance. Call <code>initialize<code> 
	 * before calling any methods, and invoke <code>shutDown</code> when you are done with the logger.
	 * 
	 * @see #initialize(String, shared.logging.ILogger.Level, boolean)
	 * @see #shutdown()
	 */
	public GraspCompilationLogger() {		
		this.debug = false;	
	}
	
	@Override
	public ILogger initialize(String name, Level level, boolean debug) {	
		// ignore name (do not create multiple consoles)
		// ignore level - print all messages regardless of severity
		this.debug = debug;	
		
		MessageConsole console = GraspPlugin.getDefault().getConsole("Grasp compiler output");	
		this.messageStream = console.newMessageStream();
		
		return this;
	}

	@Override
	public void shutdown() {
		try {
			this.messageStream.close();
		} catch (IOException e) {
			Log.error(e);
		}
	}
	
	@Override
	public void compiler_error(String format, Object... args) {
		// always print errors
		String message = String.format(format, args);
		printToConsole("COMPILER_ERROR", message);
	}

	@Override
	public void compiler_warn(String format, Object... args) {
		// always print warnings
		String message = String.format(format, args);
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
		printToConsoleFormatted("TRACE", format, args);
	}

	@Override
	public void warn(String format, Object... args) {
		printToConsoleFormatted("TRACE", format, args);
	}
	
	private void printToConsoleFormatted(String source, String format, Object... args) {
		String message = String.format(format, args);
		printToConsole(source, message);
	}
	
	private void printToConsole(String source, String message) {
		String txt;
		if (debug) {
			txt = "!" + source + ": " + message;
		} else {
			txt = message;
		}
		this.messageStream.println(txt);
	}
}
