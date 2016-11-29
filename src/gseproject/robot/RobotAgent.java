package gseproject.robot;

import gseproject.core.Block;
import gseproject.core.ICallbackArgumented;
import gseproject.core.ServiceType;
import gseproject.core.grid.Position;
import gseproject.core.interaction.IActuator;
import gseproject.infrastructure.contracts.ProtocolTemplates;
import gseproject.robot.communicator.DummyCommunicator;
import gseproject.robot.communicator.ICommunicator;
import gseproject.robot.communicator.RobotFloorInitiator;
import gseproject.robot.communicator.RobotSourcePaletteInitiator;
import gseproject.robot.controller.DummyController;
import gseproject.robot.controller.IController;
import gseproject.robot.domain.RobotState;
import gseproject.robot.skills.ISkill;
import gseproject.robot.skills.SkillsSettings;
import gseproject.robot.skills.TransportSkill;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RobotAgent extends Agent {

	private IController _controller;
	private ICommunicator _communicator;
	private SkillsSettings _settings;
	private RobotState _state;

	public RobotAgent() {
		_communicator = new DummyCommunicator(this);
		_controller = new DummyController();

		_state = new RobotState();
		_state.isCarryingBlock = false;
		_state.position = new Position(5, 1);


	}

	public void setup() {
		/*
		 * TransportSkill transportSkill = new TransportSkill(_actuator);
		 * transportSkill.registerService(this);
		 * 
		 * _skills.add(transportSkill);
		 */
		_state = new RobotState();
		_state.isCarryingBlock = false;
		_state.position = new Position(5, 1);
		_state.block = null;
		
		

		Behaviour getDirtyBlock = new OneShotBehaviour(this) {

			@Override
			public void action() {
				requestDirtyBlock();
				ACLMessage reply = myAgent.blockingReceive(MessageTemplate
						.MatchProtocol(ProtocolTemplates.ServiceTypeProtocolTemplate.ROBOT_SOURCE_PALETTE_PROTOCOL));
				getDirtyBlock(reply);
			}

			private void getDirtyBlock(ACLMessage reply) {
				if (reply.getPerformative() == ACLMessage.INFORM) {
					Block block = null;
					try {
						block = (Block) reply.getContentObject();
					} catch (UnreadableException e) {
						e.printStackTrace();
					}
					_state.block = block;
					_state.isCarryingBlock = true;
					System.out.println(_state);
				} else {

				}
			}

			private void requestDirtyBlock() {
				ACLMessage messageToSourcePalette = new ACLMessage(ACLMessage.REQUEST);
				messageToSourcePalette.addReceiver(new AID("SourcePalette", AID.ISLOCALNAME));
				messageToSourcePalette
						.setProtocol(ProtocolTemplates.ServiceTypeProtocolTemplate.ROBOT_SOURCE_PALETTE_PROTOCOL);
				messageToSourcePalette.setContent(ServiceType.TAKE_BLOCK.name());
				send(messageToSourcePalette);
			}

		};

		Behaviour giveBlockToCleanStation = new OneShotBehaviour(this) {

			@Override
			public void action() {
				requestGiveBlock();
				ACLMessage reply = myAgent.blockingReceive(MessageTemplate
						.MatchProtocol(ProtocolTemplates.ServiceTypeProtocolTemplate.ROBOT_CLEANING_FLOOR_PROTOCOL));
				if (reply.getPerformative() == ACLMessage.INFORM) {
					_state.block = null;
					_state.isCarryingBlock = false;
					System.out.println(_state);
				} else {
					System.out.println("Something went wrong");
				}
			}

			private void requestGiveBlock() {
				ACLMessage messageToSourcePalette = new ACLMessage(ACLMessage.REQUEST);
				messageToSourcePalette.addReceiver(new AID("CleaningFloor", AID.ISLOCALNAME));
				messageToSourcePalette
						.setProtocol(ProtocolTemplates.ServiceTypeProtocolTemplate.ROBOT_CLEANING_FLOOR_PROTOCOL);
				try {
					messageToSourcePalette.setContentObject(_state.block);
				} catch (IOException e) {
					e.printStackTrace();
				}
				send(messageToSourcePalette);
			}

		};
		
		Behaviour getCleanedBlockFromCleanStation = new OneShotBehaviour(this){

			@Override
			public void action() {
				
			}
			
		};
		
		SequentialBehaviour sq = new SequentialBehaviour(this) {

		};

		sq.addSubBehaviour(getDirtyBlock);
		sq.addSubBehaviour(giveBlockToCleanStation);
		addBehaviour(sq);

		/*
		 * addBehaviour(new TickerBehaviour(this, 1000) {
		 * 
		 * @Override protected void onTick() {
		 * _communicator.notifyGridAgent(_state); } }); /*
		 * _communicator.getRoute(new AID("PaintingFloor", true), _state, new
		 * ICallbackArgumented<Position>() {
		 * 
		 * @Override
		 * 
		 * public void invoke(Position arg) { doSomethingWithGoal(arg); }
		 * 
		 * });
		 */
	}

	private void doSomethingWithGoal(Position position) {

	}
}
