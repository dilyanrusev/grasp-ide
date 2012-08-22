package uk.ac.standrews.grasp.ide.editors;

import org.eclipse.gef.palette.CombinedTemplateCreationEntry;
import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.PanningSelectionToolEntry;
import org.eclipse.gef.requests.SimpleFactory;

import uk.ac.standrews.grasp.ide.editParts.IconsCache;
import uk.ac.standrews.grasp.ide.model.ComponentModel;
import uk.ac.standrews.grasp.ide.model.ConnectorModel;
import uk.ac.standrews.grasp.ide.model.LayerModel;
import uk.ac.standrews.grasp.ide.model.LinkModel;
import uk.ac.standrews.grasp.ide.model.ProvidesModel;
import uk.ac.standrews.grasp.ide.model.RequiresModel;

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
		add(createInterfacesDrawer());
	}
	
	private PaletteContainer createToolsGroup() {
		PaletteGroup group = new PaletteGroup("Tools");
		PanningSelectionToolEntry selection = new PanningSelectionToolEntry();
		setDefaultEntry(selection);
		group.add(selection);
		return group;
	}
	
	private PaletteContainer createElementsDrawer() {
		PaletteDrawer elementsDrawer = new PaletteDrawer("Grasp Elements");
		
		// layer
		// component
		// connector
		// link
		
		CombinedTemplateCreationEntry entry;
		entry = new CombinedTemplateCreationEntry(
				"Layer" // label 
				, "Groups components, connectors and links" // description
				, LayerModel.class // template
				, new SimpleFactory(LayerModel.class) // creation factory
				, IconsCache.getDefault().getLayerDescriptor() // small pic
				, IconsCache.getDefault().getBigLayerDescriptor()); // big pic
		elementsDrawer.add(entry);
		
		entry = new CombinedTemplateCreationEntry(
				"Component" // label 
				, "Encapsulates functionality" // description
				, ComponentModel.class
				, new SimpleFactory(ComponentModel.class) // creation factory
				, IconsCache.getDefault().getComponentDescriptor() // small pic
				, IconsCache.getDefault().getBigComponentDescriptor()); // big pic
		elementsDrawer.add(entry);
		
		entry = new CombinedTemplateCreationEntry(
				"Connector" // label 
				, "Functional connection between components" // description
				, ConnectorModel.class
				, new SimpleFactory(ConnectorModel.class) // creation factory
				, IconsCache.getDefault().getConnectorDescriptor() // small pic
				, IconsCache.getDefault().getBigConnectorDescriptor()); // big pic
		elementsDrawer.add(entry);
		
		entry = new CombinedTemplateCreationEntry(
				"Link" // label 
				, "Connects provides/requires interfaces of components and connectors" // description
				, LinkModel.class
				, new SimpleFactory(LinkModel.class) // creation factory
				, IconsCache.getDefault().getLinkDescriptor() // small pic
				, IconsCache.getDefault().getBigLinkDescriptor()); // big pic
		elementsDrawer.add(entry);
		
		return elementsDrawer;
	}
	
	private PaletteContainer createInterfacesDrawer() {
		PaletteDrawer elementsDrawer = new PaletteDrawer("Component/connector Interfaces");
		
		// provides
		// requires		
		
		CombinedTemplateCreationEntry entry;
		entry = new CombinedTemplateCreationEntry(
				"Provides" // label 
				, "Exposes functionality to other components/connectors" // description
				, ProvidesModel.class // template
				, new SimpleFactory(ProvidesModel.class) // creation factory
				, IconsCache.getDefault().getProvidesDescriptor() // small pic
				, IconsCache.getDefault().getBigProvidesDescriptor()); // big pic
		elementsDrawer.add(entry);
		
		entry = new CombinedTemplateCreationEntry(
				"Requires" // label 
				, "Imports functionality from other components/connectors" // description
				, RequiresModel.class
				, new SimpleFactory(RequiresModel.class) // creation factory
				, IconsCache.getDefault().getRequiresDescriptor() // small pic
				, IconsCache.getDefault().getBigRequiresDescriptor()); // big pic
		elementsDrawer.add(entry);
		
		return elementsDrawer;
	}
}
