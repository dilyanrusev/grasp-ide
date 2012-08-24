package uk.ac.standrews.grasp.ide.commands;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.util.Util;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import uk.ac.standrews.grasp.ide.Msg;
import uk.ac.standrews.grasp.ide.editors.TextUtil;
import uk.ac.standrews.grasp.ide.model.FirstClassModel;

/**
 * Dialog that prompts the user for a valid and unique element name
 * @author Dilyan Rusev
 *
 */
public class ChooseNameDialog extends Dialog {
	private String elementName;
	private final FirstClassModel parent;
	private Label errorLabel;
	private Text entryText;

	/**
	 * Construct a new dialog
	 * @param parent Parent element. This will determine whether
	 * @param initialName
	 */
	public ChooseNameDialog(FirstClassModel parent, String initialName) {
		super(Msg.getShell());
		this.parent = parent;
		this.elementName = initialName;
	}
	
	/**
	 * Retrieve the stored element name. Value will be valid only after {@link #open()} returns 
	 * {@link Window#OK}.
	 * @return Picked element name
	 */
	public String getElementName() {
		return elementName;
	}
	
	@Override
	protected void configureShell(Shell newShell) {		
		super.configureShell(newShell);
		newShell.setText("Choose element name");
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {		
		Composite container = (Composite) super.createDialogArea(parent);
		Composite contents = new Composite(container, SWT.NONE);
		contents.setLayoutData(new GridData(GridData.FILL_BOTH));
		doCreateDialogArea(contents);
		return container;
	}

	private void doCreateDialogArea(Composite contents) {
		GridLayout rootLayout = new GridLayout();
		rootLayout.numColumns = 1;		
		contents.setLayout(rootLayout);
		
		errorLabel = createLabel(contents, Util.ZERO_LENGTH_STRING);
		createLabel(contents, "Enter name");
		entryText = new Text(contents, SWT.SINGLE);
		entryText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		entryText.addModifyListener(new ModifyListener() {			
			@Override
			public void modifyText(ModifyEvent e) {
				updateElementName();
			}
		});
		String initialText = ModelHelper.createUniqueName(elementName, parent);
		elementName = null;
		entryText.setText(initialText);
		entryText.setSelection(0, initialText.length());
	}
	
	@Override
	protected void okPressed() {		
		if (elementName != null) {
			super.okPressed();
		}
	}
	
	private void updateElementName() {
		String txt = entryText.getText();
		String error = null;
		if (TextUtil.isNullOrWhitespace(txt)) {
			error = "No text entered";
		}
		if (error == null && !TextUtil.isIdentifier(txt)) {
			error = "Not a valid Grasp identifier";
		}
		if (error == null && parent.symLookup(txt)) {
			error = "There is already an element with that name";
		}
		if (error == null) {
			elementName = txt;
			error = Util.ZERO_LENGTH_STRING;
		} else {
			elementName = null;
		}
		final String theErrorMessage = error;
		Display.getDefault().syncExec(new Runnable() {					
			@Override
			public void run() {
				errorLabel.setText(theErrorMessage);
			}
		});
	}
	
	private static Label createLabel(Composite container, String text) {
		Label label = new Label(container, SWT.WRAP);
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		label.setText(text);
		
		return label;
	}
}
