package gseproject.core.grid;

import java.io.Serializable;

public class Position implements Serializable {
	private static final long serialVersionUID = 9059049490283355217L;
	private int x;
	private int y;

	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Position(Position position) {
		this(position.getX(), position.getY());
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof Position)) {
			return false;
		}

		Position pos = (Position) obj;
		return pos.getX() == this.getX() && pos.getY() == this.getY();
	}

	public String toString() {
		return String.format("[x=%d; y=%d]", x, y);
	}
}
