package gseproject.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import gseproject.core.Block;
import gseproject.passive.floor.core.Floor;
import gseproject.passive.floor.core.FloorException;

public abstract class FloorTest {
    protected Floor f;

    @Test
    public void iOccupyTest() {
	try {
	    f.iOccupy();
	} catch (FloorException e) {
	    e.printStackTrace();
	}
	assertTrue(f.isOccupied());
    }

    @Test
    public void iLeaveTest() {
	try {
	    f.iOccupy();
	    f.iLeave();
	} catch (FloorException e) {
	    e.printStackTrace();
	}
	assertFalse(f.isOccupied());
    }
}
