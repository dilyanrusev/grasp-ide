package uk.ac.standrews.grasp.ide.editors;


import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;

import uk.ac.standrews.grasp.ide.GraspPlugin;
import uk.ac.standrews.grasp.ide.Log;

/**
 * Multi-page editor for grasp architectural files. 
 * First tab contains source code editor, and the second tab contains a graphical designer.

 * @author Dilyan Rusev
 * @see GraspTextEditor
 */
public class GraspEditor extends MultiPageEditorPart implements IResourceChangeListener {
	/** Attribute of the grasp marker that contains the architecture */
	public static final String ARCHITECTURE_ATTRIBUTE = "architecture";

	/** The text editor used in page 0. */
	private GraspTextEditor textEditor;
	private GraspDesigner designer;
	
	private int designerIndex;
	private int textEditorIndex;
	
	/**
	 * Creates a new instance of the Grasp Editor
	 */
	public GraspEditor() {
		super();
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
	}
	
	/**
	 * The <code>MultiPageEditorExample</code> implementation of this method
	 * checks that the input is an instance of <code>IFileEditorInput</code>.
	 */
	@Override
	public void init(IEditorSite site, IEditorInput editorInput) throws PartInitException {
		assertInputIsGraspContent(this, editorInput);
		super.init(site, editorInput);
		setPartName(((IFileEditorInput)editorInput).getFile().getName());
	}
	
	/**
	 * Asserts that the editor input is a Grasp file
	 * @param input Editor input, as passed by <code>MultiPageEditorPart.init(IEditorSite, IEditorInput)</code>
	 * @throws PartInitException When the content type of <code>input</code> is not Grasp
	 * @see {@link org.eclipse.ui.part.MultiPageEditorPart#init(IEditorSite, IEditorInput)}
	 */
	static void assertInputIsGraspContent(EditorPart editor, IEditorInput input) throws PartInitException {
		if (!(input instanceof IFileEditorInput))
			throw new PartInitException("Input is not a file");
		IFileEditorInput fileInput = (IFileEditorInput)input;
		String contentID;
		try {
			IFile file = fileInput.getFile();
			contentID = file.getContentDescription().getContentType().getId();
		} catch (CoreException e) {
			throw new PartInitException("Cannot determine content type for " + input, e);
		}
		if (!GraspPlugin.ID_GRASP_CONTENT_TYPE.equals(contentID)) {
			throw new PartInitException("Input must be a Grasp file");
		}
	}
	
	/**
	 * Creates the pages of the multi-page editor.
	 */
	@Override
	protected void createPages() {
		try {
			createGraphicalEditorPage();
			createTextEditorPage();
		} catch (PartInitException e) {
			Log.error(e);
		}		
	}
	
	/**
	 * Creates text editor page
	 * 	
	 * @throws PartInitException When <code>GraspTextEditor</code> fails to initialize
	 */
	private void createTextEditorPage() throws PartInitException {
		textEditor = new GraspTextEditor();
		textEditorIndex = addPage(textEditor, getEditorInput());
		setPageText(textEditorIndex, "Source");
	}	
	
	/**
	 * Creates graphical designer page
	 */
	private void createGraphicalEditorPage() throws PartInitException {
		designer = new GraspDesigner();		
		designerIndex = addPage(designer, getEditorInput());
		setPageText(designerIndex, "Designer");
	}		
	
	
	/**
	 * The <code>MultiPageEditorPart</code> implementation of this 
	 * <code>IWorkbenchPart</code> method disposes all nested editors.
	 * Subclasses may extend.
	 */
	@Override
	public void dispose() {
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
		super.dispose();
	}
	
	/**
	 * Save file contents
	 */
	@Override
	public void doSave(IProgressMonitor monitor) {		
		int active = getActivePage();
		if (active != -1) {
			getEditor(active).doSave(monitor);
		}
		Log.warn("GraspEditor.doSave -> no active editor");
	}
	/**
	 * Saves the multi-page editor's document as another file.
	 * Also updates the text for page 0's tab, and updates this multi-page editor's input
	 * to correspond to the nested editor's.
	 */
	@Override
	public void doSaveAs() {
		int active = getActivePage();
		if (active != -1) {
			IEditorPart editor = getEditor(active);
			editor.doSaveAs();
			setPageText(active, editor.getTitle());
			setInput(editor.getEditorInput());
		}		
	}
	
	public void gotoMarker(IMarker marker) {
		setActivePage(0);
		IDE.gotoMarker(getEditor(0), marker);
	}
	
	
	
	/* (non-Javadoc)
	 * Method declared on IEditorPart.
	 */
	@Override
	public boolean isSaveAsAllowed() {
		return true;
	}
	
	/**
	 * Calculates the contents of page 2 when the it is activated.
	 */
	@Override
	protected void pageChange(int newPageIndex) {
		super.pageChange(newPageIndex);
		// TODO: this could update the model
	}
	
	/**
	 * Closes all project files on project close.
	 */
	@Override
	public void resourceChanged(final IResourceChangeEvent event){		
		if(event.getType() == IResourceChangeEvent.PRE_CLOSE){
			Display.getDefault().asyncExec(new Runnable(){
				public void run(){
					IWorkbenchPage[] pages = getSite().getWorkbenchWindow().getPages();
					for (int i = 0; i<pages.length; i++){
						if(((FileEditorInput)textEditor.getEditorInput()).getFile().getProject().equals(event.getResource())){
							IEditorPart editorPart = pages[i].findEditor(textEditor.getEditorInput());
							pages[i].closeEditor(editorPart,true);
						}
					}
				}            
			});
		} else if (event.getType() == IResourceChangeEvent.POST_CHANGE) {
			
			try {
				event.getDelta().accept(new IResourceDeltaVisitor() {				
					@Override
					public boolean visit(IResourceDelta delta) throws CoreException {
						if (delta.getKind() == IResourceDelta.REMOVED) {
							IResource deleted = delta.getResource();
							for (IWorkbenchPage page: getSite().getWorkbenchWindow().getPages()) {
								for (IEditorReference editor: page.getEditorReferences()) {
									if (editor.getId().equals(GraspPlugin.ID_GRASP_EDITOR)
											&& ((IFileEditorInput)editor.getEditorInput()).getFile().equals(deleted)) {
										IEditorPart part = editor.getEditor(true);
										if (part != null) {
											page.closeEditor(part, false);
										}
									}
								}
							}
							return false;
						} else {
							return true;
						}
					}
				});
			} catch (CoreException e) {
				Log.error(e);
			}
		}
	}	
}
