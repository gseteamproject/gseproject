package gseproject.experiments.grid;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import gseproject.experiments.grid.objects.GridObject;
import gseproject.experiments.grid.objects.SourcePalette;


/**
 * 
 * @author Steffen Teichmann
 * @version 2016-11-17
 *
 */
public class Grid {

	private int width;
	private int height;
	private Tile[][] tiles;
	private Map<String, GridObject> gridObjects;
	
	public Grid(int width, int height, Tile[][] tiles, Map<String, GridObject> gridObjects) {
		this.width  = width;
		this.height = height;
		this.tiles  = tiles;
		this.gridObjects = gridObjects;
	}

	public Grid(int width, int height) {
		this(width, height, new Tile[width][height], new HashMap<>());
		initializeTiles(tiles);
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public Tile[][] getTiles() {
		return tiles;
	}
	
	public Map<String, GridObject> getGridObjects() {
		return gridObjects;
	}
	
	public Tile getTile(int x, int y) {
		return tiles[x][y];
	}
	
	public Tile getTile(Position position) {
		return getTile(position.getX(), position.getY());
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

		Grid other = (Grid) obj;
		
		return Arrays.deepEquals(this.tiles, other.tiles);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("");
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				sb.append(String.format("[%s]", tiles[x][y].name()));
			}
			sb.append(System.lineSeparator());
		}
		return sb.toString();
	}

	public static class GridBuilder {
		
		private int width;
		private int height;
		private Tile[][] tiles;
		private Map<String, GridObject> gridObjects;
		
		public GridBuilder(int width, int height) {
			this.width	= width;
			this.height = height;
			this.tiles	= new Tile[width][height];
			initializeTiles(tiles);
			this.gridObjects = new HashMap<>();
		}
		
		public GridBuilder addSourcePalette(String id, SourcePalette sourcePalette) {
			Position position = sourcePalette.getPosition();
			for (int x = position.getX(); x < position.getX() + sourcePalette.getWidth(); x++) {
				for (int y = position.getY(); y < position.getY() + sourcePalette.getHeight(); y++) {
					tiles[x][y] = sourcePalette.getTile();
					gridObjects.put(id, sourcePalette);
				}
			}
			return this;
		}
		
		public GridBuilder addGridObject(String id, GridObject gridObject) {
			Position position = gridObject.getPosition();
			for (int x = position.getX(); x < position.getX() + gridObject.getWidth(); x++) {
				for (int y = position.getY(); y < position.getY() + gridObject.getHeight(); y++) {
					tiles[x][y] = gridObject.getTile();
					gridObjects.put(id, gridObject);
				}
			}
			return this;
		}
		
		public Grid build() {
			return new Grid(width, height, tiles, gridObjects);
		}
	}
	

	private static void initializeTiles(Tile[][] tiles, Tile tile) {
		for (Tile[] row : tiles) {
			Arrays.fill(row, tile);
		}
	}
	
	private static void initializeTiles(Tile[][] tiles) {
		initializeTiles(tiles, Tile.NO_TRACK);
	}
}