package uk.ac.standrews.grasp.ide.model;

import grasp.lang.elements.Architecture;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.Assert;

/**
 * Root-level for the grasp model
 * @author Dilyan Rusev
 *
 */
public final class GraspManager {
	/** Lists all keywords in Grasp */
	public static final Set<String> KEYWORDS;
	
	private static final GraspManager INSTANCE;	
	
	private Map<IFile, Architecture> fileToArchitecture;
	
	static {
		INSTANCE = new GraspManager();
		KEYWORDS = Collections.unmodifiableSet(new HashSet<String>(Arrays.asList(new String[] {
				"architecture", "requirement", "quality_attribute", "property",
				"rationale", "reason", "template", "system", "layer", "over",
				"component", "connector", "provides", "requires", "check",
				"link", "extends", "because", "supports", "inhibits", "to", 
				"true", "false"
		})));
	}
	

	// singleton
	private GraspManager() {
		fileToArchitecture = new HashMap<IFile, Architecture>();
	}
	
	/**
	 * Retrieve the singleton instance
	 * @return The only instance
	 */
	public static GraspManager get() { 
		return INSTANCE;
	}
	
	/**
	 * Invoke when a file is changed to associate architecture model with a file
	 * @param compiledUnit Source code file that was compiled
	 * @param architecture Architectural model
	 */
	public void updateFileArchitecture(IFile compiledUnit, Architecture architecture) {
		Assert.isNotNull(compiledUnit);
		Assert.isNotNull(architecture);
		
		fileToArchitecture.put(compiledUnit, architecture);
	}
	
	/**
	 * Invoke when a file has been removed
	 * @param sourceFile Source code file that was compiled
	 */
	public void handleFileRemoved(IFile sourceFile) {
		Assert.isNotNull(sourceFile);
		fileToArchitecture.remove(sourceFile);
	}
}
