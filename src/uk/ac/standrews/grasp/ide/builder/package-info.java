/**
 * Contains the incremental builder and the project nature.
 * 
 * <p>GraspNature associates GraspBuilder with a project. The builder uses the abstracted compiler
interface (in the next section) to build individual Grasp source code files. GraspBuilder’s methods are
invoked by the Eclipse runtime. GraspNature is associated with a project when a project is created,
that is – by the new project wizard.</p>
 */
package uk.ac.standrews.grasp.ide.builder;