package gseproject.infrastructure.interaction;

import com.sun.org.apache.xpath.internal.operations.Bool;
import gseproject.IGridSpace;

public interface ISensor<IState> {

	void init();

	public boolean isInGrid(IGridSpace Grid);

	public boolean didPickBlock(IGridSpace Grid);
	IState updateStatement();
}
