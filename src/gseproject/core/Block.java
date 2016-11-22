package gseproject.core;

import java.io.Serializable;

public class Block implements Serializable {
	private static final long serialVersionUID = -1589700626683942575L;

	public enum possibleBlockStatus {
		DIRTY, CLEANED, PAINTED
	}

	public possibleBlockStatus Status = possibleBlockStatus.DIRTY;

	@Override
	public String toString() {
		return "Block [Status=" + Status + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Status == null) ? 0 : Status.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Block other = (Block) obj;
		if (Status != other.Status)
			return false;
		return true;
	}

}