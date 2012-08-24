/**
 * Grasp model and helper classes.
 * 
 * <p>The class hierarchy for Grasp elements copies the Java API of the Grasp compiler implemented by
Lakshitha de Silva. There are a few very important changes, not immediately obvious from this class
diagram.</p>
<p>The Grasp compiler does not guarantee that elements are consistently ordered. This means that
after each successful compilation, the AST serialized into XML will be randomly reordered. Since GEF
is order-sensitive for child figures (it determines both the graphical Z-Order and the selection order
(51)), the root element, ElementModel, implements java.lang.Comparable. Furthermore, child
elements are stored in SortedSet, and as a consequence all elements implement both equals and
hashCode. Since this model is used as GEF model, it is a requirement that all elements are
observable. This includes collections that contain child elements. Thus, Grasp IDE provides a
framework for observable collections via IObservableCollection, and observable elements via
IObservable. The built-in java.util.Observable is not used, because a class and not an interface, and
because the associated java.util.Observer does not support notification for change in single
properties, at least not in a strongly-typed API.</p>
<p>GraspFile maintains mapping between Eclipse files (IFIle) and Grasp model (ArchitectureModel). It is
in itself observable, allowing for potential listeners to be notified when a file has been successfully
compiled (and loaded from XML). GaspModel is a singleton with utility methods for Grasp model
classes. It most prominently maintains all GraspFile-s, thus providing a single point of access for the
file-to-model mapping.</p>
<p>GraspFile maintains mapping between Eclipse files (IFIle) and Grasp model (ArchitectureModel). It is
in itself observable, allowing for potential listeners to be notified when a file has been successfully
compiled (and loaded from XML). GaspModel is a singleton with utility methods for Grasp model
classes. It most prominently maintains all GraspFile-s, thus providing a single point of access for the
file-to-model mapping.</p>

 */
package uk.ac.standrews.grasp.ide.model;