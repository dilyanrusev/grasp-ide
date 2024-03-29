package uk.ac.standrews.grasp.ide.commands;

import uk.ac.standrews.grasp.ide.model.ElementModel;
import uk.ac.standrews.grasp.ide.model.ElementType;
import uk.ac.standrews.grasp.ide.model.InstantiableModel;
import uk.ac.standrews.grasp.ide.model.TemplateModel;

/**
 * Provides utility methods for manupulating the model from the designer
 * @author Dilyan Rusev
 *
 */
public class ModelHelper {	
	
	/**
	 * Create a unique name starting with name
	 * @param name Starting name
	 * @param parent Element to use for calculating qualified name and => uniqueness
	 * @return Name that is not used by any other element in the architecture graph
	 */
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
	
	/**
	 * Create a unique name starting the type of the element
	 * @param type Type to use to calculate the start of the unique name
	 * @param parent Element to use for calculating qualified name and => uniqueness
	 * @return Name that is not used by any other element in the architecture graph
	 */
	public static String getUniqueName(ElementType type, ElementModel parent) {
		String name = getNameForType(type);
		return getUniqueName(name, parent);
	}
	
	/**
	 * Determine whether the base of an instantiable is managed solely by the designer
	 * @param instantiable
	 * @return True if a new template was created
	 */
	public static boolean ensureTempalteIsDesigned(InstantiableModel instantiable) {
		if (instantiable.getBase().isCreatedByDesigner()) {
			return false;
		} else {
			TemplateModel designed = createDesignedTemplate(instantiable.getBase(), instantiable);
			instantiable.setBase(designed);
			return true;
		}
	}
	
	/**
	 * Create a template that is managed solely by the designer
	 * @param original Template to copy
	 * @param forParent Component or connector for which this template is used
	 * @return Designed template
	 */
	public static TemplateModel createDesignedTemplate(TemplateModel original, InstantiableModel forParent) {
		TemplateModel designed = new TemplateModel(original, original.getArchitecture());
		String startName = forParent.getName() + "Template";		
		designed.setName(startName);
		ModelHelper.ensureUniqueName(designed);
		designed.setDesignerCreated(true);
		return designed;
	}
	
	/**
	 * Create a name that is unique within a paren'ts symbol table
	 * @param initialName Initial name
	 * @param parent Parent to use for uniqueness
	 * @return Name that is unique for the chose parent. Tries with the preferred name, and if
	 * it fails, it will keep appending increasing numbers until the name is unique
	 */
	public static String createUniqueName(String initialName, ElementModel parent) {
		StringBuilder finalName = new StringBuilder(initialName);
		int suffix = 1;
		while (parent.symLookup(finalName.toString())) {
			finalName.delete(initialName.length(), finalName.length());
			finalName.append(suffix);
			suffix++;
		}
		return finalName.toString();
	}
	
	private static String getNameForType(ElementType type) {
		String start = type.name().substring(0, 1).toUpperCase();
		String rest = type.name().substring(1).toLowerCase();
		if (type == ElementType.PROVIDES || type == ElementType.REQUIRES) {
			start = "I" + start;
		}
		return start + rest;		
	}

	/**
	 * Uses {@link #createUniqueName(String, ElementModel)} on an element
	 * @param element Element that has parent and name != null
	 */
	public static void ensureUniqueName(ElementModel element) {
		ElementModel parent = element.getParent();
		String initialName = element.getName();
		String finalName = createUniqueName(initialName, parent);
		element.setName(finalName);
	}
}
