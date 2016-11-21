package gseproject.core.grid;

public class Position {

	private int x;
	private int y;
	
	public Position (int x, int y) {
		this.x = x;
		this.y = y;
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
		return pos.getX() == this.getX() 
			&& pos.getY() == this.getY();
	}

	public String toString() {
		return String.format("[x=%d; y=%d]", x, y);
	}
}
