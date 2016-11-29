package gseproject.robot.communicator;

import gseproject.core.Block;
import jade.lang.acl.ACLMessage;

public interface IRobotToStationComm {
	public void requestDirtyBlock();
	public void giveDirtyBlock(Block dirtyBlock);
	public void requestCleanedBlock();
	public void giveCleanedBlock(Block cleanedBlock);
	public void requestPaintedBlock();
	public void givePaintedBlock(Block paintedBlock);
	public ACLMessage receiveReply();
}
