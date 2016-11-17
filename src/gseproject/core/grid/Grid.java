package gseproject.core.grid;

import java.io.Serializable;

public class Grid implements Serializable {
	private static final long serialVersionUID = -7361783478616973845L;
	private final int width;
	private final int height;
	private final SpaceType[][] spaces;

	public Grid(int width, int height, SpaceType[][] spaces) {
		this.width = width;
		this.height = height;
		this.spaces = spaces;
	}

	public Grid(int width, int height) {
		this(width, height, new SpaceType[width][height]);
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

	public void set(int x, int y, SpaceType space) {
		spaces[x][y] = space;
	}

	public SpaceType get(int x, int y) {
		return spaces[x][y];
	}

	public SpaceType[][] getSpaces() {
		return spaces;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		
		if (obj == null) {
			return false;
		}
		
		if (getClass() != obj.getClass()) {
			return false;
		}
		
		Grid grid = (Grid) obj;
		for (int x = 0; x < grid.getWidth(); x++) {
			for (int y = 0; y < grid.getHeight(); y++) {
				if (this.spaces[x][y] != grid.get(x, y)) {
					return false;
				}
			}
		}
		
		return true;
	}

	public static class GridBuilder {
		private final int width;
		private final int height;
		private final SpaceType[][] spaces;

		public GridBuilder(int width, int height) {
			this.width = width;
			this.height = height;
			this.spaces = new SpaceType[width][height];
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					spaces[x][y] = SpaceType.NO_TRACK;
				}
			}
		}

		public GridBuilder setSpaceType(int x, int y, SpaceType type) {
			spaces[x][y] = type;
			return this;
		}

		public Grid build() {
			return new Grid(width, height, spaces);
		}
	}

}