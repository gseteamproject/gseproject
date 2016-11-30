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
	private String serviceName;
	
	public WorkerBehaviour(IRobotToStationComm communicator, IController controller, RobotState robotState,
			String serviceName, String serviceType) {
		this._communicator = communicator;
		this._controller = controller;
		this._state = robotState;
		this.serviceName = serviceName;
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
				if(this.serviceName.equals("needClean")){
					_communicator.requestCleanBlock();
				} else {
					_communicator.requestPaintBlock();;
				}
				ACLMessage reply = _communicator.receiveReply();
				if (reply.getPerformative() == ACLMessage.INFORM) {
					System.out.println("performed working successfully");
				} else {
					System.out.println("working failed");
				}
			}
		}
		this.myAgent.doWait(1000);
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
