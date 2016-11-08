package gseproject.core;

import java.io.Serializable;

public class Block implements Serializable {
    private static final long serialVersionUID = -1589700626683942575L;

    public enum possibleBlockStatus {
	DIRTY, CLEANED, PAINTED
    }

    public possibleBlockStatus Status = possibleBlockStatus.DIRTY;
}