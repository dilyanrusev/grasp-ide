/**
 * Code completion engine.
 * 
 * <p>GraspScanner is a class that re-implements the Grasp compiler. The idea is that the compiler should
be able to handle errors gracefully. Instead of returning a null Grasp architecture graph when there
are errors in the code, the scanner should be able to ignore errors. It should also report the position
in the text document of every language element, in addition to any other information that might be
helpful not only for code completion, but also for refactoring. Implementing this class is not a
priority, but without it context-sensitive code completion is impossible.</p>
<p>ICodeCompletionContext and ICodeCompletionProcessor are Grasp IDE’s own API for code
completion. The idea is to turn GraspCodeCompletionProcessor into rule-based engine. It provides a
context, which each rule evaluates. If the rule succeeds in providing any code completion
suggestions, the rest of the rules are ignored. Thus, code for keyword-base code completion can be
separated from code for completing individual Grasp statements. In fact, every Grasp statement can
have its own rule, thus greatly simplifying the that handles each individual statement.</p>

 */
package uk.ac.standrews.grasp.ide.editors.completion;