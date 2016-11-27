package gseproject.tests.unitTests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import gseproject.passive.core.Floor;
import gseproject.passive.core.FloorException;

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
