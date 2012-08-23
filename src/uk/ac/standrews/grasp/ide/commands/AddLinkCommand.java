package uk.ac.standrews.grasp.ide.commands;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.util.Util;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import uk.ac.standrews.grasp.ide.Msg;
import uk.ac.standrews.grasp.ide.model.ElementType;
import uk.ac.standrews.grasp.ide.model.FirstClassModel;
import uk.ac.standrews.grasp.ide.model.InstantiableModel;
import uk.ac.standrews.grasp.ide.model.InterfaceModel;
import uk.ac.standrews.grasp.ide.model.LayerModel;
import uk.ac.standrews.grasp.ide.model.LinkModel;
import uk.ac.standrews.grasp.ide.model.ProvidesModel;
import uk.ac.standrews.grasp.ide.model.RequiresModel;

public class AddLinkCommand extends Command {
	private final FirstClassModel parent;	
	private LinkModel link;
	private RequiresModel consumer;
	private ProvidesModel provider;
	private List<InterfaceModel> parentInterfaces;
	private boolean fakeUndo; // if cancel is selected we need to simulate a successful undo
	
	public AddLinkCommand(FirstClassModel parent) {
		Assert.isNotNull(parent);		
		Assert.isLegal(parent.getType() == ElementType.LAYER || parent.getType() == ElementType.SYSTEM);
		
		this.parent = parent;		
	}
	
	@Override
	public void execute() {
		if (link == null && parentInterfaces != null) {			
			if (provider == null || consumer == null) {
				Shell shell = Msg.getShell();
				ChooseConsumerProviderDialog dlg = 
						new ChooseConsumerProviderDialog(shell, parentInterfaces);		
				if (dlg.open() == Dialog.OK) {
					consumer = dlg.getConsumer();
					provider = dlg.getProvider();
				} 
			}
			
			if (provider != null && consumer != null) {
				link = new LinkModel(parent);
				link.setConsumer(consumer);
				link.setProvider(provider);
				link.setReferencingName(ModelHelper.getUniqueName(ElementType.LINK, parent));
			}			
		}
		if (link != null) {
			parent.addChildElement(link);
		} else {
			fakeUndo = true;
		}
	}
	
	@Override
	public boolean canExecute() {
		if (link == null) {
			provider = null;
			consumer = null;
			parentInterfaces = findInterfaces();
			// attempt to deduce link pair
			if (!parentInterfaces.isEmpty()) {
				RequiresModel firstConsumer = null;
				int numConsumers = 0;
				List<ProvidesModel> providers = new ArrayList<ProvidesModel>();
				for (InterfaceModel iface: parentInterfaces) {
					if (iface.getType() == ElementType.REQUIRES) {
						if (firstConsumer == null) {
							firstConsumer = (RequiresModel) iface;
						}
						numConsumers++;
					} else {
						providers.add((ProvidesModel) iface);
					}
				}
				if (numConsumers > 0) {
					if (numConsumers == 1) {
						consumer = firstConsumer;
						ProvidesModel firstProvider = null;
						int numProviders = 0;
						for (ProvidesModel provider: providers) {
							if (provider.getName().equals(consumer.getName())) {
								firstProvider = firstProvider == null ? provider : firstProvider;
								numProviders++;
							}
						}
						if (numProviders == 1) {
							provider = firstProvider;
						}
					}
					
					return true;
				}
			}
			return false;
		} else { // link is not null
			return true;
		}
	}
	
	@Override
	public boolean canUndo() {
		return link != null || fakeUndo;
	}
	
	@Override
	public void undo() {
		if (link != null) {
			link.removeFromParent();
		} 
		fakeUndo = false;
	}
	
	private List<InterfaceModel> findInterfaces() {
		List<InterfaceModel> ifaces = new ArrayList<InterfaceModel>();
		
		// find all interfaces that do not have links
		
		if (parent.getType() == ElementType.SYSTEM) {
			for (FirstClassModel sysChild: parent.getBody()) {
				if (sysChild instanceof InterfaceModel && 
						((InterfaceModel) sysChild).getConnections().size() == 0) {
					ifaces.add((InterfaceModel) sysChild);
				} else if (sysChild instanceof LayerModel) {
					extractInterfacesFromLayer((LayerModel) sysChild, ifaces);
				}				
			}
		} else {
			extractInterfacesFromLayer((LayerModel) parent, ifaces);
		}
		
		
		return ifaces;
	}
	
	private void extractInterfacesFromLayer(LayerModel layer,
			List<InterfaceModel> list) {
		for (FirstClassModel layerChild: layer.getBody()) {
			if (layerChild instanceof InstantiableModel) {
				for (FirstClassModel instChild: layerChild.getBody()) {
					if (instChild instanceof InterfaceModel &&
							((InterfaceModel) instChild).getConnections().size() == 0) {
						list.add((InterfaceModel) instChild);
					}
				}
			}
		}
	}
	
	
}

class ChooseConsumerProviderDialog extends Dialog {
	private final List<InterfaceModel> parentInterfaces;
	private Label labelError;
	private ProvidesModel provider;
	private RequiresModel consumer;
	
	public ChooseConsumerProviderDialog(Shell parentShell, 
			List<InterfaceModel> parentInterfaces) {
		super(parentShell);		
		this.parentInterfaces = parentInterfaces;		
	}
	
