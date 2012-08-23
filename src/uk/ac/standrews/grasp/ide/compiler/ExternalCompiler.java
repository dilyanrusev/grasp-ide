package uk.ac.standrews.grasp.ide.compiler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;

import uk.ac.standrews.grasp.ide.GraspPlugin;
import uk.ac.standrews.grasp.ide.Log;
import uk.ac.standrews.grasp.ide.model.GraspModel;
import uk.ac.standrews.grasp.ide.preferences.Preferences;

class ExternalCompiler implements ICompiler {
	// file_name [line_number, column_start:column_end]: message
	private static final Pattern ERROR_PATTERN = 
			Pattern.compile("\\[(\\d+), +(\\d+) *: *(\\d+)\\] *:(.+)");

	@Override
	public CompilationResult compile(IFile file, CompilationOptions options) {
		MessageConsoleStream log = null;
		try {			
			CompilationResult result = new CompilationResult();
			MessageConsole console =
					options.getConsoleName() != null
					? GraspPlugin.getDefault().getConsole(options.getConsoleName())
					: null;
			log = console != null ? console.newMessageStream() : null;
			Process compiler = runExternalCompiler(file, options, log);
			compiler.waitFor();
			List<CompilationError> errors = parseCompilerOutput(file, compiler, log); 
			result.setErrors(errors);
			GraspModel.INSTANCE.ensureFileStats(file).compiled(result);
			if (options.getXmlFile() != null) {
				try {
					IFile xmlFile = options.getXmlFile();					
					xmlFile.refreshLocal(1, null);										
					result.setXmlBuilt(true);
					GraspModel.INSTANCE.ensureFileStats(file).refreshFromXml(xmlFile);
				} catch (CoreException e) {	
					Log.error(e);
				}
			}
			return result;
		} catch (IOException e) {
			return buildResultFromException(e, log);
		} catch (InterruptedException e) {
			return buildResultFromException(e, log);
		} finally {
			if (log != null) {
				try {
					log.close();
				} catch (IOException e) {
					Log.error(e);
				}
			}
		}
	}
	
	private static CompilationResult buildResultFromException(Exception e,
			MessageConsoleStream log) {
		if (log != null) {
			log.println("Error while invoking compiler");
		}
		RuntimeException wrapper = 
				new RuntimeException("Cannot invoke external compiler located at \"" + 
						Preferences.getExternalCompilerPath() + "\". " +
						"Here follows a list of errors that might help diagnose the problem", 
						e);
		CompilationResult res = new CompilationResult();
		List<CompilationError> list = new ArrayList<CompilationError>(1);
		Throwable current = wrapper;
		while (current != null) {
			list.add(new CompilationError().setMessage(current.getMessage()));
			current = current.getCause();
		}
		res.setErrors(list);
		return res;
	}
	
	private static List<CompilationError> parseCompilerOutput(IFile file, 
			Process compiler, MessageConsoleStream log) throws IOException  {
		List<CompilationError> errors = new ArrayList<CompilationError>();				
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(compiler.getInputStream()));
		String line;		
		int colonIdx;
		String fileName = file.getName();
			
		while ((line = reader.readLine()) != null) {
			if (log != null) {
				log.println(line);
			}
			colonIdx = line.indexOf(':');
			Matcher m = ERROR_PATTERN.matcher(line);
			if (m.find()) {
				int lineNumber = Integer.parseInt(m.group(1));
				int column = Integer.parseInt(m.group(2));
				int columnEnd = Integer.parseInt(m.group(3));
				String message = m.group(4).trim();
				errors.add(new CompilationError()
					.setMessage(message)
					.setLocation(lineNumber, column, columnEnd + 1));
			} else if (colonIdx != -1 && line.length() > colonIdx) {
				String start = line.substring(0, colonIdx);				
				if (start.trim().equals(fileName)) {
					// looks like a more relaxed error
					String message = line.substring(colonIdx + 1).trim();
					errors.add(new CompilationError().setMessage(message));
				}
			}			
		}		
		
		return errors;
	}
	
	
	private static Process runExternalCompiler(IFile file, CompilationOptions options,
			MessageConsoleStream log) throws IOException {
		String java;
		if (System.getProperty("os.name").contains("Windows")) {
			java = "javaw.exe";
		} else {
			java = "java";
		}
		String argJar = "-jar";
		String argCompiler = Preferences.getExternalCompilerPath();
		assertExternalCompiler(argCompiler);		
		String argInput = file.getLocation().toOSString();
		String argOutput =
				options.getXmlFile() != null 
				? options.getXmlFile().getLocation().toOSString()
				: file.getLocation().addFileExtension("xml").toOSString();		
		String[] command = { java, argJar, argCompiler, argInput, argOutput }; 
		return new ProcessBuilder(command).redirectErrorStream(true).start();
	}

	
	private static void assertExternalCompiler(String compiler) throws IOException {
		if (compiler == null) {
			throw new IOException("External compiler has not been configured");
		} else {
			File jar = new File(compiler);
			if (!jar.exists()) {
				throw new IOException("Cannot find external compiler \"" + jar + "\"");
			} 
			if (!jar.getAbsolutePath().endsWith(".jar")) {
				throw new IOException("External compiler is not a .jar");
			}
		}
	}
}
