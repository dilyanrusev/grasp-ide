/**
 * GEF Figures.
 * 
 * <p>There are two types of figures: containers and nodes. Containers are figures whose primary function
is to host/group other figures. Nodes are figures that represent autonomous Grasp elements.
Regardless of their purpose, all figures feature common interface, so that
AbstractElementNodeEditPart can update the visual representation when trivial changes to the
model are made. Conceptually, every element contains a header, a tooltip, and a body. The first two
are provided by IFirstClassFigure, and the latter is a natural part of the IFigure interface, part of the
Draw2D SDK.</p>
<p>This package also contains any helper classes and figures that are part of the visual tree.</p>

 */
package uk.ac.standrews.grasp.ide.figures;