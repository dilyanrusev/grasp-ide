package uk.ac.standrews.grasp.ide.wizards;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ContainerSelectionDialog;

import uk.ac.standrews.grasp.ide.editors.TextUtil;

public class NewArchitectureWizardPage extends WizardPage {
	private IGraspExample selectedExample;
	private Label labelDescription;
	private Label labelFileName;
	private Text textArchitecture;
	private IContainer fileContainer;
	private IFile architectureFile;	
	private String architectureName;
	
	public NewArchitectureWizardPage(IContainer fileContainer) {
		super("new-architecture");
		setTitle("Create a new architecture");
		setDescription("Type architecture name and select a predefined template from the list below");
		this.fileContainer = fileContainer;
		setPageComplete(false);
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = createContainer(parent);	
		createSourceFolderGroup(container);
		createNameGroup(container);
		createTemplateGroup(container);		
	}
	
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (visible && textArchitecture != null) {
			textArchitecture.setFocus();
		}
	}

	private Composite createContainer(Composite parent) {
		Composite container = new Composite(parent, 0);
		setControl(container);		
		GridLayout topLayout = new GridLayout();
		topLayout.numColumns = 3;
		topLayout.verticalSpacing = 0;
		container.setLayout(topLayout);	
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		return container;
	}
	
	private void createSourceFolderGroup(Composite parent) {
		Label label = new Label(parent, SWT.NONE);
		label.setText("Source folder");
		
		final Text text = new Text(parent, SWT.SINGLE | SWT.READ_ONLY);
		text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		if (fileContainer != null) {
			text.setText(fileContainer.getProjectRelativePath().toString());
		}
		
		Button browse = new Button(parent, SWT.PUSH);	
		browse.setText("Browse...");
		browse.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				browseForParentContainer(text);
			}
		});
	}

	private void createNameGroup(Composite parent) {
		Label label = new Label(parent, SWT.NONE);
		label.setText("Name");
		
		textArchitecture = new Text(parent, SWT.SINGLE);
		textArchitecture.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		textArchitecture.addModifyListener(new ModifyListener() {			
			@Override
			public void modifyText(ModifyEvent e) {
				String txt = textArchitecture.getText();
				if (txt.length() > 0) {
					if (TextUtil.isIdentifier(txt)) {
						IFile file = getArchitectureFileFromName(txt);
						if (!file.exists()) {
							setErrorMessage(null);
							setPageComplete(true);
							architectureFile = file;
							architectureName = textArchitecture.getText();
						} else {
							setErrorMessage("There is a file with that name");
							setPageComplete(false);
						}
					} else {
						setErrorMessage("Name is not valid identifier. Identifiers start with underscore or English letter, and may contain underscores, letters or digits");
					}
				} else {
					setErrorMessage("You must enter architecture name");
					setPageComplete(false);
				}
			}
		});
		
		new Composite(parent, SWT.NONE); // space
		
		Composite space1 = new Composite(parent, SWT.NONE); // space		
		GridDataFactory.fillDefaults().applyTo(space1);
		
		labelFileName = new Label(parent, SWT.NONE);
		labelFileName.setText("Enter architecture to view the file name");
		GridDataFactory.fillDefaults().applyTo(labelFileName);
		
		Composite space2 = new Composite(parent, SWT.NONE); // space
		GridDataFactory.fillDefaults().applyTo(space2);
	}
	
	private void createTemplateGroup(Composite parent) {
		Label labelHeader = new Label(parent, SWT.NONE);
		labelHeader.setText("Choose a predefined template");
		GridDataFactory.defaultsFor(labelHeader)
			.span(3, 1)
			.align(SWT.FILL, SWT.CENTER)
			.applyTo(labelHeader);
				
		Composite group = new Composite(parent, SWT.NONE);
		GridDataFactory.defaultsFor(group)
			.span(3, 1)
			.align(SWT.FILL, SWT.FILL)
			.grab(true, true)
			.applyTo(group);
		FillLayout groupLayout = new FillLayout();
		groupLayout.spacing = 10;
		group.setLayout(groupLayout);
		
		ListViewer examples = new ListViewer(group, SWT.SINGLE);
		examples.setLabelProvider(new ExampleLableProvider());
		examples.setContentProvider(new ArrayContentProvider());
		examples.setInput(GraspExamples.values());
		examples.addSelectionChangedListener(new ISelectionChangedListener() {			
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection)event.getSelection();
				selectedExample = (IGraspExample)selection.getFirstElement();
				if (labelDescription != null && selectedExample != null) {
					labelDescription.setText(selectedExample.getDescription());
				}
			}
		});
		
		
		labelDescription = new Label(group, SWT.WRAP);
		
		examples.setSelection(new StructuredSelection(GraspExamples.EMPTY_FILE));
	}
	
	private void browseForParentContainer(Text textField) {
		ContainerSelectionDialog dlg =
				new ContainerSelectionDialog(getShell(), fileContainer, true, 
						"Select folder in which to create the new architecture");
		dlg.setTitle("Select parent folder");
		if (dlg.open() == Window.OK) {
			IPath selection = (IPath) dlg.getResult()[0];
			IWorkspaceRoot root = fileContainer.getWorkspace().getRoot();
			IProject project = fileContainer.getProject();
			IPath prjPath = project.getLocation();
			IPath prjRel = selection.removeFirstSegments(project.getFullPath().segmentCount());
			IPath combined = prjPath.append(prjRel);
			fileContainer = root.getContainerForLocation(combined);
			textField.setText(fileContainer.getProjectRelativePath().toString());
		}
	}
	
	private IFile getArchitectureFileFromName(String architectureName) {
		IPath relative = new Path(architectureName + ".grasp");
		IFile file = fileContainer.getFile(relative);
		return file;
	}	
	
	public IGraspExample getSelectedExample() {
		return selectedExample;
	}

	public IContainer getFileContainer() {
		return fileContainer;
	}

	public IFile getArchitectureFile() {
		return architectureFile;
	}
	
	public String getArchitectureName() {
		return architectureName;
	}

	private static class ExampleLableProvider extends LabelProvider {
		@Override
		public Image getImage(Object element) {
			return null;
		}
		
		@Override
		public String getText(Object element) {
			IGraspExample example = (IGraspExample)element;
			return example.getName();
		}
	}
}
