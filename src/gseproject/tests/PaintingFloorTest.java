package gseproject.tests;

import org.junit.Before;

import gseproject.passive.floor.PaintingFloor;

public class PaintingFloorTest extends FloorTest{
    @Before
    public void initialize() {
	this.f = new PaintingFloor();
    }
}
