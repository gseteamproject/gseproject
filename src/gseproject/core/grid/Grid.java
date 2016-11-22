package gseproject.core.grid;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import gseproject.core.grid.objects.GridObject;
import gseproject.core.grid.objects.SourcePalette;
import gseproject.robot.domain.RobotState;
import jade.core.AID;
import gseproject.core.Direction;
import gseproject.core.grid.Position;


/**
 * 
 * @author Steffen Teichmann
 * @version 2016-11-17
 *
 */
public class Grid implements Serializable {

	private static final long serialVersionUID = 6133479077974440132L;
	private int width;
	private int height;
	private SpaceType[][] tiles; // this is the overall space devided in tiles
	private Map<AID, GridObject> gridObjects;
	
	public Grid(int width, int height, SpaceType[][] tiles, Map<AID, GridObject> gridObjects) {
		this.width  = width;
		this.height = height;
		this.tiles  = tiles;
		this.gridObjects = gridObjects;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public SpaceType[][] getTiles() {
		return tiles;
	}
	
	public Map<AID, GridObject> getGridObjects() {
		return gridObjects;
	}
	
	public SpaceType getSpaceType(int x, int y) {
		return tiles[x][y];
	}
	
	public SpaceType getSpaceType(Position position) {
		return tiles[position.getX()][position.getY()];
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
		private SpaceType[][] tiles;
		private Map<AID, GridObject> gridObjects;
		
		public GridBuilder(int width, int height) {
			this.width	= width;
			this.height = height;
			this.tiles	= new SpaceType[width][height];
			this.gridObjects = new HashMap<>();
			initializeTiles(tiles);
		}
		
		public GridBuilder addGridObject(AID aid, GridObject gridObject) {
			Position position = gridObject.getPosition();
			for (int x = position.getX(); x < position.getX() + gridObject.getWidth(); x++) {
				for (int y = position.getY(); y < position.getY() + gridObject.getHeight(); y++) {
					tiles[x][y] = gridObject.getTile();
					gridObjects.put(aid, gridObject);
				}
			}
			return this;
		}
		
		public GridBuilder addTrack(Position position, Direction direction, int length) {
			Position next = new Position(position);
			for (int i = 0; i < length; i++) {
				tiles[next.getX()][next.getY()] = SpaceType.TRACK;
				next = RobotPositionResponder.getNextPosition(next, direction);
			}
			return this;
		}
		
		public Grid build() {
			return new Grid(width, height, tiles, gridObjects);
		}

		public void setTile(int x, int y, SpaceType tile) {
			this.tiles[x][y] = tile;
		}
	}
	

	private static void initializeTiles(SpaceType[][] tiles, SpaceType tile) {
		for (SpaceType[] row : tiles) {
			Arrays.fill(row, tile);
		}
	}
	
	private static void initializeTiles(SpaceType[][] tiles) {
		initializeTiles(tiles, SpaceType.NO_TRACK);
	}
}