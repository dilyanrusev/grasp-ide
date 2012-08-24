package uk.ac.standrews.grasp.ide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import uk.ac.standrews.grasp.ide.editParts.IconsCache;
import uk.ac.standrews.grasp.ide.model.GraspModel;

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
	/** ID of the Grasp Designer editor */
	public static final String ID_GRASP_DESIGNER = "uk.ac.standrews.grasp.ide.editors.GraspDesigner";
	/** Builder ID, as per plugin.xml */
	public static final String ID_BUILDER = "uk.ac.standrews.grasp.ide.graspBuilder";
	/** Problem and text marker ID used to report compilation errors */
	public static final String ID_PROBLEM_MARKER = "uk.ac.standrews.grasp.ide.graspProblem";	
	
	// The shared instance
	private static GraspPlugin plugin;
	
	private Map<RGB, Color> colours;	
	private List<Runnable> pluginCloseTasks;
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;		
		colours = new HashMap<RGB, Color>();
		pluginCloseTasks = new ArrayList<Runnable>();
		GraspModel.INSTANCE.init();		
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		for (Runnable task: pluginCloseTasks) {
			task.run();			
		}
		pluginCloseTasks.clear();
		IconsCache.getDefault().dispose();
		GraspModel.INSTANCE.dispose();
		disposeOfColours();
		plugin = null;
		super.stop(context);
		
	}	
	
	/**
	 * Execute a task when {@link #stop(BundleContext)} is called
	 * @param task Task to execute
	 */
	public void executeAtPluginStop(Runnable task) {
		pluginCloseTasks.add(task);
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
