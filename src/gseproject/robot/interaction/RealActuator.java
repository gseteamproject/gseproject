package gseproject.robot.interaction;

import gseproject.core.Block;

public class RealActuator {

	public void init() {

	}

	/* Return TRUE if Ok, and FALSE if fails */
	public boolean moveForward() {
		
		
		return true;
	}

	public boolean turn() {
		return true;
	}

	public boolean stop() {
		return true;
	}

	/* Return BLOCK if Ok, and null if fails */
	public Block pick() {
		return null;
	}

	/* Return TRUE if Ok, and FALSE if fails */
	public boolean drop() {
		return true;
	}

	/* Return TRUE if Ok, and FALSE if fails */
	public boolean doWork() {
		return true;
	}
}