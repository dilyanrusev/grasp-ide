package uk.ac.standrews.grasp.ide.editors;

import org.eclipse.gef.palette.CombinedTemplateCreationEntry;
import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.PanningSelectionToolEntry;
import org.eclipse.gef.requests.CreationFactory;

import uk.ac.standrews.grasp.ide.editParts.IconsCache;
import uk.ac.standrews.grasp.ide.model.ElementType;

/**
 * Implements the default palette for {@link GraspDesigner}
 * @author Dilyan Rusev
 *
 */
public class DesignerPalette extends PaletteRoot {
	private static DesignerPalette INSTANCE;
	
	/**
	 * Returns the singleton instance
	 * @return Singleton
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
				, ElementType.LAYER // template
				, new Factory(ElementType.LAYER) // creation factory
				, IconsCache.getDefault().getLayerDescriptor() // small pic
				, IconsCache.getDefault().getBigLayerDescriptor()); // big pic
		elementsDrawer.add(entry);
		
		entry = new CombinedTemplateCreationEntry(
				"Component" // label 
				, "Encapsulates functionality" // description
				, ElementType.COMPONENT
				, new Factory(ElementType.COMPONENT) // creation factory
				, IconsCache.getDefault().getComponentDescriptor() // small pic
				, IconsCache.getDefault().getBigComponentDescriptor()); // big pic
		elementsDrawer.add(entry);
		
		entry = new CombinedTemplateCreationEntry(
				"Connector" // label 
				, "Functional connection between components" // description
				, ElementType.CONNECTOR
				, new Factory(ElementType.CONNECTOR) // creation factory
				, IconsCache.getDefault().getConnectorDescriptor() // small pic
				, IconsCache.getDefault().getBigConnectorDescriptor()); // big pic
		elementsDrawer.add(entry);
		
		entry = new CombinedTemplateCreationEntry(
				"Link" // label 
				, "Connects provides/requires interfaces of components and connectors" // description
				, ElementType.LINK
				, new Factory(ElementType.LINK) // creation factory
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
				, ElementType.PROVIDES // template
				, new Factory(ElementType.PROVIDES) // creation factory
				, IconsCache.getDefault().getProvidesDescriptor() // small pic
				, IconsCache.getDefault().getBigProvidesDescriptor()); // big pic
		elementsDrawer.add(entry);
		
		entry = new CombinedTemplateCreationEntry(
				"Requires" // label 
				, "Imports functionality from other components/connectors" // description
				, ElementType.REQUIRES
				, new Factory(ElementType.REQUIRES) // creation factory
				, IconsCache.getDefault().getRequiresDescriptor() // small pic
				, IconsCache.getDefault().getBigRequiresDescriptor()); // big pic
		elementsDrawer.add(entry);
		
		return elementsDrawer;
	}
	
	/**
	 * Simple factory for creating new items
	 * @author Dilyan Rusev
	 *
	 */
	public static class Factory implements CreationFactory {
		private final ElementType type;
		
		/**
		 * Construct a new factory
		 * @param type Type of elements to create
		 */
		public Factory(ElementType type) {
			this.type = type;
		}

		/**
		 * Always returns null
		 */
		@Override
		public Object getNewObject() {
			return null;
		}

		/**
		 * Returns the value passed at the constructor
		 */
		@Override
		public Object getObjectType() {
			return type;
		}
		
	}
}
