package uk.ac.standrews.grasp.ide.commands;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import uk.ac.standrews.grasp.ide.Msg;
import uk.ac.standrews.grasp.ide.model.ArchitectureModel;
import uk.ac.standrews.grasp.ide.model.ElementType;
import uk.ac.standrews.grasp.ide.model.FirstClassModel;
import uk.ac.standrews.grasp.ide.model.InstantiableModel;
import uk.ac.standrews.grasp.ide.model.InterfaceModel;
import uk.ac.standrews.grasp.ide.model.LayerModel;
import uk.ac.standrews.grasp.ide.model.LinkModel;
import uk.ac.standrews.grasp.ide.model.ProvidesModel;
import uk.ac.standrews.grasp.ide.model.RequiresModel;
import uk.ac.standrews.grasp.ide.model.SystemModel;

public class AddLinkCommand extends Command {
	private final FirstClassModel parent;	
	private LinkModel link;
	
	public AddLinkCommand(FirstClassModel parent) {
		Assert.isNotNull(parent);		
		Assert.isLegal(parent.getType() == ElementType.LAYER || parent.getType() == ElementType.SYSTEM);
		
		this.parent = parent;		
	}
	
	@Override
	public void execute() {
		if (link == null) {
			Shell shell = Msg.getShell();
			ChooseConsumerProviderDialog dlg = 
					new ChooseConsumerProviderDialog(shell, parent.getArchitecture());		
			if (dlg.open() == Dialog.OK) {
				link = new LinkModel(parent);
				link.setConsumer(dlg.getConsumer());
				link.setProvider(dlg.getProvider());
				link.setReferencingName(ModelHelper.getUniqueName(ElementType.LINK, parent));
			}
		}
		if (link != null) {
			parent.addChildElement(link);
		}
	}
	
	@Override
	public boolean canUndo() {
		return link != null;
	}
	
	@Override
	public void undo() {
		link.removeFromParent();
	}
}

class ChooseConsumerProviderDialog extends Dialog {
	private final ArchitectureModel architecture;
	private Label labelError;
	private ProvidesModel provider;
	private RequiresModel consumer;
	
	public ChooseConsumerProviderDialog(Shell parentShell, ArchitectureModel architecture) {
		super(parentShell);
		Assert.isNotNull(architecture);
		this.architecture = architecture;
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
	
	private List<InterfaceModel> findInterfaces() {
		List<InterfaceModel> ifaces = new ArrayList<InterfaceModel>();
		SystemModel system = (SystemModel) architecture.getBodyByType(ElementType.SYSTEM).iterator().next();
		if (system != null) {
			for (FirstClassModel child: system.getBody()) {
				if (child instanceof InstantiableModel) {
					fillWithInstantiableInterfaces((InstantiableModel) child, ifaces);
				} else if (child instanceof LayerModel) {
					fillWithLayerInterfaces((LayerModel) child, ifaces);
				}
			}
		}
		return ifaces;
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
	
	private void fillWithLayerInterfaces(LayerModel model, List<InterfaceModel> list) {
		for (FirstClassModel child: model.getBody()) {
			if (child instanceof InstantiableModel) {
				fillWithInstantiableInterfaces((InstantiableModel) child, list);
			}
		}
	}
	
	private void fillWithInstantiableInterfaces(InstantiableModel model, List<InterfaceModel> list) {
		for (FirstClassModel child: model.getBody()) {
			if (child instanceof InterfaceModel) {
				list.add((InterfaceModel) child);
			}
		}
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
		List<InterfaceModel> allInterfaces = findInterfaces();
		List<InterfaceModel> consumers = getConsumers(allInterfaces);
		final List<InterfaceModel> providers = getProviders(allInterfaces);
		
		if (allInterfaces.size() == 0) {
			doCreateErrorDialogArea(parent, 
					"You have not yet created any interfaces. Create components or connectors, and add provides and requires to them");
		} else if (consumers.size() == 0) {
			doCreateErrorDialogArea(parent,
					"You have not created any components/connectors with requires interfaces");
		} else if (providers.size() == 0) {
			doCreateErrorDialogArea(parent,
					"You have not created any components/connectors with provides interfaces");
		} else {
			RowLayout rootLayout = new RowLayout(SWT.VERTICAL);
			rootLayout.fill = true;
			rootLayout.pack = false;
			parent.setLayout(rootLayout);
			
			labelError = new Label(parent, SWT.WRAP);			
			
			Label label = new Label(parent, SWT.WRAP);
			label.setText("1. Choose consumer");
			
			ListViewer consumerViewer = new ListViewer(parent, SWT.SINGLE);
			
			label = new Label(parent, SWT.WRAP);
			label.setText("2. Choose provider");
			final ListViewer providersViewer = new ListViewer(parent, SWT.SINGLE);
			providersViewer.setContentProvider(new ArrayContentProvider());
			providersViewer.setLabelProvider(new InterfaceLabelProvider());
			providersViewer.addSelectionChangedListener(new ISelectionChangedListener() {
				
				@Override
				public void selectionChanged(SelectionChangedEvent event) {
					Object selected = ((IStructuredSelection) event.getSelection()).getFirstElement();
					provider = (ProvidesModel) selected;					
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
						}
					});						
					consumer = (RequiresModel) selected;
				}
			});
			consumerViewer.setInput(consumers);
		}
	}
	
	private void doCreateErrorDialogArea(Composite parent, String message) {
		parent.setLayout(new FillLayout());
		labelError = new Label(parent, SWT.WRAP);
		Label lblMessage = new Label(parent, SWT.WRAP);
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
			String name = full.substring(prefix.length());
			return name;
		}
		
		protected String getFullName(InterfaceModel iface) {
			return iface.getQualifiedName();
		}
	}
}
