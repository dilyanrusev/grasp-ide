package uk.ac.standrews.grasp.ide.builder;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import shared.io.ISource;

public class GraspStringSource implements ISource {
	private String name;
	private String fullName;
	private String contents;
	
	public GraspStringSource(String name, String fullName, String contents) {
		this.name = name;
		this.fullName = fullName;
		this.contents = contents;
	}

	@Override
	public String getFullName() {
		return fullName;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Reader getReader() throws IOException {
		return new StringReader(contents);
	}
	
	@Override
	public String toString() {
		return name;
	}

}
