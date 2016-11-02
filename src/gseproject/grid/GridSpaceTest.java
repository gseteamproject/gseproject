package gseproject.grid;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;
import gseproject.grid.*;

public class GridSpaceTest {
	
	@Test
	public void testGetDescription() {
		ArrayList<Direction> directions = new ArrayList<Direction>(2);
		directions.add(Direction.FORWARD);
		
		GridSpace space = new GridSpace(0,1,SpaceType.DEFAULT,directions);
		assertFalse((SpaceType.PAINT_TRANSPORT==space.getSpaceType()));
		assertTrue(SpaceType.DEFAULT == space.getSpaceType());
	}
	@Test
	public void testSetDescription() {
		ArrayList<Direction> directions = new ArrayList<Direction>(2);
		directions.add(Direction.FORWARD);
		
		GridSpace space = new GridSpace(0,1,SpaceType.DEFAULT,directions);
		space.setSpaceType(SpaceType.CLEAN_TRANSPORT);
		assertTrue(SpaceType.CLEAN_TRANSPORT == space.getSpaceType());
	}
	@Test
	public void testGetXCoordinate() {
		ArrayList<Direction> directions = new ArrayList<Direction>(2);
		directions.add(Direction.FORWARD);
		
		GridSpace space = new GridSpace(4,2,SpaceType.DEFAULT,directions);
		assertEquals(4,space.getXCoordinate());
	}
	@Test
	public void testGetYCoordinate() {
		ArrayList<Direction> directions = new ArrayList<Direction>(2);
		directions.add(Direction.FORWARD);
		
		GridSpace space = new GridSpace(6,1,SpaceType.CLEAN_WORK,directions);
		assertEquals(1,space.getYCoordinate());
	}
	
	@Test
	public void testGetDirections() {
		ArrayList<Direction> directions = new ArrayList<Direction>(2);
		directions.add(Direction.FORWARD);
		directions.add(Direction.RIGHT);
		
		GridSpace space = new GridSpace(0,1,SpaceType.DEFAULT,directions);
		
		assertTrue(space.getDirections().get(0) == Direction.FORWARD);
		assertTrue(space.getDirections().get(1) == Direction.RIGHT);
		
	}
	
}
