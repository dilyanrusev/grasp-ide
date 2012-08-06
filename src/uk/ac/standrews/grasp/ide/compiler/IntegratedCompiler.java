package uk.ac.standrews.grasp.ide.compiler;

import grasp.lang.Compiler;
import grasp.lang.IArchitecture;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;

import shared.error.IError;
import shared.error.IErrorReport;
import shared.io.ISource;
import shared.logging.ILogger;
import shared.logging.ILogger.Level;
import shared.xml.DomXmlWriter;
import shared.xml.IXmlWriter;
import uk.ac.standrews.grasp.ide.GraspPlugin;
import uk.ac.standrews.grasp.ide.Log;
import uk.ac.standrews.grasp.ide.model.GraspModel;

/**
 * Represents the integrated (fall-back) Grasp compiler
 * @author Dilyan Rusev
 *
 */
public class IntegratedCompiler implements ICompiler {

	@Override
	public CompilationResult compile(IFile file, CompilationOptions options) {
		ILogger logger;
		if (options.getConsoleName() != null) {
			logger = new GraspCompilationLogger(options.getConsoleName()).initialize(file.getName(), Level.ERROR, false);
		} else {
			logger = NullLogger.INSTANCE;
		}
		return doCompile(file, new GraspSourceFile(file), logger, options);
	}

	@Override
	public CompilationResult compile(String text, CompilationOptions options) {
		return doCompile(null, new GraspStringSource("<memory>", "<none>", text), NullLogger.INSTANCE, options);
	}
	
	private CompilationResult doCompile(IFile file, ISource source, ILogger logger, CompilationOptions options) {
		Compiler compiler = new Compiler();
		IArchitecture graph = compiler.compile(source, logger);
		SubMonitor progress = options.getProgressMonitor();
		IFile xmlFile = options.getXmlFile();	
		boolean buildXml = file != null && xmlFile != null;		
		CompilationResult result = new CompilationResult();
		
		try {
			progress.worked(1);
			
			GraspModel.INSTANCE.ensureFileStats(file).compiled(result);
	
			IErrorReport errorReport = compiler.getErrors();
			List<CompilationError> errors = new ArrayList<CompilationError>();
			if (errorReport.isAny()) {
				for (IError compilerError: errorReport.getErrors()) {
					errors.add(new CompilationError()
						.setMessage(compilerError.getMessage())
						.setLocation(
								compilerError.getLine() >= 1 ? compilerError.getLine() : 1, 
								compilerError.getColumn() + 1, 
								compilerError.getColumnEnd() + 2));
				}
			}
			result.setErrors(errors);
			if (!errorReport.isAny() && graph != null && buildXml) {				
				progress.setTaskName("Building xml for " + file);
				IProject project = file.getProject();
				if (project == null) {
					Log.error("Cannot find project of " + file, null);
					return result;
				}
				try {	
					StringWriter stringWriter = new StringWriter();
					BufferedWriter output = new BufferedWriter(stringWriter);
					IXmlWriter xml = new DomXmlWriter();
					graph.toXml(xml);
					xml.serialize(output);
					output.close();
					String txt = stringWriter.toString();	
					if (!xmlFile.exists()) {
						xmlFile.create(new ByteArrayInputStream(txt.getBytes("utf-8")), true, progress.newChild(1));
					}
					else {
						xmlFile.setContents(new ByteArrayInputStream(txt.getBytes("utf-8")), true, true, progress.newChild(1));
					}
					xmlFile.setDerived(true, progress.newChild(1));
					GraspModel.INSTANCE.ensureFileStats(file).refreshFromXml(xmlFile);					
					result.setXmlBuilt(true);
				} catch (CoreException e) {
					Log.error(e);
				} catch (FileNotFoundException e) {
					Log.error(e);
				} catch (IOException e) {
					Log.error(e);
				} catch (ParserConfigurationException e) {
					Log.error(e);
				} catch (TransformerException e) {
					Log.error(e);
				}				
			}
		} finally {
			logger.shutdown();
		}
		return result;
	}
}

/**
 * Adapts <code>IFile</code> to <code>ISource</code>
 * @author Dilyan Rusev
 *
 * @see org.eclipse.core.resources.IFile
 * @see shared.io.ISource
 */
final class GraspSourceFile implements ISource {
	private IFile adapted;
	
	/**
	 * Creates a new grasp source file
	 * @param adapted Eclipse file resource to adapt
	 */
	public GraspSourceFile(IFile adapted) {
		Assert.isNotNull(adapted);
		this.adapted = adapted;
	}

	@Override
	public String getFullName() {
		return adapted.getFullPath().toOSString();
	}

	@Override
	public String getName() {
		return adapted.getName();
	}

	@Override
	public Reader getReader() throws IOException {
		InputStream contents;
		try {
			contents = adapted.getContents();
		} catch (CoreException e) {
			throw new IOException(e);
		}
		Reader reader = new InputStreamReader(contents);
		return reader;
	}

	@Override
	public String toString() {
		return adapted.getName();
	}
}

final class GraspStringSource implements ISource {
	private String name;
	private String fullName;
	private String contents;
	
	public GraspStringSource(String name, String fullName, String contents) {
		this.name = name;
		this.fullName = fullName;
		this.contents = contents;
	}

	@Override
	public String getFullName() {
		return fullName;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Reader getReader() throws IOException {
		return new StringReader(contents);
	}
	
	@Override
	public String toString() {
		return name;
	}

}

/**
 * Forwards logging messages to the Eclipse Console View. Ignores severity level and prints all messages.
 * @author Dilyan Rusev
 */
final class GraspCompilationLogger implements ILogger {		
	private boolean debug;
	private MessageConsoleStream messageStream;
	private String consoleName;
	
	/**
	 * Creates a new instance. Call <code>initialize<code> 
	 * before calling any methods, and invoke <code>shutDown</code> when you are done with the logger.
	 * 
	 * @see #initialize(String, shared.logging.ILogger.Level, boolean)
	 * @see #shutdown()
	 */
	public GraspCompilationLogger(String consoleName) {		
		this.debug = false;	
		this.consoleName = consoleName;		
	}
	
	@Override
	public ILogger initialize(String name, Level level, boolean debug) {	
		// ignore name (do not create multiple consoles)
		// ignore level - print all messages regardless of severity
		this.debug = debug;	
		
		MessageConsole console = GraspPlugin.getDefault().getConsole(consoleName);	
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
		if (!messageStream.isClosed()) {
			messageStream.println(txt);
		}
	}
}


final class NullLogger implements ILogger {
	public static final NullLogger INSTANCE = new NullLogger();	
	
	@Override
	public void compiler_error(String s, Object... aobj) {}

	@Override
	public void compiler_warn(String s, Object... aobj) {}

	@Override
	public void error(String s, Object... aobj) {}

	@Override
	public void error(String s, Exception exception) {}

	@Override
	public void info(String s, Object... aobj) {}

	@Override
	public ILogger initialize(String s, Level level, boolean flag) { return this ;}

	@Override
	public void print() {}

	@Override
	public void print(String s, Object... aobj) {}

	@Override
	public void shutdown() {}

	@Override
	public void trace(String s, Object... aobj) {}

	@Override
	public void warn(String s, Object... aobj) {}
}
