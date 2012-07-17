package uk.ac.standrews.grasp.ide.builder;

import shared.logging.ILogger;

public class NullLogger implements ILogger {
	public static final NullLogger INSTANCE = new NullLogger();
	
	
	@Override
	public void compiler_error(String s, Object... aobj) {
		// ignore

	}

	@Override
	public void compiler_warn(String s, Object... aobj) {
		// ignore

	}

	@Override
	public void error(String s, Object... aobj) {
		// ignore

	}

	@Override
	public void error(String s, Exception exception) {
		// ignore

	}

	@Override
	public void info(String s, Object... aobj) {
		// ignore

	}

	@Override
	public ILogger initialize(String s, Level level, boolean flag) {
		// ignore
		return null;
	}

	@Override
	public void print() {
		// ignore

	}

	@Override
	public void print(String s, Object... aobj) {
		// ignore

	}

	@Override
	public void shutdown() {
		// ignore

	}

	@Override
	public void trace(String s, Object... aobj) {
		// ignore

	}

	@Override
	public void warn(String s, Object... aobj) {
		// ignore

	}

}
