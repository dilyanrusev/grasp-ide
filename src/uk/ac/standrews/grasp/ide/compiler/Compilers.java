package uk.ac.standrews.grasp.ide.compiler;

import uk.ac.standrews.grasp.ide.preferences.Preferences;

/**
 * Helper class to retrieve the currently configured compiler
 * @author Dilyan Rusev
 *
 */
public final class Compilers {
	private static final ICompiler INTEGRATED = new IntegratedCompiler();
	private static final ICompiler EXTERNAL = new ExternalCompiler();
	
	/**
	 * Get the currently configured compiler
	 * @return
	 */
	public static ICompiler getCurrent() {
		return Preferences.isIntegratedCompilerEnabled() ? INTEGRATED : EXTERNAL;
	}
}
