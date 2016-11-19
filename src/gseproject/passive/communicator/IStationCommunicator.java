package gseproject.passive.communicator;

import jade.lang.acl.ACLMessage;

public interface IStationCommunicator {
    /**
     * This method checks whether the station needs the service which is
     * serialized in the serviceTypeRequest
     * 
     * @param serviceTypeRequest
     *            the message from the robot who offers a service
     * @return AGREE-Message if the service is needed else REFUSE-Message
     */
    public ACLMessage handleServiceTypeRequest(ACLMessage serviceTypeRequest);

    /**
     * This method updates the the station with the content from the given
     * message
     * 
     * @param actionDone
     *            the message from the robot which informs the sation-agent that
     *            he has done an action
     * @return INFORM-Message if updating station was successful else
     *         FAILURE-Message
     */
    public ACLMessage handleAction(ACLMessage actionDone);

    /**
     * This method creates a message which will be send to the Grid, because the
     * floor-data-object has changed
     * 
     * @return INFORM-Message containing the new floor object
     */
    public ACLMessage notifyGrid();

    /**
     * This creates a message which will be send to the robot, who stands on the
     * station. The message will notify the robot, that he can start working
     * (e.g. clean, paint)
     * 
     * @return INFORM-Message
     */
    public ACLMessage notifyRobot();
}
