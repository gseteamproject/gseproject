package gseproject.robot.domain;

import gseproject.core.Block;
import gseproject.core.grid.Position;
import gseproject.core.interaction.IState;

public class RobotState implements IState {

	public boolean isCarryingBlock;
	public Position position;
	public Block block;

	@Override
	public IState Clone() {

		RobotState state = new RobotState();

		state.isCarryingBlock = isCarryingBlock;
		state.position = position;
		state.block = block;

		return state;
	}

	@Override
	public String toString() {
		return "RobotState [isCarryingBlock=" + isCarryingBlock + ", position=" + position + ", block=" + block + "]";
	}

}
