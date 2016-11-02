package gseproject.infrastructure.interaction;

/*
	Every Actuators implementation should have action methods with callback
 */

import gseproject.robot.interaction.IRobotActions;

public interface IActuator extends IRobotActions {
	
	void init();

}
