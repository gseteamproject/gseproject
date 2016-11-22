package gseproject.infrastructure.contracts;

import java.io.Serializable;

import gseproject.core.grid.Grid;

public class GridContract implements Serializable {
	private static final long serialVersionUID = 5044875157820601627L;
	private Grid grid;

	public GridContract(Grid grid) {
		this.grid = grid;
	}

	public Grid getGrid() {
		return grid;
	}

	public void setGrid(Grid grid) {
		this.grid = grid;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GridContract other = (GridContract) obj;
		if (grid == null) {
			if (other.grid != null)
				return false;
		} else if (!grid.equals(other.grid))
			return false;
		return true;
	}
}