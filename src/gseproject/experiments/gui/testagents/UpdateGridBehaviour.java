package gseproject.experiments.gui.testagents;

import java.awt.Point;
import java.io.IOException;

import gseproject.core.grid.Grid;
import gseproject.core.grid.SpaceType;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

/**
 * 
 * @author Tobias This class updates the GRID. It receives messages from ROBOTS.
 *         These messages should contain the POSITION of the ROBOT. Whenever a
 *         message is received and the GRID could be updated without errors, it
 *         sends the new GRID to the GUI.
 */
public class UpdateGridBehaviour extends CyclicBehaviour {

    private static final long serialVersionUID = -6486151536920332834L;
    private Grid grid;
    private AID GUI;
    
    public UpdateGridBehaviour(Agent a, Grid grid, AID GUI){
	super(a);
	this.grid = grid;
	this.GUI = GUI;
    }

    @Override
    public void action() {
	Point position = receivePositionFromRobot();
	grid.set(position.x, position.y, SpaceType.DEFAULT);
	sendNewGridToGUI();
    }

    private Point receivePositionFromRobot() {
	ACLMessage msg = myAgent.blockingReceive();
	Point position = null;
	try {
	    position = (Point) msg.getContentObject();
	} catch (UnreadableException e) {
	    e.printStackTrace();
	}
	return position;
    }
    
    private void sendNewGridToGUI(){
	ACLMessage informGUI = new ACLMessage(ACLMessage.INFORM);
	try {
	    informGUI.setContentObject(grid);
	} catch (IOException e) {
	    e.printStackTrace();
	}
	informGUI.addReceiver(GUI);
	myAgent.send(informGUI);
    }

}
