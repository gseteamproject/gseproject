package gseproject.passive.communicator;

import gseproject.core.Block;
import gseproject.core.ServiceType;
import gseproject.infrastructure.contracts.ServiceTypeContract;
import gseproject.infrastructure.serialization.SerializationController;
import gseproject.infrastructure.serialization.basic.ServiceTypeReader;
import gseproject.infrastructure.serialization.basic.ServiceTypeWriter;
import gseproject.passive.communicator.IStationCommunicator;
import gseproject.passive.core.StationException;
import gseproject.passive.floor.core.Floor;
import gseproject.passive.floor.core.FloorException;
import jade.lang.acl.ACLMessage;

public class FloorCommunicator implements IStationCommunicator {
    private Floor floor;
    private ServiceType currServiceType;
    private SerializationController serializationController;

    public FloorCommunicator(Floor floor) {
	this.floor = floor;
	initSerialization();
    }

    private void initSerialization() {
	ServiceTypeReader serviceTypeReader = new ServiceTypeReader();
	ServiceTypeWriter serviceTypeWriter = new ServiceTypeWriter();
	serializationController.RegisterSerializator(ServiceTypeContract.class, serviceTypeWriter, serviceTypeReader);
    }

    private static ACLMessage replyAgreeMessage(ACLMessage message) {
	ACLMessage reply = message.createReply();
	reply.setPerformative(ACLMessage.AGREE);
	return reply;
    }

    private static ACLMessage replyRefuseMessage(ACLMessage message) {
	ACLMessage reply = message.createReply();
	reply.setPerformative(ACLMessage.REFUSE);
	return reply;
    }

    @Override
    public ACLMessage handleServiceTypeRequest(ACLMessage serviceTypeRequest) {
	ServiceTypeContract serviceTypeContract = serializationController.Deserialize(ServiceTypeContract.class,
		serviceTypeRequest.getContent());
	ServiceType serviceType = serviceTypeContract.serviceType;
	if (serviceNeeded(serviceType)) {
	    this.currServiceType = serviceType;
	    return replyAgreeMessage(serviceTypeRequest);
	}
	return replyRefuseMessage(serviceTypeRequest);
    }

    private boolean serviceNeeded(ServiceType serviceType) {
	switch (serviceType) {
	case GIVE_BLOCK: {
	    if (!floor.hasBlock()) {
		return true;
	    }
	    return false;
	}
	case TAKE_BLOCK: {
	    if (floor.hasFinishedBlock()) {
		return true;
	    }
	    return false;
	}
	case FINISH_BLOCK: {
	    if (floor.hasBlock()) {
		return true;
	    }
	    return false;
	}
	default:
	    return false;
	}
    }

    private ACLMessage replyBlock(ACLMessage messageFromRobot) {
	ACLMessage reply = messageFromRobot.createReply();
	reply.setPerformative(ACLMessage.INFORM);
	try {
	    reply.setContent(serializationController.Serialize(floor.takeBlock()));
	} catch (StationException e) {
	    reply.setPerformative(ACLMessage.FAILURE);
	    return reply;
	}
	return reply;
    }

    private ACLMessage replyTookBlock(ACLMessage messageFromRobot) {
	ACLMessage reply = messageFromRobot.createReply();
	reply.setPerformative(ACLMessage.INFORM);
	try {
	    floor.giveBlock(serializationController.Deserialize(Block.class, messageFromRobot.getContent()));
	} catch (StationException e) {
	    reply.setPerformative(ACLMessage.FAILURE);
	    return reply;
	}
	return reply;
    }

    private ACLMessage replyFinishedBlock(ACLMessage messageFromRobot) {
	ACLMessage reply = messageFromRobot.createReply();
	reply.setPerformative(ACLMessage.INFORM);
	try {
	    floor.finishBlock();
	} catch (FloorException e) {
	    reply.setPerformative(ACLMessage.FAILURE);
	    return reply;
	}
	return reply;
    }

    @Override
    public ACLMessage handleAction(ACLMessage action) {
	switch (currServiceType) {
	case GIVE_BLOCK: {
	    //TODO: deregister service
	    //TODO: register "need worker service"
	    return replyTookBlock(action);
	}
	case FINISH_BLOCK: {
	    //TODO: deregister "need worker service"
	    //TODO: register "need transporter service"
	    return replyFinishedBlock(action);
	}
	case TAKE_BLOCK: {
	    //TODO: deregister "need transporter service"
	    //TODO: register "need block service"
	    return replyBlock(action);
	}
	default:
	    return null;
	}
    }

    @Override
    public ACLMessage notifyGrid() {
	return null;
    }

    @Override
    public ACLMessage notifyRobot() {
	// TODO Auto-generated method stub
	return null;
    }

}
