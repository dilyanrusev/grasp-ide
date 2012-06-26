/**
 * 
 */
package uk.ac.standrews.grasp.ide.wizards;

/**
 * Provides a set of examples for the new Grasp project wizard
 * @author Dilyan Rusev
 *
 */
public enum GraspExamples implements IGraspExample {
	/**
	 * WSN Simulator example
	 */
	WSN_SIMULATOR("WSN Simulator", "Default Grasp template", WsnSimulatorExample.SOURCE_CODE)
	,;

	private final String text;
	private final String description;
	private final String name;
	
	/**
	 * Constructs a new example
	 * @param name Name of the example
	 * @param description Short description of the example
	 * @param text Source code for the example
	 */
	GraspExamples(String name, String description, String text) {
		this.text = text;
		this.description = description;
		this.name = name;
	}
	
	@Override
	public String getText() {
		return this.text;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getDescription() {
		return this.description;
	}
}

// Do not pollute the enum definition with the actual source code
class WsnSimulatorExample {
	public static final String SOURCE_CODE =
			"//\n" +
			"// A simple Grasp architecture specification of a WSN simulator\n" +
			"//\n" + 
			"architecture WsnSimulator\n" +
			"{\n" +
			"	// Rationale descriptors\n" +
			"	rationale R1() {\n" +
			"		reason #'Use layered architecture style';\n" +
			"		reason #'Achieve clear separation of concerns';\n" +
			"	}\n" +
			"	rationale R2() {\n" +
			"		reason #'Use MVC design pattern';\n" +
			"	}\n" +
			"	rationale R3() {\n" +
			"		reason #'Simulator engine must perform event logging';\n" +
			"	}\n\n" +

			"	// Templates\n" +
			"	template ViewComponent() because R2() {\n" +
			"		provides IView;\n" +
			"		requires IModel;\n" +
			"	}\n" +
			"	template ControllerComponent() because R2() {\n" +
			"		requires IView vt;\n" +
			"		requires IView vg;\n" +
			"		requires IModel;\n" +
			"	}\n" +
			"	template ModelComponent() because R2() {\n" +
			"		provides IModel;\n" +
			"		requires ISequencer;\n" +
			"		requires INetwork;\n" +
			"	}\n" +
			"	template SensorNetworkComponent() {\n" +
			"		provides INetwork;\n" +
			"		requires ISequencer;\n" +
			"		requires ILogger;\n" +
			"	}\n" +
			"	template SequencerComponent() {\n" +
			"		provides ISequencer;\n" +
			"		requires ILogger;\n" +
			"	}\n" +
			"	template LoggerComponent() {\n" +
			"		provides ILogger;\n" +
			"	}\n\n" +

			"	// Runtime static model\n" +
			"	@Visualiser(Canvas = [1000,1000])\n" +
			"	system StaticModel\n" +
			"	{\n" +
			"		layer UtilityLayer because R1()\n" +
			"		{\n" +
			"			component logger = LoggerComponent();\n" +
			"		}\n\n" +
					
			"		layer SimulatorLayer over UtilityLayer because R1()\n" +
			"		{\n" +
			"			component network = SensorNetworkComponent();\n" +
			"			component sequencer = SequencerComponent();\n" +
			"			link network.ISequencer to sequencer.ISequencer;\n" +
			"		}\n\n" +
					
			"		layer PresentationLayer over SimulatorLayer, UtilityLayer because R1()\n" +
			"		{\n" +
			"			component model = ModelComponent();\n" +
			"			component controller = ControllerComponent();\n" +
			"			component textView = ViewComponent();\n" +
			"			component graphicView = ViewComponent();\n\n" +
						
			"			link controller.IModel to model.IModel;\n" +
			"			link textView.IModel to model.IModel;\n" +
			"			link graphicView.IModel to model.IModel;\n" +
			"			link controller.vt to textView.IView;\n" +
			"			link controller.vg to graphicView.IView;\n" +		
			"		}\n\n" +
					
			"		link PresentationLayer.model.INetwork to SimulatorLayer.network.INetwork;\n" +
			"		link PresentationLayer.model.ISequencer to SimulatorLayer.sequencer.ISequencer;	\n" +	
			"		link SimulatorLayer.network.ILogger to UtilityLayer.logger.ILogger because R3();\n" +
			"		link SimulatorLayer.sequencer.ILogger to UtilityLayer.logger.ILogger because R3();\n" +
			"	}\n" +
			"}";
}
