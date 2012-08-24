package uk.ac.standrews.grasp.ide.compiler;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;

/**
 * Options for invoking the compiler
 * @author Dilyan Rusev
 *
 */
public class CompilationOptions {
	private String consoleName;
	private IFile xmlFile; 
	private SubMonitor monitor;
	
	/**
	 * Constructs the default options
	 */
	public CompilationOptions() {
		this.consoleName = "Grasp compiler output";
		this.monitor = SubMonitor.convert(new NullProgressMonitor());
	}
	
	/**
	 * Set the Eclipse console name
	 * @param consoleName New name
	 * @return This
	 */
	public CompilationOptions setConsoleName(String consoleName) {
		this.consoleName = consoleName;
		return this;
	}
	
	/**
	 * Set the file to use for the compiled XML output
	 * @param xmlFile New xml file
	 * @return This
	 */
	public CompilationOptions setXmlFile(IFile xmlFile) {
		this.xmlFile = xmlFile;
		return this;
	}
	
	/**
	 * Set progress monitor to use during compilation
	 * @param monitor Progress monitor
	 * @return This
	 */
	public CompilationOptions setProgressMonitor(SubMonitor monitor) {
		this.monitor = monitor;
		return this;
	}
	
	/**
	 * Return the eclipse console name used for printing the compiler's output
	 * @return Console name
	 */
	public String getConsoleName() {
		return consoleName;
	}
	
	/**
	 * Get the XML file used for storing the model
	 * @return File
	 */
	public IFile getXmlFile() {
		return xmlFile;
	}
	
	/**
	 * Progress monitor for compiler output
	 * @return Progress monitor
	 */
	public SubMonitor getProgressMonitor() {
		return monitor;
	}
	
	@Override
	public String toString() {
		return new StringBuilder().append("console=").append(consoleName)
				.append("; xml=").append(xmlFile)
				.append("; monitor=").append(monitor)
				.toString();
	}
}
