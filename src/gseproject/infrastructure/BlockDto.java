package gseproject.infrastructure;

public class BlockDto {

    public BlockState state;

    public enum BlockState{
        Dirty,
        Cleared,
        Painted
    }
}
