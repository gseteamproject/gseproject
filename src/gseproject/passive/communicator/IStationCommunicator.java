package gseproject.passive.communicator;

import jade.lang.acl.ACLMessage;

public interface IStationCommunicator {
	/**
	 * This method checks whether the station needs the service which is in the
	 * content of the message. If the serviceType is GIVE_BLOCK the message also
	 * contains a block and the station needs to be updated. If the serviceType
	 * is TAKE_BLOCK the return message will contain a block from the station.
	 * 
	 * @param serviceTypeRequest
	 *            the message from the robot who offers a service
	 * @return AGREE-Message if the service is needed else FAILURE-Message
	 */
	public ACLMessage handleServiceTypeRequest(ACLMessage serviceTypeRequest);

	/**
	 * This method creates a message which will be send to the Grid, because the
	 * station-data-object has changed
	 * 
	 * @return INFORM-Message containing the updated station object
	 */
	public ACLMessage updateTrack();

}
