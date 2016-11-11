package gseproject.experiments.gui.testagents;

import java.awt.Point;
import java.io.IOException;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

/**
 * 
 * @author Tobias
 *
 * This class can be used by EVERY AGENT that needs to INFORM the GRID, because his POSITION has CHANGED.
 */
public class SendPositionBehaviour extends OneShotBehaviour {
    private static final long serialVersionUID = -2519846384541203120L;
    private Point position;
    private AID grid;

    public SendPositionBehaviour(Agent a, Point position, AID grid) {
	super(a);
	this.position = position;
	this.grid = grid;
    }

    @Override
    public void action() {
	ACLMessage position = new ACLMessage(ACLMessage.INFORM);
	position.addReceiver(grid);
	try {
	    position.setContentObject(position);
	} catch (IOException e) {
	    e.printStackTrace();
	}
	myAgent.send(position);
    }

    public Point getPosition() {
	return position;
    }

}
