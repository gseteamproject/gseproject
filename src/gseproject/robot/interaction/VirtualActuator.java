package gseproject.robot.interaction;

import gseproject.core.Block;
import gseproject.core.grid.Position;
import gseproject.robot.skills.SkillsSettings;

public class VirtualActuator {
	
	private SkillsSettings _skillsSettings;
	
	public VirtualActuator(SkillsSettings skillsSettings){
		this._skillsSettings = skillsSettings;
	}
	public VirtualActuator(){
	}
	public void init() {

	}

	/* Return TRUE if Ok, and FALSE if fails */
	public boolean move(Position goalPosition) {
		System.out.println("moving to " + goalPosition);
		return true;
	}

	/* Return Block instance if Ok, and null if fails */
	public Block pick(Position blockPosition) {
		return null;
	}

	/* Return TRUE if Ok, and FALSE if fails */
	public boolean drop(Position dropPosition) {
		return true;
	}

	/* Return TRUE if Ok, and FALSE if fails */
	public boolean doWork(Position workPosition) {

		return true;
	}
}