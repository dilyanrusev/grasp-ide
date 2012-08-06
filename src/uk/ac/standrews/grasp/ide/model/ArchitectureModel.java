package uk.ac.standrews.grasp.ide.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IFile;

public class ArchitectureModel extends FirstClassModel {
	private IFile file;
	private List<Runnable> archBuiltTasks;
	private boolean fullyCopied = false;
	
	public ArchitectureModel(IFile file) {
		super(ElementType.ARCHITECTURE, null);
		this.file = file;
		fullyCopied = true;
	}
	
	public ArchitectureModel(ArchitectureModel other, IFile file) {
		super(other, null);
		this.file = file;
		
		if (archBuiltTasks != null) {
			for (Runnable task: archBuiltTasks) {
				task.run();
			}
			archBuiltTasks.clear();
		}
		fullyCopied = true;
	}
	
	public ElementModel findByQualifiedName(String qualifiedName) {
		ElementModel found = findByQualifiedName(this, qualifiedName);
		// could be an alias
		if (found == null) {
			int idx = qualifiedName.lastIndexOf('.');
			if (idx != -1 && qualifiedName.length() > idx + 1) {
				String name = qualifiedName.substring(idx + 1);
				String parentQualifiedName = qualifiedName.substring(0, idx);
				ElementModel parent = findByQualifiedName(parentQualifiedName);
				if (parent instanceof FirstClassModel) {
					for (FirstClassModel child: ((FirstClassModel) parent).getBody()) {
						// Fully qualified names prefer alias over name
						if (name.equals(child.getName())) {
							return child;
						}
					}
				}
			}			
		}
		return found;
	}
	
	private ElementModel findByQualifiedName(ElementModel current, String qualifiedName) {
    	if (qualifiedName.equals(current.getQualifiedName())) {
    		return current;
    	}
    	if (current instanceof FirstClassModel) {
    		FirstClassModel fc = (FirstClassModel) current;
    		for (AnnotationModel annotation: fc.getAnnotations()) {
    			ElementModel found = findByQualifiedName(annotation, qualifiedName);
        		if (found != null) {
        			return found;
        		}
    		}
    		for (FirstClassModel child: fc.getBody()) {
    			ElementModel found = findByQualifiedName(child, qualifiedName);
        		if (found != null) {
        			return found;
        		}
        	}
    	}    	
    	return null;
    }
	
	public IFile getFile() {
		return file;
	}
	
	void executeAtTheEndOfCopy(Runnable task) {
		if (!fullyCopied) {
			if (archBuiltTasks == null) {
				archBuiltTasks = new ArrayList<Runnable>();
			}
			archBuiltTasks.add(task);
		} else {
			task.run();
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj)) return false;
		ArchitectureModel other = (ArchitectureModel) obj;	
		
		if (!objectsEqual(getFile(), other.getFile())) return false;
		
		if (getBody().size() != other.getBody().size()) return false;
		Iterator<FirstClassModel> i1 = getBody().iterator();
		Iterator<FirstClassModel> i2 = other.getBody().iterator();
		
		while (i1.hasNext()) {
			if (!i2.hasNext()) return false;
			FirstClassModel child1 = i1.next();
			FirstClassModel child2 = i2.next();
			if (!childrenEqual(child1, child2)) return false;
		}
		
		return true;
	}
	
	private static boolean childrenEqual(FirstClassModel child1, FirstClassModel child2) {
		if (!child1.equals(child2)) return false;
		Collection<FirstClassModel> body1 = child1.getBody();
		Collection<FirstClassModel> body2 = child2.getBody();
		if (body1.size() != body2.size()) return false;
		
		Iterator<FirstClassModel> i1 = body1.iterator();
		Iterator<FirstClassModel> i2 = body2.iterator();
		FirstClassModel childOfChild1;
		FirstClassModel childOfChild2;
		
		while (i1.hasNext()) {
			if (!i2.hasNext()) return false;
			childOfChild1 = i1.next();
			childOfChild2 = i2.next();
			if (!childrenEqual(childOfChild1, childOfChild2)) return false;
		}
		
		return true;
	}
	
	@Override
	public int hashCode() {
		int result = super.hashCode();
		
		for (FirstClassModel child: getBody()) {
			result = 17 * result + computeChildHashCode(child);
		}
		
		return result;
	}
	
	private static int computeChildHashCode(FirstClassModel element) {
		int result = element.hashCode();
		
		for (FirstClassModel child: element.getBody()) {
			result = 17 * result + computeChildHashCode(child);
		}
		
		return result;
	}
}
