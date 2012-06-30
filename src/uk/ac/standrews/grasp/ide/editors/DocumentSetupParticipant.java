package uk.ac.standrews.grasp.ide.editors;

import org.eclipse.core.filebuffers.IDocumentSetupParticipant;
import org.eclipse.jface.text.IDocument;

/**
 * Creates partitions in the document to aid with syntax colouring
 * @author Dilyan Rusev
 *
 */
public class DocumentSetupParticipant implements IDocumentSetupParticipant {

	@Override
	public void setup(IDocument document) {
		DocumentPartitioner partitionScanner = new DocumentPartitioner(
				new PartitionScanner(), PartitionScanner.PARTITIONS);
		partitionScanner.connect(document, false);
		document.setDocumentPartitioner(partitionScanner);
	}

}
