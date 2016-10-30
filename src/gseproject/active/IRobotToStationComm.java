package gseproject.active;

public interface IRobotToStationComm {
	
	public Object receiveReply();
	public void informState(Object info);
}
