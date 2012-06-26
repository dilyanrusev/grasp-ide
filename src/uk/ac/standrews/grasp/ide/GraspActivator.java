package uk.ac.standrews.grasp.ide;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.resource.ImageDescriptor;
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
public class GraspActivator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "uk.ac.standrews.grasp.ide"; //$NON-NLS-1$
	
	// The shared instance
	private static GraspActivator plugin;
	
	private List<String> createdConsoleNames;
	
	/**
	 * The constructor
	 */
	public GraspActivator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		createdConsoleNames = new ArrayList<String>(1);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		disposeOfConsoles();		
		plugin = null;
		super.stop(context);
		
	}
	

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static GraspActivator getDefault() {
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
		createdConsoleNames.add(name);
		consoleManager.addConsoles(new IConsole[] { newConsole });
		return newConsole;
	}
	
	private void disposeOfConsoles() {
		IConsoleManager consoleManager = ConsolePlugin.getDefault().getConsoleManager();		
		List<IConsole> toRemove = new ArrayList<IConsole>(createdConsoleNames.size());
		for (String name: createdConsoleNames) {
			IConsole existing = findConsole(consoleManager, name);
			if (existing != null) {
				toRemove.add(existing);
			}
		}
		consoleManager.removeConsoles(toRemove.toArray(new IConsole[toRemove.size()]));
	}
}
