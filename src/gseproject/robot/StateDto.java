package gseproject.robot;
import gseproject.interaction.IState;

public class StateDto implements IState {

    public boolean isCarryingBlock;
    public float position;

    @Override
    public Object Clone() {

        StateDto state = new StateDto();

        state.isCarryingBlock = isCarryingBlock;
        state.position = position;

        return state;
    }
}
