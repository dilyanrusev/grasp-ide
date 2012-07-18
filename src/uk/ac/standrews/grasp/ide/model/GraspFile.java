package uk.ac.standrews.grasp.ide.model;

import grasp.lang.Compiler;
import grasp.lang.IArchitecture;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.Assert;

import shared.error.IErrorReport;
import uk.ac.standrews.grasp.ide.builder.NullLogger;
import uk.ac.standrews.grasp.ide.builder.GraspSourceFile;


public class GraspFile {
	private IFile file;
	private IErrorReport errors;
	private GraspArchitecture architecture;
	
	public GraspFile(IFile file) {
		Assert.isNotNull(file);
		this.file = file;
		this.errors = null;
		this.architecture = null;
	}
	
	public boolean compile() {
		Compiler compiler = new Compiler();
		IArchitecture originalArch = compiler.compile(new GraspSourceFile(file), NullLogger.INSTANCE);
		if (originalArch != null) {
			architecture = new GraspArchitecture(originalArch);
		}
		errors = compiler.getErrors();
		return originalArch != null && !errors.isAny();
	}
	
	public IFile getFile() {
		return file;
	}
	
	public IErrorReport getErrors() {
		return errors;
	}
	
	public boolean compiledSuccesfully() {
		return errors != null && !errors.isAny() && architecture != null;
	}
	
	public GraspArchitecture getArchitecture() {
		return architecture;
	}
	
	@Override
	public String toString() {
		return file.toString();
	}
	
	@Override
	public int hashCode() {
		return file.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (!(obj instanceof GraspFile)) return false;
		GraspFile other = (GraspFile)obj;
		return file.equals(other.file);
	}
}

class GraspArchitecture {
	public GraspArchitecture() {
		
	}
	
	public GraspArchitecture(IArchitecture other) {
		
	}
}
