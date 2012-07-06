package uk.ac.standrews.grasp.ide.editors;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.jface.text.rules.IPartitionTokenScanner;

/**
 * Extend the default partitioner to enable printing debugging statements
 * @author Dilyan Rusev
 *
 */
class DocumentPartitioner extends FastPartitioner {

	public DocumentPartitioner(IPartitionTokenScanner scanner, String[] legalContentTypes) {
		super(scanner, legalContentTypes);
	}
	
	@Override
	public void connect(IDocument document, boolean delayInitialization) {
		super.connect(document, delayInitialization);
		printPartitions(document);
	}
	
	private void printPartitions(IDocument document) {
		// http://www.realsolve.co.uk/site/tech/jface-text.php
//		StringBuffer buffer = new StringBuffer();
//
//	    ITypedRegion[] partitions = computePartitioning(0, document.getLength());
//	    for (int i = 0; i < partitions.length; i++)
//	    {
//	        try
//	        {
//	            buffer.append("Partition type: " 
//	              + partitions[i].getType() 
//	              + ", offset: " + partitions[i].getOffset()
//	              + ", length: " + partitions[i].getLength());
//	            buffer.append("\n");
//	            buffer.append("Text:\n");
//	            buffer.append(document.get(partitions[i].getOffset(), 
//	             partitions[i].getLength()));
//	            buffer.append("\n---------------------------\n\n\n");
//	        }
//	        catch (BadLocationException e)
//	        {
//	            e.printStackTrace();
//	        }
//	    }
//	    System.out.print(buffer);
	}

}
