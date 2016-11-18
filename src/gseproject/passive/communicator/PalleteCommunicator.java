package gseproject.passive.communicator;

import gseproject.core.Block;
import gseproject.core.ServiceType;
import gseproject.infrastructure.contracts.ServiceTypeContract;
import gseproject.infrastructure.serialization.SerializationController;
import gseproject.infrastructure.serialization.basic.ServiceTypeReader;
import gseproject.infrastructure.serialization.basic.ServiceTypeWriter;
import gseproject.passive.communicator.IStationCommunicator;
import gseproject.passive.pallete.core.Pallete;
import gseproject.passive.pallete.core.PalleteException;
import jade.lang.acl.ACLMessage;

public class PalleteCommunicator implements IStationCommunicator {
	private ServiceType currServiceType;
	private SerializationController serializationController;
	private Pallete pallete;

	public PalleteCommunicator(Pallete pallete) {
		this.pallete = pallete;
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
			return true;
		}
		case TAKE_BLOCK: {
			return true;
		}
		default:
			return false;
		}
	}

	private ACLMessage replyBlock(ACLMessage messageFromRobot) {
		ACLMessage reply = messageFromRobot.createReply();
		reply.setPerformative(ACLMessage.INFORM);
		try {
			reply.setContent(serializationController.Serialize(pallete.takeBlock()));
		} catch (PalleteException e) {
			reply.setPerformative(ACLMessage.FAILURE);
			return reply;
		}
		return reply;
	}

	private ACLMessage replyTookBlock(ACLMessage messageFromRobot) {
		ACLMessage reply = messageFromRobot.createReply();
		reply.setPerformative(ACLMessage.INFORM);
		try {
			pallete.giveBlock(serializationController.Deserialize(Block.class, messageFromRobot.getContent()));
		} catch (PalleteException e) {
			reply.setPerformative(ACLMessage.FAILURE);
			return reply;
		}
		return reply;
	}

	@Override
	public ACLMessage handleAction(ACLMessage action) {
		switch (currServiceType) {
		case TAKE_BLOCK: {
			return replyBlock(action);
		}
		case GIVE_BLOCK: {
			return replyTookBlock(action);
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
