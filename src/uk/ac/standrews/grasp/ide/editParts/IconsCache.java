package uk.ac.standrews.grasp.ide.editParts;

import java.util.HashMap;
import java.util.Map;

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
