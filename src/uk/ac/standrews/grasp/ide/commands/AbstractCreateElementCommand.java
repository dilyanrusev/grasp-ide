package uk.ac.standrews.grasp.ide.commands;

import org.eclipse.core.runtime.Assert;
import org.eclipse.gef.commands.Command;

import uk.ac.standrews.grasp.ide.model.ElementModel;
import uk.ac.standrews.grasp.ide.model.ElementType;
import uk.ac.standrews.grasp.ide.model.FirstClassModel;

/**
 * Provides a framework for commands that create Grasp elements
 * @author Dilyan Rusev
 *
 */
public abstract class AbstractCreateElementCommand extends Command {
	private boolean fakeUndo;
	private boolean needsNameDialog;
	private final FirstClassModel parent;
	private FirstClassModel element;	
	private final ElementType elementType;
	
	/**
	 * Create a new command
	 * @param parent Parent element. May not be null.
	 * @param elementType Type of the element to create. May not be null.
	 */
	public AbstractCreateElementCommand(FirstClassModel parent, ElementType elementType) {
		Assert.isNotNull(parent);
		Assert.isNotNull(elementType);
		fakeUndo = false;
		this.parent = parent;
		this.elementType = elementType;
		needsNameDialog = true;
		setLabel("Add " + elementType.getDisplayName());
	}
	
	/**
	 * Return the type of element to create. By default, returns the value passed at the constructor
	 * @return
	 */
	protected final ElementType getElementType() {
		return elementType;
	}
	
	/**
	 * Return the cached value of {@link #createElement(String)}
	 * @return
	 */
	protected final FirstClassModel getElement() {
		return element;
	}
	
	/**
	 * Return the parent element. By default, returns the value passed at the constructor
	 * @return
	 */
	protected final FirstClassModel getParent() {
		return parent;
	}
	
	/** 
	 * @see #doExecute(FirstClassModel)
	 * @see #createElement(String)
	 */
	@Override
	public final void execute() {		
		if (element == null) {			
			if (needsNameDialog) {
				String name = getPreferredElementName();
				ChooseNameDialog dlg = new ChooseNameDialog(parent, name);
				if (dlg.open() == ChooseNameDialog.OK) {
					name = dlg.getElementName();
					element = createElement(name);					
				} else {
					fakeUndo = true;
				}
			} else {
				element = createElement(getPreferredElementName());				
			}
		} 
		
		if (element != null) {
			doExecute(element);
		} else {
			fakeUndo = true;
		}
	}
	
	/**
	 * Execute the command when an element has been created
	 * @param element Element that was created in {@link #execute()}
	 */
	protected void doExecute(FirstClassModel element) {
		parent.addChildElement(element);
	}
	
	
	/**
	 * Returns true to use {@link ChooseNameDialog}. Use {@link #setNeedsNameDialog(boolean)} in constructor. 
	 * Default value is true.
	 * @return
	 */
	protected final boolean getNeedsNameDialog() {
		return needsNameDialog;
	}
	
	/**
	 * Use in constructor.
	 * Set to true to invoke {@link ChooseNameDialog} before {@link #createElement(String)} is called.
	 * Set to false so that the value returned by {@link #getPreferredElementName()} will be passed to
	 * {@link #createElement(String)} directly.
	 * @param flag True to use {@link ChooseNameDialog}
	 */
	protected final void setNeedsNameDialog(boolean flag) {
		needsNameDialog = flag;
	}
	
	/**
	 * Return the name that initialises {@link ChooseNameDialog} or is passed to {@link #createElement(String)},
	 * depending on {@link #getNeedsNameDialog()}. By default, returns the display name of {@link #getElementType()}
	 * @return
	 */
	protected String getPreferredElementName() {
		return elementType.getDisplayName();
	}
	
	/**
	 * Override to create the Grasp element during {@link #execute()}
	 * @param name Name to use to create the element. If {@link #getNeedsNameDialog()} returns true, this will be guaranteed to be
	 * unique within {@link #getParent()}. If it is false the value will be {@link #getPreferredElementName()}
	 * @return Item that was created. Should not be null
	 */
	protected abstract FirstClassModel createElement(String name);
	
	/**
	 * Makes certain that the command can be undone. By default, checks that the created element is not null.
	 * @return
	 */
	protected boolean verify() {
		return element != null;
	}
	
	/**
	 * @see #verify()
	 */
	@Override
	public final boolean canUndo() {		
		return fakeUndo || verify();
	}
	
	/**
	 * By default, calls {@link ElementModel#removeFromParent()} if the element was successfully created by
	 * in {@link #execute()}
	 */
	@Override
	public void undo() {
		if (element != null) {
			element.removeFromParent();
		}
		fakeUndo = false;
	}
}
