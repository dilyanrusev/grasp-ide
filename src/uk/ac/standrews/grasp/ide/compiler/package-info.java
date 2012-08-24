/**
 * API for calling the Grasp compiler.
 * 
 * <p>ICompiler is the common compiler interface to both the integrated, and the external compilers. The
shared compiler defines API which is independent on the Grasp compiler’s API.
ICompiler compiles one file at a time, and can optionally return a list of errors, along with other
information if the build was successful. It does not return the XML file that is produced as a result of
a successful compilation.</p>
<p>ExternalCompiler implements the common interface and uses the plugin preferences to invoke a
custom compiler specified by the user. IntegratedCompiler uses the Java API of the compiler that is
embedded into the plugin itself. IntegratedCompiler is one of the only places where the Grasp
compiler’s Java API is allowed to be used. This makes the plugin more resistant to breaking future
changes. The common API for invoking the Grasp compiler also makes it possible for the plugin to
work with a newer version of the Grasp compiler. It will work so long as the compiler output format
for messages remains the same and so long as no breaking changes to the XML schema for serialising
the Grasp architecture graph are introduced in future versions.</p>

 */
package uk.ac.standrews.grasp.ide.compiler;