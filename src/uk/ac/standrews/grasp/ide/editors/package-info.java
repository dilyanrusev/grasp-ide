/**
 * Eclipse editors and helper classes.
 * 
 * <p>Editors package contains all classes directly related to the classes that implement Eclipse Editors.
GraspEditor is associated with grasp files, and is a tabbed editor. It mainly synchronises the editors
when user switches tabs.</p>
<p>One of the tabs contains GraspTextEditor. GraspTextEditor is responsible for editing Grasp source
code. It features code completion through GraspCodeCompletionProcessor. The text document is
separated in partitions by PartitionScanner. Syntax highlighting is supported by configuring
GrapTextEditor’s source viewer with GraspSoureViewerConfiguration. The latter configures the
default reconciler for non-block comment partitions so that it uses GraspTokenScanner. The scanner
extends the blank Eclipse rule-based scanner with rules that detect grasp language elements in the
text document, and associate text attributes to the ranges of text. In short, it is the class that
provides syntax highlighting.</p>
<p>On the other hand, there is the graphical designer, implemented in GraspDesigner. It extends the
default GEF editor, GraphicalEditorWithFlyoutPalette. GraspDesigner contains a palette,
DesignerPalette. DesignerPalette contains tools which initiate the interaction with the designer.
Tools create new elements and place them on the diagram (through the request-command
framework). GraspEditDomain maintains a stack of commands, and provides an association between
the editor, the tools, the palette, and the command stack. EditDomain-s can be thought of as the
state of the GEF framework.</p>
<p>Most classes associated with the graphical designer reside in dedicated packages, such as commands,
editParts, figures, etc.</p>

 */
package uk.ac.standrews.grasp.ide.editors;