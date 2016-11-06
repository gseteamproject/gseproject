package gseproject.robot.domain;
import gseproject.core.interaction.IState;

public class RobotStateDto implements IState {

    public boolean isCarryingBlock;
    public float position;

    @Override
    public Object Clone() {

        RobotStateDto state = new RobotStateDto();

        state.isCarryingBlock = isCarryingBlock;
        state.position = position;

        return state;
    }
}
