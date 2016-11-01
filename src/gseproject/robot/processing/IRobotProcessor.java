package gseproject.robot.processing;

import com.sun.org.apache.xpath.internal.operations.Bool;
import gseproject.IGridSpace;
import jade.lang.acl.ACLMessage;

import java.util.List;

/**
 * Created by Greg Metelev on 02/11/2016.
 */
public interface IRobotProcessor {

    public void AddMessage(ACLMessage message);
    public IGridSpace CalculatePath(IGridSpace position);
    public boolean GetTask(String task);
    public boolean GoToGrid(IGridSpace Grid);
}