	@Override
	protected void configureShell(Shell newShell) {		
		super.configureShell(newShell);
		newShell.setText("Create a new link");
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		doCreateDialogArea(new Composite(container, SWT.NONE));
		return container;
	}
	
	private List<InterfaceModel> getConsumers(List<InterfaceModel> allInterfaces) {
		List<InterfaceModel> consumers = new ArrayList<InterfaceModel>();
		for (InterfaceModel iface: allInterfaces) {
			if (iface.getType() == ElementType.REQUIRES) {
				consumers.add(iface);
			}
		}
		return consumers;
	}
	
	private List<InterfaceModel> getProviders(List<InterfaceModel> allInterfaces) {
		List<InterfaceModel> providers = new ArrayList<InterfaceModel>();
		for (InterfaceModel iface: allInterfaces) {
			if (iface.getType() == ElementType.PROVIDES) {
				providers.add(iface);
			}
		}
		return providers;
	}	
	
	private List<InterfaceModel> findProvidersFor(InterfaceModel iface, List<InterfaceModel> allProviders) {
		List<InterfaceModel> result = new ArrayList<InterfaceModel>();
		
		for (InterfaceModel provider: allProviders) {
			if (iface.getName().equals(provider.getName())) {
				result.add(provider);
			}
		}
		
		return result;
	}
	
	private void doCreateDialogArea(Composite parent) {		
		List<InterfaceModel> consumers = getConsumers(parentInterfaces);
		final List<InterfaceModel> providers = getProviders(parentInterfaces);
		
		parent.setLayoutData(new GridData(GridData.FILL_BOTH));
		if (parentInterfaces.size() == 0) {
			doCreateErrorDialogArea(parent, 
					"No components/connectors with provides/requires");
		} else if (consumers.size() == 0) {
			doCreateErrorDialogArea(parent,
					"No components/connectors with Requires");
		} else if (providers.size() == 0) {
			doCreateErrorDialogArea(parent,
					"No components/connectors with Provides");
		} else {
			GridLayout rootLayout = new GridLayout();
			rootLayout.numColumns = 1;			
			parent.setLayout(rootLayout);
			
			labelError = new Label(parent, SWT.WRAP);	
			labelError.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			
			Label label = new Label(parent, SWT.WRAP);
			label.setText("1. Choose consumer");
			label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			
			ListViewer consumerViewer = new ListViewer(parent, SWT.SINGLE);
			consumerViewer.getControl().setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			
			label = new Label(parent, SWT.WRAP);
			label.setText("2. Choose provider");
			label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			final ListViewer providersViewer = new ListViewer(parent, SWT.SINGLE);
			providersViewer.getControl().setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			providersViewer.setContentProvider(new ArrayContentProvider());
			providersViewer.setLabelProvider(new InterfaceLabelProvider());
			providersViewer.addSelectionChangedListener(new ISelectionChangedListener() {
				
				@Override
				public void selectionChanged(SelectionChangedEvent event) {
					Object selected = ((IStructuredSelection) event.getSelection()).getFirstElement();
					provider = (ProvidesModel) selected;
					Display.getDefault().syncExec(new Runnable() {						
						@Override
						public void run() {
							labelError.setText(Util.ZERO_LENGTH_STRING);
						}
					});
				}
			});			
			consumerViewer.setContentProvider(new ArrayContentProvider());
			consumerViewer.setLabelProvider(new InterfaceLabelProvider());
			consumerViewer.addSelectionChangedListener(new ISelectionChangedListener() {				
				@Override
				public void selectionChanged(SelectionChangedEvent event) {
					final Object selected = ((IStructuredSelection) event.getSelection()).getFirstElement();					
					Display.getDefault().syncExec(new Runnable() {						
						@Override
						public void run() {
							List<InterfaceModel> newProviders = 
									findProvidersFor((InterfaceModel) selected, providers);
							providersViewer.setInput(newProviders);
							if (newProviders.size() == 1) {
								providersViewer.setSelection(new StructuredSelection(newProviders.get(0)));
							}							
						}
					});						
					consumer = (RequiresModel) selected;
				}
			});
			consumerViewer.setInput(consumers);
		}
	}
	
	private void doCreateErrorDialogArea(Composite parent, String message) {
		GridLayout rootLayout = new GridLayout();
		rootLayout.numColumns = 1;		
		parent.setLayout(rootLayout);
		labelError = new Label(parent, SWT.WRAP);
		labelError.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		Label lblMessage = new Label(parent, SWT.WRAP);
		lblMessage.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		lblMessage.setText(message);
	}
	
	@Override
	protected void okPressed() {
		if (consumer == null) {
			labelError.setText("You have not selected a consumer");
			return;
		} 
		if (provider == null) {
			labelError.setText("You have not selected a provider");
			return;
		}
		super.okPressed();
	}
	
	public RequiresModel getConsumer() {
		return consumer;
	}
	
	public ProvidesModel getProvider() {
		return provider;
	}
	
	private static class InterfaceLabelProvider extends LabelProvider {
		@Override
		public String getText(Object element) {
			InterfaceModel iface = (InterfaceModel) element;
			String prefix = iface.getArchitecture().getBodyByType(ElementType.SYSTEM).iterator().next().getQualifiedName();
			String full = getFullName(iface);
			String name = full.substring(prefix.length() + 1);
			return name;
		}
		
		protected String getFullName(InterfaceModel iface) {
			return iface.getQualifiedName();
		}
	}
}
