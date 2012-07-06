package uk.ac.standrews.grasp.ide;

import grasp.lang.IArchitecture;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 * 
 * @author Dilyan Rusev
 */
public class GraspPlugin extends AbstractUIPlugin {

	/** Plug-in ID */
	public static final String PLUGIN_ID = "uk.ac.standrews.grasp.ide"; //$NON-NLS-1$
	/** ID of the Grasp content type */
	public static final String ID_GRASP_CONTENT_TYPE = "uk.ac.standrews.grasp.ide.graspSourceFile";
	/** ID of the multi-page Grasp Editor */
	public static final String ID_GRASP_EDITOR = "uk.ac.standrews.grasp.ide.editors.GraspEditor";
	/** ID of the Grasp source code editor */
	public static final String ID_GRASP_TEXT_EDITOR = "uk.ac.standrews.grasp.ide.editors.GraspTextEditor";
	/** Builder ID, as per plugin.xml */
	public static final String ID_BUILDER = "uk.ac.standrews.grasp.ide.graspBuilder";
	/** Problem and text marker ID used to report compilation errors */
	public static final String ID_PROBLEM_MARKER = "uk.ac.standrews.grasp.ide.graspProblem";	
	
	// The shared instance
	private static GraspPlugin plugin;
	private static Map<IFile, IArchitecture> architectures = 
			Collections.synchronizedMap(new HashMap<IFile, IArchitecture>());
	
	private Map<RGB, Color> colours;
	
	
	/**
	 * The constructor
	 */
	public GraspPlugin() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;		
		colours = new HashMap<RGB, Color>();
		architectures.clear();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {	
		disposeOfColours();
		plugin = null;
		super.stop(context);
		
	}	

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static GraspPlugin getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}	
	
	/**
	 * Retrieve the compiled architecture for a file
	 * @param file File whose architecture is requested
	 * @return Compiled architecture for the file or null
	 */
	public static IArchitecture getFileArchitecture(IFile file) {
		return file != null ? architectures.get(file) : null;
	}
	
	/**
	 * Associate an architecture with a file
	 * @param file File that was compiled
	 * @param architecture AST of the file
	 */
	public static void setFileArchitecture(IFile file, IArchitecture architecture) {
		architectures.put(file, architecture);
	}
	
	/**
	 * Get or create a console by name
	 * @param name Name of the console
	 * @return Console
	 */
	public MessageConsole getConsole(String name) {
		IConsoleManager consoleManager = ConsolePlugin.getDefault().getConsoleManager();
		
		MessageConsole existing = findConsole(consoleManager, name);
		if (existing != null) {
			return existing;
		} else {
			return createConsole(consoleManager, name);
		}	
	}	
	
	private MessageConsole findConsole(IConsoleManager consoleManager, String name) {
		for (IConsole existing: consoleManager.getConsoles()) {
			if (existing instanceof MessageConsole && name.equals(existing.getName())) {
				return (MessageConsole) existing;
			}
		}
		return null;
	}
	
	private MessageConsole createConsole(IConsoleManager consoleManager, String name) {
		MessageConsole newConsole = new MessageConsole(name, null);		
		consoleManager.addConsoles(new IConsole[] { newConsole });
		return newConsole;
	}	
	
	/**
	 * Retrieve a colour that will be disposed when the plug-in closes
	 * @param rgb Red, Green and Blue components of the desired colour
	 * @return Cached colour that will be disposed of when the plug-in shuts down
	 */
	public Color getColour(RGB rgb) {
		Assert.isNotNull(rgb);
		Color color = colours.get(rgb);
		if (color == null) {
			color = new Color(Display.getDefault(), rgb);
			colours.put(rgb, color);
		}
		return color;
	}
	
	/**
	 * Retrieve a colour that will be disposed when the plug-in shuts down
	 * @param red Red component
	 * @param green Green component
	 * @param blue Blue component
	 * @return Cached colour that will be disposed of when the plug-in shuts down
	 */
	public Color getColour(int red, int green, int blue) {
		return getColour(new RGB(red, green, blue));
	}
	
	private void disposeOfColours() {
		for (Color color: colours.values()) {
			color.dispose();
		}
		colours.clear();
	}
}
