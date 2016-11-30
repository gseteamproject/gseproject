package gseproject.passive.communicator;

import java.io.IOException;

import gseproject.core.Block;
import gseproject.core.ServiceType;
import gseproject.passive.core.CleaningFloor;
import gseproject.passive.core.Floor;
import gseproject.passive.core.FloorException;
import gseproject.passive.core.StationException;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public class FloorCommunicator extends StationCommunicator {
	private Floor floor;
	private Agent floorAgent;

	private DFAgentDescription needBlock = new DFAgentDescription();
	private DFAgentDescription needTransporter = new DFAgentDescription();
	private DFAgentDescription needWorker = new DFAgentDescription();

	public FloorCommunicator(Floor floor, Agent floorAgent) {
		this.floor = floor;
		this.floorAgent = floorAgent;
		initServices();
	}

	private void initServices() {
		ServiceDescription needBlock = new ServiceDescription();
		needBlock.setName("needBlock");
		needBlock.setType("needBlock");
		this.needBlock.addServices(needBlock);

		ServiceDescription needTransporter = new ServiceDescription();
		needTransporter.setName("needTransporter");
		needTransporter.setType("needTransporter");
		this.needTransporter.addServices(needTransporter);

		ServiceDescription needWorker = new ServiceDescription();
		if(this.floor instanceof CleaningFloor){
			needWorker.setName("needClean");
			needWorker.setType("needClean");
		} else {
			needWorker.setName("needPaint");
			needWorker.setType("needPaint");
		}
		this.needTransporter.addServices(needWorker);
	}

	private static ACLMessage addBlockToMessage(ACLMessage message, Block block) {
		if (block == null) {
			message.setPerformative(ACLMessage.FAILURE);
			return message;
		}
		try {
			message.setContentObject(block);
		} catch (IOException e) {
			e.printStackTrace();
			message.setPerformative(ACLMessage.FAILURE);
			return message;
		}
		return message;
	}

	private ACLMessage replyBlock(ACLMessage message) {
		Block block = null;
		try {
			block = floor.takeBlock();
		} catch (StationException e) {
			e.printStackTrace();
			return failureMessage(message);
		}
		return addBlockToMessage(informMessage(message), block);
	}

	private ACLMessage handleTakeBlockRequest(ACLMessage serviceTypeRequest) {
		if (!floor.hasFinishedBlock()) {
			try {
				floor.takeBlock();
			} catch (StationException e) {
				e.printStackTrace();
			}
			return failureMessage(serviceTypeRequest);
		}
		try {
			DFService.deregister(this.floorAgent, this.needTransporter);
			DFService.register(this.floorAgent, this.needBlock);
		} catch (FIPAException e) {
			e.printStackTrace();
		}
		return replyBlock(serviceTypeRequest);
	}

	private ACLMessage handleOccupyRequest(ACLMessage serviceTypeRequest) {
		if (floor.isOccupied()) {
			return failureMessage(serviceTypeRequest);
		} else {
			try {
				floor.iOccupy();
			} catch (FloorException e) {
				e.printStackTrace();
				return failureMessage(serviceTypeRequest);
			}
			return informMessage(serviceTypeRequest);
		}
	}

	private ACLMessage handleGiveBlockRequest(ACLMessage serviceTypeRequest) {
		Block block = null;
		try {
			block = (Block) serviceTypeRequest.getContentObject();
		} catch (UnreadableException e) {
			e.printStackTrace();
		}
		if (block != null && !floor.hasBlock()) {
			try {
				floor.giveBlock(block);
			} catch (StationException e) {
				e.printStackTrace();
				return failureMessage(serviceTypeRequest);
			}
		} else {
			return failureMessage(serviceTypeRequest);
		}
		try {
			DFService.deregister(this.floorAgent, this.needBlock);
			DFService.register(this.floorAgent, this.needWorker);
		} catch (FIPAException e) {
			e.printStackTrace();
		}
		return informMessage(serviceTypeRequest);
	}

	private ACLMessage handleFinishBlockRequest(ACLMessage serviceTypeRequest) {
		if (!floor.isOccupied() || !floor.hasBlock()) {
			return failureMessage(serviceTypeRequest);
		} else {
			try {
				floor.finishBlock();
			} catch (FloorException e) {
				e.printStackTrace();
				return failureMessage(serviceTypeRequest);
			}
			try {
				DFService.deregister(this.floorAgent, this.needWorker);
				DFService.register(this.floorAgent, this.needTransporter);
			} catch (FIPAException e) {
				e.printStackTrace();
			}
			return informMessage(serviceTypeRequest);
		}
	}

	@Override
	public ACLMessage handleServiceTypeRequest(ACLMessage serviceTypeRequest) {
		if (serviceTypeRequest == null || serviceTypeRequest.getContent() == null) {
			return failureMessage(serviceTypeRequest);
		}
		String serviceType = serviceTypeRequest.getContent();
		if (serviceType.equals(ServiceType.TAKE_BLOCK.name())) {
			return handleTakeBlockRequest(serviceTypeRequest);
		} else if (serviceType.equals(ServiceType.I_OCCUPY.name())) {
			return handleOccupyRequest(serviceTypeRequest);
		} else if (serviceType.equals(ServiceType.FINISH_BLOCK.name())) {
			return handleFinishBlockRequest(serviceTypeRequest);
		} else {
			return handleGiveBlockRequest(serviceTypeRequest);
		}
	}

	@Override
	public ACLMessage notifyGrid() {
		ACLMessage message = new ACLMessage(ACLMessage.INFORM);
		return message;
	}

}
