package uk.ac.standrews.grasp.ide.commands;

import uk.ac.standrews.grasp.ide.model.ElementModel;
import uk.ac.standrews.grasp.ide.model.ElementType;
import uk.ac.standrews.grasp.ide.model.InstantiableModel;
import uk.ac.standrews.grasp.ide.model.TemplateModel;

public class ModelHelper {	
	
	public static String getUniqueName(String name, ElementModel parent) {		
		StringBuilder sb = new StringBuilder(name);
		sb.append(parent.getQualifiedName());
		sb.append('.');		
		sb.append(name);
		sb.append(new Object().hashCode());
		int initLen = sb.length();
		int num = 1;		
		while (parent.getArchitecture().findByQualifiedName(sb.toString()) != null
				|| parent.symLookup(sb.toString())) {
			if (initLen != sb.length()) {
				sb.delete(initLen, sb.length());
			}
			num++;
			sb.append(num);
		}
		// now turn into regular name
		sb.delete(0, sb.lastIndexOf(".") + 1);		
		return sb.toString();
	}
	
	public static String getUniqueName(ElementType type, ElementModel parent) {
		String name = getNameForType(type);
		return getUniqueName(name, parent);
	}
	
	public static boolean ensureTempalteIsDesigned(InstantiableModel instantiable) {
		if (instantiable.getBase().isCreatedByDesigner()) {
			return false;
		} else {
			TemplateModel designed = createDesignedTemplate(instantiable.getBase(), instantiable);
			instantiable.setBase(designed);
			return true;
		}
	}
	
	public static TemplateModel createDesignedTemplate(TemplateModel original, InstantiableModel forParent) {
		TemplateModel designed = new TemplateModel(original, original.getArchitecture());
		String startName = forParent.getName() + "Template";
		String name = ModelHelper.getUniqueName(startName, designed.getParent());
		designed.setName(name);
		designed.setDesignerCreated(true);
		return designed;
	}
	
	private static String getNameForType(ElementType type) {
		String start = type.name().substring(0, 1).toUpperCase();
		String rest = type.name().substring(1).toLowerCase();
		if (type == ElementType.PROVIDES || type == ElementType.REQUIRES) {
			start = "I" + start;
		}
		return start + rest;		
	}
}
