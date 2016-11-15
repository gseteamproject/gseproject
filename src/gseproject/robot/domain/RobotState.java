package gseproject.robot.domain;
import gseproject.core.grid.Position;
import gseproject.core.interaction.IState;

import java.awt.*;

public class RobotState implements IState {

    public boolean isCarryingBlock;
    public Position position;

    @Override
    public Object Clone() {

        RobotState state = new RobotState();

        state.isCarryingBlock = isCarryingBlock;
        state.position = position;

        return state;
    }
}
