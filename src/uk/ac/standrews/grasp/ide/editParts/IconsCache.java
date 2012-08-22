package uk.ac.standrews.grasp.ide.editParts;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

import uk.ac.standrews.grasp.ide.GraspPlugin;

public final class IconsCache {
	private static final IconsCache INSTANCE = new IconsCache();
	
	private static final String PATH_CHECK = "icons/check-sm.png";
	private static final String PATH_COMPONENT = "icons/component-sm.png";
	private static final String PATH_CONNECTOR = "icons/connector-sm.png";
	private static final String PATH_LAYER = "icons/layer-sm.png";
	private static final String PATH_LINK = "icons/link-sm.png";
	private static final String PATH_PROPERTY = "icons/property-sm.png";
	private static final String PATH_PROVIDES = "icons/provides-sm.png";
	private static final String PATH_REQUIRES = "icons/requires-sm.png";
	private static final String PATH_SYSTEM = "icons/system-sm.png";
	private static final String PATH_TEMPLATE = "icons/template-sm.png";
	private static final String PATH_LAYER_BIG = "icons/layer-big.png";
	private static final String PATH_COMPONENT_BIG = "icons/component-big.png";
	private static final String PATH_CONNECTOR_BIG = "icons/connector-big.png";
	private static final String PATH_LINK_BIG = "icons/link-big.png";
	private static final String PATH_PROVIDES_BIG = "icons/provides-big.png";
	private static final String PATH_REQUIRES_BIG = "icons/requires-big.png";
	
	
	private Map<String, Image> cache;
	
	private IconsCache() {
		cache = new HashMap<String, Image>();
	}
	
	public static IconsCache getDefault() {
		return INSTANCE;
	}
	
	public Image getImage(String path) {
		Image res = cache.get(path);
		if (res == null) {
			res = GraspPlugin.getImageDescriptor(path).createImage();
			cache.put(path, res);
		}
		return res;
	}
	
	public ImageDescriptor getDescriptor(String path) {
		return GraspPlugin.getImageDescriptor(path);
	}
	
	public ImageDescriptor getLayerDescriptor() {
		return getDescriptor(PATH_LAYER);
	}
	
	public ImageDescriptor getBigLayerDescriptor() {
		return getDescriptor(PATH_LAYER_BIG);
	}
	
	public ImageDescriptor getBigComponentDescriptor() {
		return getDescriptor(PATH_COMPONENT_BIG);
	}
	
	public ImageDescriptor getBigConnectorDescriptor() {
		return getDescriptor(PATH_CONNECTOR_BIG);
	}
	
	public ImageDescriptor getBigLinkDescriptor() {
		return getDescriptor(PATH_LINK_BIG);
	}
	
	public ImageDescriptor getBigProvidesDescriptor() {
		return getDescriptor(PATH_PROVIDES_BIG);
	}
	
	public ImageDescriptor getBigRequiresDescriptor() {
		return getDescriptor(PATH_REQUIRES_BIG);
	}
	
	public ImageDescriptor getConnectorDescriptor() {
		return getDescriptor(PATH_CONNECTOR);
	}
	
	public ImageDescriptor getComponentDescriptor() {
		return getDescriptor(PATH_COMPONENT);
	}
	
	public ImageDescriptor getLinkDescriptor() {
		return getDescriptor(PATH_LINK);
	}
	
	public ImageDescriptor getProvidesDescriptor() {
		return getDescriptor(PATH_PROVIDES);
	}
	
	public ImageDescriptor getRequiresDescriptor() {
		return getDescriptor(PATH_REQUIRES);
	}
	
	
	public Image getCheckIcon() {
		return getImage(PATH_CHECK);
	}
	
	public Image getComponentIcon() {
		return getImage(PATH_COMPONENT);
	}
	
	public Image getConnectorIcon() {
		return getImage(PATH_CONNECTOR);
	}
	
	public Image getLayerIcon() {
		return getImage(PATH_LAYER);
	}
	
	public Image getLinkIcon() {
		return getImage(PATH_LINK);
	}
	
	public Image getPropertyIcon() {
		return getImage(PATH_PROPERTY);
	}
	
	public Image getProvidesIcon() {
		return getImage(PATH_PROVIDES);
	}
	
	public Image getRequiresIcon() {
		return getImage(PATH_REQUIRES);
	}
	
	public Image getTemplateIcon() {
		return getImage(PATH_TEMPLATE);
	}
	
	public Image getSystemIcon() {
		return getImage(PATH_SYSTEM);
	}
	
	public void dispose() {
		for (Image allocatedImage: cache.values()) {
			allocatedImage.dispose();
		}
		cache.clear();
	}

	
}

