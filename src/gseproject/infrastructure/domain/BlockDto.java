package gseproject.infrastructure.domain;

public class BlockDto {

    public BlockState state;

    public enum BlockState{
        Dirty,
        Cleared,
        Painted
    }
}
