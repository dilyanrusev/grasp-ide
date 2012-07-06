package uk.ac.standrews.grasp.ide.editors;

import org.eclipse.core.filebuffers.IDocumentSetupParticipant;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentExtension3;

/**
 * Creates partitions in the document to aid with syntax colouring
 * @author Dilyan Rusev
 *
 */
public class DocumentSetupParticipant implements IDocumentSetupParticipant {
	private static final String PARTITIONING = "uk.ac.standrews.grasp.ide.editors.graspPartitioning";

	@Override
	public void setup(IDocument document) {
		DocumentPartitioner partitionScanner = new DocumentPartitioner(
				PartitionScanner.INSTANCE, PartitionScanner.PARTITIONS);
		if (document instanceof IDocumentExtension3) {
			((IDocumentExtension3)document).setDocumentPartitioner(PARTITIONING, partitionScanner);
		} else {
			document.setDocumentPartitioner(partitionScanner);
		}
		partitionScanner.connect(document);
		
	}

}
