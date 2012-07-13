package uk.ac.standrews.grasp.ide.editors.completion;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * Scans a grasp source file
 * @author Dilyan Rusev
 *
 */
public class GraspScanner {
	private List<IChunk> chunks;
	private int currentChunkIndex;
	
	public GraspScanner() {
		this.chunks = new ArrayList<IChunk>();
		this.currentChunkIndex = -1;
	}
	
	public void parse(Reader input) {
		chunks.clear();
		ChunkReader reader = null;
		try {
			reader = new ChunkReader(new CharacterReader(input));
			IChunk current = null;
			while ((current = reader.nextChunk()) != null) {
				chunks.add(current);
			}			
			System.out.println(chunks);			
		} finally {
			if (reader != null) {
				reader.dispose();
			}
		}
	}
	
	public IChunk getChunkAtPosition(int line, int column) {
		for (IChunk chunk: chunks) {
			if (chunk.getLine() == line
					&& column >= chunk.getColumn() 
					&& column < chunk.getColumnEnd()) {
				return chunk;
			}
		}
		return null;
	}
	
	public void setCurrentChunk(IChunk chunk) {
		currentChunkIndex = chunks.indexOf(chunk);
	}
	
	public boolean hasNextChunk() {
		int proposed = currentChunkIndex + 1;
		return proposed >= 0 && proposed < chunks.size();
	}
	
	public IChunk nextChunk() {
		if (hasNextChunk()) {
			currentChunkIndex++;
			return chunks.get(currentChunkIndex);
		} else {
			return null;
		}
	}
	
	public boolean hasPrevChunk() {
		int proposed = currentChunkIndex - 1;
		return proposed >= 0 && proposed < chunks.size();
	}
	
	public IChunk prevChunk() {
		if (hasPrevChunk()) {
			currentChunkIndex--;
			return chunks.get(currentChunkIndex);
		} else {
			return null;
		}
	}
	
	public void reset() {
		currentChunkIndex = -1;
	}
	
	@Override
	public String toString() {
		return chunks != null ? chunks.toString() : "<empty GraspScanner>";
	}
}
