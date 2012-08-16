package uk.ac.standrews.grasp.ide.editors;

import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.ui.IEditorPart;

public class GraspEditDomain extends DefaultEditDomain {

	public GraspEditDomain(IEditorPart editorPart) {
		super(editorPart);		
		setCommandStack(new MyCommandStack());
	}
	
	private static class MyCommandStack extends CommandStack {
		
	}
}
