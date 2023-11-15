package it.unicam.cs.ids.loyalty.util;

public class IdGenerator {
	private static IdGenerator instance;
	private int incrementalCounter;

	private IdGenerator() {
		incrementalCounter = 0;
	}

	public static IdGenerator getInstance() {
		if (instance == null) {
			instance = new IdGenerator();
		}
		return instance;
	}

	public int generateIncrementalId() {
		return ++incrementalCounter;
	}
}
