/**
 * Example code showing how to launch the Grasp compiler from within
 * a Java program.
 * 
 * @author Lakshitha de Silva
 *
 */

import java.io.BufferedWriter;
import java.io.FileWriter;

import shared.io.FileSource;
import shared.io.ISource;
import shared.logging.ConsoleLogger;
import shared.logging.ILogger;
import shared.xml.DomXmlWriter;
import shared.xml.IXmlWriter;

import grasp.lang.Compiler;
import grasp.lang.IArchitecture;
import grasp.lang.ICompiler;
import grasp.lang.IFirstClass;



public class Launcher {
	/**
	 * Launch grasp compiler
	 * @param args
	 */
	public static void main(String[] args) {
		/*
		 * Setup a logger. Compiler errors are reported through the logger
		 */
		ILogger logger = new ConsoleLogger().initialize("Grasp", ILogger.Level.ERROR, true);
		
		/*
		 * Create a file source object for input file
		 */
		ISource source = new FileSource("wsn_simulator.grasp");
		
		/*
		 * Create the compiler object and invoke compiler
		 */
		logger.print("Compiling Grasp source file...");
		ICompiler compiler = new Compiler();
		IArchitecture graph = compiler.compile(source, logger);
		
		/*
		 * Check if there were any compiler errors
		 */
		if (compiler.getErrors().isAny() || graph == null) {
			logger.print("Errors encountered during compilation.");
			return;
		}

		/*
		 * Encode architecture graph in XML and write to a file
		 */
		logger.print("Converting architecture graph into xml. ...");
		try {
			BufferedWriter output = new BufferedWriter(new FileWriter("out.xml"));
			IXmlWriter xml = new DomXmlWriter();
			graph.toXml(xml);
			xml.serialize(output);
			output.close();
		} catch (Exception e) {
			logger.error("Unable to write to xml file.", e);
			return;
		}

		/*
		 * Print architecture graph while navigating through it
		 */
		logger.print("Printing architecture graph...");
		printArchitecture(graph, 0);
		
		logger.print("Compilation successfull.");
		logger.shutdown();
	}
	
	/**
	 *  Print architecture graph recursively. This method illustrates how to navigate
	 *  the architecture graph generate by the Grasp compiler.
	 * @param node
	 * @param indent
	 */
	private static void printArchitecture(IFirstClass node, int indent) {
		StringBuffer sb = new StringBuffer(indent);
		for ( int i = 0; i < indent; i++ )
			sb = sb.append("    ");
		System.out.print(sb.toString());
		System.out.printf("%s <%s (%s)>\n", node.getType().toString(), node.getName(), node.getAlias());
		for (IFirstClass child : node.getBody()) {
			printArchitecture(child, indent+1);
		}
	}
}
