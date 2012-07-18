package uk.ac.standrews.grasp.ide.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;

import grasp.lang.IAnnotation;
import grasp.lang.IArchitecture;
import grasp.lang.IElement;
import grasp.lang.IFirstClass;

public class ArchitectureModel extends FirstClassModel implements IArchitecture {
	private IFile file;
	private List<Runnable> archBuiltTasks;
	private boolean fullyCopied = false;
	
	public ArchitectureModel(IFile file) {
		super(ElementType.ARCHITECTURE, null);
		this.file = file;
		fullyCopied = true;
	}
	
	public ArchitectureModel(IArchitecture other, IFile file) {
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
	
	public IElement findByQualifiedName(String qualifiedName) {
		return findByQualifiedName(this, qualifiedName);
	}
	
	private IElement findByQualifiedName(IElement current, String qualifiedName) {
    	if (qualifiedName.equals(current.getQualifiedName())) {
    		return current;
    	}
    	if (current instanceof IFirstClass) {
    		IFirstClass fc = (IFirstClass) current;
    		for (IAnnotation annotation: fc.getAnnotations()) {
    			IElement found = findByQualifiedName(annotation, qualifiedName);
        		if (found != null) {
        			return found;
        		}
    		}
    		for (IFirstClass child: fc.getBody()) {
        		IElement found = findByQualifiedName(child, qualifiedName);
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
}
