package gseproject.tests;

import org.junit.Before;

import gseproject.passive.floor.CleaningFloor;

public class CleaningFloorTest extends FloorTest {
    @Before
    public void initialize() {
	this.f = new CleaningFloor();
    }
}
