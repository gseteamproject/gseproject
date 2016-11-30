package gseproject.robot.communicator;

import gseproject.robot.controller.IController;
import gseproject.robot.domain.RobotState;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

public class WorkerBehaviour extends CyclicBehaviour {
	private static final long serialVersionUID = -5824911581918820127L;
	private IRobotToStationComm _communicator;
	private IController _controller;
	private RobotState _state;
	private DFAgentDescription service;

	public WorkerBehaviour(IRobotToStationComm communicator, IController controller, RobotState robotState,
			String serviceName, String serviceType) {
		this._communicator = communicator;
		this._controller = controller;
		this._state = robotState;
		ServiceDescription sd = new ServiceDescription();
		sd.setName(serviceName);
		sd.setType(serviceType);
		this.service = new DFAgentDescription();
		this.service.addServices(sd);
	}

	@Override
	public void action() {
		DFAgentDescription[] cleanServices = findCleanServices();
		if (cleanServices != null) {
			if (!_controller.doWork()) {
				// failure
			} else {
				_communicator.requestCleanBlock();
				ACLMessage reply = _communicator.receiveReply();
				if (reply.getPerformative() != ACLMessage.INFORM) {
					System.out.println("performed working successfully");
					this.myAgent.doWait(5000);
				} else {
					System.out.println("working failed");
				}
			}
		}
	}

	private DFAgentDescription[] findCleanServices() {
		DFAgentDescription[] result = null;
		try {
			result = DFService.search(this.myAgent, this.service);
		} catch (FIPAException e) {
			System.out.println(myAgent.getAID().getLocalName() + " couldnt find service");
		}
		if (result.length < 1) {
			return null;
		} else {
			return result;
		}
	}
}
