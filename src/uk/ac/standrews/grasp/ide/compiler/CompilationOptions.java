package uk.ac.standrews.grasp.ide.compiler;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;

public class CompilationOptions {
	private String consoleName;
	private IFile xmlFile; 
	private SubMonitor monitor;
	
	public CompilationOptions() {
		this.consoleName = "Grasp compiler output";
		this.monitor = SubMonitor.convert(new NullProgressMonitor());
	}
	
	public CompilationOptions setConsoleName(String consoleName) {
		this.consoleName = consoleName;
		return this;
	}
	
	public CompilationOptions setXmlFile(IFile xmlFile) {
		this.xmlFile = xmlFile;
		return this;
	}
	
	public CompilationOptions setProgressMonitor(SubMonitor monitor) {
		this.monitor = monitor;
		return this;
	}
	
	public String getConsoleName() {
		return consoleName;
	}
	
	public IFile getXmlFile() {
		return xmlFile;
	}
	
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
