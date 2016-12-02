package gseproject.core;

public enum Color {
	BLACK(1), BLUE(2), GREEN(3), YELLOW(4), RED(5), WHITE(6), BROWN(7);

	private final int value;

	Color(final int newValue) {
		value = newValue;
	}

	public int getValue() {
		return value;
	}
}
