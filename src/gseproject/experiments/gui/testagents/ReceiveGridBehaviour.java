package gseproject.experiments.gui.testagents;

import gseproject.core.grid.Grid;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

/**
 * 
 * @author Tobias This class receives messages from the GRID-AGENT. Once a
 *         message arrives, the content is casted to a Grid and with this Grid
 *         the GUI is updated.
 */
public class ReceiveGridBehaviour extends CyclicBehaviour {
    private static final long serialVersionUID = 194896594483828925L;

    public ReceiveGridBehaviour(Agent a) {
	super(a);
    }

    @Override
    public void action() {
	ACLMessage msg = myAgent.blockingReceive();
	Grid g = null;
	try {
	    g = (Grid) msg.getContentObject();
	} catch (UnreadableException e) {
	    e.printStackTrace();
	}
	updateGUI(g);
    }

    private void updateGUI(Grid g) {

    }

}
