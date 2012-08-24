/**
 * GEF edit parts.
 * 
 * <p>In this package reside all the GEF edit parts and their supporting classes.
IconsCache contains images used by the icons. Since Draw2D is built on top of SWT, each image is
associated with native resources. There can be many active editors and each editor can have many
elements. The cache makes certain that the images used by the diagram editor are shared. The
lifecycle of the native images are handled by hooking into the plugin activator’s start and stop
methods.</p>
<p>GraspEditPartFatory is the GEF factory associated with every graphical viewer (the GEF control that
displays the diagrams in both Views and Editor-s). It is responsible for creating edit parts from model
objects.</p>
<p>Every Grasp edit part inherits AbstractElementEditPart. It provides basic API for all edit parts, such as
strongly-typed access to the model, as well as listening to model changes.
The content root (51) is ArchitectureEditPart. It represents the architecture Grasp element, and
naturally features the Grasp architecture graph.
AbstractElementNodeEditPart is mostly a marker class, to distinguish the architecture from the other
nodes. Nodes are, unlike the architecture, visible on the designer’s canvas, and the user can interact
with their figures.</p>
<p>LayerEditPart, LinkEditPart, SystemEditPart, ComponentEditPart and ConnectorEditPart represent
the Grasp language elements that are supported by the designer. Components and connectors
feature a superclass because they are the same, both in semantics and behaviour. The concrete
implementations most prominently provide different visual appearance.
This package also contains all the ComponentEditPolicy-ies supported by the designer’s edit parts.</p>

 */
package uk.ac.standrews.grasp.ide.editParts;