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
	
	public IChunk getCurrentChunk() {
		if (currentChunkIndex >= 0 && currentChunkIndex < chunks.size()) {
			return chunks.get(currentChunkIndex);
		} else {
			return null;
		}
	}
	
	public IChunk getChunkAtPosition(int line, int column) {
		return findChunkAtPosition(line, column).getChunk();
	}
	
	private ChunkAtPosition findChunkAtPosition(int line, int column) {
		for (int i = 0, len = chunks.size(); i < len; i++) {
			IChunk chunk = chunks.get(i);
			if (chunk.getLine() == line
					&& column >= chunk.getColumn() 
					&& column < chunk.getColumnEnd()) {
				return new ChunkAtPosition(i, chunk);
			}
		}
		return ChunkAtPosition.NOT_FOUND;
	}
	
	public IChunk getChunkBeforePosition(int line, int column) {
		return findChunkBeforePosition(line, column).getChunk();
	}
	
	private ChunkAtPosition findChunkBeforePosition(int line, int column) {	
		ChunkAtPosition next = findChunkAfterPosition(line, column);
		if (next.getIndex() >= 1) {
			int idx = next.getIndex() - 1;
			IChunk chunk = chunks.get(idx);
			return new ChunkAtPosition(idx, chunk);
		}		
		return ChunkAtPosition.NOT_FOUND;
	}
	
	public IChunk getChunkAfterPosition(int line, int column) {
		return findChunkAfterPosition(line, column).getChunk();
	}
	
	private ChunkAtPosition findChunkAfterPosition(int line, int column) {
		for (int i = 0, len = chunks.size(); i < len; i++) {
			IChunk chunk = chunks.get(i);
			if ((chunk.getLine() == line && chunk.getColumn() > column)
					|| chunk.getLine() > line) {
				return new ChunkAtPosition(i, chunk);
			}
		}
		return ChunkAtPosition.NOT_FOUND;
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

final class ChunkAtPosition {
	public static final ChunkAtPosition NOT_FOUND = new ChunkAtPosition(-1, null);
	
	private final int index;
	private final IChunk chunk;
	
	public ChunkAtPosition(int index, IChunk chunk) {
		this.index = index;
		this.chunk = chunk;
	}		
	
	public IChunk getChunk() {
		return chunk;
	}
	
	public int getIndex() {
		return index;
	}
	
	public boolean isFound() {
		return index != -1;
	}
}
