package uk.ac.standrews.grasp.ide.compiler;

import org.eclipse.core.resources.IFile;

/**
 * Abstraction over the Grasp compiler. Can be either the one configured through the 
 * plugin preferences, or the integrated (fallback) compiler
 * @author Dilyan Rusev
 *
 */
public interface ICompiler {
	/**
	 * Compile from a file
	 * @param file File to compile
	 * @param options Specify compilation options. Cannot be <code>null</code>
	 * @return Whether the compilation was successful, and the errors in case it wasn't
	 */
	CompilationResult compile(IFile file, CompilationOptions options);
}
