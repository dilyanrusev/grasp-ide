package uk.ac.standrews.grasp.ide.editors;

import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.PanningSelectionToolEntry;

/**
 * Implements the default palette for {@link GraspDesigner}
 * @author Dilyan Rusev
 *
 */
public class DesignerPalette extends PaletteRoot {
	private static DesignerPalette INSTANCE;
	
	/**
	 * Returns the singleton instance
	 * @return
	 */
	public static DesignerPalette getDefault() {
		if (INSTANCE == null) {
			INSTANCE = new DesignerPalette();
		}
		return INSTANCE;
	}
	
	private DesignerPalette() {
		add(createToolsGroup());
		add(createElementsDrawer());
	}
	
	private PaletteContainer createToolsGroup() {
		PaletteGroup group = new PaletteGroup("Tools");
		PanningSelectionToolEntry selection = new PanningSelectionToolEntry();
		setDefaultEntry(selection);
		group.add(selection);
		return group;
	}
	
	private PaletteContainer createElementsDrawer() {
		PaletteDrawer elemntsDrawer = new PaletteDrawer("Grasp Elements");
		return elemntsDrawer;
	}
}
