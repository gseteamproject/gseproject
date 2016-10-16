package gseproject.infrastructure.contracts;


import java.util.UUID;

public class RobotStateContract implements IContract{

    private static final UUID id = UUID.fromString("2492633c-192c-464f-bb56-41951b193e90");

    public boolean isCarryingBlock;
    public float position;

    @Override
    public UUID getId() {

        return this.id;
    }
}
