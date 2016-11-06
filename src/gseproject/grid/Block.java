package gseproject.grid;

public class Block{
	public enum possibleBlockStatus {
		DIRTY,
		CLEANED,
		PAINTED
	}
	
	public possibleBlockStatus Status = possibleBlockStatus.DIRTY;
}