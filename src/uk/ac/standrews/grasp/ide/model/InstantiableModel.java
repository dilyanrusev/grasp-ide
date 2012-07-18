package uk.ac.standrews.grasp.ide.model;

import java.util.List;

import grasp.lang.IBecause;
import grasp.lang.IFirstClass;
import grasp.lang.IInstantiable;
import grasp.lang.ITemplate;

public abstract class InstantiableModel extends BecauseModel implements
		IInstantiable {
	public static final String PROPERTY_BASE = "base";
	
	private final List<IFirstClass> arguments = new ObservableList<IFirstClass>();
	private ITemplate base;
	
	public InstantiableModel(IInstantiable other, IFirstClass parent) {
		super((IBecause) other, parent);
		if (other.getBase() != null) {
			this.base = new TemplateModel(other.getBase(), (IFirstClass) other.getBase().getParent());
		}
		for (IFirstClass argument: other.getArguments()) {
			IFirstClass observable = (IFirstClass) GraspModel.INSTANCE.makeObservable(argument, this);
			arguments.add(observable);
		}
	}
	
	public InstantiableModel(ElementType type, IFirstClass parent) {
		super(type, parent);
	}

	@Override
	public List<IFirstClass> getArguments() {
		return arguments;
	}

	@Override
	public ITemplate getBase() {
		return base;
	}

	@Override
	public void setBase(ITemplate base) {
		this.base = base;
		fireElementChanged(PROPERTY_BASE);
	}

}
