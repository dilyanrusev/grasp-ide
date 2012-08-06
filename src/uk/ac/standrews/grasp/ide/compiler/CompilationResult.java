package uk.ac.standrews.grasp.ide.compiler;

import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.Assert;

/**
 * Reports whether the compilation was successful
 * @author Dilyan Rusev
 *
 */
public class CompilationResult {	
	private boolean xmlBuilt;
	private List<CompilationError> errors;
	
	public CompilationResult() {
		errors = Collections.emptyList();
	}	
	
	CompilationResult setXmlBuilt(boolean xmlBuilt) {
		this.xmlBuilt = xmlBuilt;
		return this;
	}
	
	CompilationResult setErrors(List<CompilationError> errors) {
		Assert.isNotNull(errors);
		this.errors = errors;
		return this;
	}
	
	/**
	 *  Errors reported by the compiler
	 * @return
	 */
	public List<CompilationError> getErrors() {
		return errors;
	}
	
	/**
	 * Reports whether or not the compilation was successful (does not include XML generation)
	 * @return
	 */
	public boolean isSuccessful() {
		return errors.size() == 0;
	}
	
	/**
	 * Whether or not the compiler was able to produce XML
	 * @return
	 */
	public boolean isXmlBuilt() {
		return xmlBuilt;
	}
}
