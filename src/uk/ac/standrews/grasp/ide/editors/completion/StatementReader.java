package uk.ac.standrews.grasp.ide.editors.completion;

import grasp.lang.IArchitecture;
import grasp.lang.IElement;
import grasp.lang.elements.Architecture;

import java.util.ArrayList;
import java.util.List;

public class StatementReader {
	private GraspScanner scanner;
	private List<Statement> statements;
	
	
	public StatementReader(GraspScanner scanner) {
		this.scanner = scanner;
		this.statements = new ArrayList<Statement>();
	}
	
	public void parse() {
		parseArchitecture();		
	}
	
	private IArchitecture parseArchitecture() {
		scanner.reset();
		IArchitecture arch = new Architecture();
		Statement statement = new Statement(arch);
		IChunk chunk = scanner.nextChunk();
		if (chunk == null) { finishStatement(statement, null); return arch; }
		
		
		return arch;
	}
	
//	private IAnnotation parseAnnotation(IElement parent) {
//		if (getNextChunk() == null) return null;
//		if (!current.getText().equals("@")) return null;		
//		List<IChunk> chunks = new ArrayList<IChunk>();
//		chunks.add(current);
//		if (getNextChunk() == null) return null;
//		IAnnotation annotation = new Annotation(parent);
//		String name = current.getText();
//		if (!"(".equals(name) && isIdentifier(name)) {
//			annotation.setHandler(name);
//			chunks.add(current);
//			if (getNextChunk() == null) return null;		
//		}
//		if (!"(".equals(current.getText())) return null;
//		chunks.add(current);
//		if (getNextChunk() == null) return null;	
//		
//	}
	
	private void finishStatement(Statement statement, IChunk end) {
		IChunk realEnd = end != null ? end : Chunk.NULL;
		if (statement.getChunkStart() == null) {
			statement.setChunkStart(Chunk.NULL);			
		} 
		statement.setChunkEnd(realEnd);
		statements.add(statement);
	}
	
//	private boolean isIdentifier(String str) {
//		for (int i = 0; i < str.length(); i++) {
//			if (!TextUtil.isIdentifier(str.charAt(i))) return false;
//		}
//		return true;
//	}
}

class Statement {
	private IElement element;
	private IChunk chunkStart;
	private IChunk chunkEnd;
	
	public Statement(IElement elem) {
		this.element = elem;
	}
	
	public IElement getElement() {
		return element;
	}
	public void setElement(IElement element) {
		this.element = element;
	}
	public IChunk getChunkStart() {
		return chunkStart;
	}
	public void setChunkStart(IChunk chunkStart) {
		this.chunkStart = chunkStart;
	}
	public IChunk getChunkEnd() {
		return chunkEnd;
	}
	public void setChunkEnd(IChunk chunkEnd) {
		this.chunkEnd = chunkEnd;
	}	
}
